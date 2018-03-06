package com.madmensoftware.sips.ui.athlete_list

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by clj00 on 3/2/2018.
 */
@Module
abstract class AthleteListFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(AthleteListFragmentModule::class))
    internal abstract fun provideAthleteListFragmentFactory(): AthleteListFragment
}
