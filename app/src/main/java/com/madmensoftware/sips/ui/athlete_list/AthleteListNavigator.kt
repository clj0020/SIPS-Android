package com.madmensoftware.sips.ui.athlete_list

import com.madmensoftware.sips.data.models.room.Athlete

/**
 * Created by clj00 on 3/2/2018.
 */
interface AthleteListNavigator {

    fun handleError(throwable: Throwable)

    fun updateAthleteList(athleteList: List<Athlete>)
}
