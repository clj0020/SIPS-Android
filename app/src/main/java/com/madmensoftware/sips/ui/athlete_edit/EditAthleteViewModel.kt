package com.madmensoftware.sips.ui.athlete_edit

import android.arch.lifecycle.MutableLiveData
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

    fun editAthlete(editedAthlete: Athlete) {
        setIsLoading(true)
        dataManager.editAthlete(editedAthlete)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnComplete({
                    setIsLoading(false)
                    navigator!!.athleteEdited()
                })
                .doOnError({throwable: Throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                })
                .subscribe({
                    setIsLoading(false)
                })
    }

    fun onSelectDOBClick(date_of_birth: String?) {
        navigator!!.openSelectBirthdayDialog(date_of_birth)
    }

    fun formatDate(isoString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = dateFormat.parse(isoString)
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val dateStr = formatter.format(date)
        return dateStr
    }

    fun isFormDataValid(athlete: Athlete): Boolean {
        if (TextUtils.isEmpty(athlete.first_name) || TextUtils.isEmpty(athlete.last_name) ||
                TextUtils.isEmpty(athlete.email) || TextUtils.isEmpty(athlete.date_of_birth) ||
                TextUtils.isEmpty(athlete.height.toString()) || TextUtils.isEmpty(athlete.weight.toString()) ||
                TextUtils.isEmpty(athlete.sport) || TextUtils.isEmpty(athlete.position)) {
            return false
        }
        if (athlete.height == 0 || athlete.weight == 0) {
            return false
        }
        if (!CommonUtils.isEmailValid(athlete.email!!)) {
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