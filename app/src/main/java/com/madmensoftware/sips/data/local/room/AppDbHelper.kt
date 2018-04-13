package com.madmensoftware.sips.data.local.room

/**
 * Created by clj00 on 3/2/2018.
 */
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.Organization
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.User
import io.reactivex.Completable

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single


/**
 * Created by amitshekhar on 07/07/17.
 */

@Singleton
class AppDbHelper @Inject constructor(internal val appDatabase: AppDatabase) : DbHelper {

    override val allUsers: Observable<kotlin.collections.List<User>> =
        Observable.fromCallable<kotlin.collections.List<User>> {
            appDatabase.userDao().loadAll()
        }


    override fun insertUser(user: User): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            this@AppDbHelper.appDatabase.userDao().insert(user)
            return@fromCallable true
        }
    }

//    override fun getAllAthletes(): Observable<kotlin.collections.List<Athlete>> {
//        return Observable.fromCallable<kotlin.collections.List<Athlete>> {
//            appDatabase.athleteDao().loadAll()
//        }
//    }


    override fun getAllAthletesFromOrganization(organizationId: String): Observable<List<Athlete>> {
        return Observable.fromCallable<List<Athlete>> {
            appDatabase.athleteDao().loadAthletesByOrganizationId(organizationId)
        }
    }


    override fun getAthlete(athleteId: String): Observable<Athlete> {
        return Observable.fromCallable<Athlete> {
            appDatabase.athleteDao().loadAthleteById(athleteId)
        }
    }


    override fun getTestDataForAthleteId(athleteId: String): Observable<kotlin.collections.List<TestData>> {
        return Observable.fromCallable<kotlin.collections.List<TestData>> {
            appDatabase.testDataDao().loadAllByAthleteId(athleteId)
        }
    }

    override fun saveTestData(testData: TestData): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.testDataDao().insert(testData)
            true
        }
    }

    override fun saveTestDataList(testDataList: kotlin.collections.List<TestData>): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.testDataDao().insertAll(testDataList)
            true
        }
    }

    override fun saveAthlete(athlete: Athlete): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.athleteDao().insert(athlete)
            true
        }
    }

    override fun saveAthleteList(athleteList: List<Athlete>): Completable {
        return Completable.fromCallable {
            appDatabase.athleteDao().insertAll(athleteList)
        }
    }

    override fun saveOrganization(organization: Organization): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.organizationDao().insert(organization)
            true
        }
//        appDatabase.organizationDao().insert(organization)
    }
}