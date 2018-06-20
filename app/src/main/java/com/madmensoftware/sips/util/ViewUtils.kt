package com.madmensoftware.sips.util

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.graphics.drawable.Drawable
import com.madmensoftware.sips.R


/**
 * Created by clj00 on 3/2/2018.
 */
object ViewUtils {

    fun changeIconDrawableToGray(context: Context, drawable: Drawable?) {
        if (drawable != null) {
            drawable.mutate()
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().getDisplayMetrics().density
        return Math.round(dp * density)
    }

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi
        return px / (densityDpi / 160f)
    }

    @JvmStatic
    fun formatName(first_name: String?, last_name: String?): String? {
        if (first_name == null || last_name == null) {
            return null
        }
        return first_name + " " + last_name
    }

    @JvmStatic
    fun formatHeightWeight(height: Int?, weight: Int?): String? {
        if (height == null || weight == null || height == 0 || weight == 0) {
            return null
        }

        val heightFeet: Number = ((height / 12)) - ((height % 12) / 12)
        val heightInches: Number = height % 12
        val heightString = heightFeet.toString() + "'" + heightInches.toString()
        val weightString = weight.toString() + " lbs"
        return heightString + " " + weightString
    }

    @JvmStatic
    fun formatSportPosition(sport: String?, position: String?): String? {
        if (sport == null || position == null) {
            return null
        }

        return sport + " (" + position + ")"
    }

}// This class is not publicly instantiable