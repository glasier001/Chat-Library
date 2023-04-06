package com.commonlib.locationutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import com.commonlib.R;
import com.commonlib.utils.CustomToastMessage;

import org.ankit.gpslibrary.MyTracker;

import static com.commonlib.utils.NetworkUtils.isNetworkAvailable;

public class LocationTracker {

    public static final int SETTINGS = 0;
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    private Location mCurrentLocation;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private Activity mActivity;
    private MyTracker myTracker;
    private String latitude = "0.0"; // latitude
    private String longitude = "0.0"; // longitude



    public LocationTracker(Activity activity) {
        if (activity != null) {
            this.mActivity = activity;
            myTracker = new MyTracker(activity);
//       checkServices();
            startLocationUpdate();
        }
    }

    private void startLocationUpdate() {
        mCurrentLocation = myTracker.getLocation();
    }

    private void checkServices() {
        locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = isNetworkAvailable(mActivity);

        if (!isGPSEnabled) {
            showAlertDialog();
        }
        if (!isNetworkEnabled) {
            CustomToastMessage.animRedTextMethod(mActivity, mActivity.getString(R.string.error_no_internet));
        }
        if (isGPSEnabled && isNetworkEnabled) {
            startLocationUpdate();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        alertDialog.setTitle(mActivity.getString(R.string.label_device_location_off));

        alertDialog.setMessage(mActivity.getString(R.string.label_location_msg));

        alertDialog.setPositiveButton(mActivity.getString(R.string.label_enable_location), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivityForResult(intent, REQUEST_CHECK_SETTINGS);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        alertDialog.setNegativeButton(mActivity.getString(R.string.label_no_thanks), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public String getLatitude() {
        if (mCurrentLocation != null) {
            return latitude = String.valueOf(mCurrentLocation.getLatitude());
        }
        return latitude;
    }

    public String getLongitude() {
        if (mCurrentLocation != null) {
            return longitude = String.valueOf(mCurrentLocation.getLongitude());
        }
        return longitude;
    }
}