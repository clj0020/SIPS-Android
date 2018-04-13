package com.madmensoftware.sips.ui.athlete_test

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.hardware.Sensor
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider
import com.madmensoftware.sips.util.SensorHelper
import com.wardellbagby.rxcountdowntimer.RxCountDownTimer
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by clj00 on 3/5/2018.
 */
class TestAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<TestAthleteNavigator>(dataManager, schedulerProvider) {

    private val ONE_LEG_SQUAT_HOLD = 0
    private val SINGLE_LEG_JUMP = 1

    var athleteId: String ?= null

    // Test Variables
    var mTestId: Int ?= null
    var mClockStarted = false

    // Timer Variables
    private val mFormattedTime: MutableLiveData<String>
    val formattedTime: LiveData<String> get() = mFormattedTime // Getter for mFormattedTime LiveData, cast to immutable value

    // Sensor Variables
    lateinit var mReactiveSensors: ReactiveSensors
    lateinit var mSensorHelper: SensorHelper

    private var mAccelerometerData: ArrayList<Array<Float>> = ArrayList<Array<Float>>()
    private var mGyroscopeData: ArrayList<Array<Float>> = ArrayList<Array<Float>>()
    private var mMagnometerData: ArrayList<Array<Float>> = ArrayList<Array<Float>>()

    private lateinit var mAccelerometerSubscription: Disposable
    private lateinit var mGyroscopeSubscription: Disposable
    private lateinit var mMagnometerSubscription: Disposable

    init {
        mFormattedTime = MutableLiveData<String>()
    }

    fun startCountdownTimer(secondsLong: Long): Disposable {

        // Starts sensors (Accelerometer, Gyroscope, Magnometer) and then subscribes to them on the computation thread, observes on the main thread.
        startSensors()

        mClockStarted = true

        //Counts down from 60 seconds with a new value being emitted at least 1 second after the last one. Emits in seconds.
        var rxCountdownTimer = RxCountDownTimer.create(secondsLong, 1, TimeUnit.SECONDS)
                .doOnComplete({
                    // Save the testing data
                    mAccelerometerData = mSensorHelper.mAccelerometerData
                    mGyroscopeData = mSensorHelper.mGyroscopeData
                    mMagnometerData = mSensorHelper.mMagnometerData

                    uploadTestData(mAccelerometerData, mGyroscopeData, mMagnometerData)

                    mSensorHelper.safelyDispose(mAccelerometerSubscription)
                    mSensorHelper.safelyDispose(mGyroscopeSubscription)
                    mSensorHelper.safelyDispose(mMagnometerSubscription)

                })
                .subscribe({secondsPassed ->
                    mFormattedTime.postValue(formatTime(secondsPassed))
                })

        compositeDisposable.add(rxCountdownTimer)


        return rxCountdownTimer
    }

    private fun startSensors() {
        if (mReactiveSensors.hasSensor(Sensor.TYPE_ACCELEROMETER)) {
            mAccelerometerSubscription = mSensorHelper.startAccelerometerSubscription()
            compositeDisposable.add(mAccelerometerSubscription)
        }
        if (mReactiveSensors.hasSensor(Sensor.TYPE_GYROSCOPE)) {
            mGyroscopeSubscription = mSensorHelper.startGyroscopeSubscription()
            compositeDisposable.add(mGyroscopeSubscription)
        }
        if (mReactiveSensors.hasSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            mMagnometerSubscription = mSensorHelper.startMagnometerSubscription()
            compositeDisposable.add(mMagnometerSubscription)
        }
    }

    // Stops the timer, stops sensors, and saves testing data
    fun stopCountDownTimer() {
        mClockStarted = false

        // Stop reading sensors
        stopSensors()

        // Save the testing data
        mAccelerometerData = mSensorHelper.mAccelerometerData
        mGyroscopeData = mSensorHelper.mGyroscopeData
        mMagnometerData = mSensorHelper.mMagnometerData

        uploadTestData(mAccelerometerData, mGyroscopeData, mMagnometerData)
    }

    private fun stopSensors() {
        if (mReactiveSensors.hasSensor(Sensor.TYPE_ACCELEROMETER)) {
            mSensorHelper.safelyDispose(mAccelerometerSubscription)
        }
        if (mReactiveSensors.hasSensor(Sensor.TYPE_GYROSCOPE)) {
            mSensorHelper.safelyDispose(mGyroscopeSubscription)
        }
        if (mReactiveSensors.hasSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            mSensorHelper.safelyDispose(mMagnometerSubscription)
        }
    }

    fun uploadTestData(accelerometerData: ArrayList<Array<Float>>, gyroscopeData: ArrayList<Array<Float>>, magnometerData: ArrayList<Array<Float>>) {
        val testData = TestData()
        testData.athleteId = this.athleteId!!
        testData.accelerometerArray = accelerometerData
        testData.gyroscopeArray = gyroscopeData
        testData.magnetometerArray = magnometerData
        testData.createdAt = Calendar.getInstance().time.toString()
        testData.testedAt = Calendar.getInstance().time

        setIsLoading(true)
        compositeDisposable.add(dataManager
                .saveTestData(testData)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({isFinished ->
                    setIsLoading(false)
                    if (isFinished != null) {
                        navigator!!.testSaved()
                    }
                }, {throwable ->
                    navigator!!.handleError(throwable)
                })
        )
    }

    // Formats the time for the clock
    private fun formatTime(elapsedTime: Long): String {
        val seconds = elapsedTime % 3600 % 60
        val minutes = elapsedTime % 3600 / 60
        val hours = elapsedTime / 3600

        val hh = if (hours < 10) "0$hours" else hours.toString()
        val mm = if (minutes < 10) "0$minutes" else minutes.toString()
        val ss = if (seconds < 10) "0$seconds" else seconds.toString()

        return "$hh:$mm:$ss"
    }

    fun onStartTestClicked() {
        when (mTestId) {
            ONE_LEG_SQUAT_HOLD -> {
                startCountdownTimer(60)
                navigator!!.testStarted()
            }
            SINGLE_LEG_JUMP -> {
                startCountdownTimer(50)
                navigator!!.testStarted()
            }
        }
    }

    fun onStopTestClicked() {
        stopCountDownTimer()
        navigator!!.testStopped()
    }

    fun onOneSquatHoldButtonClick() {
        mTestId = ONE_LEG_SQUAT_HOLD
        navigator!!.testSelected(mTestId!!)
    }

    fun onSingleLegJumpButtonClick() {
        mTestId = SINGLE_LEG_JUMP
        navigator!!.testSelected(mTestId!!)
    }

    // Called when viewmodel is destroyed.
    override fun onCleared() {
        super.onCleared()

        // Just in case, stop the timer. This is safe because of the checks that stopTimer makes
        stopSensors()
    }

}