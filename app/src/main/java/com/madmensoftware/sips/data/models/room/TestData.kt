package com.madmensoftware.sips.data.models.room

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
@Entity(tableName = "test_data",
        indices= arrayOf(Index(value = ["athlete_id"])),
        foreignKeys = arrayOf(ForeignKey(
                entity = Athlete::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("athlete_id"),
                onDelete = ForeignKey.CASCADE)
        ))
class TestData {

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null

    @Expose
    @SerializedName("athlete_id")
    @ColumnInfo(name = "athlete_id")
    var athleteId: String = ""

    @Expose
    @SerializedName("tested_at")
    @ColumnInfo(name = "tested_at")
    var testedAt: Date = Date()

    @Expose
    @SerializedName("accelerometer_array")
    @ColumnInfo(name = "accelerometer_array")
    var accelerometerArray: ArrayList<Array<Float>> ?= null

    @Expose
    @SerializedName("gyroscope_array")
    @ColumnInfo(name = "gyroscope_array")
    var gyroscopeArray: ArrayList<Array<Float>> ?= null

    @Expose
    @SerializedName("magnetometer_array")
    @ColumnInfo(name = "magnetometer_array")
    var magnetometerArray: ArrayList<Array<Float>> ?= null

}