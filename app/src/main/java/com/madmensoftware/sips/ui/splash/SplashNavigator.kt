package com.madmensoftware.sips.ui.splash

/**
 * Created by clj00 on 3/2/2018.
 */
interface SplashNavigator {

    fun openLoginActivity()

    fun openMainActivity()

    fun handleError(throwable: Throwable)
}