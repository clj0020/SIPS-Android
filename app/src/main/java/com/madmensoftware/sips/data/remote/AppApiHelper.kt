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
import com.google.gson.Gson
import com.madmensoftware.sips.data.models.SensorData
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.TestType
import io.reactivex.Observer
import org.json.JSONObject
import org.json.JSONException






/**
 * Created by clj00 on 3/2/2018.
 */

@Singleton
class AppApiHelper @Inject constructor(override val apiHeader: ApiHeader) : ApiHelper {

    /**
     * User Functions
     */
    override fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_LOGIN)
                .addHeaders(apiHeader.publicApiHeader)
                .addBodyParameter(request)
                .build()
                .getObjectSingle<LoginResponse>(LoginResponse::class.java)
    }

    /**
     * Athlete Functions
     */
    override fun getAthleteByIdServer(athleteId: String): Single<Athlete> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ATHLETE_BY_ID)
                .addHeaders(apiHeader.protectedApiHeader)
                .addPathParameter("athleteId", athleteId)
                .build()
                .getObjectSingle<AthleteResponse.GetAthlete>(AthleteResponse.GetAthlete::class.java)
                .map {apiAthleteResponse: AthleteResponse.GetAthlete? ->
                    mapAthleteResponseToAthleteModel(apiAthleteResponse?.athlete)
                }
    }

    override fun getAthletesFromOrganizationServer(organizationId: String) : Single<List<Athlete>> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ATHLETES_FROM_ORGANIZATION)
                .addHeaders(apiHeader.protectedApiHeader)
                .addPathParameter("organizationId", organizationId)
                .build()
                .getObjectSingle<AthleteListResponse>(AthleteListResponse::class.java)
                .map { apiAthleteListResponse: AthleteListResponse? ->
                    mapAthleteListResponseToAthleteModel(apiAthleteListResponse?.athletes ?: emptyList()) }
    }

    override fun saveAthleteServer(email: String, organization: String): Single<Athlete> {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ADD_ATHLETE)
                .addHeaders(apiHeader.protectedApiHeader)
                .addBodyParameter("email", email)
                .addBodyParameter("organization", organization)
                .build()
                .getObjectSingle<AthleteResponse.AddAthlete>(AthleteResponse.AddAthlete::class.java)
                .map { apiAthleteResponse: AthleteResponse.AddAthlete ->
                    mapAddAthleteResponseToAthleteModel(apiAthleteResponse.athlete!!)
                }
    }

    /**
     * Test Data Functions
     */

    /**
     * Saves test data to server
     */
    override fun saveTestDataServer(request: TestDataRequest.UploadTestDataRequest): Single<TestData> {
        var gson = Gson()
        var accelerometer_data = gson.toJson(request.accelerometer_data)
        var gyroscope_data = gson.toJson(request.gyroscope_data)
        var magnometer_data = gson.toJson(request.magnometer_data)

        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ADD_TEST_DATA)
                .addHeaders(apiHeader.protectedApiHeader)
                .addBodyParameter("athlete", request.athleteId)
                .addBodyParameter("testType", request.testTypeId)
                .addBodyParameter("accelerometer_data", accelerometer_data)
                .addBodyParameter("gyroscope_data", gyroscope_data)
                .addBodyParameter("magnometer_data", magnometer_data)
                .build()
                .getObjectSingle<TestDataResponse.AddTest>(TestDataResponse.AddTest::class.java)
                .map { apiTestDataResponse: TestDataResponse.AddTest ->
                    mapTestDataResponseToTestDataModel(apiTestDataResponse.testData!!)
                }
    }

    override fun getTestDataForAthleteServer(athleteId: String): Single<List<TestData>> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_TEST_DATA_FOR_ATHLETE)
                .addHeaders(apiHeader.protectedApiHeader)
                .addPathParameter("athleteId", athleteId)
                .build()
                .getObjectSingle<TestDataResponse.GetTestDataForAthlete>(TestDataResponse.GetTestDataForAthlete::class.java)
                .map { apiTestDataListResponse: TestDataResponse.GetTestDataForAthlete ->
                    mapTestDataListResponseToTestDataModel(apiTestDataListResponse.testDataList ?: emptyList()) }
    }

    /**
     * Test Type Functions
     */
    override fun getTestTypesFromOrganizationServer(organizationId: String): Single<List<TestType>> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_TEST_TYPES_FROM_ORGANIZATION)
                .addHeaders(apiHeader.protectedApiHeader)
                .addPathParameter("organizationId", organizationId)
                .build()
                .getObjectSingle<TestTypeResponse>(TestTypeResponse::class.java)
                .map { apiTestTypeListResponse: TestTypeResponse? ->
                    mapTestTypeListResponseToTestTypeModel(apiTestTypeListResponse?.testTypes ?: emptyList()) }
    }

    /**
     * Response Mappers
     */
    fun mapAthleteResponseToAthleteModel(athleteResponse: AthleteResponse.GetAthlete.Athlete?): Athlete {
        val athlete = Athlete()
        athlete._id = athleteResponse!!._id
        athlete.created_at = athleteResponse.created_at
        athlete.date_of_birth = athleteResponse.date_of_birth
        athlete.email = athleteResponse.email
        athlete.first_name = athleteResponse.first_name
        athlete.last_name = athleteResponse.last_name
        athlete.status = athleteResponse.status
        athlete.height = athleteResponse.height
        athlete.weight = athleteResponse.weight
        athlete.profileImageUrl = athleteResponse.profileImageUrl
        athlete.organization = athleteResponse.organization!!
        return athlete
    }

    fun mapAddAthleteResponseToAthleteModel(athleteResponse: AthleteResponse.AddAthlete.Athlete?): Athlete {
        val athlete = Athlete()
        athlete._id = athleteResponse!!._id
        athlete.created_at = athleteResponse.created_at
        athlete.email = athleteResponse.email
        athlete.status = athleteResponse.status
        athlete.organization = athleteResponse.organization!!
        return athlete
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
            athlete.status = item.status
            athlete.height = item.height
            athlete.weight = item.weight
            athlete.profileImageUrl = item.profileImageUrl
            athlete.organization = item.organization!!._id
            _listAthletes.add(athlete)
        }

        return _listAthletes.toList()
    }

    fun mapTestDataListResponseToTestDataModel(testDataList: List<TestDataResponse.GetTestDataForAthlete.TestData>): List<TestData> {
        val _listTestData = mutableListOf<TestData>()
        for (item in testDataList) {
            val testData = TestData()
            testData._id = item._id!!
            testData.created_at = item.created_at
            testData.athlete = item.athlete!!
            testData.testType = item.testType!!._id

            val _listAccelerometerData = mutableListOf<SensorData>()
            for (dataItem in item.accelerometer_data!!) {
                val sensorData = SensorData()
                sensorData._id = dataItem._id
                sensorData.time = dataItem.time?.toFloat()
                sensorData.x = dataItem.x?.toFloat()
                sensorData.y = dataItem.y?.toFloat()
                sensorData.z = dataItem.z?.toFloat()
                _listAccelerometerData.add(sensorData)
            }

            val _listGyroscopeData = mutableListOf<SensorData>()
            for (dataItem in item.gyroscope_data!!) {
                val sensorData = SensorData()
                sensorData._id = dataItem._id
                sensorData.time = dataItem.time?.toFloat()
                sensorData.x = dataItem.x?.toFloat()
                sensorData.y = dataItem.y?.toFloat()
                sensorData.z = dataItem.z?.toFloat()
                _listGyroscopeData.add(sensorData)
            }

            val _listMagnometerData = mutableListOf<SensorData>()
            for (dataItem in item.magnometer_data!!) {
                val sensorData = SensorData()
                sensorData._id = dataItem._id
                sensorData.time = dataItem.time?.toFloat()
                sensorData.x = dataItem.x?.toFloat()
                sensorData.y = dataItem.y?.toFloat()
                sensorData.z = dataItem.z?.toFloat()
                _listMagnometerData.add(sensorData)
            }


            testData.accelerometer_array = _listAccelerometerData.toList()
            testData.gyroscope_array = _listGyroscopeData.toList()
            testData.magnometer_array = _listMagnometerData.toList()

            _listTestData.add(testData)
        }

        return _listTestData.toList()
    }

    fun mapTestDataResponseToTestDataModel(testDataApi: TestDataResponse.AddTest.TestData): TestData {
        val testData = TestData()
        testData._id = testDataApi._id!!
        testData.created_at = testDataApi.created_at
        testData.athlete = testDataApi.athlete!!
        testData.testType = testDataApi.testType!!

        val _listAccelerometerData = mutableListOf<SensorData>()
        for (dataItem in testDataApi.accelerometer_data!!) {
            val sensorData = SensorData()
            sensorData._id = dataItem._id
            sensorData.time = dataItem.time?.toFloat()
            sensorData.x = dataItem.x?.toFloat()
            sensorData.y = dataItem.y?.toFloat()
            sensorData.z = dataItem.z?.toFloat()
            _listAccelerometerData.add(sensorData)
        }

        val _listGyroscopeData = mutableListOf<SensorData>()
        for (dataItem in testDataApi.gyroscope_data!!) {
            val sensorData = SensorData()
            sensorData._id = dataItem._id
            sensorData.time = dataItem.time?.toFloat()
            sensorData.x = dataItem.x?.toFloat()
            sensorData.y = dataItem.y?.toFloat()
            sensorData.z = dataItem.z?.toFloat()
            _listGyroscopeData.add(sensorData)
        }

        val _listMagnometerData = mutableListOf<SensorData>()
        for (dataItem in testDataApi.magnometer_data!!) {
            val sensorData = SensorData()
            sensorData._id = dataItem._id
            sensorData.time = dataItem.time?.toFloat()
            sensorData.x = dataItem.x?.toFloat()
            sensorData.y = dataItem.y?.toFloat()
            sensorData.z = dataItem.z?.toFloat()
            _listMagnometerData.add(sensorData)
        }

        testData.accelerometer_array = _listAccelerometerData.toList()
        testData.gyroscope_array = _listGyroscopeData.toList()
        testData.magnometer_array = _listMagnometerData.toList()

        return testData
    }

    fun mapTestTypeListResponseToTestTypeModel(testTypeList: List<TestTypeResponse.TestType>): List<TestType> {
        val _listTestTypes = mutableListOf<TestType>()
        for (item in testTypeList) {
            val testType = TestType()
            testType._id = item._id
            testType.created_at = item.created_at
            testType.title = item.title
            testType.description = item.description
            testType.duration = item.duration
            testType.imageUrl = item.imageUrl
            testType.organization = item.organization
            _listTestTypes.add(testType)
        }

        return _listTestTypes.toList()
    }

}
