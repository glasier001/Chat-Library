package com.commonlib.constants;

// sagar : 5/12/18 This class is not to be extended by any other class.
public final class AppConstants {

    public static final String APP_DB_NAME = "pasupal.db";
    public static final String APP_SHARE_URL = "To get started as an owner with yummy compass, click on below link using my referral.\n\n#";
    /**
     * Application folder for media files
     */
    public static final String TAG = " :mylibrary: ";
    public static final String LOGIN_WITH_APP="1";
    public static final int REF_SCREEN_HEIGHT = 1024;
    public static final int REF_SCREEN_WIDTH = 800;
    public static final int DIALOG_BORDER_RADIUS = 40;
    public static final String THEME_COLOR = "#f96283";
    public static final String APP_TITLE = "Pasupal";
    public static final long LOADING_DELAY = 500; // sagar : 22/10/18 milliseconds
    /**
     * 1=IOS And 2=Android
     */
    public static final String DEVICE_TYPE = "2";
    /**
     * Application folder for media files
     */
    public static final String APPLICATION_DIR_NAME = "Pasupal";
    public static final String IMAGES_DIR = "/Images/";
    public static final String VIDEOS_DIR = "/Videos/";
    public static final String DEVICE_DIR = "/DeviceInfo/";
    //Type Means What purpose of call api
    public static final String REGISTER = "1";
    public static final String FORGOT_PASSWORD = "4";
    public static final String EDIT_PROFILE="5";
    public static final String DEVICE_INFO_TXT_FILE = "log.txt";
//    public static String DEVICE_TOKEN = "1"; /*Device token*/
    public static String API_KEY = "VFDS-3448-RYRU-PTYU-3453-PU49-10PP-123-PCLA-MWFH";
    public static String API_SECRET = "BFGHHRT435345FGDG34CTRYZ423RTPQSG2445";

    public static String PACKAGE_NAME = "com.pasupal.android";
    public static String DEVICE_ID = "1"; /*Device token*/

    private AppConstants() {
    }

    public static final class UsedFor {
        public static final String REGISTER = "1";
        public static final String CHANGE_PASSWORD = "2";
        public static final String EDIT_PROFILE="3"; //change phone
        public static final String FORGOT_PASSWORD = "4";
    }

    public static class SmsSent {
        public static final String SMS_SENT = "0";
        public static final String SMS_NOT_SENT = "1";
    }

    // sagar : 19/1/19 3:03 PM Being used for viewPagerAdapter savedInstance key-values
    private final static String KEY_TAB_COUNTS = "tabCounts";
    private static final String KEY_TITLES = "title";

    public static final String DEFAULT_COUNTRY_CODE = "91";

    // sagar : 12/2/19 11:01 AM Redirect to google map
    /*https://developers.google.com/maps/documentation/urls/guide*/
    /*https://developers.google.com/maps/documentation/urls/android-intents*/
    // sagar : 12/2/19 11:02 AM Example to search centurylink
    /*https://www.google.com/maps/search/?api=1&query=centurylink+field*/

    public static final String GOOGLE_MAP_SEARCH_BASE_URL = "https://www.google.com/maps/search/?api=1";
    public static final String GOOGLE_MAP_QUERY_PARAM = "query";
    public static final String CONTACT_US_EMAIL = "info@pasupal.com";

    public static class SaveInstanceKeys {

        public static final String IS_DESTROYED = "isDestroyed";
        public static final String IS_DETACHED = "isDetached";
    }

    public static final String MULTIPARTS_NAME_ORDER_MEDICINES = "upload_file";
    public static final String MULTIPARTS_NAME_CHAT_PRESCRIPTION = "file_path";
    public static final String CONTEXT = "context";
}
