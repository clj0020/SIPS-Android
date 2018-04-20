package com.madmensoftware.sips.ui.athlete_test

import android.support.v7.widget.LinearLayoutManager
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import com.madmensoftware.sips.util.SensorHelper
import dagger.Module
import dagger.Provides

/**
 * Created by clj00 on 3/5/2018.
 */
@Module
class TestAthleteFragmentModule {

    @Provides
    internal fun provideTestAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): TestAthleteViewModel {
        return TestAthleteViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideReactiveSensors(fragment: TestAthleteFragment): ReactiveSensors {
        return ReactiveSensors(fragment.activity)
    }

    @Provides
    internal fun provideSensorHelper(reactiveSensors: ReactiveSensors, schedulerProvider: SchedulerProvider): SensorHelper {
        return SensorHelper(reactiveSensors, schedulerProvider)
    }

    @Provides
    internal fun provideTestTypeListAdapter(): TestTypeListAdapter {
        return TestTypeListAdapter(ArrayList())
    }

    @Provides
    internal fun provideLinearLayoutManager(fragment: TestAthleteFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.getActivity())
    }


}