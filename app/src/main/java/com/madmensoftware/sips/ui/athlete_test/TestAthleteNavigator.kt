package com.madmensoftware.sips.ui.athlete_test

import com.madmensoftware.sips.data.models.api.TestDataRequest
import com.madmensoftware.sips.data.models.room.TestType
import com.wardellbagby.rxcountdowntimer.RxCountDownTimer
import io.reactivex.disposables.Disposable

/**
 * Created by clj00 on 3/5/2018.
 */
interface TestAthleteNavigator {

    fun handleError(throwable: Throwable)

    fun showConfirmDialog(testDataRequest: TestDataRequest.UploadTestDataRequest)

    fun testSaved()

    fun goBack()

    fun testStarted()

    fun testStopped()

    fun testSelected(testType: TestType)

    fun updateTestTypeList(testTypeList: List<TestType>)

}