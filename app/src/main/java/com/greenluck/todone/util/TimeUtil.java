package com.greenluck.todone.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtil {

    public static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("d / M / E", Locale.getDefault());

    public static String getReadableTime(long time){
        return TIME_FORMAT.format(time);
    }

    public static int getYear(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


}
