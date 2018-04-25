package com.madmensoftware.sips.data.local.room.converters

/**
 * Created by clj00 on 3/2/2018.
 */
import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.madmensoftware.sips.data.models.SensorData
import java.util.*
import java.util.Collections.emptyList




/**
 * Created by clj00 on 2/28/2018.
 */
class SensorDataConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToSensorDataList(data: String?): List<SensorData> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<SensorData>>() {

        }.type

        return gson.fromJson<List<SensorData>>(data, listType)
    }

    @TypeConverter
    fun sensorDataListToString(sensorDataList: List<SensorData>): String {
        return gson.toJson(sensorDataList)
    }
}