package com.commonlib.locationutils;

import android.app.Activity;

import com.commonlib.utils.StringUtils;
import com.commonlib.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by root on 16/5/16.
 */
public class GetUserLocation {

    // TODO: Bhargav Savsasani : 17/1/19 call this class example

    /*
     public static String getDefaultLatLng(Double val) {
        if (val != null && val > 0.0) {
            return String.valueOf(val);
        } else {
            return "";
        }
    }

        getDefaultLatLng(GetUserLocation.getCurrentLocation(activity,false).latitude);
        getDefaultLatLng(GetUserLocation.getCurrentLocation(activity,false).longitude);

     */

    public static LocationTracker locationTracker;

    public static LatLng getCurrentLocation(Activity activity) {
        locationTracker = new LocationTracker(activity);
        return new LatLng(Double.parseDouble(locationTracker.getLatitude()), Double.parseDouble(locationTracker.getLongitude()));
    }

    public static String getLatitude(Activity activity) {
        locationTracker = new LocationTracker(activity);
        return Utils.getDefaultLatLng(Double.parseDouble(locationTracker.getLatitude()));
    }

    public static String getLongitude(Activity activity) {
        locationTracker = new LocationTracker(activity);
        return Utils.getDefaultLatLng(Double.parseDouble(locationTracker.getLongitude()));
    }

    public static double getLatitudeInDouble(Activity activity) {
        locationTracker = new LocationTracker(activity);
        return Double.parseDouble(locationTracker.getLatitude());
    }

    public static double getLongitudeInDouble(Activity activity) {
        locationTracker = new LocationTracker(activity);
        return Double.parseDouble(locationTracker.getLongitude());
    }

    public static boolean canGiveLocation(Activity activity) {
        locationTracker = new LocationTracker(activity);
        return StringUtils.isNotNullNotEmpty(locationTracker.getLatitude());
    }

}


