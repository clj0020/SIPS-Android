package com.madmensoftware.sips.data.models.room

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.madmensoftware.sips.data.models.SensorData
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
@Entity(tableName = "test_data",
        indices= arrayOf(
                Index(value = ["athlete"]),
                Index(value = ["testType"])
        ),
        foreignKeys = arrayOf(
                ForeignKey(
                    entity = Athlete::class,
                    parentColumns = arrayOf("_id"),
                    childColumns = arrayOf("athlete"),
                    onDelete = ForeignKey.CASCADE),
                ForeignKey(
                    entity = TestType::class,
                    parentColumns = arrayOf("_id"),
                    childColumns = arrayOf("testType"),
                    onDelete = ForeignKey.CASCADE)
        )
)
class TestData {

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String = ""

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var created_at: String? = null

    @Expose
    @SerializedName("athlete")
    @ColumnInfo(name = "athlete")
    var athlete: String = ""

    @Expose
    @SerializedName("testType")
    @ColumnInfo(name = "testType")
    var testType: String = ""

    @Expose
    @SerializedName("accelerometer_array")
    @ColumnInfo(name = "accelerometer_array")
    var accelerometer_array: List<SensorData> ?= null

    @Expose
    @SerializedName("gyroscope_array")
    @ColumnInfo(name = "gyroscope_array")
    var gyroscope_array: List<SensorData> ?= null

    @Expose
    @SerializedName("magnetometer_array")
    @ColumnInfo(name = "magnetometer_array")
    var magnometer_array: List<SensorData> ?= null

}