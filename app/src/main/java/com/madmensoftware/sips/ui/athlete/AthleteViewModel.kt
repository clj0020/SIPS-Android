package com.madmensoftware.sips.ui.athlete

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider

/**
 * Created by clj00 on 3/4/2018.
 */
class AthleteViewModel(dataManager: DataManager,
                           schedulerProvider: SchedulerProvider) : BaseViewModel<AthleteNavigator>(dataManager, schedulerProvider) {

    var athleteId: String = ""

    val athleteLiveData: MutableLiveData<Athlete> = MutableLiveData()

    val testDataObservableList: ObservableList<TestData>?= ObservableArrayList<TestData>()

    val testDataListLiveData: MutableLiveData<List<TestData>> = MutableLiveData<List<TestData>>()

    init {

    }

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
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .getTestDataForAthleteId(athleteId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ testDataList ->
                    if (testDataList != null) {
                        testDataListLiveData.setValue(testDataList)
                    }
                    setIsLoading(false)
                }, { throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                }))
    }

    fun onTestAthleteButtonClick(athleteId: String) {
        navigator!!.showTestAthleteFragment(athleteId)
    }

    fun onEditAthleteButtonClick(athleteId: String) {
        navigator!!.showEditAthleteFragment(athleteId)
    }
}