package com.tdillon.studentapp.database;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.tdillon.studentapp.model.AssessmentType;

class AssessmentTypeConverter {

    //Converter for AssessmentType enum
    @TypeConverter
    public static String fromAssessmentTypeToString(AssessmentType assessmentType) {
        if(assessmentType == null) {
            return null;
        }
        return assessmentType.name();
    }

    @TypeConverter
    public static AssessmentType fromStringToAssessmentType(String assessmentType) {
        if(TextUtils.isEmpty(assessmentType)) {
            return AssessmentType.OA;
        }
        return AssessmentType.valueOf(assessmentType);
    }
}