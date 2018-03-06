package com.madmensoftware.sips.util

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.SystemClock
import android.util.Log
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import com.github.pwittchen.reactivesensors.library.SensorNotFoundException
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by clj00 on 3/5/2018.
 */
class SensorHelper(private val reactiveSensors: ReactiveSensors, private val schedulerProvider: SchedulerProvider) {

    val mAccelerometerData: ArrayList<Array<Float>>
    val mGyroscopeData: ArrayList<Array<Float>>
    val mMagnometerData: ArrayList<Array<Float>>
    val mStartTime: Float

    init {
        mAccelerometerData = ArrayList<Array<Float>>()
        mGyroscopeData = ArrayList<Array<Float>>()
        mMagnometerData = ArrayList<Array<Float>>()
        mStartTime = SystemClock.elapsedRealtime().toFloat()
    }

    fun startAccelerometerSubscription(): Disposable {
            Log.i("SensorHelper: ", " Accelerometer found. Reading values...")

            return reactiveSensors.observeSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.io())
                    .subscribe(object : Consumer<ReactiveSensorEvent> {
                        @Throws(Exception::class)
                        override fun accept(reactiveSensorEvent: ReactiveSensorEvent) {
                            val event = reactiveSensorEvent.sensorEvent

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            val time = SystemClock.elapsedRealtime().toFloat() - mStartTime

                            mAccelerometerData.add(arrayOf(time, x, y, z))

                            val format = "%s readings:\n time = %f\n x = %f\n y = %f\n z = %f"
                            val message = String.format(Locale.getDefault(), format, "accelerometer", time, x, y, z)
                            Log.i("SensorHelper: ", message)
                        }
                    }, object : Consumer<Throwable> {
                        @Throws(Exception::class)
                        override fun accept(throwable: Throwable) {
                            if (throwable is SensorNotFoundException) {
                                Log.i("SensorHelper: ", "Sorry, your device doesn't have required sensor.")
                            }
                        }
                    })
    }

    fun startGyroscopeSubscription(): Disposable {
            Log.i("SensorHelper: ", " Gyroscope found. Reading values...")
            return reactiveSensors.observeSensor(Sensor.TYPE_GYROSCOPE, SensorManager.SENSOR_DELAY_NORMAL)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.io())
                    .subscribe(object : Consumer<ReactiveSensorEvent> {
                        @Throws(Exception::class)
                        override fun accept(reactiveSensorEvent: ReactiveSensorEvent) {
                            val event = reactiveSensorEvent.sensorEvent

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            val time = SystemClock.elapsedRealtime().toFloat() - mStartTime

                            mGyroscopeData.add(arrayOf(time, x, y, z))

                            val format = "%s readings:\n time = %f\n x = %f\n y = %f\n z = %f"
                            val message = String.format(Locale.getDefault(), format, "gyroscope", time, x, y, z)
                            Log.i("SensorHelper: ", message)
                        }
                    }, object : Consumer<Throwable> {
                        @Throws(Exception::class)
                        override fun accept(throwable: Throwable) {
                            if (throwable is SensorNotFoundException) {
                                Log.i("SensorHelper: ", "Sorry, your device doesn't have required sensor.")
                            }
                        }
                    })
    }

    fun startMagnometerSubscription(): Disposable {
            Log.i("SensorHelper: ", " Magnometer found. Reading values...")
            return reactiveSensors.observeSensor(Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_NORMAL)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.io())
                    .subscribe(object : Consumer<ReactiveSensorEvent> {
                        @Throws(Exception::class)
                        override fun accept(reactiveSensorEvent: ReactiveSensorEvent) {
                            val event = reactiveSensorEvent.sensorEvent

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            val time = SystemClock.elapsedRealtime().toFloat() - mStartTime

                            mMagnometerData.add(arrayOf(time, x, y, z))

                            val format = "%s readings:\n time = %f\n x = %f\n y = %f\n z = %f"
                            val message = String.format(Locale.getDefault(), format, "magnometer", time, x, y, z)
                            Log.i("SensorHelper: ", message)
                        }
                    }, object : Consumer<Throwable> {
                        @Throws(Exception::class)
                        override fun accept(throwable: Throwable) {
                            if (throwable is SensorNotFoundException) {
                                Log.i("SensorHelper: ", "Sorry, your device doesn't have required sensor.")
                            }
                        }
                    })
    }

    fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }


}