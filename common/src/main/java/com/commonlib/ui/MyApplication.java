package com.commonlib.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.commonlib.countrycodeinfo.CountryCodeInfoController;

import androidx.multidex.MultiDex;


public class MyApplication extends Application {

    public CountryCodeInfoController countryCodeInfoController;


    //this will initialize multidex in your own Application class
    @Override
    protected void attachBaseContext(Context base) {
        countryCodeInfoController = new CountryCodeInfoController(base);
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed(Activity act) {
        /*this code is use for in all activity first check user loginin or not and then send usre to login*/
//        if (act != null
//                && act instanceof Act_Login_Screen == false
//                && act instanceof Act_Splash_Screen == false
//                && act instanceof Act_Sign_up_Screen == false
//                && act instanceof Act_Forgot_Password_Screen == false
//                && act instanceof MarshMallowPermissionActy == false
//                && act instanceof Act_Verification_Code_Screen == false
//                && act instanceof Act_New_Password_Screen == false
//                && act instanceof Act_Term_Privacy_FAQ_Screen == false
//                ) {
//            prefsPrivate = act.getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
//            if (prefsPrivate.getString(Prefkeys.LOGIN_USER.USER_ID, "").equalsIgnoreCase("") && prefsPrivate.getString(Prefkeys.LOGIN_USER.USER_ID, "").toString().trim().length() <= 0) {
//                logout_controller = new Logout_Controller();
//                logout_controller.methodDeleteUserData(act);
//            }
//        }
//
        activityVisible = true;
        activity = act;
    }


    public static void activityPaused() {
        activityVisible = false;
        activity = null;
    }

    public static Activity getActivity() {
        return activity;
    }

    private static boolean activityVisible;
    private static Activity activity;


}
