package com.madmensoftware.sips.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation



/**
 * Created by clj00 on 3/2/2018.
 */
object ViewAnimationUtils {

    fun scaleAnimateView(view: View) {
        val animation = ScaleAnimation(
                1.15f, 1f, 1.15f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        view.setAnimation(animation)
        animation.duration = 100
        animation.start()
    }
}// This class is not publicly instantiable