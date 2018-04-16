package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class TestDataResponse {

    @Expose
    @SerializedName("success")
    var success: Boolean? = null

    @Expose
    @SerializedName("msg")
    var msg: String? = null

    @Expose
    @SerializedName("testData")
    var testData: TestData? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is TestDataResponse) {
            return false
        }

        val that = other as TestDataResponse?

        if (success != that!!.success) {
            return false
        }
        if (msg != that.msg) {
            return false
        }
        return testData == that.testData
    }

    override fun hashCode(): Int {
        var result = success!!.hashCode()
        result = 31 * result + msg!!.hashCode()
        result = 31 * result + testData!!.hashCode()
        return result
    }


    class TestData {
        @Expose
        @SerializedName("_id")
        var _id: String ?= null

        @Expose
        @SerializedName("athlete")
        var athlete: String? = null

        @Expose
        @SerializedName("tester")
        var tester: String? = null

        @Expose
        @SerializedName("created_at")
        var created_at: Date? = null

        @Expose
        @SerializedName("accelerometer_data")
        var accelerometer_data: List<SensorData>? = null

        @Expose
        @SerializedName("gyroscope_data")
        var gyroscope_data: List<SensorData>? = null

        @Expose
        @SerializedName("magnometer_data")
        var magnometer_data: List<SensorData>? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is TestData) {
                return false
            }

            val that = other as TestData?
            if (_id != that!!._id) {
                return false
            }
            if (athlete != that.athlete) {
                return false
            }
            if (tester != that.tester) {
                return false
            }
            if (accelerometer_data != that.accelerometer_data) {
                return false
            }
            if (gyroscope_data != that.gyroscope_data) {
                return false
            }
            if (magnometer_data != that.magnometer_data) {
                return false
            }
            return created_at == that.created_at
        }

        override fun hashCode(): Int {
            var result = _id!!.hashCode()
            result = 31 * result + athlete!!.hashCode()
            result = 31 * result + tester!!.hashCode()
            result = 31 * result + accelerometer_data!!.hashCode()
            result = 31 * result + gyroscope_data!!.hashCode()
            result = 31 * result + magnometer_data!!.hashCode()
            result = 31 * result + created_at!!.hashCode()
            return result
        }
    }

    class SensorData {

        @Expose
        @SerializedName("_id")
        var _id: String? = null

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
            if (_id != that!!._id) {
                return false
            }
            if (x != that.x) {
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
            result = 31 * result + _id!!.hashCode()
            result = 31 * result + x!!.hashCode()
            result = 31 * result + y!!.hashCode()
            result = 31 * result + z!!.hashCode()
            return result
        }
    }
}