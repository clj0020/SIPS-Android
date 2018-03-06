package com.madmensoftware.sips.ui.athlete_add

import dagger.Module
import dagger.android.ContributesAndroidInjector



/**
 * Created by clj00 on 3/2/2018.
 */
@Module
abstract class AddAthleteFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(AddAthleteFragmentModule::class))
    internal abstract fun provideAddAthleteFragmentFactory(): AddAthleteFragment

}