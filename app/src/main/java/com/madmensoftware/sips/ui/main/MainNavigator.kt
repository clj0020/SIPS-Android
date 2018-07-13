package com.madmensoftware.sips.ui.main

/**
 * Created by clj00 on 3/2/2018.
 */
interface MainNavigator {

    fun handleError(throwable: Throwable)

    fun openLoginActivity()

    fun onAthleteActivityRecognitionServiceRequested()

}
