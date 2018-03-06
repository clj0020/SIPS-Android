package com.madmensoftware.sips.ui.athlete

import android.support.v7.widget.LinearLayoutManager
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import dagger.Provides

/**
 * Created by clj00 on 3/4/2018.
 */
@Module
class AthleteFragmentModule {

    @Provides
    internal fun provideAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): AthleteViewModel {
        return AthleteViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideTestDataListAdapter(): TestDataListAdapter {
        return TestDataListAdapter(ArrayList())
    }

    @Provides
    internal fun provideLinearLayoutManager(fragment: AthleteFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.getActivity())
    }
}