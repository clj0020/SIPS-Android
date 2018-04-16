package com.madmensoftware.sips.ui.athlete_list

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteListViewModel(dataManager: DataManager,
                           schedulerProvider: SchedulerProvider) : BaseViewModel<AthleteListNavigator>(dataManager, schedulerProvider) {

    val athleteObservableList: ObservableList<Athlete> ?= ObservableArrayList<Athlete>()

    val athleteListLiveData: MutableLiveData<List<Athlete>> = MutableLiveData<List<Athlete>>()
    init {
        fetchAthletes()
    }

    fun addAthleteItemsToList(athletes: List<Athlete>) {
        athleteObservableList?.clear()
        athleteObservableList?.addAll(athletes)
    }

    fun fetchAthletes() {
        navigator?.setRefreshing(true)
        compositeDisposable.add(dataManager
                .getAthleteList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ athleteList ->
                    if (athleteList != null) {
                            athleteListLiveData.setValue(athleteList)
                    }
                    navigator?.setRefreshing(false)
                }, { throwable ->
                    navigator?.setRefreshing(false)
                    navigator?.handleError(throwable)
                }))
    }
}
