package com.madmensoftware.sips.ui.athlete

import android.databinding.ObservableField
import com.madmensoftware.sips.data.models.room.TestData
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by clj00 on 3/4/2018.
 */
class TestDataItemViewModel(private val mTestData: TestData, val mListener: TestDataItemViewModelListener) {

    val created_at: ObservableField<String>

    init {
        created_at = ObservableField(formatIsoDateString(mTestData.created_at))
    }

    fun formatIsoDateString(dateString: String?): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
        val date = dateFormat.parse(dateString)//You will get date object relative to server/client timezone wherever it is parsed
        val formatter = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US) //If you need time just put specific format for time like 'HH:mm:ss'
        return formatter.format(date)
    }

    fun onItemClick() {
        mListener.onItemClick(mTestData._id)
    }

    interface TestDataItemViewModelListener {

        fun onItemClick(id: String)
    }
}
