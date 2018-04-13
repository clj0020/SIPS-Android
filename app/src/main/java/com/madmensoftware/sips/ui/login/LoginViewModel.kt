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
                .doOnSuccess({response ->
                    Log.i("OrganizationId: ", response.user!!.organization!!._id)

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
                    setIsLoading(false)
                    navigator!!.openMainActivity()
                }, { throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                })


//                .doOnSuccess()
//                .flatMap {response ->
//                    val organization = Organization()
//                    organization.id = response.user!!.organization!!._id
//                    organization.title = response.user!!.organization!!.title
//                    organization.createdAt = response.user!!.organization!!.createdAt
//                    organization.creator = response.user!!.organization!!.creator
//                    dataManager.saveOrganization(organization)
//                    return@flatMap response
//                }
//                .flatMap{
//
////                    Observable.fromCallable({
////                        dataManager.saveOrganization(organization)
////                    })
////                    Observable.fromCallable(() ->db.countriesDao().addCountries(countriesList))
////
////                    compositeDisposable.add(Observable.fromCallable(() -> dataManager.saveOrganization(organization))
//
//                    dataManager.updateUserInfo(
//                            _id = response.user!!._id,
//                            accessToken = response.token,
//                            loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
//                            first_name = response.user!!.first_name,
//                            last_name = response.user!!.last_name,
//                            email = response.user!!.email,
//                            status = response.user!!.status,
//                            organizationId = response.user!!.organization!!._id,
//                            kind = response.user!!.kind)
//
//                    val _newAthleteList = mutableListOf<Athlete>()
//                    for (athlete in response.athletes!!) {
//                        val newAthlete = Athlete()
//                        newAthlete.id = athlete._id
//                        newAthlete.first_name = athlete.first_name
//                        newAthlete.last_name = athlete.last_name
//                        newAthlete.email = athlete.email
//                        newAthlete.status = athlete.status
//                        newAthlete.date_of_birth = athlete.date_of_birth
//                        newAthlete.height = athlete.height
//                        newAthlete.weight = athlete.weight
//                        newAthlete.created_at = athlete.created_at
//                        newAthlete.organization = response.user!!.organization!!._id
//
//                        dataManager.saveAthlete(newAthlete)
//                    }
//
//                }

        )


    }

    fun onFbLoginClick() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest("test3", "test4"))
                .doOnSuccess({ response ->
//                    dataManager
//                            .updateUserInfo(
//                                    accessToken = response.token,
//                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
//                                    name = response.user!!.name,
//                                    email = response.user!!.email)
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    setIsLoading(false)
                    navigator!!.openMainActivity()
                }, { throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                }))
    }

    fun onGoogleLoginClick() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest("test1", "test1"))
                .doOnSuccess({ response ->
//                    dataManager
//                            .updateUserInfo(
//                                    accessToken = response.token,
//                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
//                                    name = response.user!!.name,
//                                    email = response.user!!.email)
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    setIsLoading(false)
                    navigator!!.openMainActivity()
                }, { throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                }))
    }

    fun onServerLoginClick() {
        navigator!!.login()
    }
}