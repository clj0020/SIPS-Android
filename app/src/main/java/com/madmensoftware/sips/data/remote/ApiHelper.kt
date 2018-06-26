package com.madmensoftware.sips.data.remote

import com.madmensoftware.sips.data.models.api.*
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.TestType
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 * Created by clj00 on 3/2/2018.
 */
interface ApiHelper {

    val apiHeader: ApiHeader

    fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse>

    fun saveAthleteServer(email: String, organization: String): Single<Athlete>

    fun getAthletesFromOrganizationServer(organizationId: String): Single<List<Athlete>>

    fun editAthleteServer(athlete: Athlete): Single<Athlete>

    fun getAthleteByIdServer(athleteId: String): Single<Athlete>

    fun uploadAthleteProfileImageServer(athleteId: String, profileImage: File): Single<Athlete>

    fun saveTestDataServer(request: TestDataRequest.UploadTestDataRequest): Single<TestData>

    fun getTestTypesFromOrganizationServer(organizationId: String): Single<List<TestType>>

    fun getTestDataForAthleteServer(athleteId: String): Single<List<TestData>>

}
