package com.madmensoftware.sips.ui.athlete_add

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.androidnetworking.error.ANError
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.CommonUtils
import com.madmensoftware.sips.util.SchedulerProvider
import retrofit2.adapter.rxjava2.Result.response




/**
 * Created by clj00 on 3/2/2018.
 */
class AddAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<AddAthleteNavigator>(dataManager, schedulerProvider) {

    fun addAthlete(email: String) {
        setIsLoading(true)
        dataManager.saveAthlete(email)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
                    navigator!!.athleteAdded()
                }, {throwable: Throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                })
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

    fun onAddAthleteClick() {
        navigator!!.addAthlete()
    }

    fun onNavBackClick() {
        navigator!!.goBack()
    }

}