package com.madmensoftware.sips.ui.athlete_list

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteEmptyItemViewModel(private val mListener: AthleteEmptyItemViewModelListener) {

    fun onRetryClick() {
        mListener.onRetryClick()
    }

    interface AthleteEmptyItemViewModelListener {

        fun onRetryClick()
    }
}
