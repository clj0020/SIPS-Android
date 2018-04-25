package com.madmensoftware.sips.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.madmensoftware.sips.data.local.prefs.PreferencesHelper
import com.madmensoftware.sips.data.local.room.DbHelper
import com.madmensoftware.sips.data.models.api.*
import com.madmensoftware.sips.data.models.room.*
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

    /**
     * Fields for preferences
     */
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

    /**
     * User Functions
     */
    override fun doServerLoginApiCall(request: LoginRequest.ServerLoginRequest): Single<LoginResponse> {
        return mApiHelper.doServerLoginApiCall(request)
    }

    override fun logout() {
        setUserAsLoggedOut()
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

    /**
     * Athlete Functions
     */
    override fun getAthleteList(): Observable<List<Athlete>?> {
        val remoteSource: Single<List<Athlete>> = mApiHelper.getAthletesFromOrganizationServer().subscribeOn(schedulerProvider.io())

        return mDbHelper.getAllAthletesFromOrganizationDatabase(this.currentUserOrganizationId!!)
                .flatMap { listFromLocal: List<Athlete> ->
                    remoteSource
                            .observeOn(schedulerProvider.computation())
                            .toObservable()
                            .filter { apiAthleteList: List<Athlete> ->
                                apiAthleteList != listFromLocal
                            }
                            .flatMapSingle { apiAthleteList ->
                                mDbHelper.saveAthleteListDatabase(apiAthleteList)
                                        .andThen(Single.just(apiAthleteList))
                            }
                            .startWith(listFromLocal)
                }
    }

    override fun getAthletesFromOrganizationServer(): Single<List<Athlete>> {
        return mApiHelper.getAthletesFromOrganizationServer()
    }

    override fun getAllAthletesFromOrganizationDatabase(organizationId: String): Observable<List<Athlete>> {
        return mDbHelper.getAllAthletesFromOrganizationDatabase(organizationId)
    }

    override fun saveAthleteListDatabase(athleteList: List<Athlete>): Completable {
        return mDbHelper.saveAthleteListDatabase(athleteList)
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
                                mDbHelper.saveAthleteDatabase(apiAthlete)
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

    override fun saveAthlete(email: String): Completable {
        return mApiHelper.saveAthleteServer(email, this.currentUserOrganizationId!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.computation())
                .flatMapCompletable { athlete: Athlete ->
                    mDbHelper.saveAthleteDatabase(athlete)
                }
    }

    override fun saveAthleteServer(email: String, organization: String): Single<Athlete> {
        return mApiHelper.saveAthleteServer(email, this.currentUserOrganizationId!!)
    }

    override fun saveAthleteDatabase(athlete: Athlete): Completable {
        return mDbHelper.saveAthleteDatabase(athlete)
    }

    /**
     * Test Data Functions
     */
    override fun getTestDataList(athleteId: String): Observable<List<TestData>?> {
        val remoteSource: Single<List<TestData>> = mApiHelper.getTestDataForAthleteServer(athleteId).subscribeOn(schedulerProvider.io())

        return mDbHelper.getTestDataForAthleteIdDatabase(athleteId)
                .flatMap { listFromLocal: List<TestData> ->
                    remoteSource
                            .observeOn(schedulerProvider.computation())
                            .toObservable()
                            .filter { apiTestDataList: List<TestData> ->
                                apiTestDataList != listFromLocal
                            }
                            .flatMapSingle { apiTestDataList ->
                                mDbHelper.saveTestDataList(apiTestDataList)
                                        .andThen(Single.just(apiTestDataList))
                            }
                            .startWith(listFromLocal)
                }
    }

    override fun getTestDataForAthleteServer(athleteId: String): Single<List<TestData>> {
        return mApiHelper.getTestDataForAthleteServer(athleteId)
    }

    override fun getTestDataForAthleteIdDatabase(athleteId: String): Observable<List<TestData>> {
        return mDbHelper.getTestDataForAthleteIdDatabase(athleteId)
    }

    override fun saveTestData(request: TestDataRequest.UploadTestDataRequest): Completable {
        return mApiHelper.saveTestDataServer(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.computation())
                .flatMapCompletable { testData: TestData ->
                        mDbHelper.saveTestDataDatabase(testData)
                }
    }

    override fun saveTestDataServer(request: TestDataRequest.UploadTestDataRequest): Single<TestData> {
        return mApiHelper.saveTestDataServer(request)
    }

    override fun saveTestDataDatabase(testData: TestData): Completable {
        return mDbHelper.saveTestDataDatabase(testData)
    }

    override fun saveTestDataList(testDataList: List<TestData>): Completable {
        return mDbHelper.saveTestDataList(testDataList)
    }

    /**
     * Organization Functions
     */
    override fun saveOrganization(organization: Organization): Completable {
        return mDbHelper.saveOrganization(organization)
    }

    /**
     * Test Type Functions
     */
    override fun getTestTypeList(): Observable<List<TestType>?> {
        val remoteSource: Single<List<TestType>> = mApiHelper.getTestTypesFromOrganizationServer().subscribeOn(schedulerProvider.io())

        return mDbHelper.getTestTypesFromOrganizationDatabase(this.currentUserOrganizationId!!)
                .flatMap { listFromLocal: List<TestType> ->
                    remoteSource
                            .observeOn(schedulerProvider.computation())
                            .toObservable()
                            .filter { apiTestTypeList: List<TestType> ->
                                apiTestTypeList != listFromLocal
                            }
                            .flatMapSingle { apiTestTypeList ->
                                mDbHelper.saveTestTypeListToDatabase(apiTestTypeList)
                                        .andThen(Single.just(apiTestTypeList))
                            }
                            .startWith(listFromLocal)
                }
    }

    override fun getTestTypesFromOrganizationDatabase(organizationId: String): Observable<List<TestType>> {
        return mDbHelper.getTestTypesFromOrganizationDatabase(organizationId)
    }

    override fun getTestTypesFromOrganizationServer(): Single<List<TestType>> {
        return mApiHelper.getTestTypesFromOrganizationServer()
    }

    override fun saveTestTypeListToDatabase(testTypeList: List<TestType>): Completable {
        return mDbHelper.saveTestTypeListToDatabase(testTypeList)
    }

}