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
import com.madmensoftware.sips.data.models.room.TestData
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

    override fun getAthleteByIdServer(athleteId: String): Single<Athlete> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ATHLETE_BY_ID)
                .addHeaders(apiHeader.protectedApiHeader)
                .addPathParameter("athleteId", athleteId)
                .build()
                .getObjectSingle<AthleteResponse>(AthleteResponse::class.java)
                .map {apiAthleteResponse: AthleteResponse? ->
                    mapAthleteResponseToAthleteModel(apiAthleteResponse?.athlete)
                }
    }

    fun mapAthleteListResponseToAthleteModel(athleteList: List<AthleteListResponse.Athlete>): List<Athlete> {
        val _listAthletes = mutableListOf<Athlete>()
        for (item in athleteList) {
            val athlete = Athlete()
            athlete._id = item._id
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

    fun mapAthleteResponseToAthleteModel(athleteResponse: AthleteResponse.Athlete?): Athlete {
        val athlete = Athlete()
        athlete._id = athleteResponse!!._id
        athlete.created_at = athleteResponse.created_at
        athlete.date_of_birth = athleteResponse.date_of_birth
        athlete.email = athleteResponse.email
        athlete.first_name = athleteResponse.first_name
        athlete.last_name = athleteResponse.last_name
        athlete.height = athleteResponse.height
        athlete.weight = athleteResponse.weight
        athlete.organization = athleteResponse.organization!!._id
        return athlete
    }

    override fun saveTestDataServer(request: TestDataRequest.UploadTestDataRequest): Single<TestDataResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ADD_TEST_DATA)
                .addHeaders(apiHeader.protectedApiHeader)
                .addBodyParameter(request)
                .build()
                .getObjectSingle<TestDataResponse>(TestDataResponse::class.java)
    }
}
