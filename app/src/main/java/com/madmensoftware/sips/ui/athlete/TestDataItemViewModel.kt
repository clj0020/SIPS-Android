package com.madmensoftware.sips.ui.athlete

import android.databinding.ObservableField
import com.madmensoftware.sips.data.models.room.TestData

/**
 * Created by clj00 on 3/4/2018.
 */
class TestDataItemViewModel(private val mTestData: TestData, val mListener: TestDataItemViewModelListener) {

    val createdAt: ObservableField<String>

    init {
        createdAt = ObservableField(mTestData.createdAt!!)
    }

    fun onItemClick() {
        mListener.onItemClick(mTestData.id)
    }

    interface TestDataItemViewModelListener {

        fun onItemClick(id: String)
    }
}
