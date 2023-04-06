package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

/**
 * Created by maulik on 18/5/17.
 */

public final class LayoutDirection {

    private LayoutDirection() {
    }

    public static Configuration config;

    public static int getLayoutDirection(Activity activity) {
//        intConfiguration(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return View.LAYOUT_DIRECTION_RTL;
        } else {
            return View.LAYOUT_DIRECTION_LTR;
        }
    }

//    private static void intConfiguration(Activity activity) {
//        config = LocaleHelper.getConfiguration();
//        if (config == null) {
//            config = activity.getResources().getConfiguration();
//        }
//    }
//
//    private static void intConfiguration(Context activity) {
//        config = LocaleHelper.getConfiguration();
//        if (config == null) {
//            config = activity.getResources().getConfiguration();
//        }
//    }

    public static int getLayoutDirection(Context context) {
//        intConfiguration(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return View.LAYOUT_DIRECTION_RTL;
        } else {
            return View.LAYOUT_DIRECTION_LTR;
        }
    }
}
