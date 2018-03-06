package com.madmensoftware.sips.ui.main

import dagger.Provides
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module


/**
 * Created by clj00 on 3/2/2018.
 */
@Module
class MainActivityModule {

//    @Provides
//    internal fun mainViewModelProvider(mainViewModel: MainViewModel): ViewModelProvider.Factory {
//        return ViewModelProviderFactory(mainViewModel)
//    }

    @Provides
    internal fun provideMainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): MainViewModel {
        return MainViewModel(dataManager, schedulerProvider)
    }
}