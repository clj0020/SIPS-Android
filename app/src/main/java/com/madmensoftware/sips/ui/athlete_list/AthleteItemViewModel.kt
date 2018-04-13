package com.madmensoftware.sips.ui.athlete_list

import android.databinding.ObservableField
import com.madmensoftware.sips.data.models.room.Athlete

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteItemViewModel(private val mAthlete: Athlete, val mListener: AthleteItemViewModelListener) {

//    val createdAt: ObservableField<String>

    val first_name: ObservableField<String>

    val last_name: ObservableField<String>

//    val updatedAt: ObservableField<String>

    val email: ObservableField<String>


    init {
//        createdAt = ObservableField(mAthlete.created_at!!)
        first_name = ObservableField(mAthlete.first_name!!)
        last_name = ObservableField(mAthlete.last_name!!)
//        updatedAt = ObservableField(mAthlete.updatedAt!!)
        email = ObservableField(mAthlete.email!!)
    }

    fun onItemClick() {
        mListener.onItemClick(mAthlete.id)
    }

    interface AthleteItemViewModelListener {

        fun onItemClick(id: String)
    }
}
