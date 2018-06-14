package com.madmensoftware.sips.ui.athlete_edit

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.text.TextUtils
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.CommonUtils
import com.madmensoftware.sips.util.SchedulerProvider
import java.text.SimpleDateFormat
import java.util.*

class EditAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<EditAthleteNavigator>(dataManager, schedulerProvider) {

    var athleteId: String = ""

    val athleteLiveData: MutableLiveData<Athlete> = MutableLiveData()

    fun fetchAthlete() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .getAthlete(this.athleteId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({athlete ->
                    if (athlete != null) {
                        athleteLiveData.value = athlete
                    }
                    setIsLoading(false)
                }, {throwable ->
                    navigator!!.handleError(throwable)
                })
        )
    }

    fun editAthlete() {
        setIsLoading(true)
    }

    fun onSelectDOBClick(date_of_birth: String) {
        navigator!!.openSelectBirthdayDialog(date_of_birth)
    }

    fun formatDate(isoString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = dateFormat.parse(isoString)
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val dateStr = formatter.format(date)
        return dateStr
    }

    fun isFormDataValid(email: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false
        }

        return true
    }

    fun onEditAthleteClick() {
        navigator!!.editAthlete()
    }

    fun onNavBackClick() {
        navigator!!.goBack()
    }

}