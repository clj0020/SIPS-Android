package com.madmensoftware.sips.util

import android.graphics.drawable.ColorDrawable
import android.app.ProgressDialog
import android.util.Patterns
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.madmensoftware.sips.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.provider.Settings;
import java.nio.charset.Charset


/**
 * Created by clj00 on 3/2/2018.
 */
object CommonUtils {

    val timestamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
        val manager = context.getAssets()
        val `is` = manager.open(jsonFileName)

        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()

        return String(buffer, Charset.forName("UTF-8"))
    }

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
}// This utility class is not publicly instantiable