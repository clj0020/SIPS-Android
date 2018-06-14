package com.madmensoftware.sips.ui.athlete_edit

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditAthleteFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(EditAthleteFragmentModule::class))
    internal abstract fun provideEditAthleteFragmentFactory(): EditAthleteFragment

}