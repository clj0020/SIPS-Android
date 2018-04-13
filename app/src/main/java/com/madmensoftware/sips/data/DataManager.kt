package com.madmensoftware.sips.data

import com.madmensoftware.sips.data.local.prefs.PreferencesHelper
import com.madmensoftware.sips.data.local.room.DbHelper
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.remote.ApiHelper
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
interface DataManager : DbHelper, ApiHelper, PreferencesHelper {

//    val questionCardData: Observable<List<QuestionCardData>>

    fun setUserAsLoggedOut()

    fun updateApiHeader(accessToken: String?)

    fun updateUserInfo(
            _id: String,
            accessToken: String?,
            loggedInMode: LoggedInMode,
            first_name: String?,
            last_name: String?,
            email: String?,
            status: String?,
            organizationId: String?,
            kind: String?)

    fun logout()

    enum class LoggedInMode private constructor(val type: Int) {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3)
    }


//    fun getAthleteList(organizationId: String, scheduler: Scheduler): Observable<List<Athlete>?>

    fun getAthleteList(): Observable<List<Athlete>?>

}