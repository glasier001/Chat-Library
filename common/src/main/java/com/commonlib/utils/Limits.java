package com.commonlib.utils;

/**
 * This class is use to input type limits and never be extended.
 *
 * @author Sagar
 * @version V1.0
 * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc" >Read more about javadoc</a>
 */

public final class Limits {

    /**
     * Limit 225.
     */
    public static final int EMAIL_TXT_LIMIT = 255;
    /**
     * Limit 5.
     */
    public static final int COUNTRY_CODE_TXT_LIMIT = 3;
    /**
     * Limit 11.
     */
    public static final int PHONE_NUMBER_TXT_LIMIT = 10;
    /**
     * Limit 6.
     */
    public static final int ZIP_TXT_LIMIT = 6;
    /**
     * Limit 6.
     */
    public static final int PASSWORD_LENGTH_MIM_LIMIT = 6;
    /**
     * Limit 50.
     */
    public static final int PASSWORD_LENGTH_MAX_LIMIT = 50;
    /**
     * Limit 4.
     */
    public static final int PHONENUMBER_LENGTH_MIM_LIMIT = 10;
    public static final int REDEEM_CODE_MAX_LIMIT = 4;
    public static final int COUNTRY_NAME = 50;
    public static final int VARIFICATIONTIME = 60000;
    /**
     * 2 Sec
     */
    public static final int SPLASH_TIME_OUT = 2000;
    /**
     * connection timeout on Android api is 3000 milliseconds
     */
    public static final int ANDROID_CONNECTION_TIMEOUT = 120000;
    /**
     * Default year to be shown to the user while selecting birth date
     */
    public static final int BIRTHDATE_YEAR_DEFAULT = 1990;
    public static final int MIN_YEAR_DEFAULT = 1950;
    /**
     * Default page number limit, load data limit for list. I.e., 20 data per page/api call
     */
    public static final int PAGENUMBER_LIMIT = 20;
    public static final int USER_COMMENT = 1000;
    public static final int SEARCH_QUERY_MIN_LENGTH = 1;
    public static final long EXIT_RESET_TIME = 2000L;

    public static final int PINCODE_MIN = 3;
    public static final int PINCODE_MAX = 6;
    public static final int PINCODE = 6;
    public static final int PHOTO_UPLOAD_LIMIT = 3; //This is not size, this is the limit to upload maximum numbers of photos

    public static final int LANDLINE_MIN_LIMIT = 6;
    public static final int LANDLINE_MAX_LIMIT = 20;

    public static final int INTERVAL_API_CALL_FOR_REQUEST = 10000;
    public static final int INTERVAL_DELAY_API_CALL_FOR_REQUEST = 1000;
    public static final int OFFLINE_LIMIT = 20;
    public static final int OFFSET = OFFLINE_LIMIT;

    private Limits() {
    }

}
