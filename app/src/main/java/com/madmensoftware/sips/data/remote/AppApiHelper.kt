package com.madmensoftware.sips.data.remote

import com.madmensoftware.sips.data.models.api.*
import com.madmensoftware.sips.data.models.room.Athlete
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import android.content.ContentValues.TAG
import android.util.Log
import io.reactivex.Observer


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

//    override fun doLogoutApiCall(): Single<LogoutResponse> {
//        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGOUT)
//                .addHeaders(apiHeader.protectedApiHeader)
//                .build()
//                .getObjectSingle<LogoutResponse>(LogoutResponse::class.java)
//    }

    override fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_LOGIN)
                .addHeaders(apiHeader.publicApiHeader)
                .addBodyParameter(request)
                .build()
                .getObjectSingle<LoginResponse>(LoginResponse::class.java)
    }

    override fun getAthletesFromOrganizationServer() : Single<List<Athlete>> {
//        var request: AthleteRequest.GetAllAthletesFromOrganizationRequest = AthleteRequest.GetAllAthletesFromOrganizationRequest(organizationId)
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ATHLETES_FROM_ORGANIZATION)
                .addHeaders(apiHeader.protectedApiHeader)
//                .addQueryParameter(request)
                .build()
                .getObjectSingle<AthleteListResponse>(AthleteListResponse::class.java)
                .map { apiAthleteListResponse: AthleteListResponse? ->
                    mapAthleteListResponseToAthleteModel(apiAthleteListResponse?.athletes ?: emptyList()) }
    }

    fun mapAthleteListResponseToAthleteModel(athleteList: List<AthleteListResponse.Athlete>): List<Athlete> {
        val _listAthletes = mutableListOf<Athlete>()
        for (item in athleteList) {
            val athlete = Athlete()
            athlete.id = item._id
            athlete.created_at = item.created_at
            athlete.date_of_birth = item.date_of_birth
            athlete.email = item.email
            athlete.first_name = item.first_name
            athlete.last_name = item.last_name
            athlete.height = item.height
            athlete.weight = item.weight
            athlete.organization = item.organization!!._id
            _listAthletes.add(athlete)
        }


        return _listAthletes.toList()
    }
}
