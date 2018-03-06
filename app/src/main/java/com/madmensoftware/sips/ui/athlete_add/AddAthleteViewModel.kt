package com.madmensoftware.sips.ui.athlete_add

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.CommonUtils
import com.madmensoftware.sips.util.SchedulerProvider


/**
 * Created by clj00 on 3/2/2018.
 */
class AddAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<AddAthleteNavigator>(dataManager, schedulerProvider) {

    val finishedAddAthleteLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun addAthlete(athlete: Athlete) {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .saveAthlete(athlete)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
//                    navigator!!.showSuccess()
                    navigator!!.athleteAdded(athlete)
                }, { throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                }))
    }

    fun isFormDataValid(firstName: String, lastName: String, email: String): Boolean {
        if (TextUtils.isEmpty(firstName)) {
            return false
        }
        if (TextUtils.isEmpty(lastName)) {
            return false
        }
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false
        }

        return true
    }

    fun onAddAthleteClick() {
        navigator!!.addAthlete()
    }

    fun onNavBackClick() {
        navigator!!.goBack()
    }

}