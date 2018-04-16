package com.madmensoftware.sips.data.local.room.dao

import android.arch.persistence.room.*
import com.madmensoftware.sips.data.models.room.Athlete

/**
 * Created by clj00 on 3/2/2018.
 */
@Dao
interface AthleteDao {

    @Delete
    fun delete(athlete: Athlete)

//    @Query("SELECT * FROM athletes WHERE name LIKE :name LIMIT 1")
//    fun findByName(name: String): Athlete

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(athlete: Athlete)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(athlete: List<Athlete>)

    @Query("SELECT * FROM athletes")
    fun loadAll(): List<Athlete>

    @Query("SELECT * FROM athletes WHERE _id = (:athleteIds)")
    fun loadAthleteById(athleteIds: String): Athlete

    @Query("SELECT * FROM athletes WHERE _id = (:organizationId)")
    fun loadAthletesByOrganizationId(organizationId: String): List<Athlete>

}