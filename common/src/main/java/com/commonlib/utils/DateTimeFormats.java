package com.commonlib.utils;

import java.text.SimpleDateFormat;

/**
 * This class is use to define app display and get date formate.
 *
 * @author MAULIK SANTOKI
 * @version V1.0
 * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc" >Read more about javadoc</a>
 */


public final class DateTimeFormats {

    private DateTimeFormats() {
    }

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    //        public static final String SERVER_DATE_FORMAT_FACEBOOK = "yyyy-MM-dd'T'hh:mm:ssZ";
//        public static final String SERVER_DATE_FORMAT_FACEBOOK_CONVERT = "MMMM dd 'at' hh:mm a";
//        public static final String SERVER_DATE_FORMAT_MILI_SEC = "yyyy-MM-dd HH:mm:ss.SSS";
//        public static final String SERVER_DATE_FORMAT_DASH = "dd MMM yyyy, hh:mm a";
//        public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm";
//        public static final String SERVER_DATE_FORMAT_DAY = "EEEE";
    public static final String SERVER_DATE_FORMAT_TIME = "hh:mm a";
    //        public static final String SERVER_TIME_FORMAT_TIME = "HH:mm:ss";
//        public static final String SERVER_TIME_FORMAT_CONVERT_TIME = "hh:mm a";
//        public static final String DATE_DEFAULT_FORMATE = "dd/MM/yyyy";
    public static final String DISPLAY_MONTHFULL_YEARFULL = "MMMM yyyy"; //November 2017
    //        public static final String CREATE_EVENT_CONVERT_FORMATE = "dd MMM yyyy";
//        public static final String CREATE_EVENT_TIME_FORMATE = "HH:mm:ss";
//        public static final String CREATE_EVENT_TIME_CONVERT_FORMATE = "hh:mm aa";
    public static final String SERVER_DATE_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss"; //2018-09-28 06:00:10
    //        public static final String CONVERT_DATE_TIME_FORMAT = "EEE dd MMM yyyy";
    public static final String CONVERT_DATE_FORMAT_AMPM = "dd MMM yyyy hh:mm aa";
    public static final String SERVER_DATE_FORMAT_NOHHMM = "yyyy-MM-dd"; /*yyyy-MM-DD*/
    public static final String WEEKLY_STATEMENT_DISPLAY = "dd MMM";
    //        public static final String CONVERT_DATE_FORMAT = "yyyy-MM-dd HH:mm aa";
//
//        public static final String POST_DATE = "'Posted on' EEEE, d MMM yyyy";
//        public static final String CONVERT_DATE_FORMAT_COMMENT = "dd MMM yyyy HH:mm aa";
//
//        public static final String CONVERT_DATE_FORMAT_TIME = "MMMM dd @ hh:mm a";
//        public static final String COMMENT_DISPLAY_FORMAT = "EEEE 'at' hh:mm a";
//        public static final String COMMENT_DISPLAY_FORMAT_ONLY_TIME = "'Today at' hh:mm a";
//
//
    public static final String YEAR_ONLY = "yyyy";
    public static final String DISPLAY_YEARFULL_MONTH = "yyyy-MM";
    public static final String DISPLAY_MONTH_YEARFULL = "MMM yyyy";
    public static final String DISPLAY_MONTH_YEAR = "MMM ''yy"; //Jan '18
    public static final String DISPLAY_MFULL_DT_YFULL_TIME = "MMMM d, yyyy 'at' hh:mm a";   //September 9, 2011 at 2:20 pm
    public static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy hh:mm a";
    public static final String DISPLAY_SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    public static final String DISPLAY_SIMPLE_DATE_FORMAT2 = "dd/MM/yyyy";
    public static final String DT_DD_MMM_YYYY = "dd MMM yyyy"; //02 Jan 2018
    public static final String DISPLAY_SIMPLE_DATE_MONTH = "dd MMM"; //02 Jan
    public static final String DISPLAY_DATE_MONTH_TIME = "dd MMM hh:mm a"; //02 Jan 10:00 am
    public static final String DISPLAY_DATE_MONTH_COMMA_TIME = "dd MMM, hh:mm a"; //02 Jan, 10:00 am
    public static final String DISPLAY_SIMPLE_TIME = "hh:mm a"; //02:59 PM

    public static final String DATE_FORMAT_SHORT = "dd MMM ''yy";
    public static final String DT_HOURS_MINUTES = "hh:mm";
    public static final String DT_HOURS_MINTUES_AM_PM_12_HOURS = "hh:mm a";
    public static final String DT_HOURS_MINTUES_AM_PM_24_HOURS = "HH:mm a";
    public static final String DT_DD_MM_YYYY_12_HOURS = "dd-MM-yyyy hh:mm"; /*dd-MM-yyyy hh:mm*/
    public static final String DT_DD_MM_YYYY_12_HOURS_AM_PM = "dd/MM/YYYY hh:mm a";
    public static final String DT_DD_MMM_YYYY_COM_12_AM_PM_SMALL = "dd MMM yyyy, hh:mm a";


    /*past date*/
    public static final String moreThanYear = "MMMM d, yyyy";//"September 9, 2011
    public static final String moreThanYearEvent = "EEE, MMM d, yyyy 'at' h:mma";   //Tue, September 9, 2011
    public static final String moreThenSevenDays = "MMM d 'at' h:mma";//March 30 at 1:14 pm
    public static final String moreThenSevenDaysEvent = "EEE, MMM d 'at' h:mma";//Tue, March 30 at 1:14 pm
    public static final String moreThenOneDays = "EEEE 'at' h:mma";//Friday at 1:48am
    public static final String yesterday = "'Yesterday at' h:mma";//Yesterday at 1:28pm
    public static final String today = "'Today at' h:mma";//Yesterday at 1:28pm

    /*future date*/
    public static final String tomorrow = "'Tomorrow at' h:mma";//Tomorrow : Tomorrow at 5 PM
    public static final String sameWeek = "EEEE 'at' h:mma";//Fri at 6:05 PM
    public static final String sameYear = "MMM d 'at' h:mma";//Nov 25 at 6:05 PM
    public static final String sameYearEvent = "EEE, MMM d 'at' h:mma";//Wed, March 30 at 1:14 pm
    public static final String moreThenOneYear = "MMM d , yyyy 'at' h:mma";//Jan 12, 2017 at 6:05 PM
    public static final String moreThenOneYearEvent = "EEE, MMM d, yyyy 'at' h:mma";   //Wed, September 9, 2011

    public static final String DISPLAY_FULL_MONTH_DATE="MMMM dd, yyyy hh:mm a";


    public static final SimpleDateFormat mFormatFullDate = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
    public static final SimpleDateFormat mFormatTime = new SimpleDateFormat("hh:mm a");
    public static final SimpleDateFormat mFormatDate = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat mFormatDay = new SimpleDateFormat("dd");
    public static final SimpleDateFormat mFormatMonth = new SimpleDateFormat("MMM");

    public static final String DISPLAY_DATE_ONLY = "dd"; //01

    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOCAL_LIST_FORMAT="dd MMM yyyy hh:mm a";

}
