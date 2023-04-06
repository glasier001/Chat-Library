package com.commonlib.constants;


@SuppressWarnings("WeakerAccess") /*As we may need to access several fields out of this class though seems not*/
public final class AppUrls {

    public static final String API_VERSION_1_0 = "v1_0/";
    public static final String LOCAL_URL_2 = "http://192.168.0.2/pasupal/api/";
    public static final String LOCAL_URL_14 = "http://192.168.0.14/pasupal/api/";
    public static final String DEV_URL = "https://dev.patel-apps.com/pasupal/api/";
    public static final String MMC_LIVE_URL = "http://mmc-apps.com/pasupal/api/";
    public static final String LIVE_URL = "https://api.pasupal.com/";
    public static final String VIDEO_DETAIL_URL = "https://www.pasupal.com/";

    public static final String CHAT_SERVER_URL = "http://192.168.0.33:7000"; //server
//    public static final String CHAT_SERVER_URL = "http://192.168.0.22:5000"; //local

    public static final String BASIC_URL = LIVE_URL;
//    public static final String BASIC_URL_NEW = "https://www.hetinfo.in/pasupal/api/";

    public static final String CURRENT_URL = BASIC_URL + API_VERSION_1_0;

    public static final String TERMS_AND_CONDITIONS_LINK="https://pasupal.com/laravelapi/terms_and_condition";
    public static final String PRIVACY_POLICY_LINK="https://pasupal.com/laravelapi/privacy_policy";

    public static final String NEW_BASE_URL="https://pasupal.com/laravelapi/api/";



}
