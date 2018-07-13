package com.madmensoftware.sips.data.services

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import com.madmensoftware.sips.ui.main.MainActivity

class AthleteActivityRecognitionHandler(athleteActivityRecognitionService: AthleteActivityRecognitionService): Handler() {


    var mAthleteActivityRecognitionService: AthleteActivityRecognitionService

    init {
        mAthleteActivityRecognitionService = athleteActivityRecognitionService
    }

    override fun handleMessage(msg: Message?) {

        // Message can only send integers as arguments, we can execute service methods using this value.
        if (msg != null) {
            Log.i(TAG, "Received message " + msg.toString() + ".  Argument 1: " + msg.arg1)


            when (msg.arg1) {
                // The Activity connecting to the AthleteActivityRecognitionService has sent a message to set up their
                // messaging system.
                MainActivity.ACTIVITY_CONNECTING_TO_SERVICE_MESSAGE -> {
                    Log.i(TAG, "Message received from the activity to set up service messaging.")

                    // Set the service's activity messenger which the activity sent in the message's replyTo.
                    mAthleteActivityRecognitionService.mActivityMessenger = msg.replyTo
                }
                MainActivity.TURN_OFF_SERVICE_MESSAGE -> {
                    Log.i(TAG, "Message received from the activity to turn off Service.")

                    mAthleteActivityRecognitionService.stopSelf()
                    mAthleteActivityRecognitionService.stopForeground(true)
                }
            }
        }

        super.handleMessage(msg)
    }

    companion object {
        val TAG = AthleteActivityRecognitionHandler::class.java.simpleName

    }

}