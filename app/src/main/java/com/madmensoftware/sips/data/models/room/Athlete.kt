package com.madmensoftware.sips.data.models.room

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by clj00 on 3/2/2018.
 */
@Entity(tableName = "athletes",
        indices= arrayOf(Index(value = ["organization"])),
        foreignKeys = arrayOf(ForeignKey(
                entity = Organization::class,
                parentColumns = arrayOf("_id"),
                childColumns = arrayOf("organization"),
                onDelete = ForeignKey.CASCADE)
        ))
class Athlete {

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String = ""

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var created_at: String? = null

    @Expose
    @SerializedName("organization")
    @ColumnInfo(name = "organization")
    var organization: String = ""

    @Expose
    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    var first_name: String? = null

    @Expose
    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    var last_name: String? = null

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    var status: String? = null

    @Expose
    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String ?= null

    @Expose
    @SerializedName("date_of_birth")
    @ColumnInfo(name = "date_of_birth")
    var date_of_birth: String ?= null

    @Expose
    @SerializedName("height")
    @ColumnInfo(name = "height")
    var height: Int ?= null

    @Expose
    @SerializedName("weight")
    @ColumnInfo(name = "weight")
    var weight: Int ?= null

}
