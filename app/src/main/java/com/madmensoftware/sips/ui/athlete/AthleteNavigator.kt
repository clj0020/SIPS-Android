package com.madmensoftware.sips.ui.athlete

import com.madmensoftware.sips.data.models.room.TestData

/**
 * Created by clj00 on 3/4/2018.
 */
interface AthleteNavigator {

    fun handleError(throwable: Throwable)

    fun onRetryClick()

    fun updateTestDataList(testDataList: List<TestData>)

    fun startTrackingAthleteWorkout(athleteId: String, athleteName: String)

    fun stopTrackingAthleteWorkout(athleteId: String)

    fun showTestAthleteFragment(athleteId: String)

    fun showEditAthleteFragment(athleteId: String)

    fun showChangeProfilePictureDialog()

    fun setRefreshing(refreshing: Boolean)

}