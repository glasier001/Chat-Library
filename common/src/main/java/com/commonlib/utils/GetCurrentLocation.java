package com.commonlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.commonlib.listeners.Callbacks;

import java.util.List;

import androidx.core.app.ActivityCompat;

import static com.commonlib.utils.GPSTracker.REQUEST_CHECK_SETTINGS;

public final class GetCurrentLocation implements PermissionListener {

    public static Callbacks.OnGetLocationListener mOnGetLocationListener;
    private static volatile GetCurrentLocation mGetCurrentLocation;
    public double mLatitude;
    public double mLongitude;
    private Activity mActivity;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public GetCurrentLocation(Activity activity, Callbacks.OnGetLocationListener onGetLocationListener) {
        this.mActivity = activity;
        mOnGetLocationListener = onGetLocationListener;
        getUpdatedLocation();
    }

    private void getUpdatedLocation() {
        locationRequest = new LocationRequest();
//        locationRequest.setInterval(7500); //use a value fo about 10 to 15s for a real app
//        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        mOnGetLocationListener.getLatitude(mLatitude);
                        mOnGetLocationListener.getLongitude(mLongitude);
                    }
                }
            });
        } else {

            TedPermission.with(mActivity)
                    .setPermissionListener(this)
                    .setDeniedMessage(MarshMallowPermissionMessage.getLocationDeniedMessage(mActivity))
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();

        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    //Update UI with location data
                    if (location != null) {
                        mOnGetLocationListener.getLatitude(mLatitude);
                        mOnGetLocationListener.getLongitude(mLongitude);
                    }
                }
            }
        };

        // FIXME: 6/6/18  sagar: start location update if and only location service is enabled
//        startLocationUpdates();

        SettingsClient settingsClient = LocationServices.getSettingsClient(mActivity);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> locationTask =
                settingsClient.checkLocationSettings(builder.build());

        locationTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> locationTask) {
                try {
                    LocationSettingsResponse response = locationTask.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    startLocationUpdates();

                    LocationSettingsStates locationSettingsStates = response.getLocationSettingsStates();
                    //Call methods on locationSettingsStates to proceed further
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                // FIXME: 6/6/18  sagar: If previously turned off, after getting location, it should be turned off again.
                                resolvable.startResolutionForResult(
                                        mActivity,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }

    }

    public static synchronized GetCurrentLocation getCurrentLocationInstance(Activity activity, Callbacks.OnGetLocationListener onGetLocationListener) {
        if (mGetCurrentLocation == null) {
            synchronized (GetCurrentLocation.class) {
                if (mGetCurrentLocation == null) {
                    mGetCurrentLocation = new GetCurrentLocation(activity, onGetLocationListener);
                }
            }
        } else {
            mGetCurrentLocation = new GetCurrentLocation(activity, onGetLocationListener);
        }

        return mGetCurrentLocation;
    }

    @Override
    public void onPermissionGranted() {
        getUpdatedLocation();

    }

    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {

    }

}
