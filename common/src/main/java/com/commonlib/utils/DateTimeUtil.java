package com.commonlib.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mauliksantoki on 16/11/16.
 */

public final class DateTimeUtil {


    public String eventType = "1";
    public String normal = "2";
    Date serverDateTime;
    Date currentDateTime;
    String serverDate;
    SimpleDateFormat serverFormat;
    Calendar startTime, endTime, cal;
    int currentYear = 0, serverDateYear = 0;


    public DateTimeUtil() {
        serverFormat = new SimpleDateFormat(DateTimeFormats.SERVER_DATE_FORMAT_SEC);
        serverFormat.setTimeZone(TimeZone.getDefault());
    }

    public static List<Integer> getTotalDayListOfMonth() {
        List<Integer> totalDays = new ArrayList<>();
        for (int i = 0, j = getTotalDaysOfMonth(); i < j; i++) {
            totalDays.add(i + 1); //Starting from 1 to...
        }
        return totalDays;
    }

    public static int getTotalDaysOfMonth() {
        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static List<Integer> getTotalDayListOfMonth(int month) {
        List<Integer> totalDays = new ArrayList<>();
        for (int i = 0, j = getTotalDaysOfMonth(month); i < j; i++) {
            totalDays.add(i + 1); //Starting from 1 to...
        }
        return totalDays;
    }

    public static int getTotalDaysOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthTitle(int monthNumber) {
        String monthTitle = "";
        String[] monthTitles = getMonths();
        int[] monthNumbers = getMonthNumbers();
        if (monthTitles.length == monthNumbers.length) {
            if (monthNumber < monthTitles.length) {
                monthTitle = monthTitles[monthNumber];
            }
        }
        return monthTitle;
    }

    public static String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
    }

    public static int[] getMonthNumbers() {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }

    public static int getMonthNumber(String monthTitle) {
        int monthNumber = 0;
        String[] monthTitles = getMonths();
        int[] monthNumbers = getMonthNumbers();
        if (monthTitles.length == monthNumbers.length) {
            for (int i = 0, j = monthTitles.length; i < j; i++) {
                if (monthTitles[i].equalsIgnoreCase(monthTitle)) {
                    monthNumber = i;
                    break;
                }
            }
        }
        return monthNumber;
    }

    public static boolean isToday(String date, String dateFormat) {
        return isCurrentDate(date, dateFormat);
    }

    public static boolean isCurrentDate(String date, String dateFormat) {
        if (date != null) {
            if (!date.isEmpty()) {
                if (dateFormat != null && !dateFormat.isEmpty()) {
                    Date givenDate = null;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                    try {
                        givenDate = simpleDateFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (givenDate != null) {
                        String simpleGivenDate = DateTimeFormats.mFormatDate.format(givenDate);
                        Date currentDate = Calendar.getInstance().getTime();
                        String simpleCurrentDate = DateTimeFormats.mFormatDate.format(currentDate);
                        return simpleGivenDate.equalsIgnoreCase(simpleCurrentDate);
                    }
                }
            }
        }
        return false;
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeFormats.SERVER_DATE_FORMAT_SEC, Locale.US);
        return simpleDateFormat.format(currentDate);
    }

