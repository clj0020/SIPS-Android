package com.madmensoftware.sips.ui.athlete_list

import android.support.v7.widget.LinearLayoutManager
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import java.util.ArrayList

/**
 * Created by clj00 on 3/2/2018.
 */
@Module
class AthleteListFragmentModule {

    @Provides
    internal fun provideAthleteListViewModel(dataManager: DataManager,
                                      schedulerProvider: SchedulerProvider): AthleteListViewModel {
        return AthleteListViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideAthleteListAdapter(): AthleteListAdapter {
        return AthleteListAdapter(ArrayList())
    }

    @Provides
    internal fun provideLinearLayoutManager(fragment: AthleteListFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.getActivity())
    }
}
