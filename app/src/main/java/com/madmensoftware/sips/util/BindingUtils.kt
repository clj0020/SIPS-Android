package com.madmensoftware.sips.util

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.data.models.room.TestType
import com.madmensoftware.sips.ui.athlete.TestDataListAdapter
import com.madmensoftware.sips.ui.athlete_list.AthleteListAdapter
import com.madmensoftware.sips.ui.athlete_test.TestTypeListAdapter
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView




/**
 * Created by clj00 on 3/2/2018.
 */
object BindingUtils {

    @JvmStatic
    @BindingAdapter("adapter")
    fun addAthleteItems(recyclerView: RecyclerView, athletes: List<Athlete>) {
        val adapter = recyclerView.adapter as AthleteListAdapter?
        if (adapter != null) {
            adapter.clearItems()
            adapter.addItems(athletes)
        }
        else {

        }
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun addTestDataItems(recyclerView: RecyclerView, testDataList: List<TestData>) {
        val adapter = recyclerView.adapter as TestDataListAdapter?
        if (adapter != null) {
            adapter.clearItems()
            adapter.addItems(testDataList)
        }
        else {

        }
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun addTestTypeItems(recyclerView: RecyclerView, testTypes: List<TestType>) {
        val adapter = recyclerView.adapter as TestTypeListAdapter?
        if (adapter != null) {
            adapter.clearItems()
            adapter.addItems(testTypes)
        }
        else {

        }
    }


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String) {
        val context = imageView.getContext()
        Glide.with(context).load(url).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setInt(view: TextView, value: Int?) {
        if (value == null) view.setText("");
        else view.setText(value.toString());
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text")
    fun getInt(view: TextView): Int {
        val num = view.getText().toString();
        if(num.isEmpty()) return 0;
        try {
            return Integer.parseInt(num);
        }
        catch (e: NumberFormatException) {
            return 0;
        }
    }
}// This class is not publicly instantiable