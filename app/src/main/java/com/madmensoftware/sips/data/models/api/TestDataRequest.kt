package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
//import com.madmensoftware.sips.data.models.SensorData
import java.util.*

object TestDataRequest {

    class UploadTestDataRequest(@field:Expose
                             @field:SerializedName("athlete")
                             val athleteId: String?,
                             @field:Expose
                             @field:SerializedName("accelerometer_data")
                             val accelerometer_data: List<SensorData>?,
                             @field:Expose
                             @field:SerializedName("gyroscope_data")
                             val gyroscope_data: List<SensorData>?,
                             @field:Expose
                             @field:SerializedName("magnometer_data")
                             val magnometer_data: List<SensorData>?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val that = other as UploadTestDataRequest?

            if (if (athleteId != null) athleteId != that!!.athleteId else that!!.athleteId != null) {
                return false
            }
            if (if (accelerometer_data != null) accelerometer_data != that!!.accelerometer_data else that!!.accelerometer_data != null) {
                return false
            }
            if (if (gyroscope_data != null) gyroscope_data != that!!.gyroscope_data else that!!.gyroscope_data != null) {
                return false
            }
            return if (magnometer_data != null) magnometer_data == that.magnometer_data else that.magnometer_data == null
        }

        override fun hashCode(): Int {
            var result = athleteId?.hashCode() ?: 0
            result = 31 * result + (accelerometer_data?.hashCode() ?: 0)
            result = 31 * result + (gyroscope_data?.hashCode() ?: 0)
            result = 31 * result + (magnometer_data?.hashCode() ?: 0)
            return result
        }

        class SensorData {

            @Expose
            @SerializedName("time")
            var time: Number? = null

            @Expose
            @SerializedName("x")
            var x: Number? = null

            @Expose
            @SerializedName("y")
            var y: Number? = null

            @Expose
            @SerializedName("z")
            var z: Number? = null

            override fun equals(other: Any?): Boolean {
                if (this === other) {
                    return true
                }
                if (other !is SensorData) {
                    return false
                }

                val that = other as SensorData?
                if (x != that!!.x) {
                    return false
                }
                if (y != that.y) {
                    return false
                }
                if (z != that.z) {
                    return false
                }
                return time == that.time
            }

            override fun hashCode(): Int {
                var result = time!!.hashCode()
                result = 31 * result + x!!.hashCode()
                result = 31 * result + y!!.hashCode()
                result = 31 * result + z!!.hashCode()
                return result
            }
        }

    }




}