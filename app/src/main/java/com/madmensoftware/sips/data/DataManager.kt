package com.madmensoftware.sips.data

import com.madmensoftware.sips.data.local.prefs.PreferencesHelper
import com.madmensoftware.sips.data.local.room.DbHelper
import com.madmensoftware.sips.data.remote.ApiHelper

/**
 * Created by clj00 on 3/2/2018.
 */
interface DataManager : DbHelper, ApiHelper, PreferencesHelper {

//    val questionCardData: Observable<List<QuestionCardData>>

    fun setUserAsLoggedOut()

    fun updateApiHeader(userId: Long?, accessToken: String?)

    fun updateUserInfo(
            accessToken: String?,
            userId: Long?,
            loggedInMode: LoggedInMode,
            userName: String?,
            email: String?,
            profilePicPath: String?,
            organizationId: Long?)

    enum class LoggedInMode private constructor(val type: Int) {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3)
    }


}