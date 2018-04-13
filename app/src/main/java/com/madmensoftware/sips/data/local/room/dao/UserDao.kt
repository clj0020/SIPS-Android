package com.madmensoftware.sips.data.local.room.dao

import android.arch.persistence.room.*
import com.madmensoftware.sips.data.models.room.User


/**
 * Created by clj00 on 3/2/2018.
 */
@Dao
interface UserDao {

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users WHERE first_name LIKE :first_name LIMIT 1")
    fun findByName(first_name: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<User>)

    @Query("SELECT * FROM users")
    fun loadAll(): List<User>

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: List<Int>): List<User>

}