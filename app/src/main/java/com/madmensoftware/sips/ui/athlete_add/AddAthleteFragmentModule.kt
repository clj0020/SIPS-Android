package com.madmensoftware.sips.ui.athlete_add

import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import dagger.Provides



/**
 * Created by clj00 on 3/2/2018.
 */
@Module
class AddAthleteFragmentModule {

    @Provides
    internal fun provideAddAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): AddAthleteViewModel {
        return AddAthleteViewModel(dataManager, schedulerProvider)
    }
}