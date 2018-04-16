package com.madmensoftware.sips.data.local.room

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverters
import com.madmensoftware.sips.data.local.room.converters.DateConverter
import com.madmensoftware.sips.data.local.room.converters.SensorDataConverter
import com.madmensoftware.sips.data.local.room.dao.AthleteDao
import com.madmensoftware.sips.data.local.room.dao.OrganizationDao
import com.madmensoftware.sips.data.local.room.dao.TestDataDao
import com.madmensoftware.sips.data.local.room.dao.UserDao
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.Organization
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.User


/**
 * Created by clj00 on 3/2/2018.
 */
@Database(entities = arrayOf(User::class, Athlete::class, TestData::class, Organization::class), version = 5, exportSchema = false)
@TypeConverters(DateConverter::class, SensorDataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun athleteDao(): AthleteDao

    abstract fun testDataDao(): TestDataDao

    abstract fun organizationDao(): OrganizationDao

}