    public static String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeFormats.YEAR_ONLY, Locale.US);
        return simpleDateFormat.format(currentDate);
    }

    public static String getCurrentDate(String desiredFormat) {
        if (StringUtils.isNotNullNotEmpty(desiredFormat)) {
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(desiredFormat, Locale.US);
            return simpleDateFormat.format(currentDate);
        }
        return "";
    }

    public static String getCurrentYear(String desiredFormat) {
        if (StringUtils.isNotNullNotEmpty(desiredFormat)) {
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(desiredFormat, Locale.US);
            return simpleDateFormat.format(currentDate);
        }
        return "";
    }

    public static String getFullDayFromShortDay(String shortDay) {
        // sagar : 28/12/18 Because we know, expect the format of shortDay here is "E"
        SimpleDateFormat parseFormat = new SimpleDateFormat("E", Locale.US);
        Date date = null;
        try {
            date = parseFormat.parse(shortDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        d("Utils", "shortDayToFullDay: :-" +new SimpleDateFormat("EEEE").format(date));
        return new SimpleDateFormat("EEEE", Locale.US).format(date);
    }

    public static String getToday() {
        // sagar : 27/12/18 Change format if you need short day to "E" instead of full day, like Monday to Mon
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        Date d = new Date();
        return sdf.format(d);
    }

    public static String getToday(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date d = new Date();
        return sdf.format(d);
    }

    // sagar : 27/12/18 true if current time is before your given time
    public static boolean isBeforeTime(String yourDateTime, String format) {

        // sagar : 27/12/18 parameter date and format must be same
        Date currentDateTime = getDate(getDate(format), format);
        Date yourDate = getDate(yourDateTime, format);

        return currentDateTime.before(yourDate);
    }

    public static Date getDate(String date, String format) {
        // sagar : 28/12/18 The value date and format must match each other
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        try {
            return sdf.parse(date);
        } catch (java.text.ParseException e) {
            return new Date();
        }
    }

    public static String getDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date date = new Date();
        return sdf.format(date);
    }

    // sagar : 27/12/18 true if current time is before your given time
    public static boolean isBeforeTime(String yourDateTime, String currentDateTime, String format) {

        // sagar : 27/12/18 parameter date and format must be same
        Date currentDate = getDate(currentDateTime, format);
        Date yourDate = getDate(yourDateTime, format);

        return currentDate.before(yourDate);
    }

    public static String getFullDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date givenDate;
        try {
            givenDate = sdf.parse(date);
        } catch (java.text.ParseException e) {
            givenDate = new Date();
        }
        return new SimpleDateFormat(DateTimeFormats.DT_DD_MM_YYYY_12_HOURS, Locale.US).format(givenDate);
    }

    public static Date getCurrentDateTime(int additionalHours, int additionalMinutes) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);

        hour = hour + additionalHours;
        minute = minute + additionalMinutes;

        return getDate(hour + ":" + minute, "hh:mm");
    }

    // sagar : 27/12/18 true if current time is after your given time
    public static boolean isAfterTime(String yourDateTime, String format) {

        // sagar : 27/12/18 parameter date and format must be same
        Date currentDateTime = getDate(getDate(format), format);
        Date yourDate = getDate(yourDateTime, format);

        return currentDateTime.after(yourDate);
    }

    // sagar : 27/12/18 true if current time is after your given time
    public static boolean isAfterTime(String yourDateTime, String currentDateTime, String format) {

        // sagar : 27/12/18 parameter date and format must be same
        Date currentDate = getDate(currentDateTime, format);
        Date yourDate = getDate(yourDateTime, format);

        return currentDate.after(yourDate);
    }

    public static boolean isEqualTime(String yourDateTime, String format) {

        // sagar : 27/12/18 parameter date and format must be same
        Date currentDateTime = getDate(getDate(format), format);
        Date yourDate = getDate(yourDateTime, format);

        return yourDate.equals(currentDateTime);
    }

    public static boolean isEqualTime(String yourDateTime, String currentDateTime, String format) {

        // sagar : 27/12/18 parameter date and format must be same
        Date currentDate = getDate(currentDateTime, format);
        Date yourDate = getDate(yourDateTime, format);

        return yourDate.equals(currentDate);
    }

    public static String getDateString(String date, String currentDateFormat, String timezone) {
        Date outputDate = getDate(date, currentDateFormat, timezone);
        return getDate(outputDate, currentDateFormat);
    }

    public static Date getDate(String date, String currentDateFormat, String timezone) {
        SimpleDateFormat format = new SimpleDateFormat(currentDateFormat, Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone(timezone));
        Date outputDate;
        try {
            outputDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            outputDate = new Date();
        }
        return outputDate;
    }

    public static String getDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(date);
    }

    public static boolean isTomorrow(Date d) {

        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isToday(Date d) {
        return DateUtils.isToday(d.getTime());
    }

    // sagar : 15/2/19 4:58 PM Make this method static
    /*https://stackoverflow.com/questions/18607096/getting-time-difference-with-string-like-a-minute-ago-or-an-hour-ago-on-andr*/
    public static String getPastDisplayDate(String enddate, String serverDateFormate, String type, String eventType) {
        try {
            Calendar cal = Calendar.getInstance();

            Date currentDateTime = cal.getTime();
            Calendar endTime = Calendar.getInstance();
            Calendar startTime = Calendar.getInstance();
            endTime.setTime(currentDateTime);
            int currentYear = cal.get(Calendar.YEAR);

            enddate = enddate.toString().trim();
            String serverDate = ConvertDateTimeFormat.convertUTCToLocalDate(enddate.toString().trim(), serverDateFormate, DateTimeFormats.SERVER_DATE_FORMAT_SEC);

            SimpleDateFormat serverFormat = new SimpleDateFormat(DateTimeFormats.SERVER_DATE_FORMAT_SEC);
            serverFormat.setTimeZone(TimeZone.getDefault());
            Date serverDateTime = serverFormat.parse(serverDate);

            startTime.setTime(serverDateTime);
            int serverDateYear = startTime.get(Calendar.YEAR);

            long elapsedDays = daysBetween(startTime, endTime);

            long different = serverDateTime.getTime() - currentDateTime.getTime();

            double d = (1000 * 60 * 60 * 24 * 365.25);
            long yearDiff = Math.round(Math.abs(different) / d);

            System.out.println("serverDateTime : " + serverDateTime);
            System.out.println("currentDateTime : " + currentDateTime);
            System.out.println("different : " + elapsedDays);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

//            elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;
            if ((elapsedDays > 0) || (elapsedHours >= 0 && elapsedMinutes >= 0 && elapsedSeconds > 0)) {
                /*future time*/
                if (yearDiff >= 1) {
                    if (eventType.equalsIgnoreCase(type)) {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(enddate, serverDateFormate, DateTimeFormats.moreThenOneYearEvent);
                    } else {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(enddate, serverDateFormate, DateTimeFormats.moreThenOneYear);
                    }
                } else {
                    if (elapsedDays >= 6) {
                        if (eventType.equalsIgnoreCase(type)) {
                            return ConvertDateTimeFormat.convertUTCToLocalDate(enddate, serverDateFormate, DateTimeFormats.sameYearEvent);
                        } else {
                            return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.sameYear);
                        }

                    } else if (elapsedDays > 1) {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.sameWeek);
                    } else if (elapsedDays == 1) {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.tomorrow);
                    } else {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.today);
                    }
                }
            } else {
                /*past time*/
                if (yearDiff >= 1) {
                    if (eventType.equalsIgnoreCase(type)) {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(enddate, serverDateFormate, DateTimeFormats.moreThanYearEvent);
                    } else {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(enddate, serverDateFormate, DateTimeFormats.moreThanYear);
                    }
                } else if (Math.abs(elapsedDays) >= 6) {
                    if (eventType.equalsIgnoreCase(type)) {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.moreThenSevenDaysEvent);
                    } else {
                        return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.moreThenSevenDays);
                    }
                } else if (Math.abs(elapsedDays) > 1) {
                    return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.moreThenOneDays);
                } else if (Math.abs(elapsedDays) == 1) {
                    return ConvertDateTimeFormat.convertUTCToLocalDate(String.valueOf(enddate), serverDateFormate, DateTimeFormats.yesterday);
                } else if (Math.abs(elapsedHours) >= 1) {
                    return Math.abs(elapsedHours) + " hours ago";
                } else if (Math.abs(elapsedMinutes) >= 1) {
                    return Math.abs(elapsedMinutes) + " minutes ago";
                } else {
                    return "Just now";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            LogShowHide.LogShowHideMethod(null, "ParseException" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LogShowHide.LogShowHideMethod(null, "Exception" + e.getMessage());
        }

        return "";

    }

    public static int daysBetween(Calendar day1, Calendar day2) {
        Date startDate = day1.getTime();
        Date endDate = day2.getTime();

        return ((int) ((startDate.getTime() / (24 * 60 * 60 * 1000)) - (int) (endDate.getTime() / (24 * 60 * 60 * 1000))));

//        Calendar dayOne = (Calendar) day1.clone(),
//                dayTwo = (Calendar) day2.clone();

//        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
//            return (dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
//        } else {
//            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
//                //swap them
//                Calendar temp = dayOne;
//                dayOne = dayTwo;
//                dayTwo = temp;
//            }
//            int extraDays = 0;
//
//            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);
//
//            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
//                dayOne.add(Calendar.YEAR, -1);
//                // getActualMaximum() important for leap years
//                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
//            }
//
//            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
//        }
    }

    public static String getRelativeTimeSpan(String date, String yourDateFormat) {
        if (StringUtils.isNotNullNotEmpty(date, yourDateFormat)) {
            Date yourDate = DateTimeUtil.getDate(date, yourDateFormat);
            long epochTime = yourDate.getTime();
            return String.valueOf(DateUtils.getRelativeTimeSpanString(epochTime, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        }
        return "";
    }

    public boolean checkExpireDateEvent(String enddate, String serverDateFormate) {
        boolean isCheckExpiredate = false;
        try {
            cal = Calendar.getInstance();

            currentDateTime = cal.getTime();
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            endTime.setTime(currentDateTime);
            currentYear = cal.get(Calendar.YEAR);

            enddate = enddate.toString().trim();
            serverDate = ConvertDateTimeFormat.convertUTCToLocalDate(enddate.toString().trim(), serverDateFormate, DateTimeFormats.SERVER_DATE_FORMAT_SEC);
            serverDateTime = serverFormat.parse(serverDate);

            startTime.setTime(serverDateTime);
            serverDateYear = startTime.get(Calendar.YEAR);

            long elapsedDays = daysBetween(startTime, endTime);

            long different = serverDateTime.getTime() - currentDateTime.getTime();

            System.out.println("serverDateTime : " + serverDateTime);
            System.out.println("currentDateTime : " + currentDateTime);
            System.out.println("different : " + elapsedDays);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

//            elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if ((elapsedDays > 0) || (elapsedHours >= 0 && elapsedMinutes >= 0 && elapsedSeconds > 0)) {
                isCheckExpiredate = false;
            } else {
                isCheckExpiredate = true;
            }

        } catch (Exception e) {
            LogShowHide.LogShowHideMethod(null, "Exception ==" + e.getMessage());
        }

        return isCheckExpiredate;
    }

}
