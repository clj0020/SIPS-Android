package com.madmensoftware.sips.data.local.room

/**
 * Created by clj00 on 3/2/2018.
 */


import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.User
import io.reactivex.Observable

/**
 * Created by amitshekhar on 07/07/17.
 */

interface DbHelper {

    val allUsers: Observable<List<User>>

    fun insertUser(user: User): Observable<Boolean>

    fun getAllAthletes(): Observable<List<Athlete>>

    fun getAthlete(athleteId: Long): Observable<Athlete>

    fun getTestDataForAthleteId(athleteId: Long?): Observable<List<TestData>>

    fun saveTestData(testData: TestData): Observable<Boolean>

    fun saveTestDataList(testDataList: List<TestData>): Observable<Boolean>

    fun saveAthlete(athlete: Athlete): Observable<Boolean>

    fun saveAthleteList(athleteList: List<Athlete>): Observable<Boolean>

}