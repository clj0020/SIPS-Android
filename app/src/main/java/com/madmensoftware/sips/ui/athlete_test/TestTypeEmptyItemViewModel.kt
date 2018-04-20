package com.madmensoftware.sips.ui.athlete_test

class TestTypeEmptyItemViewModel(private val mListener: TestTypeEmptyItemViewModelListener) {

    fun onRetryClick() {
        mListener.onRetryClick()
    }

    interface TestTypeEmptyItemViewModelListener {

        fun onRetryClick()
    }
}
