package com.madmensoftware.sips.ui.athlete

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by clj00 on 3/4/2018.
 */
@Module
abstract class AthleteFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(AthleteFragmentModule::class))
    internal abstract fun provideAddAthleteFragmentFactory(): AthleteFragment

}