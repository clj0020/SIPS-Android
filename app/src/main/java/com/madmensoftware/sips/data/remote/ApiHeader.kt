package com.madmensoftware.sips.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.madmensoftware.sips.di.ApiInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by clj00 on 3/2/2018.
 */

@Singleton
class ApiHeader @Inject constructor(val publicApiHeader: PublicApiHeader, val protectedApiHeader: ProtectedApiHeader) {

    class ProtectedApiHeader(@field:Expose
                             @field:SerializedName("api_key")
                             var apiKey: String?, @field:Expose
                             @field:SerializedName("user_id")
                             var userId: Long?,
                             @field:Expose
                             @field:SerializedName("access_token")
                             var accessToken: String?)

    class PublicApiHeader @Inject constructor(
            @param:ApiInfo
            @field:Expose
            @field:SerializedName("api_key")
            var apiKey: String?
    )
}
