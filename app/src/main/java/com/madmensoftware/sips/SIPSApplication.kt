package com.madmensoftware.sips

import android.app.Activity
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.HasActivityInjector
import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.facebook.stetho.Stetho
import com.madmensoftware.sips.di.DaggerAppComponent
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Created by clj00 on 3/2/2018.
 */
class SIPSApplication : Application(), HasActivityInjector {

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var mCalligraphyConfig: CalligraphyConfig


    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        AndroidNetworking.initialize(applicationContext)
        Stetho.initializeWithDefaults(applicationContext)

        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(com.androidnetworking.interceptors.HttpLoggingInterceptor.Level.BODY)
        }

        CalligraphyConfig.initDefault(mCalligraphyConfig)
    }
}