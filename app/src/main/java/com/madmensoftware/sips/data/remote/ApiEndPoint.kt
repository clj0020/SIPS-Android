package com.madmensoftware.sips.data.remote

import com.madmensoftware.sips.BuildConfig

/**
 * Created by clj00 on 3/2/2018.
 */
object ApiEndPoint {

    val ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_URL + "/users/login"

    val ENDPOINT_ADD_ATHLETE = BuildConfig.BASE_URL + "/athletes"

    val ENDPOINT_GET_ATHLETES_FROM_ORGANIZATION = BuildConfig.BASE_URL + "/athletes/organization/{organizationId}"

    val ENDPOINT_GET_ATHLETE_BY_ID = BuildConfig.BASE_URL + "/athletes/{athleteId}"

    val ENDPOINT_ADD_TEST_DATA = BuildConfig.BASE_URL + "/testingData/add"

    val ENDPOINT_GET_TEST_DATA_FOR_ATHLETE = BuildConfig.BASE_URL + "/testingData/get-athlete-test-data/{athleteId}"

    val ENDPOINT_GET_TEST_TYPES_FROM_ORGANIZATION = BuildConfig.BASE_URL + "/testTypes/organization/{organizationId}"

}// This class is not publicly instantiable