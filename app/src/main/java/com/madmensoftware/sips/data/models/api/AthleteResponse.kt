package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteResponse {

    @Expose
    @SerializedName("data")
    val data: List<Athlete>? = null

    @Expose
    @SerializedName("message")
    val message: String? = null

    @Expose
    @SerializedName("status_code")
    val statusCode: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is AthleteResponse) {
            return false
        }

        val that = other as AthleteResponse?

        if (statusCode != that!!.statusCode) {
            return false
        }
        return if (message != that.message) {
            false
        } else data == that.data

    }

    override fun hashCode(): Int {
        var result = statusCode!!.hashCode()
        result = 31 * result + message!!.hashCode()
        result = 31 * result + data!!.hashCode()
        return result
    }

    class Athlete {

        @Expose
        @SerializedName("first_name")
        val first_name: String = ""

        @Expose
        @SerializedName("last_name")
        val last_name: String = ""

        @Expose
        @SerializedName("created_at")
        val created_at: String = ""

        @Expose
        @SerializedName("test_data")
        val data: List<TestData>? = null



        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is Athlete) {
                return false
            }

            val athlete = other as Athlete?

            if (first_name != athlete!!.first_name) {
                return false
            }
            if (last_name != athlete!!.last_name) {
                return false
            }
            return created_at == athlete!!.created_at

        }

        override fun hashCode(): Int {
            var result = first_name.hashCode()
            result = 31 * result + last_name.hashCode()
            result = 31 * result + created_at.hashCode()
            return result
        }
    }

    class TestData {

        @Expose
        @SerializedName("created_at")
        val created_at: String = ""

        @Expose
        @SerializedName("athlete_id")
        var athleteId: Int ?= 0

        @Expose
        @SerializedName("tested_at")
        var testedAt: Date = Date()

        @Expose
        @SerializedName("accelerometer_array")
        var accelerometerArray: ArrayList<Array<Float>>?= null

        @Expose
        @SerializedName("gyroscope_array")
        var gyroscopeArray: ArrayList<Array<Float>>?= null

        @Expose
        @SerializedName("magnetometer_array")
        var magnetometerArray: ArrayList<Array<Float>>?= null


        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is TestData) {
                return false
            }

            val testData = other

            if (created_at != testData.created_at) {
                return false
            }
            if (athleteId != testData.athleteId) {
                return false
            }
            if (testedAt != testData.testedAt) {
                return false
            }
            if (accelerometerArray != testData.accelerometerArray) {
                return false
            }
            if (gyroscopeArray != testData.gyroscopeArray) {
                return false
            }
            if (magnetometerArray != testData.magnetometerArray) {
                return false
            }
            return created_at == testData.created_at

        }

        override fun hashCode(): Int {
            var result = created_at.hashCode()
            result = 31 * result + athleteId!!.hashCode()
            result = 31 * result + testedAt.hashCode()
            result = 31 * result + accelerometerArray!!.hashCode()
            result = 31 * result + gyroscopeArray!!.hashCode()
            result = 31 * result + magnetometerArray!!.hashCode()
            return result
        }
    }
}