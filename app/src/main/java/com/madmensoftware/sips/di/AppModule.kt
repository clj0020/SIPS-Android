package com.madmensoftware.sips.di

import dagger.Provides
import javax.inject.Singleton
import com.madmensoftware.sips.util.AppConstants
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.madmensoftware.sips.BuildConfig
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.AppDataManager
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.local.prefs.AppPreferencesHelper
import com.madmensoftware.sips.data.local.prefs.PreferencesHelper
import com.madmensoftware.sips.data.local.room.AppDatabase
import com.madmensoftware.sips.data.local.room.AppDbHelper
import com.madmensoftware.sips.data.local.room.DbHelper
import com.madmensoftware.sips.data.remote.ApiHeader
import com.madmensoftware.sips.data.remote.ApiHelper
import com.madmensoftware.sips.data.remote.AppApiHelper
import com.madmensoftware.sips.util.AppSchedulerProvider
import com.madmensoftware.sips.util.SchedulerProvider
import dagger.Module
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Created by clj00 on 3/2/2018.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper {
        return appApiHelper
    }

//    @Provides
//    @ApiInfo
//    internal fun provideApiKey(): String {
//        return BuildConfig.API_KEY
//    }

    @Provides
    @Singleton
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase {
//        val mockAthletes = DataGenerator.generateAthletes()

        val database = Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration()
                .build()

//        database.runInTransaction {
//            database.athleteDao().insertAll(mockAthletes)
////                database.testDataDao().insertAll(testData)
//        }

        return database
    }

    @Provides
    @Singleton
    internal fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
        return CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @DatabaseInfo
    internal fun provideDatabaseName(): String {
        return AppConstants.DB_NAME
    }

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @Singleton
    internal fun provideProtectedApiHeader(preferencesHelper: PreferencesHelper): ApiHeader.ProtectedApiHeader {
        return ApiHeader.ProtectedApiHeader(
                preferencesHelper.accessToken)
    }

    @Provides
    @Singleton
    internal fun providePublicApiHeader(): ApiHeader.PublicApiHeader {
        return ApiHeader.PublicApiHeader()
    }


    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

}