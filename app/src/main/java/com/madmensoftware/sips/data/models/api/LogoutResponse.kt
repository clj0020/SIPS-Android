package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by clj00 on 3/2/2018.
 */

class LogoutResponse {

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
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as LogoutResponse?

        if (if (statusCode != null) statusCode != that!!.statusCode else that!!.statusCode != null) {
            return false
        }
        return if (message != null) message == that.message else that.message == null

    }

    override fun hashCode(): Int {
        var result = statusCode?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }
}
