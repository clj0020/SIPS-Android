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
import java.io.File


/**
 * Created by clj00 on 3/2/2018.
 */
class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): BaseViewModel<MainNavigator>(dataManager, schedulerProvider) {

    val appVersion = ObservableField<String>()

    val userEmail = ObservableField<String>()

    val userName = ObservableField<String>()

    val userProfilePicUrl = ObservableField<String>()

    val organizationId = ObservableField<String>()

    val backStackLiveData: MutableLiveData<Data> = MutableLiveData<Data>()

    init {
        backStackLiveData.value = Data.AthletesData
    }

    fun onAthleteDetails(athleteId: String) {
        backStackLiveData.value = Data.AthleteData(athleteId)
    }

    fun onAddAthlete() {
        backStackLiveData.value = Data.AddAthleteData
    }

    fun onTestAthlete(athleteId: String) {
        backStackLiveData.value = Data.TestAthleteData(athleteId)
    }

    fun onEditAthlete(athleteId: String) {
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
            is Data.EditAthleteData -> {
                backStackLiveData.value = Data.AthleteData((backStackLiveData.value as Data.EditAthleteData).athleteId)
                true
            }
            else -> false
        }
    }

    fun logout() {
        setIsLoading(true)
        dataManager.logout()
        navigator!!.openLoginActivity()
    }

    fun onNavMenuCreated() {
        val currentUserName = dataManager.currentUserFirstName + " " + dataManager.currentUserLastName
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

    fun onAthleteActivityRecognized(any: Any) {

    }


    interface Data {
        object AthletesData : Data
        data class AthleteData(val athleteId: String) : Data
        object AddAthleteData : Data
        data class TestAthleteData(val athleteId: String) : Data
        data class EditAthleteData(val athleteId: String) : Data
    }

}