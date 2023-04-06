package com.commonlib.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.commonlib.R;


/**
 * Created by mauliksantoki on 5/7/17.
 */

public final class RedirectToMap {

    private RedirectToMap(){}

    public static void redirectToMap(Activity activity, Double lat, Double lng) {
        try {
            boolean isGoogleMapInstalled = appInstalledOrNot(activity, "com.google.android.apps.maps");
            if (isGoogleMapInstalled) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "," + lng));
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.error_google_map_not_installed), Toast.LENGTH_SHORT).show();
                try {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
                }
            }
        } catch (ActivityNotFoundException ex) {
            CustomToastMessage.animRedTextMethod(activity, activity.getResources().getString(R.string.error_google_map_failed_please_try_again_later));
        }
    }
    public static void directionToMapByAddress(Activity activity,String address) {
        try {
            boolean isGoogleMapInstalled = appInstalledOrNot(activity, "com.google.android.apps.maps");
            if (isGoogleMapInstalled) {

                LogShowHide.LogShowHideMethod(activity, "http://maps.google.com/maps?saddr=&daddr=" + address + "&directionsmode=driving");

                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + address + "&directionsmode=driving"));
                activity.startActivity(intent);*/

                /*https://www.google.com/maps/search/?api=1&parameters*/


                // sagar : 25/3/19 12:59 PM Gives address
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);

                // sagar : 25/3/19 12:59 PM Gives navigation (driving mode)
//                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);

                // sagar : 25/3/19 1:08 PM Gives direction
//                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=&destination=" + address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);

            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.error_google_map_not_installed), Toast.LENGTH_SHORT).show();
                try {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
                }
            }
        } catch (ActivityNotFoundException ex) {
            CustomToastMessage.animRedTextMethod(activity, activity.getResources().getString(R.string.error_google_map_failed_please_try_again_later));
        }
    }


    public static void directionToMap(Activity activity, Double lat, Double lng) {
        try {
            boolean isGoogleMapInstalled = appInstalledOrNot(activity, "com.google.android.apps.maps");
            if (isGoogleMapInstalled) {
                LogShowHide.LogShowHideMethod(activity, "http://maps.google.com/maps?saddr=&daddr=" + lat + "," + lng + "&directionsmode=driving");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + lat + "," + lng + "&directionsmode=driving"));
                activity.startActivity(intent);

            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.error_google_map_not_installed), Toast.LENGTH_SHORT).show();
                try {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
                }
            }
        } catch (ActivityNotFoundException ex) {
            CustomToastMessage.animRedTextMethod(activity, activity.getResources().getString(R.string.error_google_map_failed_please_try_again_later));
        }
    }


    /* check App install or not */
    public static boolean appInstalledOrNot(Activity activity, String uri) {
        PackageManager pm = activity.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
