package com.madmensoftware.sips.ui.main

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import android.databinding.ObservableField
import android.util.Log
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.api.LogoutResponse
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.util.SchedulerProvider


/**
 * Created by clj00 on 3/2/2018.
 */
class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): BaseViewModel<MainNavigator>(dataManager, schedulerProvider) {

    val appVersion = ObservableField<String>()

    val userEmail = ObservableField<String>()

    val userName = ObservableField<String>()

    val userProfilePicUrl = ObservableField<String>()

    val organizationId = ObservableField<Long>()

    val backStackLiveData: MutableLiveData<Data> = MutableLiveData<Data>()

    init {
        backStackLiveData.value = Data.AthletesData
    }

//    fun onSaveAthlete(athlete: Athlete) {
//        backStackLiveData.value = Data.AthletesData
//    }

    fun onAthleteDetails(athleteId: Long) {
        backStackLiveData.value = Data.AthleteData(athleteId)
    }

    fun onAddAthlete() {
        backStackLiveData.value = Data.AddAthleteData
    }

    fun onTestAthlete(athleteId: Long) {
        backStackLiveData.value = Data.TestAthleteData(athleteId)
    }

    fun onEditAthlete(athleteId: Long) {
        backStackLiveData.value = Data.EditAthleteData(athleteId)
    }

    fun onBackPress(): Boolean {
        return when (backStackLiveData.value) {
            Data.AddAthleteData, is Data.AthleteData -> {
                backStackLiveData.value = Data.AthletesData
                Log.i("MainViewModel: ", backStackLiveData.value.toString())
                true
            }
            is Data.TestAthleteData -> {
                backStackLiveData.value = Data.AthleteData((backStackLiveData.value as Data.TestAthleteData).athleteId)
                true
            }
            else -> false
        }
    }

    fun logout() {
        setIsLoading(true)
        compositeDisposable.add(dataManager.doLogoutApiCall()
                .doOnSuccess({
                    dataManager.setUserAsLoggedOut()
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
                    navigator!!.openLoginActivity()
                }, { throwable: Throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                }))
    }

    fun onNavMenuCreated() {
        val currentUserName = dataManager.currentUserName
        if (!TextUtils.isEmpty(currentUserName)) {
            userName.set(currentUserName)
        }

        val currentUserEmail = dataManager.currentUserEmail
        if (!TextUtils.isEmpty(currentUserEmail)) {
            userEmail.set(currentUserEmail)
        }

        val profilePicUrl = dataManager.currentUserProfilePicUrl
        if (!TextUtils.isEmpty(profilePicUrl)) {
            userProfilePicUrl.set(profilePicUrl)
        }
    }

    fun updateAppVersion(version: String) {
        appVersion.set(version)
    }


    interface Data {
        object AthletesData : Data
        data class AthleteData(val athleteId: Long) : Data
        object AddAthleteData : Data
        data class TestAthleteData(val athleteId: Long) : Data
        data class EditAthleteData(val athleteId: Long) : Data
    }




}