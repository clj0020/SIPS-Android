package com.madmensoftware.sips.ui.athlete_test

import android.databinding.ObservableField
import com.madmensoftware.sips.data.models.room.TestType

class TestTypeItemViewModel(private val mTestType: TestType, val mListener: TestTypeItemViewModelListener) {

    val title: ObservableField<String>
    val description: ObservableField<String>
    val duration: ObservableField<Int>
    val image: ObservableField<String?>

    init {
        title = ObservableField(mTestType.title!!)
        description = ObservableField(mTestType.description!!)
        duration = ObservableField(mTestType.duration!!)
        image = ObservableField(mTestType.imageUrl)
    }

    fun onItemClick() {
        mListener.onItemClick(mTestType)
    }

    interface TestTypeItemViewModelListener {

        fun onItemClick(testType: TestType)
    }
}