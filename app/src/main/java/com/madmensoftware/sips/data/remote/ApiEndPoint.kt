package com.madmensoftware.sips.data.remote

import com.madmensoftware.sips.BuildConfig

/**
 * Created by clj00 on 3/2/2018.
 */
object ApiEndPoint {

    val ENDPOINT_BLOG = BuildConfig.BASE_URL + "/5926ce9d11000096006ccb30"

    val ENDPOINT_FACEBOOK_LOGIN = BuildConfig.BASE_URL + "/588d15d3100000ae072d2944"

    val ENDPOINT_GOOGLE_LOGIN = BuildConfig.BASE_URL + "/588d14f4100000a9072d2943"

//    val ENDPOINT_LOGOUT = BuildConfig.BASE_URL + "/588d161c100000a9072d2946"

    val ENDPOINT_OPEN_SOURCE = BuildConfig.BASE_URL + "/5926c34212000035026871cd"

    val ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_URL + "/users/login"

    val ENDPOINT_GET_ATHLETES_FROM_ORGANIZATION = BuildConfig.BASE_URL + "/athletes/get-athletes-from-organization"

    val ENDPOINT_GET_ATHLETE_BY_ID = BuildConfig.BASE_URL + "/athletes/{athleteId}"

    val ENDPOINT_ADD_TEST_DATA = BuildConfig.BASE_URL + "/testingData/add"

    val ENDPOINT_GET_TEST_TYPES_FROM_ORGANIZATION = BuildConfig.BASE_URL + "/testTypes/organization/{organizationId}"

}// This class is not publicly instantiable