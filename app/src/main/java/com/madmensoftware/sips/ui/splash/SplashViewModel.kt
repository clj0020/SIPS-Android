package com.madmensoftware.sips.ui.splash

import android.util.Log
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider
import com.auth0.android.jwt.JWT
import com.madmensoftware.sips.data.models.room.TestType


/**
 * Created by clj00 on 3/2/2018.
 */
class SplashViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {

    fun decideNextActivity() {
        if (dataManager.currentUserLoggedInMode == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type) {
            navigator!!.openLoginActivity()
        } else {
            val jwt = JWT(dataManager.accessToken!!.substring(4))
            Log.i("TOKEN Expiration Date:",jwt.expiresAt.toString())

            if (jwt.isExpired(10)) {
                dataManager.setUserAsLoggedOut()
                navigator!!.openLoginActivity()
            }
            else {
                seedDatabase()
            }
        }
    }

    fun seedDatabase() {
        setIsLoading(true)
        dataManager.getTestTypeList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ testTypeList ->
                    dataManager.getAthleteList()
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe({ athleteList ->
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
    }
}