package com.madmensoftware.sips.data.local.room.dao

import android.arch.persistence.room.*
import com.madmensoftware.sips.data.models.room.TestType

@Dao
interface TestTypeDao {

    @Delete
    fun delete(testType: TestType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(testType: TestType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(testType: List<TestType>)

    @Query("SELECT * FROM test_type")
    fun loadAll(): List<TestType>

    @Query("SELECT * FROM test_type WHERE organization = (:organization)")
    fun loadAllByOrganizationId(organization: String): List<TestType>

}