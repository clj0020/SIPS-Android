package com.madmensoftware.sips.ui.athlete_test

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.hardware.Sensor
import android.media.MediaPlayer
import android.util.Log
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.data.models.api.TestDataRequest
import com.madmensoftware.sips.data.models.api.TestDataResponse
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.TestType
import com.madmensoftware.sips.ui.base.BaseViewModel
import com.madmensoftware.sips.util.SchedulerProvider
import com.madmensoftware.sips.util.SensorHelper
import com.wardellbagby.rxcountdowntimer.RxCountDownTimer
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer


/**
 * Created by clj00 on 3/5/2018.
 */
class TestAthleteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<TestAthleteNavigator>(dataManager, schedulerProvider) {

    private val ONE_LEG_SQUAT_HOLD = 0
    private val SINGLE_LEG_JUMP = 1

    val testTypeObservableList: ObservableList<TestType>?= ObservableArrayList<TestType>()

    val testTypeListLiveData: MutableLiveData<List<TestType>> = MutableLiveData<List<TestType>>()

    var athleteId: String ?= null

    // Test Variables
    var mTestId: String ?= null
    var mClockStarted = false

    // Test Type Variables
    private val mTestType: MutableLiveData<TestType>
    val testType: LiveData<TestType> get() = mTestType

    // Timer Variables
    private val mFormattedTime: MutableLiveData<String>
    val formattedTime: LiveData<String> get() = mFormattedTime // Getter for mFormattedTime LiveData, cast to immutable value

    // Sensor Variables
    lateinit var mReactiveSensors: ReactiveSensors
    lateinit var mSensorHelper: SensorHelper

    private var mAccelerometerData: ArrayList<TestDataRequest.UploadTestDataRequest.SensorData> = ArrayList<TestDataRequest.UploadTestDataRequest.SensorData>()
    private var mGyroscopeData: ArrayList<TestDataRequest.UploadTestDataRequest.SensorData> = ArrayList<TestDataRequest.UploadTestDataRequest.SensorData>()
    private var mMagnometerData: ArrayList<TestDataRequest.UploadTestDataRequest.SensorData> = ArrayList<TestDataRequest.UploadTestDataRequest.SensorData>()

    private lateinit var mAccelerometerSubscription: Disposable
    private lateinit var mGyroscopeSubscription: Disposable
    private lateinit var mMagnometerSubscription: Disposable
    private lateinit var mRxCountDownTimerSubscription: Disposable

    init {
        mFormattedTime = MutableLiveData<String>()
        mTestType = MutableLiveData<TestType>()
        fetchTestTypes()
    }

    fun addTestTypeItemsToList(testTypes: List<TestType>) {
        testTypeObservableList?.clear()
        testTypeObservableList?.addAll(testTypes)
    }

