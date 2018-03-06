package com.madmensoftware.sips.ui.athlete_test

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by clj00 on 3/5/2018.
 */
@Module
abstract class TestAthleteFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(TestAthleteFragmentModule::class))
    internal abstract fun provideTestAthleteFragmentFactory(): TestAthleteFragment

}