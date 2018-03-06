package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by clj00 on 3/2/2018.
 */

class LoginResponse {

    @Expose
    @SerializedName("access_token")
    val accessToken: String? = null

    @Expose
    @SerializedName("fb_profile_pic_url")
    val fbProfilePicUrl: String? = null

    @Expose
    @SerializedName("google_profile_pic_url")
    val googleProfilePicUrl: String? = null

    @Expose
    @SerializedName("message")
    val message: String? = null

    @Expose
    @SerializedName("server_profile_pic_url")
    val serverProfilePicUrl: String? = null

    @Expose
    @SerializedName("status_code")
    val statusCode: String? = null

    @Expose
    @SerializedName("email")
    val userEmail: String? = null

    @Expose
    @SerializedName("organization_id")
    val organizationId: Long? = null

    @Expose
    @SerializedName("user_id")
    val userId: Long? = null

    @Expose
    @SerializedName("user_name")
    val userName: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as LoginResponse?

        if (if (statusCode != null) statusCode != that!!.statusCode else that!!.statusCode != null) {
            return false
        }
        if (if (userId != null) userId != that.userId else that.userId != null) {
            return false
        }
        if (if (accessToken != null) accessToken != that.accessToken else that.accessToken != null) {
            return false
        }
        if (if (userName != null) userName != that.userName else that.userName != null) {
            return false
        }
        if (if (userEmail != null) userEmail != that.userEmail else that.userEmail != null) {
            return false
        }
        if (if (organizationId != null) organizationId != that.organizationId else that.organizationId != null) {
            return false
        }
        if (if (serverProfilePicUrl != null)
                    serverProfilePicUrl != that.serverProfilePicUrl
                else
                    that.serverProfilePicUrl != null) {
            return false
        }
        if (if (fbProfilePicUrl != null)
                    fbProfilePicUrl != that.fbProfilePicUrl
                else
                    that.fbProfilePicUrl != null) {
            return false
        }
        if (if (googleProfilePicUrl != null)
                    googleProfilePicUrl != that.googleProfilePicUrl
                else
                    that.googleProfilePicUrl != null) {
            return false
        }
        return if (message != null) message == that.message else that.message == null

    }

    override fun hashCode(): Int {
        var result = statusCode?.hashCode() ?: 0
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (accessToken?.hashCode() ?: 0)
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + (userEmail?.hashCode() ?: 0)
        result = 31 * result + (organizationId?.hashCode() ?: 0)
        result = 31 * result + (serverProfilePicUrl?.hashCode() ?: 0)
        result = 31 * result + (fbProfilePicUrl?.hashCode() ?: 0)
        result = 31 * result + (googleProfilePicUrl?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }
}