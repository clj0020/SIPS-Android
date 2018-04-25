package com.madmensoftware.sips.ui.athlete_add

import com.madmensoftware.sips.data.models.room.Athlete

/**
 * Created by clj00 on 3/2/2018.
 */
interface AddAthleteNavigator {

    fun handleError(throwable: Throwable)

    fun addAthlete()

    fun athleteAdded()

    fun goBack()

}