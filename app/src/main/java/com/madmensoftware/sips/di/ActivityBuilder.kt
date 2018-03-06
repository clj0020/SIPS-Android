package com.madmensoftware.sips.di

import com.madmensoftware.sips.ui.athlete.AthleteFragmentProvider
import com.madmensoftware.sips.ui.athlete_add.AddAthleteFragmentProvider
import com.madmensoftware.sips.ui.athlete_list.AthleteListFragmentProvider
import com.madmensoftware.sips.ui.athlete_test.TestAthleteFragmentProvider
import dagger.android.ContributesAndroidInjector
import com.madmensoftware.sips.ui.main.MainActivity
import com.madmensoftware.sips.ui.main.MainActivityModule
import com.madmensoftware.sips.ui.login.LoginActivity
import com.madmensoftware.sips.ui.login.LoginActivityModule
import com.madmensoftware.sips.ui.splash.SplashActivity
import com.madmensoftware.sips.ui.splash.SplashActivityModule
import dagger.Module


/**
 * Created by clj00 on 3/2/2018.
 */
@Module
abstract class ActivityBuilder {

//    @ContributesAndroidInjector(modules = arrayOf(FeedActivityModule::class, BlogFragmentProvider::class, OpenSourceFragmentProvider::class))
//    internal abstract fun bindFeedActivity(): FeedActivity

    @ContributesAndroidInjector(modules = arrayOf(LoginActivityModule::class))
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class,
            AthleteListFragmentProvider::class,
            AthleteFragmentProvider::class,
            AddAthleteFragmentProvider::class,
            TestAthleteFragmentProvider::class))
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(SplashActivityModule::class))
    internal abstract fun bindSplashActivity(): SplashActivity

}