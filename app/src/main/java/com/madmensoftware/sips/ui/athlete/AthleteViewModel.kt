package com.madmensoftware.sips.ui.athlete

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.util.Log
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider
import java.io.File

/**
 * Created by clj00 on 3/4/2018.
 */
class AthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<AthleteNavigator>(dataManager, schedulerProvider) {

    var athleteId: String = ""

    val athleteLiveData: MutableLiveData<Athlete> = MutableLiveData()

    val testDataObservableList: ObservableList<TestData>?= ObservableArrayList<TestData>()

    val testDataListLiveData: MutableLiveData<List<TestData>> = MutableLiveData<List<TestData>>()

    lateinit var imageFilePath: String

    val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR: Float = 0.9f
    val PERCENTAGE_TO_HIDE_TITLE_DETAILS: Float = 0.3f
    val ALPHA_ANIMATIONS_DURATION: Long = 200

    var mIsTheTitleVisible = false
    var mIsTheTitleContainerVisible = true

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

    fun addTestDataItemsToList(testDataList: List<TestData>) {
        testDataObservableList?.clear()
        testDataObservableList?.addAll(testDataList)
    }

    fun fetchTestData() {
        navigator!!.setRefreshing(true)
        compositeDisposable.add(dataManager
                .getTestDataList(this.athleteId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ testDataList ->
                    if (testDataList != null) {
                        testDataListLiveData.setValue(testDataList)
                    }
                    navigator!!.setRefreshing(false)
                }, { throwable ->
                    navigator!!.setRefreshing(false)
                    navigator!!.handleError(throwable)
                }))
    }

    fun uploadProfileImage(profileImage: File) {
        navigator!!.setRefreshing(true)
        compositeDisposable.add(dataManager.uploadAthleteProfileImage(this.athleteId, profileImage)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    navigator!!.setRefreshing(false)
                    fetchAthlete()
                }, {throwable: Throwable ->
                    navigator!!.setRefreshing(false)
                    navigator!!.handleError(throwable)
                })
        )
    }

    fun onProfilePictureClick() {
        navigator!!.showChangeProfilePictureDialog()
    }

    fun onTestAthleteButtonClick(athleteId: String) {
        navigator!!.showTestAthleteFragment(athleteId)
    }

    fun onEditAthleteButtonClick(athleteId: String) {
        navigator!!.showEditAthleteFragment(athleteId)
    }
}