    fun fetchTestTypes() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .getTestTypeList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ testTypeList ->
                    if (testTypeList != null) {
                        testTypeListLiveData.setValue(testTypeList)
                    }
                    setIsLoading(false)
                }, { throwable ->
                    setIsLoading(false)
                    navigator?.handleError(throwable)
                }))
    }

    fun startCountdownTimer(context: Context, testDuration: Long) {
        compositeDisposable.clear()
        mClockStarted = true

        val beepMediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.beep)
        val completedMediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.completed)

        val initialCountdown = 3 // This is the countdown to let you know before it starts.

        //Counts down from 60 seconds with a new value being emitted at least 1 second after the last one. Emits in seconds.
        mRxCountDownTimerSubscription = RxCountDownTimer.create(testDuration + initialCountdown, 1, TimeUnit.SECONDS)
                .doOnComplete({
                    completedMediaPlayer.start()

                    // Package the testing data
                    mAccelerometerData = mSensorHelper.mAccelerometerData
                    mGyroscopeData = mSensorHelper.mGyroscopeData
                    mMagnometerData = mSensorHelper.mMagnometerData

                    val testDataRequest = prepareTestDataRequest(mAccelerometerData, mGyroscopeData, mMagnometerData)
                    navigator!!.showConfirmDialog(testDataRequest)

                    // Disposes of the sensor observables safely.
                    stopSensors()

                    mClockStarted = false
                })
                .subscribe({ secondsPassed ->
                    Log.i("Countdown", "Seconds Passed:" + secondsPassed.toString())
                    if (secondsPassed > testDuration-1) {
                        Log.i("Countdown", "Timer about to start")
                        beepMediaPlayer.start()
                    }
                    else if (secondsPassed == testDuration - 1) {
                        Log.i("Countdown", "Starting Sensors")
                        startSensors()
                    }
                    else {
                        // Broadcast new values from the MutableLiveData to the LiveData
                        mFormattedTime.postValue(formatTime(secondsPassed))
                    }

                })

        // add the countdowntimer to the composite disposable.
        compositeDisposable.add(mRxCountDownTimerSubscription)
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
        // Stop reading sensors
        stopSensors()

        // This hopefully stops the timer instance if it is still running safely.
        safelyDispose(mRxCountDownTimerSubscription)

        mClockStarted = false

        // Save the testing data
        mAccelerometerData = mSensorHelper.mAccelerometerData
        mGyroscopeData = mSensorHelper.mGyroscopeData
        mMagnometerData = mSensorHelper.mMagnometerData

        val testDataRequest = prepareTestDataRequest(mAccelerometerData, mGyroscopeData, mMagnometerData)
        navigator!!.showConfirmDialog(testDataRequest)
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

    fun prepareTestDataRequest(accelerometerData: ArrayList<TestDataRequest.UploadTestDataRequest.SensorData>, gyroscopeData: ArrayList<TestDataRequest.UploadTestDataRequest.SensorData>, magnometerData: ArrayList<TestDataRequest.UploadTestDataRequest.SensorData>): TestDataRequest.UploadTestDataRequest {
        return TestDataRequest.UploadTestDataRequest(
                athleteId = this.athleteId,
                testTypeId = mTestType.value!!._id,
                accelerometer_data = accelerometerData.toList(),
                magnometer_data = magnometerData.toList(),
                gyroscope_data = gyroscopeData.toList()
        )
    }

    fun uploadTestData(testDataRequest: TestDataRequest.UploadTestDataRequest) {
        setIsLoading(true)
        dataManager.saveTestData(testDataRequest)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnComplete({
                    setIsLoading(false)
                    navigator!!.testSaved()
                })
                .doOnError({throwable: Throwable ->
                    setIsLoading(false)
                    navigator!!.handleError(throwable)
                })
                .subscribe({
                    setIsLoading(false)
                })
    }

    // Formats the time for the clock
    fun formatTime(elapsedTime: Long): String {
        val seconds = elapsedTime % 3600 % 60
        val minutes = elapsedTime % 3600 / 60
        val hours = elapsedTime / 3600

        val hh = if (hours < 10) "0$hours" else hours.toString()
        val mm = if (minutes < 10) "0$minutes" else minutes.toString()
        val ss = if (seconds < 10) "0$seconds" else seconds.toString()

        return "$hh:$mm:$ss"
    }

    fun onStartTestClicked(context: Context) {
        startCountdownTimer(context, mTestType.value!!.duration!!.toLong())
        navigator!!.testStarted()
    }

    fun onStopTestClicked() {
        navigator!!.testStopped()
    }

    fun onTestTypeSelected(testType: TestType) {
        mTestId = testType._id
        mTestType.postValue(testType)
        mFormattedTime.postValue(formatTime(testType.duration!!.toLong()))
        navigator!!.testSelected(testType)
    }

    fun resetTime() {
        mFormattedTime.postValue(formatTime(mTestType.value!!.duration!!.toLong()))
    }

    fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    // Called when viewmodel is destroyed.
    override fun onCleared() {
        super.onCleared()

        // Just in case, stop the timer. This is safe because of the checks that stopTimer makes
        stopSensors()

        safelyDispose(mRxCountDownTimerSubscription)
    }

}