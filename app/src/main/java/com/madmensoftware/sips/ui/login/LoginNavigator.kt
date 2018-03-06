package com.madmensoftware.sips.ui.login

/**
 * Created by clj00 on 3/2/2018.
 */
interface LoginNavigator {

    fun handleError(throwable: Throwable)

    fun login()

    fun openMainActivity()
}