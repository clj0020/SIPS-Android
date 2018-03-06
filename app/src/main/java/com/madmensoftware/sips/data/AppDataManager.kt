package com.madmensoftware.sips.data

import android.content.Context
import com.google.gson.Gson
import com.madmensoftware.sips.data.local.prefs.PreferencesHelper
import com.madmensoftware.sips.data.local.room.DbHelper
import com.madmensoftware.sips.data.models.api.LoginRequest
import com.madmensoftware.sips.data.models.api.LoginResponse
import com.madmensoftware.sips.data.models.api.LogoutResponse
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.User
import com.madmensoftware.sips.data.remote.ApiHeader
import com.madmensoftware.sips.data.remote.ApiHelper
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton



/**
 * Created by clj00 on 3/2/2018.
 */
@Singleton
class AppDataManager @Inject constructor(private val mContext: Context,
                                         private val mDbHelper: DbHelper,
                                         private val mPreferencesHelper: PreferencesHelper,
                                         private val mApiHelper: ApiHelper, private val mGson: Gson) : DataManager {

    override var accessToken: String?
        get() = mPreferencesHelper.accessToken
        set(accessToken) {
            mPreferencesHelper.accessToken = accessToken
            mApiHelper.apiHeader.protectedApiHeader.accessToken = accessToken
        }

    override val allUsers: Observable<List<User>>
        get() = mDbHelper.allUsers

    override val apiHeader: ApiHeader
        get() = mApiHelper.apiHeader

    override var currentUserEmail: String?
        get() = mPreferencesHelper.currentUserEmail
        set(email) {
            mPreferencesHelper.currentUserEmail = email
        }

    override var currentUserId: Long?
        get() = mPreferencesHelper.currentUserId
        set(userId) {
            mPreferencesHelper.currentUserId = userId
        }

    override val currentUserLoggedInMode: Int
        get() = mPreferencesHelper.currentUserLoggedInMode

    override var currentUserName: String?
        get() = mPreferencesHelper.currentUserName
        set(userName) {
            mPreferencesHelper.currentUserName = userName
        }

    override var currentUserProfilePicUrl: String?
        get() = mPreferencesHelper.currentUserProfilePicUrl
        set(profilePicUrl) {
            mPreferencesHelper.currentUserProfilePicUrl = profilePicUrl
        }

    override var currentUserOrganizationId: Long?
        get() = mPreferencesHelper.currentUserOrganizationId
        set(organizationId) {
            mPreferencesHelper.currentUserOrganizationId = organizationId
        }

    override fun doFacebookLoginApiCall(request: LoginRequest.FacebookLoginRequest): Single<LoginResponse> {
        return mApiHelper.doFacebookLoginApiCall(request)
    }

    override fun doGoogleLoginApiCall(request: LoginRequest.GoogleLoginRequest): Single<LoginResponse> {
        return mApiHelper.doGoogleLoginApiCall(request)
    }

    override fun doLogoutApiCall(): Single<LogoutResponse> {
        return mApiHelper.doLogoutApiCall()
    }

    override fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse> {
        return mApiHelper.doServerLoginApiCall(request)
    }

    override fun setCurrentUserLoggedInMode(mode: DataManager.LoggedInMode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode)
    }

    override fun insertUser(user: User): Observable<Boolean> {
        return mDbHelper.insertUser(user)
    }

    override fun setUserAsLoggedOut() {
        updateUserInfo(null, null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT, null, null, null, null)
    }

    override fun updateApiHeader(userId: Long?, accessToken: String?) {
        mApiHelper.apiHeader.protectedApiHeader.userId = userId
        mApiHelper.apiHeader.protectedApiHeader.accessToken = accessToken
    }

    override fun updateUserInfo(accessToken: String?, userId: Long?, loggedInMode: DataManager.LoggedInMode, userName: String?, email: String?, profilePicPath: String?, organizationId: Long?) {

        this.accessToken = accessToken
        currentUserId = userId
        setCurrentUserLoggedInMode(loggedInMode)
        currentUserName = userName
        currentUserEmail = email
        currentUserProfilePicUrl = profilePicPath
        currentUserOrganizationId = organizationId

        updateApiHeader(userId, accessToken)
    }

    override fun getAllAthletes(): Observable<List<Athlete>> {
        return mDbHelper.getAllAthletes()
    }

    override fun getAthlete(athleteId: Long): Observable<Athlete> {
        return mDbHelper.getAthlete(athleteId)
    }

    override fun getTestDataForAthleteId(athleteId: Long?): Observable<List<TestData>> {
        return mDbHelper.getTestDataForAthleteId(athleteId)
    }

    override fun saveTestData(testData: TestData): Observable<Boolean> {
        return mDbHelper.saveTestData(testData)
    }

    override fun saveTestDataList(testDataList: List<TestData>): Observable<Boolean> {
        return mDbHelper.saveTestDataList(testDataList)
    }

    override fun saveAthlete(athlete: Athlete): Observable<Boolean> {
        return mDbHelper.saveAthlete(athlete)
    }

    override fun saveAthleteList(athleteList: List<Athlete>): Observable<Boolean> {
        return mDbHelper.saveAthleteList(athleteList)
    }

}