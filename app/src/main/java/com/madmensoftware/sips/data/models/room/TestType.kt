package com.madmensoftware.sips.data.models.room

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "test_type",
        indices= arrayOf(Index(value = ["organization"])),
        foreignKeys = arrayOf(ForeignKey(
                entity = Organization::class,
                parentColumns = arrayOf("_id"),
                childColumns = arrayOf("organization"),
                onDelete = ForeignKey.CASCADE)
        ))
class TestType {

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String = ""

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var created_at: String = ""

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String = ""

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String = ""

    @Expose
    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    var duration: Int ?= null

    @Expose
    @SerializedName("organization")
    @ColumnInfo(name = "organization")
    var organization: String = ""



}