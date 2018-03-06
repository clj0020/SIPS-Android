package com.madmensoftware.sips.data.models.room

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by clj00 on 3/2/2018.
 */
@Entity(tableName = "users",
        indices= arrayOf(Index(value = ["organization_id"])),
        foreignKeys = arrayOf(ForeignKey(
                entity = Organization::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("organization_id"),
                onDelete = ForeignKey.CASCADE)
        ))
class User {

    @ColumnInfo(name = "created_at")
    var createdAt: String? = null

    @PrimaryKey
    var id: Long? = null

    @Expose
    @SerializedName("organization_id")
    @ColumnInfo(name = "organization_id")
    var organizationId: Long ?= 0

    var name: String? = null

    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

}