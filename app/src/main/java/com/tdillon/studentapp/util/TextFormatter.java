package com.tdillon.studentapp.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFormatter {

    public static String getDateFormatted(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(date);
    }

    public static Date getDateFormattedString(String sDate) throws Exception {
        @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("MM/dd/yyyy").parse(sDate);
        return date;
    }
}