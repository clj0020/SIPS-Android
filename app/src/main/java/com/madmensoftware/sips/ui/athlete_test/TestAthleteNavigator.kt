package com.madmensoftware.sips.ui.athlete_test

/**
 * Created by clj00 on 3/5/2018.
 */
interface TestAthleteNavigator {

    fun handleError(throwable: Throwable)

    fun testSaved()

    fun goBack()

    fun testStarted()

    fun testStopped()

    fun testSelected(testId: Int)

}