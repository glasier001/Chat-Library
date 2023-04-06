package com.commonlib.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by root on 16/5/16.
 */
public final class ConvertDateTimeFormat {

    private static SimpleDateFormat sdf;

    private ConvertDateTimeFormat() {
    }

    public static String convertLocalToUtcDate(String date, String baseFormat, String convertFormat) {
        try {
            LogShowHide.LogShowHideMethod(null, "Before converting date" + date);
            // Convert to local
            SimpleDateFormat format = new SimpleDateFormat(baseFormat, Locale.ENGLISH);
            format.setTimeZone(TimeZone.getDefault());
            Date d = format.parse(date);

            // Convert to utc
            SimpleDateFormat serverFormat = new SimpleDateFormat(convertFormat, Locale.ENGLISH);
            serverFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            LogShowHide.LogShowHideMethod(null, "After converting date" + serverFormat.format(d));
            String finalDate = serverFormat.format(d);
            return finalDate.replace("AM", "am").replace("PM", "pm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertDate(String date, String dtformat, String needFormat, boolean isSmallLetter) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dtformat, Locale.ENGLISH);
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat(needFormat, Locale.ENGLISH);
            String finalDate = serverFormat.format(d);
            if (isSmallLetter) {
                finalDate = finalDate.replace("AM", "am").replace("PM", "pm");
            }
            return finalDate;
        } catch (Exception e) {
            e.printStackTrace();
            LogShowHide.LogShowHideMethod(null, "Exception" + e.getMessage());
        }
        return "";
    }

    public static String convertUTCToLocalDate(String date, String baseFormat, String convertFormat) {
        try {
            LogShowHide.LogShowHideMethod(null, "Before converting date" + date);
            // Convert to UTC
            SimpleDateFormat format = new SimpleDateFormat(baseFormat, Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date d = format.parse(date);
            // Convert to LOCAL
            SimpleDateFormat serverFormat = new SimpleDateFormat(convertFormat, Locale.ENGLISH);
            serverFormat.setTimeZone(TimeZone.getDefault());
            LogShowHide.LogShowHideMethod(null, "After converting date" + serverFormat.format(d));
            String finalDate = serverFormat.format(d);
            return finalDate.replace("AM", "am").replace("PM", "pm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertUTCToLocalDate(String date, String baseFormat, String convertFormat, String timeZoneDiff) {
        try {
            LogShowHide.LogShowHideMethod(null, "Before converting date" + date);
            // Convert to UTC
            SimpleDateFormat format = new SimpleDateFormat(baseFormat, Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date d = format.parse(date);

            SimpleDateFormat format1 = new SimpleDateFormat(baseFormat, Locale.ENGLISH);
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date timediff = format1.parse(timeZoneDiff.replace("+", "").replace("-", ""));

            Calendar dcalendar = Calendar.getInstance();
            Calendar timediffcalendar = Calendar.getInstance();

            dcalendar.setTime(d);
            timediffcalendar.setTime(timediff);

            if (timeZoneDiff.contains("+")) {
                dcalendar.add(Calendar.HOUR_OF_DAY, timediffcalendar.get(Calendar.HOUR_OF_DAY));
                dcalendar.add(Calendar.MINUTE, timediffcalendar.get(Calendar.MINUTE));
            } else {
                dcalendar.add(Calendar.HOUR_OF_DAY, -timediffcalendar.get(Calendar.HOUR_OF_DAY));
                dcalendar.add(Calendar.MINUTE, -timediffcalendar.get(Calendar.MINUTE));
            }

            // Convert to LOCAL
            SimpleDateFormat serverFormat = new SimpleDateFormat(convertFormat, Locale.ENGLISH);
            serverFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            LogShowHide.LogShowHideMethod(null, "After converting date" + serverFormat.format(dcalendar.getTime()));

            String finalDate = serverFormat.format(dcalendar);
            return finalDate.replace("AM", "am").replace("PM", "pm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertWorkingHours(String date, String timeZone, String convertTime) {
        // FIXME: 30/1/18  sagar: Alter method can be found that doesn't use static values
        try {
            Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
            try {
                Date d1 = (new SimpleDateFormat("HH:mm", Locale.ENGLISH)).parse(date);
                c1.setTime(d1);
                Log.e("test hours: ", String.valueOf(Integer.parseInt(timeZone.substring(0, 3))));
                Log.e("test minutes: ", String.valueOf(Integer.parseInt(timeZone.substring(4, 6))));
                c1.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timeZone.substring(0, 3)));
                c1.add(Calendar.MINUTE, Integer.parseInt(timeZone.substring(4, 6)));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return (new SimpleDateFormat(convertTime, Locale.ENGLISH)).format(c1.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertLocalDate(String date, String baseFormat, String convertFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(baseFormat, Locale.ENGLISH);
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat(convertFormat, Locale.ENGLISH);
            return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String method_convert_24_hour_to_12_hours(String time) {
        String[] format = time.split(":");
        if (Integer.parseInt(format[0]) > 12) {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            final Date dateObj;
            try {
                dateObj = sdf.parse(time);
                time = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(dateObj) + " PM";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            time = time + " AM";
        }
        return time;
    }

    public static String getSQLDate(String dateReceivedFromUser) throws ParseException {
        // 09/16/2013 format mm/dd/yyyy
        // String dateReceivedFromUser = "12/13/2012";
        DateFormat userDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.ENGLISH);
        DateFormat dateFormatNeeded = new SimpleDateFormat("dd MMM, hh:mm aa", Locale.ENGLISH);
        Date date = userDateFormat.parse(dateReceivedFromUser);
        String convertedDate = dateFormatNeeded.format(date);
        // System.out.println(convertedDate);
        return convertedDate;
    }

    public static String gethourDate(String dateReceivedFromUser) throws ParseException {
        // 09/16/2013 format mm/dd/yyyy
        // String dateReceivedFromUser = "12/13/2012";
        DateFormat userDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.ENGLISH);
        DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        Date date = userDateFormat.parse(dateReceivedFromUser);
        String convertedDate = dateFormatNeeded.format(date);
        // System.out.println(convertedDate);
        return convertedDate;
    }

    public static String getSQLDateTime(String dateReceivedFromUser) throws ParseException {
        // 09/16/2013 format mm/dd/yyyy
        // String dateReceivedFromUser = "12/13/2012";
        DateFormat userDateFormat = new SimpleDateFormat(
                "E, MMM dd yyyy HH:mm: a", Locale.ENGLISH);
        DateFormat dateFormatNeeded = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
        Date date = userDateFormat.parse(dateReceivedFromUser);
        String convertedDate = dateFormatNeeded.format(date);
        // System.out.println(convertedDate);
        return convertedDate;
    }

    public static long getDiffrenceBetweenTwoDate(String startdate, String enddate) {
//        getDateDiff diff = lisner;
        SimpleDateFormat serverFormat = new SimpleDateFormat(DateTimeFormats.CONVERT_DATE_FORMAT_AMPM, Locale.ENGLISH);
        Date startDate, endDate;
        try {
            startDate = serverFormat.parse(startdate);
            endDate = serverFormat.parse(enddate);

            return (endDate.getTime() - startDate.getTime());

//            System.out.println("startDate : " + startDate);
//            System.out.println("endDate : " + endDate);
//            System.out.println("different : " + different);
//
//            long secondsInMilli = 1000;
//            long minutesInMilli = secondsInMilli * 60;
//            long hoursInMilli = minutesInMilli * 60;
//            long daysInMilli = hoursInMilli * 24;
//
//            long elapsedDays = different / daysInMilli;
//            different = different % daysInMilli;
//
//            long elapsedHours = different / hoursInMilli;
//            different = different % hoursInMilli;
//
//            long elapsedMinutes = different / minutesInMilli;
//            different = different % minutesInMilli;
//
//            long elapsedSeconds = different / secondsInMilli;
//
//
//            diff.dateTime(elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        } catch (ParseException e) {
            e.printStackTrace();
            LogShowHide.LogShowHideMethod(null, "ParseException" + e.getMessage());
        }
        return -1;

    }

    public static boolean checkTimeBetweenStartTimeAndEndtime(String strStartTime, String strEndTime) {
        try {
            sdf = new SimpleDateFormat("HH:mm");
            Date dateCurrent = sdf.parse(getCurrentTime());
            Date dateStart = sdf.parse(strStartTime);
            Date dateEnd = sdf.parse(strEndTime);


            if (dateStart.before(dateCurrent) && dateEnd.after(dateCurrent)) {
                return true;
            } else {
                return false;
            }


        } catch (ParseException e) {
            e.printStackTrace();
            LogShowHide.LogShowHideMethod(null, "ParseException==>" + e.getMessage());
        }
        return false;
    }

    public static String getCurrentTime() {
        sdf = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        String s = sdf.format(calendar.getTime());
        return s;
    }

    public static boolean isTimeAfter(Date startTime, Date endTime) {
        if (endTime.before(startTime)) { //Same way you can check with after() method also.
            return false;
        } else {
            return true;
        }
    }

    public static boolean isTimeAfter(Calendar startTime, Calendar endTime) {
        if (endTime.before(startTime)) { //Same way you can check with after() method also.
            return false;
        } else {
            return true;
        }
    }

    public static String getDayOfTheWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Date d = new Date();
        return sdf.format(d);
    }

    public static Date[] calcDateRangeWeek(Calendar c, int day) {
        Date[] dr = new Date[2];
        // setMin
        c.set(day, Calendar.MONDAY);
        dr[0] = c.getTime();
        // setMax
        c.set(day, Calendar.SUNDAY);
        dr[1] = c.getTime();
        return dr;
    }

    public static Calendar firstDayOfWeek(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.set(Calendar.DAY_OF_YEAR, --day);
        }
        return cal;
    }

    public static Calendar lastDayOfWeek(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.set(Calendar.DAY_OF_YEAR, ++day);
        }
        return cal;
    }

    public static String getTime(String timezone, String date, String format) {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        Calendar c = Calendar.getInstance(tz);
        c.setTime(DateTimeUtil.getDate(date, format));
        return String.valueOf(c.getTime());
    }

    boolean isTimebefore(Date startTime, Date endTime) {
        if (endTime.after(startTime)) { //Same way you can check with after() method also.
            return false;
        } else {
            return true;
        }
    }

    public interface getDateDiff {
        public void dateTime(long days, long hours, long minutes, long seconds);
    }

    public static String convertUTCToTimezone(String date, String baseFormat, String needFormat, String timezone) {
        try {

//            String fullDateString = DateTimeUtil.getFullDate(date, baseFormat); /*30-12-2018 08:00*/

            /*01-01-1970 08:00*/
            // sagar : 28/12/18 If the year date is less than 1990, it is not working here!
            // sagar : 28/12/18 Otherwise, only above one line is enough instead of all string builder stuffs

            StringBuilder stringDate=new StringBuilder();
            stringDate.append("30-12-2018 "); /*30-12-2018 */
            stringDate.append(date);
            StringBuilder stringBaseFormat=new StringBuilder();
            stringBaseFormat.append("dd-MM-yyyy ");
            stringBaseFormat.append(baseFormat);

            SimpleDateFormat sourceFormat = new SimpleDateFormat(stringBaseFormat.toString());
            sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsed = sourceFormat.parse(stringDate.toString());

            TimeZone tz = TimeZone.getTimeZone(timezone);
            SimpleDateFormat destFormat = new SimpleDateFormat(needFormat);
            destFormat.setTimeZone(tz);

            return destFormat.format(parsed);
        } catch (Exception e) {
            e.printStackTrace();
            LogShowHide.LogShowHideMethod(null, "Exception" + e.getMessage());
        }
        return "";
    }

    public static String getUtcToTimezone(String date) {
        ZoneId denverTimeZone = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            denverTimeZone = ZoneId.of("Asia/Singapore");
            ZonedDateTime zdt = ZonedDateTime.parse(date);
            String output2 = zdt.toLocalTime().toString();
            return "Current time in " + denverTimeZone + ": " + output2;
        }
        return "";
    }

    public static String getTimezone(String date, String format) {
        SimpleDateFormat sdfAmerica = new SimpleDateFormat(format);
        TimeZone tzInAmerica = TimeZone.getTimeZone("Asia/Singapore");
        sdfAmerica.setTimeZone(tzInAmerica);

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String sDateInAmerica = sdfAmerica.format(date); // Convert to String first
        Date dateInAmerica;
        try {
            dateInAmerica = formatter.parse(sDateInAmerica); // Create a new Date object
        } catch (ParseException e) {
            e.printStackTrace();
            dateInAmerica = new Date();
        }

        return DateTimeUtil.getDate(dateInAmerica, format);
    }
}
