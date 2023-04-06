package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.commonlib.BuildConfig;
import com.commonlib.constants.AppConstants;


public final class LogShowHide {

    static String tag = AppConstants.APP_TITLE;

    private LogShowHide() {
    }

    public static void LogShowHideMethod(Activity activity, String text2) {
        // TODO Auto-generated constructor stub

        if (BuildConfig.DEBUG) {
            try {
                Log.d(tag, (activity != null) ? activity.getClass().getSimpleName() + "=>" + text2 : text2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void LogShowHideMethod(Context activity, String text2) {
        // TODO Auto-generated constructor stub

        if (BuildConfig.DEBUG) {
            try {
                Log.d(tag, (activity != null) ? activity.getClass().getSimpleName() + "=>" + text2 : text2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
