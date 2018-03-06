package com.madmensoftware.sips.data.local.room.dao

import android.arch.persistence.room.*
import com.madmensoftware.sips.data.models.room.Organization

/**
 * Created by clj00 on 3/6/2018.
 */
@Dao
interface OrganizationDao {

    @Delete
    fun delete(organization: Organization)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organization: Organization)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(organizations: List<Organization>)

    @Query("SELECT * FROM organizations")
    fun loadAll(): List<Organization>

    @Query("SELECT * FROM organizations WHERE id IN (:organizationId)")
    fun loadById(organizationId: Long): Organization

}