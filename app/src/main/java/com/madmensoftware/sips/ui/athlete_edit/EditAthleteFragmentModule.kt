package com.madmensoftware.sips.ui.athlete_edit

import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class EditAthleteFragmentModule {

    @Provides
    internal fun provideEditAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): EditAthleteViewModel {
        return EditAthleteViewModel(dataManager, schedulerProvider)
    }
}