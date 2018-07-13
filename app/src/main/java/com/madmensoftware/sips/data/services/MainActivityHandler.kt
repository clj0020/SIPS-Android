package com.madmensoftware.sips.data.services

import android.os.Handler
import android.os.Message
import android.util.Log
import com.madmensoftware.sips.ui.main.MainActivity

class MainActivityHandler(mainActivity: MainActivity): Handler() {

    var mMainActivity: MainActivity

    init {
        mMainActivity = mainActivity
    }

    override fun handleMessage(msg: Message?) {

        if (msg != null) {

            when (msg.arg1) {
                ATHLETE_ACTIVITY_RECOGNIZED_MESSAGE -> {
                    // Athlete's activity has been recognized.
                    Log.i(TAG, "Received message from Activity Recognition service.")

                    if (msg.obj != null) {
                        mMainActivity.viewModel.onAthleteActivityRecognized(msg.obj)
                    }
                }
            }

        }

        super.handleMessage(msg)
    }

    companion object {
        val TAG = MainActivityHandler::class.java.simpleName
        val ATHLETE_ACTIVITY_RECOGNIZED_MESSAGE = 1

    }

}