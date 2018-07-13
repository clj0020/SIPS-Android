package com.madmensoftware.sips.data.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection
import android.os.Messenger
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import com.madmensoftware.sips.R
import com.madmensoftware.sips.ui.main.MainActivity
import dagger.Module

@Module
class AthleteActivityRecognitionService: Service() {

    // Create the service's messenger so we can send messages between the activity and service.
    // The Messenger takes a handler and the AthleteActivityRecognitionHandler takes a service.
    var mMessenger = Messenger(AthleteActivityRecognitionHandler(this@AthleteActivityRecognitionService))
    var mActivityMessenger: Messenger? = null

    var mAthleteId: String? = null
    var mAthleteName: String? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    /** Called when service is started. **/
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand called.")

        if (intent != null) {
            if (intent.extras != null) {
                // Get the activity's messenger and athlete id.
                Log.i(TAG, "Got intent from MainActivity.")
                mActivityMessenger = intent.extras.get(MainActivity.EXTRA_ACTIVITY_MESSENGER) as Messenger
                mAthleteId = intent.extras.get(MainActivity.EXTRA_WORKOUT_TRACKING_ATHLETE_ID) as String
                mAthleteName = intent.extras.get(MainActivity.EXTRA_WORKOUT_TRACKING_ATHLETE_NAME) as String
            }
            else {
                // Don't have the activity messenger or the athlete's id for some reason
                Log.e(TAG,"Did not get intent with activity messenger.")
            }
        }

        // If the service is killed, then android will restart it with the last intent that was delivered to the service.
        return Service.START_REDELIVER_INTENT
    }

    /** Called when service is bound to. **/
    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "onBind called.")

        // return this service's messenger
        return mMessenger.binder
    }

    /** Called when the service is bound to after unbind has been called. **/
    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)

        Log.i(TAG, "onRebind called.")

        // If the activity is bound, then we don't need to show the notification telling the user that workout tracking service is running.
        stopForeground(true)
    }

    /** If the activity is unbound from the service, then we need to show a persistent notification that tells the user that the service is still running. **/
    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnBind called.")

        /**
         * We can increase the importance of the service by making the service a foreground service.
         * Foreground Services require a notification.
         * Here we'll build a notification that lets us know that the service is running in the foreground..
         */

        // TODO: We could fix the bug in the athlete fragment displaying start/stop workout tracking if this PendingIntent opened up the athlete's profile fragment.
        // Register a receiver to stop the Service
        registerReceiver(stopServiceReceiver, IntentFilter(STOP_SERVICE_INTENT_FILTER))
        val contentIntent = PendingIntent.getBroadcast(this, 0, Intent(STOP_SERVICE_INTENT_FILTER), PendingIntent.FLAG_UPDATE_CURRENT)

        // Build the notification
        val notificationBuilder: Notification.Builder = Notification.Builder(this)
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
        notificationBuilder.setContentTitle("Tracking " + mAthleteName + "'s Workout.")
        notificationBuilder.setContentText("Tap to turn off.")
        notificationBuilder.setContentIntent(contentIntent)
        val notification: Notification = notificationBuilder.build()

        // Starts the service in the foreground.
        startForeground(SERVICE_ID, notification)

        // Return true since we want the service to be rebound to.
        return true
    }

    /** Called when service is destroyed. Unregisters stop service receiver. **/
    override fun onDestroy() {
        try {
            Log.i(TAG, "Unregistering stop service receiver.")
            unregisterReceiver(stopServiceReceiver)
        }
        catch (e: IllegalArgumentException) {
            Log.e(TAG, "Unable to unregister the Activity Recognition stop receiver.")
        }

        super.onDestroy()
    }

    //We need to declare the receiver with onReceive function as below
    protected var stopServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i(TAG, "Stopping service requested.")
            stopSelf()
        }
    }

    companion object {
        val TAG = AthleteActivityRecognitionService::class.java.simpleName
        val SERVICE_ID = 11
        val STOP_SERVICE_INTENT_FILTER = "stop_service_intent_filter"
    }
}