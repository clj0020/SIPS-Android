package com.madmensoftware.sips.ui.login


import android.text.TextUtils
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.api.LoginRequest
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.CommonUtils
import com.madmensoftware.sips.util.SchedulerProvider


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
                .doOnSuccess({ response ->
                    dataManager
                            .updateUserInfo(
                                    accessToken = response.accessToken,
                                    userId = response.userId,
                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                    userName = response.userName,
                                    email = response.userEmail,
                                    profilePicPath = response.googleProfilePicUrl,
                                    organizationId = response.organizationId)
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

    fun onFbLoginClick() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest("test3", "test4"))
                .doOnSuccess({ response ->
                    dataManager
                            .updateUserInfo(
                                    accessToken = response.accessToken,
                                    userId = response.userId,
                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                    userName = response.userName,
                                    email = response.userEmail,
                                    profilePicPath = response.googleProfilePicUrl,
                                    organizationId = response.organizationId)
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
                    dataManager
                            .updateUserInfo(
                                    accessToken = response.accessToken,
                                    userId = response.userId,
                                    loggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                    userName = response.userName,
                                    email = response.userEmail,
                                    profilePicPath = response.googleProfilePicUrl,
                                    organizationId = response.organizationId)
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