package com.commonlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mauliksantoki on 5/7/17.
 */

public final class GetUTCLocalDiffrence {

    static Calendar calendar;
    static String timeZone, timeZone_hr, timeZone_min;
    static SimpleDateFormat dateFormat;

    private GetUTCLocalDiffrence() {
    }

    public static String getUTCLocalDiffrence() {
        dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());

        return timeZone();
//        getCurrentUTC();
//        return convertedTime();
    }


    public static String timeZone() {
        timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        timeZone_hr = timeZone.substring(0, 3);
        timeZone_min = timeZone.substring(3, 5);
        return timeZone_hr + ":" + timeZone_min;
    }

    public static String getCurrentUTC() {
        return dateFormat.format(calendar.getTime());
    }

    public static String convertedTime() {
        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timeZone_hr));
        calendar.add(Calendar.MINUTE, Integer.parseInt(timeZone_min));
        return dateFormat.format(calendar.getTime());
    }
}
