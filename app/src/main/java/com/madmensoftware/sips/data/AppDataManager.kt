package com.madmensoftware.sips.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.madmensoftware.sips.data.local.prefs.PreferencesHelper
import com.madmensoftware.sips.data.local.room.DbHelper
import com.madmensoftware.sips.data.models.api.*
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.Organization
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.User
import com.madmensoftware.sips.data.remote.ApiHeader
import com.madmensoftware.sips.data.remote.ApiHelper
import com.madmensoftware.sips.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
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
                                         private val schedulerProvider: SchedulerProvider,
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

    override var currentUserOrganizationId: String?
        get() = mPreferencesHelper.currentUserOrganizationId
        set(organization) {
            mPreferencesHelper.currentUserOrganizationId = organization
        }

    override var currentUserId: String?
        get() = mPreferencesHelper.currentUserId
        set(userId) {
            mPreferencesHelper.currentUserId = userId
        }

    override val currentUserLoggedInMode: Int
        get() = mPreferencesHelper.currentUserLoggedInMode

    override var currentUserFirstName: String?
        get() = mPreferencesHelper.currentUserFirstName
        set(userFirstName) {
            mPreferencesHelper.currentUserFirstName = userFirstName
        }

    override var currentUserLastName: String?
        get() = mPreferencesHelper.currentUserLastName
        set(userLastName) {
            mPreferencesHelper.currentUserLastName = userLastName
        }

    override var currentUserProfilePicUrl: String?
        get() = mPreferencesHelper.currentUserProfilePicUrl
        set(profilePicUrl) {
            mPreferencesHelper.currentUserProfilePicUrl = profilePicUrl
        }


    override var currentUserStatus: String?
        get() = mPreferencesHelper.currentUserStatus
        set(status) {
            mPreferencesHelper.currentUserStatus = status
        }

    override var currentUserKind: String?
        get() = mPreferencesHelper.currentUserKind
        set(status) {
            mPreferencesHelper.currentUserKind = status
        }

    override fun doFacebookLoginApiCall(request: LoginRequest.FacebookLoginRequest): Single<LoginResponse> {
        return mApiHelper.doFacebookLoginApiCall(request)
    }

    override fun doGoogleLoginApiCall(request: LoginRequest.GoogleLoginRequest): Single<LoginResponse> {
        return mApiHelper.doGoogleLoginApiCall(request)
    }

//    override fun doLogoutCall(): Single<LogoutResponse> {
//        return mPreferencesHelper.doLogoutApiCall()
//    }

    override fun logout() {
        setUserAsLoggedOut()
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
        updateUserInfo("",null, DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT, null, null, null, null, null, null)
    }

    override fun updateApiHeader(accessToken: String?) {
        mApiHelper.apiHeader.protectedApiHeader.accessToken = accessToken
    }

    override fun updateUserInfo(_id: String, accessToken: String?, loggedInMode: DataManager.LoggedInMode,
                                first_name: String?, last_name: String?, email: String?,
                                status: String?, organizationId: String?, kind: String?) {

        this.accessToken = accessToken
        setCurrentUserLoggedInMode(loggedInMode)
        this.currentUserId = _id
        this.currentUserFirstName = first_name
        this.currentUserLastName = last_name
        this.currentUserEmail = email
        this.currentUserStatus = status
        this.currentUserOrganizationId = organizationId
        this.currentUserKind = kind

        updateApiHeader(accessToken)
    }

    override fun getAthleteList(): Observable<List<Athlete>?> {
        val remoteSource: Single<List<Athlete>> = mApiHelper.getAthletesFromOrganizationServer().subscribeOn(schedulerProvider.io())

        return mDbHelper.getAllAthletesFromOrganization(this.currentUserOrganizationId!!)
                .flatMap { listFromLocal: List<Athlete> ->
                    remoteSource
                            .observeOn(schedulerProvider.computation())
                            .toObservable()
                            .filter { apiAthleteList: List<Athlete> ->
                                apiAthleteList != listFromLocal
                            }
                            .flatMapSingle { apiAthleteList ->
                                mDbHelper.saveAthleteList(apiAthleteList)
                                        .andThen(Single.just(apiAthleteList))
                            }
                            .startWith(listFromLocal)
                }
    }

    override fun getAthletesFromOrganizationServer(): Single<List<Athlete>> {
        return mApiHelper.getAthletesFromOrganizationServer()
    }

    override fun getAllAthletesFromOrganization(organizationId: String): Observable<List<Athlete>> {
        return mDbHelper.getAllAthletesFromOrganization(organizationId)
    }

    override fun saveAthleteList(athleteList: List<Athlete>): Completable {
        return mDbHelper.saveAthleteList(athleteList)
    }

    override fun getAthlete(athleteId: String): Observable<Athlete> {
        val remoteSource: Single<Athlete> = mApiHelper.getAthleteByIdServer(athleteId).subscribeOn(schedulerProvider.io())

        return mDbHelper.getAthleteFromDatabase(athleteId)
                .flatMap { athleteFromLocal: Athlete ->
                    remoteSource
                            .observeOn(schedulerProvider.computation())
                            .toObservable()
                            .filter { apiAthlete: Athlete ->
                                apiAthlete != athleteFromLocal
                            }
                            .flatMapSingle { apiAthlete ->
                                mDbHelper.saveAthlete(apiAthlete)
                                        .andThen(Single.just(apiAthlete))
                            }
                            .startWith(athleteFromLocal)
                }


    }

    override fun getAthleteFromDatabase(athleteId: String): Observable<Athlete> {
        return mDbHelper.getAthleteFromDatabase(athleteId)
    }

    override fun getAthleteByIdServer(athleteId: String): Single<Athlete> {
        return mApiHelper.getAthleteByIdServer(athleteId)
    }

    override fun getTestDataForAthleteId(athleteId: String): Observable<List<TestData>> {
        return mDbHelper.getTestDataForAthleteId(athleteId)
    }

    override fun saveTestDataServer(request: TestDataRequest.UploadTestDataRequest): Single<TestDataResponse> {

        return mApiHelper.saveTestDataServer(request)
    }

    override fun saveTestData(testData: TestData): Observable<Boolean> {
        return mDbHelper.saveTestData(testData)
    }

    override fun saveTestDataList(testDataList: List<TestData>): Observable<Boolean> {
        return mDbHelper.saveTestDataList(testDataList)
    }

    override fun saveAthlete(athlete: Athlete): Completable {
        return mDbHelper.saveAthlete(athlete)
    }

    override fun saveOrganization(organization: Organization): Completable {
        return mDbHelper.saveOrganization(organization)
    }



}