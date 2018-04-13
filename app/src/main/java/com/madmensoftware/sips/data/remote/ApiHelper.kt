package com.madmensoftware.sips.data.remote

import com.madmensoftware.sips.data.models.api.*
import com.madmensoftware.sips.data.models.room.Athlete
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by clj00 on 3/2/2018.
 */
interface ApiHelper {

    val apiHeader: ApiHeader

    fun doFacebookLoginApiCall(request: LoginRequest.FacebookLoginRequest): Single<LoginResponse>

    fun doGoogleLoginApiCall(request: LoginRequest.GoogleLoginRequest): Single<LoginResponse>

//    fun doLogoutApiCall(): Single<LogoutResponse>

    fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse>

    fun getAthletesFromOrganizationServer(): Single<List<Athlete>>

}
