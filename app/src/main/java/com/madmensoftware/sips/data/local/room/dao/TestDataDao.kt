package com.madmensoftware.sips.data.local.room.dao

import android.arch.persistence.room.*
import com.madmensoftware.sips.data.models.room.TestData

/**
 * Created by clj00 on 3/2/2018.
 */
@Dao
interface TestDataDao {

    @Delete
    fun delete(testData: TestData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(testData: TestData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(testData: List<TestData>)

    @Query("SELECT * FROM test_data")
    fun loadAll(): List<TestData>

    @Query("SELECT * FROM test_data WHERE athlete_id = :athleteId")
    fun loadAllByAthleteId(athleteId: Long?): List<TestData>

}