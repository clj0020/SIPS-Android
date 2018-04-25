package com.madmensoftware.sips.data.local.room

/**
 * Created by clj00 on 3/2/2018.
 */
import com.madmensoftware.sips.data.models.room.*
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

    override fun getAllAthletesFromOrganizationDatabase(organizationId: String): Observable<List<Athlete>> {
        return Observable.fromCallable<List<Athlete>> {
            appDatabase.athleteDao().loadAthletesByOrganizationId(organizationId)
        }
    }

    override fun getAthleteFromDatabase(athleteId: String): Observable<Athlete> {
        return Observable.fromCallable<Athlete> {
            appDatabase.athleteDao().loadAthleteById(athleteId)
        }
    }

    override fun getTestDataForAthleteIdDatabase(athleteId: String): Observable<kotlin.collections.List<TestData>> {
        return Observable.fromCallable<kotlin.collections.List<TestData>> {
            appDatabase.testDataDao().loadAllByAthleteId(athleteId)
        }
    }

    override fun saveTestDataDatabase(testData: TestData): Completable {
        return Completable.fromCallable {
            appDatabase.testDataDao().insert(testData)
            true
        }
    }

    override fun saveTestDataList(testDataList: kotlin.collections.List<TestData>): Completable {
        return Completable.fromCallable {
            appDatabase.testDataDao().insertAll(testDataList)
            true
        }
    }

    override fun saveAthlete(athlete: Athlete): Completable {
        return Completable.fromCallable {
            appDatabase.athleteDao().insert(athlete)
            true
        }
    }

    override fun saveAthleteListDatabase(athleteList: List<Athlete>): Completable {
        return Completable.fromCallable {
            appDatabase.athleteDao().insertAll(athleteList)
            true
        }
    }

    override fun saveOrganization(organization: Organization): Completable {
        return Completable.fromCallable {
            appDatabase.organizationDao().insert(organization)
            true
        }
    }

    override fun saveTestTypeListToDatabase(testTypeList: List<TestType>): Completable {
        return Completable.fromCallable {
            appDatabase.testTypeDao().insertAll(testTypeList)
            true
        }
    }

    override fun getTestTypesFromOrganizationDatabase(organizationId: String): Observable<List<TestType>> {
        return Observable.fromCallable<List<TestType>> {
            appDatabase.testTypeDao().loadAllByOrganizationId(organizationId)
        }
    }
}