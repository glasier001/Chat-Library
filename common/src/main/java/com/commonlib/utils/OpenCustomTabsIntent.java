package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.commonlib.R;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;


/**
 * Created by mauliksantoki on 20/6/17.
 */

public final class OpenCustomTabsIntent {

    private OpenCustomTabsIntent() {
    }

    public static void openCustomTabsIntent(Activity activity, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.ThemeColor));

        builder.setStartAnimations(activity, R.anim.slide_in_left, R.anim.slide_out_left);
        builder.setExitAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = builder.build();
        if (appInstalledOrNot(activity, "com.android.chrome")) {
            customTabsIntent.intent.setPackage("com.android.chrome");
        }
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    public static void openCustomTabsIntent(Context activity, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.ThemeColor));
        CustomTabsIntent customTabsIntent = builder.build();
        if (appInstalledOrNot(activity, "com.android.chrome")) {
            customTabsIntent.intent.setPackage("com.android.chrome");
        }
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    public static boolean appInstalledOrNot(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai.enabled) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static boolean appInstalledOrNot(Context activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai.enabled) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
