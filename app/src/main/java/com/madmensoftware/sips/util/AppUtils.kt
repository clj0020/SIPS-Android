package com.madmensoftware.sips.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.madmensoftware.sips.R



/**
 * Created by clj00 on 3/2/2018.
 */
object AppUtils {

    fun openPlayStoreForApp(context: Context) {
        val appPackageName = context.getPackageName()
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_market_link) + appPackageName)))
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_google_play_store_link) + appPackageName)))
        }

    }

}