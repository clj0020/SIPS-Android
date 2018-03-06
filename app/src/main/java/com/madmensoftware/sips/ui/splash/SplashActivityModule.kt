package com.madmensoftware.sips.ui.splash

import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import dagger.Provides



/**
 * Created by clj00 on 3/2/2018.
 */
@Module
class SplashActivityModule {

    @Provides
    internal fun provideSplashViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): SplashViewModel {
        return SplashViewModel(dataManager, schedulerProvider)
    }
}