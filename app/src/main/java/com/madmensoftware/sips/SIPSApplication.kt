package com.madmensoftware.sips

import android.app.Activity
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.HasActivityInjector
import android.app.Application
import android.app.Service
import com.androidnetworking.AndroidNetworking
import com.facebook.stetho.Stetho
import com.madmensoftware.sips.di.DaggerAppComponent
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.android.HasServiceInjector
import okhttp3.OkHttpClient




/**
 * Created by clj00 on 3/2/2018.
 */
class SIPSApplication : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    internal lateinit var mCalligraphyConfig: CalligraphyConfig


    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    override fun serviceInjector(): DispatchingAndroidInjector<Service>? {
        return serviceDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        // Adding an Network Interceptor for Debugging purpose :
        val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        AndroidNetworking.initialize(applicationContext, okHttpClient)

        Stetho.initializeWithDefaults(applicationContext)

        CalligraphyConfig.initDefault(mCalligraphyConfig)
    }
}