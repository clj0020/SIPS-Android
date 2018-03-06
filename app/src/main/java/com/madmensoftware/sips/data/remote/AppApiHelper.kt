package com.madmensoftware.sips.data.remote

import com.madmensoftware.sips.data.models.api.LoginRequest
import com.madmensoftware.sips.data.models.api.LoginResponse
import com.madmensoftware.sips.data.models.api.LogoutResponse
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by clj00 on 3/2/2018.
 */

@Singleton
class AppApiHelper @Inject constructor(override val apiHeader: ApiHeader) : ApiHelper {

    override fun doFacebookLoginApiCall(request: LoginRequest.FacebookLoginRequest): Single<LoginResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_FACEBOOK_LOGIN)
                .addHeaders(apiHeader.publicApiHeader)
                .addBodyParameter(request)
                .build()
                .getObjectSingle<LoginResponse>(LoginResponse::class.java)
    }

    override fun doGoogleLoginApiCall(request: LoginRequest.GoogleLoginRequest): Single<LoginResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_GOOGLE_LOGIN)
                .addHeaders(apiHeader.publicApiHeader)
                .addBodyParameter(request)
                .build()
                .getObjectSingle<LoginResponse>(LoginResponse::class.java)
    }

    override fun doLogoutApiCall(): Single<LogoutResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGOUT)
                .addHeaders(apiHeader.protectedApiHeader)
                .build()
                .getObjectSingle<LogoutResponse>(LogoutResponse::class.java)
    }

    override fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_LOGIN)
                .addHeaders(apiHeader.publicApiHeader)
                .addBodyParameter(request)
                .build()
                .getObjectSingle<LoginResponse>(LoginResponse::class.java)
    }
}
