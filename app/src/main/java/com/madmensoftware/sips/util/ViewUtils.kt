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
}// This class is not publicly instantiable