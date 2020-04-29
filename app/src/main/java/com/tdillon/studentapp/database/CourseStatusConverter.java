package com.tdillon.studentapp.database;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.tdillon.studentapp.model.CourseStatus;

class CourseStatusConverter {

    //Converter for CourseStatus enum
    @TypeConverter
    public static String fromCourseStatusToString(CourseStatus courseStatus) {
        if(courseStatus == null) {
            return null;
        }
        return courseStatus.name();
    }

    @TypeConverter
    public static CourseStatus fromStringToCourseStatus(String courseStatus) {
        if(TextUtils.isEmpty(courseStatus)) {
            return CourseStatus.IN_PROGRESS;
        }
        return CourseStatus.valueOf(courseStatus);
    }
}