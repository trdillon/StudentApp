package com.tdillon.studentapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFormatter {

    public static String getDateFormatted(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(date);
    }

    public static Date getDateFormattedString(String sDate) throws Exception {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(sDate);
        return date;
    }
}