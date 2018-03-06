package com.madmensoftware.sips.ui.splash

import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider


/**
 * Created by clj00 on 3/2/2018.
 */
class SplashViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {

//    fun startSeeding() {
//        compositeDisposable.add(dataManager
//                .seedDatabaseQuestions()
//                .flatMap({ aBoolean -> dataManager.seedDatabaseOptions() })
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe({ aBoolean -> decideNextActivity() }, { throwable -> decideNextActivity() }))
//    }

    fun decideNextActivity() {
        if (dataManager.currentUserLoggedInMode == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type) {
            navigator!!.openLoginActivity()
        } else {
            navigator!!.openMainActivity()
        }
    }
}