package com.madmensoftware.sips.data.local.room.converters

/**
 * Created by clj00 on 3/2/2018.
 */
import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by clj00 on 2/28/2018.
 */
class SensorDataConverter {

    @TypeConverter
    fun fromString(value: String): ArrayList<Array<Float>> {
        val listType = object : TypeToken<ArrayList<Array<Float>>>() {

        }.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromSensorDataArrayList(list: ArrayList<Array<Float>>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}