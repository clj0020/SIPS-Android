package com.madmensoftware.sips.data.local.room

/**
 * Created by clj00 on 3/2/2018.
 */


import com.madmensoftware.sips.data.models.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by amitshekhar on 07/07/17.
 */

interface DbHelper {

    // User

    val allUsers: Observable<List<User>>

    fun insertUser(user: User): Observable<Boolean>

    // Athlete
    fun getAthleteFromDatabase(athleteId: String): Observable<Athlete>

    fun saveAthlete(athlete: Athlete): Completable

    fun saveAthleteListDatabase(athleteList: List<Athlete>): Completable

    fun getAllAthletesFromOrganizationDatabase(organizationId: String): Observable<List<Athlete>>

    // Test Data
    fun getTestDataForAthleteIdDatabase(athleteId: String): Observable<List<TestData>>

    fun saveTestDataDatabase(testData: TestData): Completable

    fun saveTestDataList(testDataList: List<TestData>): Completable

    // Test Type

    fun saveTestTypeListToDatabase(testTypeList: List<TestType>): Completable

    fun getTestTypesFromOrganizationDatabase(organizationId: String): Observable<List<TestType>>

    // Organization

    fun saveOrganization(organization: Organization): Completable

}