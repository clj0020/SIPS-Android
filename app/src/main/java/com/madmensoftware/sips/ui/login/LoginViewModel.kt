package com.madmensoftware.sips.ui.login


import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.api.LoginRequest
import com.madmensoftware.sips.data.models.api.LoginResponse
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.Organization
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.CommonUtils
import com.madmensoftware.sips.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Created by clj00 on 3/2/2018.
 */
class LoginViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<LoginNavigator>(dataManager, schedulerProvider) {

    fun isEmailAndPasswordValid(email: String, password: String): Boolean {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false
        }
        return if (TextUtils.isEmpty(password)) {
            false
        } else true
    }

    fun login(email: String, password: String) {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doServerLoginApiCall(LoginRequest.ServerLoginRequest(email, password))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSuccess({response: LoginResponse ->
                    dataManager.updateUserInfo(
                            _id = response.user!!._id,
                            accessToken = response.token,
                            loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                            first_name = response.user!!.first_name,
                            last_name = response.user!!.last_name,
                            email = response.user!!.email,
                            status = response.user!!.status,
                            organizationId = response.user!!.organization!!._id,
                            kind = response.user!!.kind)
                })
                .subscribe({ response: LoginResponse ->
                    val organization = Organization()
                    organization._id = response.user!!.organization!!._id
                    organization.title = response.user!!.organization!!.title
                    organization.createdAt = response.user!!.organization!!.createdAt
                    organization.creator = response.user!!.organization!!.creator

                    dataManager.saveOrganization(organization)
                            .doOnError({throwable ->
                                setIsLoading(false)
                                navigator!!.handleError(throwable) })
                            .doFinally({
                                dataManager.getAthleteList()
                                        .subscribeOn(schedulerProvider.io())
                                        .observeOn(schedulerProvider.ui())
                                        .subscribe({ athleteList ->
                                            dataManager.getTestTypeList()
                                                    .subscribeOn(schedulerProvider.io())
                                                    .observeOn(schedulerProvider.ui())
                                                    .subscribe({ testTypeList ->
                                                        setIsLoading(false)
                                                        navigator!!.openMainActivity()
                                                    }, { throwable: Throwable ->
                                                        setIsLoading(false)
                                                        navigator?.handleError(throwable)
                                                    })
                                        }, { throwable: Throwable ->
                                            setIsLoading(false)
                                            navigator?.handleError(throwable)
                                        })
                            })
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe({})
                }, { throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                })
        )
    }

    fun onFbLoginClick() {
        Log.i("LoginViewModel", "Facebook login clicked.")
//        setIsLoading(true)
//        compositeDisposable.add(dataManager
//                .doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest("test3", "test4"))
//                .doOnSuccess({ response ->
//                    dataManager
//                            .updateUserInfo(
//                                    accessToken = response.token,
//                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
//                                    name = response.user!!.name,
//                                    email = response.user!!.email)
//                })
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe({ response ->
//                    setIsLoading(false)
//                    navigator!!.openMainActivity()
//                }, { throwable ->
//                    setIsLoading(false)
//                    navigator!!.handleError(throwable)
//                }))
    }

    fun onGoogleLoginClick() {
        Log.i("LoginViewModel", "Google Login clicked..")

//        setIsLoading(true)
//        compositeDisposable.add(dataManager
//                .doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest("test1", "test1"))
//                .doOnSuccess({ response ->
//                    dataManager
//                            .updateUserInfo(
//                                    accessToken = response.token,
//                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
//                                    name = response.user!!.name,
//                                    email = response.user!!.email)
//                })
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe({ response ->
//                    setIsLoading(false)
//                    navigator!!.openMainActivity()
//                }, { throwable ->
//                    setIsLoading(false)
//                    navigator!!.handleError(throwable)
//                }))
    }

    fun onServerLoginClick() {
        navigator!!.login()
    }
}