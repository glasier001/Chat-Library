package com.commonlib.utils;

import android.app.Activity;

import com.commonlib.locationutils.LocationTracker;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by root on 16/5/16.
 */
public final class GetUserLocation {

    public static GPSTracker gpsTracker;
    public static LocationTracker locationTracker;
    public static LatLng latLng;

    public static LatLng getCurrentLocation(Activity a, boolean showSetting) {
        gpsTracker = new GPSTracker(a);
//        locationTracker = new LocationTracker(a);
        double lat = gpsTracker.getLatitude(); /*Double.parseDouble(locationTracker.getLatitude());*/
        double lng = gpsTracker.getLongitude(); /*Double.parseDouble(locationTracker.getLongitude());*/

        if (showSetting) {
            if (!gpsTracker.canGetLocation()) {
                gpsTracker.checkLocationSettings();
            }
        }

        // if (LogShowHide.developer_mode) {
//            Toast.makeText(a, lat + ":" + lng, Toast.LENGTH_SHORT).show();
        //}

        latLng = new LatLng(lat, lng);
        gpsTracker.stopUsingGPS();
        return latLng;
    }

}


