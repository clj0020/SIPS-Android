package com.madmensoftware.sips.data.local.room.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by clj00 on 3/2/2018.
 */
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return (if (date == null) null else date.getTime())?.toLong()
    }
}