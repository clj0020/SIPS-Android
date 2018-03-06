package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by clj00 on 3/2/2018.
 */

object LoginRequest {

    class FacebookLoginRequest(@field:Expose
                               @field:SerializedName("fb_user_id")
                               val fbUserId: String?, @field:Expose
                               @field:SerializedName("fb_access_token")
                               val fbAccessToken: String?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val that = other as FacebookLoginRequest?

            if (if (fbUserId != null) fbUserId != that!!.fbUserId else that!!.fbUserId != null) {
                return false
            }
            return if (fbAccessToken != null)
                fbAccessToken == that.fbAccessToken
            else
                that.fbAccessToken == null
        }

        override fun hashCode(): Int {
            var result = fbUserId?.hashCode() ?: 0
            result = 31 * result + (fbAccessToken?.hashCode() ?: 0)
            return result
        }
    }

    class GoogleLoginRequest(@field:Expose
                             @field:SerializedName("google_user_id")
                             val googleUserId: String?, @field:Expose
                             @field:SerializedName("google_id_token")
                             val idToken: String?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val that = other as GoogleLoginRequest?

            if (if (googleUserId != null)
                        googleUserId != that!!.googleUserId
                    else
                        that!!.googleUserId != null) {
                return false
            }
            return if (idToken != null) idToken == that.idToken else that.idToken == null

        }

        override fun hashCode(): Int {
            var result = googleUserId?.hashCode() ?: 0
            result = 31 * result + (idToken?.hashCode() ?: 0)
            return result
        }
    }

    class ServerLoginRequest(@field:Expose
                             @field:SerializedName("email")
                             val email: String?, @field:Expose
                             @field:SerializedName("password")
                             val password: String?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val that = other as ServerLoginRequest?

            if (if (email != null) email != that!!.email else that!!.email != null) {
                return false
            }
            return if (password != null) password == that.password else that.password == null
        }

        override fun hashCode(): Int {
            var result = email?.hashCode() ?: 0
            result = 31 * result + (password?.hashCode() ?: 0)
            return result
        }
    }
}// This class is not publicly instantiable
