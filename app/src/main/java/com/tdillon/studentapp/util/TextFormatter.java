package com.tdillon.studentapp.util;

import java.text.SimpleDateFormat;

public class TextFormatter {

    private static String cardPattern = "MM dd yyyy";
    private static String fullPattern = "MM/dd/yyyy";
    public static SimpleDateFormat cardDateFormat = new SimpleDateFormat(cardPattern);
    public static SimpleDateFormat fullDateFormat = new SimpleDateFormat(fullPattern);
}