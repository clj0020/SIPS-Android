package com.madmensoftware.sips.ui.splash

import android.util.Log
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider
import com.auth0.android.jwt.JWT




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

            navigator!!.openMainActivity()
        }
    }
}