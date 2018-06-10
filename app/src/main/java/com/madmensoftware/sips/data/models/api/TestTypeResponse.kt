package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class TestTypeResponse {

    @Expose
    @SerializedName("success")
    var success: Boolean? = null

    @Expose
    @SerializedName("msg")
    var msg: String? = null

    @Expose
    @SerializedName("testTypes")
    var testTypes: List<TestType>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is TestTypeResponse) {
            return false
        }

        val that = other as TestTypeResponse?

        if (success != that!!.success) {
            return false
        }
        if (msg != that.msg) {
            return false
        }
        return testTypes == that.testTypes
    }

    override fun hashCode(): Int {
        var result = success!!.hashCode()
        result = 31 * result + msg!!.hashCode()
        result = 31 * result + testTypes!!.hashCode()
        return result
    }


    class TestType {
        @Expose
        @SerializedName("_id")
        var _id: String = ""

        @Expose
        @SerializedName("created_at")
        var created_at: String = ""

        @Expose
        @SerializedName("title")
        var title: String = ""

        @Expose
        @SerializedName("description")
        var description: String = ""

        @Expose
        @SerializedName("duration")
        var duration: Int ?= null

        @Expose
        @SerializedName("imageUrl")
        var imageUrl: String ?= null

        @Expose
        @SerializedName("organization")
        var organization: String = ""

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is TestType) {
                return false
            }

            val that = other as TestType?
            if (_id != that!!._id) {
                return false
            }
            if (title != that.title) {
                return false
            }
            if (description != that.description) {
                return false
            }
            if (duration != that.duration) {
                return false
            }
            if (imageUrl != that.imageUrl) {
                return false
            }
            if (organization != that.organization) {
                return false
            }
            return created_at == that.created_at
        }

        override fun hashCode(): Int {
            var result = _id!!.hashCode()
            result = 31 * result + title!!.hashCode()
            result = 31 * result + description!!.hashCode()
            result = 31 * result + duration!!.hashCode()
            result = 31 * result + imageUrl!!.hashCode()
            result = 31 * result + organization!!.hashCode()
            result = 31 * result + created_at!!.hashCode()
            return result
        }
    }
}