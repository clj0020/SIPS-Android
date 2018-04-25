package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteResponse {

    class GetAthlete {
        @Expose
        @SerializedName("success")
        var success: Boolean? = null

        @Expose
        @SerializedName("msg")
        var msg: String? = null

        @Expose
        @SerializedName("athlete")
        var athlete: Athlete? = null


        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is AthleteResponse.GetAthlete) {
                return false
            }

            val that = other as AthleteResponse.GetAthlete?

            if (success != that!!.success) {
                return false
            }
            if (msg != that.msg) {
                return false
            }
            return athlete == that.athlete
        }

        override fun hashCode(): Int {
            var result = success!!.hashCode()
            result = 31 * result + msg!!.hashCode()
            result = 31 * result + athlete!!.hashCode()
            return result
        }

        class Athlete {

            @Expose
            @SerializedName("_id")
            var _id: String = ""

            @Expose
            @SerializedName("first_name")
            var first_name: String? = null

            @Expose
            @SerializedName("last_name")
            var last_name: String? = null

            @Expose
            @SerializedName("email")
            var email: String? = null

            @Expose
            @SerializedName("status")
            var status: String? = null

            @Expose
            @SerializedName("tests")
            var tests: List<TestData>? = null

            @Expose
            @SerializedName("date_of_birth")
            var date_of_birth: String? = null

            @Expose
            @SerializedName("height")
            var height: Int? = null

            @Expose
            @SerializedName("weight")
            var weight: Int? = null

            @Expose
            @SerializedName("created_at")
            var created_at: String? = null

            @Expose
            @SerializedName("organization")
            var organization: Organization? = null


            @Expose
            @SerializedName("kind")
            var kind: String? = null

//        @Expose
//        @SerializedName("organization")
//        var organization: Organization? = null


            override fun equals(other: Any?): Boolean {
                if (this === other) {
                    return true
                }
                if (other !is Athlete) {
                    return false
                }

                val that = other as Athlete?
                if (_id != that!!._id) {
                    return false
                }
                if (first_name != that.first_name) {
                    return false
                }
                if (last_name != that.last_name) {
                    return false
                }
                if (email != that.email) {
                    return false
                }
                if (status != that.status) {
                    return false
                }
                if (organization != that.organization) {
                    return false
                }
                if (tests != that.tests) {
                    return false
                }
                if (date_of_birth != that.date_of_birth) {
                    return false
                }
                if (height != that.height) {
                    return false
                }
                if (weight != that.weight) {
                    return false
                }
                if (created_at != that.created_at) {
                    return false
                }
//            if (organization != that.organization) {
//                return false
//            }
                return kind == that.kind
            }

            override fun hashCode(): Int {
                var result = _id.hashCode()
                result = 31 * result + first_name!!.hashCode()
                result = 31 * result + last_name!!.hashCode()
                result = 31 * result + email!!.hashCode()
                result = 31 * result + status!!.hashCode()
                result = 31 * result + tests!!.hashCode()
                result = 31 * result + date_of_birth!!.hashCode()
                result = 31 * result + height!!.hashCode()
                result = 31 * result + weight!!.hashCode()
                result = 31 * result + created_at!!.hashCode()
                result = 31 * result + kind!!.hashCode()
                result = 31 * result + organization!!.hashCode()
                return result
            }
        }

        class Organization {

            @Expose
            @SerializedName("_id")
            var _id: String = ""

            @Expose
            @SerializedName("title")
            var title: String? = null

            @Expose
            @SerializedName("createdAt")
            var createdAt: String? = null

            @Expose
            @SerializedName("creator")
            var creator: String? = null


            override fun equals(other: Any?): Boolean {
                if (this === other) {
                    return true
                }
                if (other !is Organization) {
                    return false
                }

                val that = other as Organization?
                if (_id != that!!._id) {
                    return false
                }
                if (title != that.title) {
                    return false
                }
                if (creator != that.creator) {
                    return false
                }
                return createdAt == that.createdAt
            }

            override fun hashCode(): Int {
                var result = _id.hashCode()
                result = 31 * result + title!!.hashCode()
                result = 31 * result + creator!!.hashCode()
                result = 31 * result + createdAt!!.hashCode()
                return result
            }
        }


        class TestData {
            @Expose
            @SerializedName("_id")
            var _id: String = ""

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
            @SerializedName("time")
            var time: Date? = null

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

    class AddAthlete {
        @Expose
        @SerializedName("success")
        var success: Boolean? = null

        @Expose
        @SerializedName("msg")
        var msg: String? = null

        @Expose
        @SerializedName("athlete")
        var athlete: Athlete? = null


        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is AthleteResponse.AddAthlete) {
                return false
            }

            val that = other as AthleteResponse.AddAthlete?

            if (success != that!!.success) {
                return false
            }
            if (msg != that.msg) {
                return false
            }
            return athlete == that.athlete
        }

        override fun hashCode(): Int {
            var result = success!!.hashCode()
            result = 31 * result + msg!!.hashCode()
            result = 31 * result + athlete!!.hashCode()
            return result
        }

        class Athlete {

            @Expose
            @SerializedName("_id")
            var _id: String = ""

            @Expose
            @SerializedName("status")
            var status: String? = null

            @Expose
            @SerializedName("kind")
            var kind: String? = null

            @Expose
            @SerializedName("email")
            var email: String? = null

            @Expose
            @SerializedName("created_at")
            var created_at: String? = null

            @Expose
            @SerializedName("organization")
            var organization: String? = null

            override fun equals(other: Any?): Boolean {
                if (this === other) {
                    return true
                }
                if (other !is Athlete) {
                    return false
                }

                val that = other as Athlete?
                if (_id != that!!._id) {
                    return false
                }
                if (status != that.status) {
                    return false
                }
                if (kind != that.kind) {
                    return false
                }
                if (email != that.email) {
                    return false
                }
                if (status != that.status) {
                    return false
                }
                if (organization != that.organization) {
                    return false
                }
                return created_at == that.created_at
            }

            override fun hashCode(): Int {
                var result = _id.hashCode()
                result = 31 * result + status!!.hashCode()
                result = 31 * result + kind!!.hashCode()
                result = 31 * result + email!!.hashCode()
                result = 31 * result + status!!.hashCode()
                result = 31 * result + organization!!.hashCode()
                result = 31 * result + created_at!!.hashCode()
                return result
            }
        }

        class Organization {

            @Expose
            @SerializedName("_id")
            var _id: String = ""

            @Expose
            @SerializedName("title")
            var title: String? = null

            @Expose
            @SerializedName("createdAt")
            var createdAt: String? = null

            @Expose
            @SerializedName("creator")
            var creator: String? = null


            override fun equals(other: Any?): Boolean {
                if (this === other) {
                    return true
                }
                if (other !is Organization) {
                    return false
                }

                val that = other as Organization?
                if (_id != that!!._id) {
                    return false
                }
                if (title != that.title) {
                    return false
                }
                if (creator != that.creator) {
                    return false
                }
                return createdAt == that.createdAt
            }

            override fun hashCode(): Int {
                var result = _id.hashCode()
                result = 31 * result + title!!.hashCode()
                result = 31 * result + creator!!.hashCode()
                result = 31 * result + createdAt!!.hashCode()
                return result
            }
        }

    }
}