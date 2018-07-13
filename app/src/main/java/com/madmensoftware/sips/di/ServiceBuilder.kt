package com.madmensoftware.sips.di

import com.madmensoftware.sips.data.services.AthleteActivityRecognitionService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector(modules = arrayOf(AthleteActivityRecognitionService::class))
    internal abstract fun bindAthleteActivityRecognitionService(): AthleteActivityRecognitionService

}