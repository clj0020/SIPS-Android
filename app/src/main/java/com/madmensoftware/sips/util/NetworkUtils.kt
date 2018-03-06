package com.madmensoftware.sips.util

import android.content.Context
import android.net.ConnectivityManager



/**
 * Created by clj00 on 3/2/2018.
 */
object NetworkUtils {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}// This class is not publicly instantiable