package com.madmensoftware.sips.util

import java.text.SimpleDateFormat

object DateUtils {

    @JvmStatic
    fun formatIsoDate(isoDate: String?): String {
        if (isoDate == null) {
            return ""
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        val date = dateFormat.parse(isoDate)
        val formatter = SimpleDateFormat("MM/dd/yyyy")
        val dateStr = formatter.format(date)
        return dateStr
    }
}// This class is not publicly instantiable