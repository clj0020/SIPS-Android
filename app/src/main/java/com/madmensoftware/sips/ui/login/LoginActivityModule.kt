package com.madmensoftware.sips.ui.login

import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import dagger.Provides



/**
 * Created by clj00 on 3/2/2018.
 */
@Module
class LoginActivityModule {

    @Provides
    internal fun provideLoginViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): LoginViewModel {
        return LoginViewModel(dataManager, schedulerProvider)
    }
}