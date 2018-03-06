package com.madmensoftware.sips.data.local.room

import com.madmensoftware.sips.data.models.room.Athlete
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
/**
 * Generates data to pre-populate the database
 */
object DataGenerator {

    private val FIRST = arrayOf("Carson", "Bryant", "David", "Jordan", "Ashish")
    private val SECOND = arrayOf("Jones", "Kelley", "Moore", "Peterson", "Gupta")

    fun generateAthletes(): List<Athlete> {
        val athletes = ArrayList<Athlete>(FIRST.size * SECOND.size)
        val rnd = Random()
        for (i in FIRST.indices) {
            for (j in SECOND.indices) {
                val athlete = Athlete()
                athlete.first_name = FIRST[i]
                athlete.last_name = SECOND[j]
                athlete.email = "test@gmail.com"
                athletes.add(athlete)
            }
        }
        return athletes
    }
//
//    fun generateTestDataForAthletes(
//            athletes: List<AthleteEntity>): List<TestDataEntity> {
//        val testDataArray = ArrayList<TestDataEntity>()
//        val rnd = Random()
//
//        for (athlete in athletes) {
//            val testDataNumber = rnd.nextInt(5) + 1
//            for (i in 0 until testDataNumber) {
//                val testData = TestDataEntity(
//                        athleteId = (athlete.id),
//                        title = (TEST_DATA[i] + " for " + athlete.name),
//                        testedAt = (Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((testDataNumber - i).toLong()) + TimeUnit.HOURS.toMillis(i.toLong())))
//                )
//                testDataArray.add(testData)
//            }
//        }
//
//        return testDataArray
//    }
}