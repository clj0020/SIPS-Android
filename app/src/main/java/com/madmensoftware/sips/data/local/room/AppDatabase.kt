package com.madmensoftware.sips.data.local.room

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverters
import com.madmensoftware.sips.data.local.room.converters.DateConverter
import com.madmensoftware.sips.data.local.room.converters.SensorDataConverter
import com.madmensoftware.sips.data.local.room.dao.*
import com.madmensoftware.sips.data.models.SensorData
import com.madmensoftware.sips.data.models.room.*


/**
 * Created by clj00 on 3/2/2018.
 */
@Database(entities = arrayOf(User::class, Athlete::class, TestData::class, Organization::class, TestType::class), version = 11, exportSchema = false)
@TypeConverters(DateConverter::class, SensorDataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun athleteDao(): AthleteDao

    abstract fun testDataDao(): TestDataDao

    abstract fun organizationDao(): OrganizationDao

    abstract fun testTypeDao(): TestTypeDao

}
