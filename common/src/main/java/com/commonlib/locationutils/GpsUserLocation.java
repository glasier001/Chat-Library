package com.commonlib.locationutils;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;

import com.commonlib.R;
import com.commonlib.utils.CustomToastMessage;

import org.ankit.gpslibrary.MyTracker;

import static com.commonlib.utils.NetworkUtils.isNetworkAvailable;

public class GpsUserLocation {

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private LocationManager mLocationManager;
    private Activity mActivity;
    private final int REQUEST_CHECK_SETTING = 10001;
    private final int SETTINGS = 0;
    private Location mCurrentLocation;
    private MyTracker mMyTracker;
    private double mLat;
    private double mLng;
    private double latitude; // latitude
    private double longitude; // longitude
    private boolean showSettings;

    public GpsUserLocation(Activity activity, boolean showSettings) {

        mActivity = activity;
        this.showSettings = showSettings;
        mMyTracker = new MyTracker(mActivity);
        canGetLocation = false;

        mLocationManager = (LocationManager) mActivity.getSystemService(mActivity.LOCATION_SERVICE);
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = isNetworkAvailable(mActivity);


        if (!isGPSEnabled) {
            if (showSettings) {
                showAlertDialog();
            }
        }
        if (!isNetworkEnabled) {
            CustomToastMessage.animRedTextMethod(mActivity, mActivity.getString(R.string.error_no_internet));
        }
        if (isGPSEnabled && isNetworkEnabled) {
            startLocationUpdate();
        }
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        alertDialog.setTitle("Device location off");

        alertDialog.setMessage("To continue, let your device turn on location using Google's location service. ");

        alertDialog.setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivityForResult(intent, REQUEST_CHECK_SETTING);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        alertDialog.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void startLocationUpdate() {
        canGetLocation = true;
        mCurrentLocation = mMyTracker.getLocation();
        mLat = mMyTracker.getLatitude();
        mLng = mMyTracker.getLongitude();
    }

    public double getLatitude() {
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (mCurrentLocation != null) {
            longitude = mCurrentLocation.getLongitude();
        }
        return longitude;
    }


} 