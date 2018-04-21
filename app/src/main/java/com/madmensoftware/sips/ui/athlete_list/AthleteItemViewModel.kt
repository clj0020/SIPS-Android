package com.madmensoftware.sips.ui.athlete_list

import android.databinding.ObservableField
import android.util.Log
import com.madmensoftware.sips.data.models.room.Athlete

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteItemViewModel(private val mAthlete: Athlete, val mListener: AthleteItemViewModelListener) {

    val first_name: ObservableField<String?>
    val last_name: ObservableField<String?>
    val email: ObservableField<String?>
    val verified: ObservableField<Boolean>

    init {
        first_name = ObservableField(mAthlete.first_name)
        last_name = ObservableField(mAthlete.last_name)
        email = ObservableField(mAthlete.email)

        Log.i("AthleteItem", mAthlete.status + "")
        if (mAthlete.status == "Verified") {
            verified = ObservableField(true)
        }
        else {
            verified = ObservableField(false)
        }
    }

    fun onItemClick() {
        mListener.onItemClick(mAthlete._id)
    }

    interface AthleteItemViewModelListener {

        fun onItemClick(id: String)
    }
}
