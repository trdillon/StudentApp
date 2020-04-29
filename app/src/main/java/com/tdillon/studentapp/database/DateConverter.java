package com.tdillon.studentapp.database;

import androidx.room.TypeConverter;

import java.util.Date;

class DateConverter {

    //Converter for date & timestamp
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}