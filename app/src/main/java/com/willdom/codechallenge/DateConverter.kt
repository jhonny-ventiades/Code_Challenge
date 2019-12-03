package com.willdom.codechallenge

import androidx.room.TypeConverter
import java.util.*


class DateConverter {
    @TypeConverter
    fun toDate(timeStamp: Long?): Date? {
        return if (timeStamp == null) null else Date(timeStamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
