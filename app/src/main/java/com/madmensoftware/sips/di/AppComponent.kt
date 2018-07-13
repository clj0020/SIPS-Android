package com.madmensoftware.sips.di

import android.app.Application
import com.madmensoftware.sips.SIPSApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton



/**
 * Created by clj00 on 3/2/2018.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class,
    ServiceBuilder::class
])
interface AppComponent {

    fun inject(app: SIPSApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}