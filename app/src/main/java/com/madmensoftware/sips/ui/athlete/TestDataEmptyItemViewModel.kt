package com.madmensoftware.sips.ui.athlete

/**
 * Created by clj00 on 3/4/2018.
 */
class TestDataEmptyItemViewModel(private val mListener: TestDataEmptyItemViewModelListener) {

    fun onRetryClick() {
        mListener.onRetryClick()
    }

    interface TestDataEmptyItemViewModelListener {

        fun onRetryClick()
    }
}
