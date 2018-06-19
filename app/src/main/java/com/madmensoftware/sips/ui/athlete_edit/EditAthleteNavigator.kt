package com.madmensoftware.sips.ui.athlete_edit

interface EditAthleteNavigator {

    fun handleError(throwable: Throwable)

    fun editAthlete()

    fun athleteEdited()

    fun openSelectBirthdayDialog(date_of_birth: String)

    fun goBack()

}