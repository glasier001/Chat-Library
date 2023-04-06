package com.commonlib.ui.fragments;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.R;
import com.commonlib.customdialogs.AlertDialog;
//import com.commonlib.databinding.FragmentMapBinding;
import com.commonlib.listeners.Callbacks;
import com.commonlib.locationutils.GPSTracker;
import com.commonlib.typefacehandler.TypefaceUtils;
import com.commonlib.ui.baseui.BaseViewStubFragment;
import com.commonlib.utils.CustomToastMessage;
import com.commonlib.utils.GetUserLocation;
import com.commonlib.utils.MarshMallowPermissionMessage;
import com.commonlib.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import static android.util.Log.d;
import static com.commonlib.constants.AppConstants.TAG;
import static com.commonlib.locationutils.LocationTracker.SETTINGS;
import static com.commonlib.utils.NetworkUtils.isNetworkAvailable;

/**
 * A simple {@link BaseViewStubFragment} subclass.
 * Simply, load this fragment whenever you need to load map
 * No controllers, only map related functionality
 */
public class MapFragment extends BaseViewStubFragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Callbacks.OnEventCallbackListener {

//    private FragmentMapBinding mBinding;
    private SupportMapFragment mapView;
    private LatLng myLatLng;
    private LocationManager locationManager;
    private boolean isGpsEnabled;
    private boolean isNetworkEnabled;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private AlertDialog locationAlert;
    private BroadcastReceiver gpsSwitchStateReceiver;
    private GoogleMap mGoogleMap;
    private Callbacks.OnEventCallbackListener onEventCallbackListener;

    public MapFragment() {
        // Required empty public constructor
    }

    public void setOnEventCallbackListener(Callbacks.OnEventCallbackListener onEventCallbackListener) {
        this.onEventCallbackListener = onEventCallbackListener;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    protected void onViewStubInflated(View inflatedView) {
//        mBinding = FragmentMapBinding.bind(inflatedView);
    }

    @Override
    protected void initControllers() {
        initBroadcastReceiver();

        if (getArguments() != null) {
            if (Utils.hasParcel(getArguments())) {
                myLatLng = (LatLng) Utils.getParcel(getArguments());
            }
        }

        if (myLatLng == null) {
            myLatLng = GetUserLocation.getCurrentLocation(getActivity(), false);
        }

        setMyLatLng(myLatLng);
//        addCurrentMarker(myLatLng);
    }

    @Override
    protected void setCustomTitleBar() {

    }

    @Override
    protected void handleViews() {
        invokeLocationPermission();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void otherStuffs() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onLocationCallback();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mapView != null && getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(mapView).commit();
        }
        if (mapView != null) {
            getChildFragmentManager().beginTransaction().remove(mapView).commit();
        }
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(gpsSwitchStateReceiver);
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void setActivity(FragmentActivity activity) {

    }

    /**
     * sagar : 26/3/19 4:42 PM
     * <p>
     * Location service was turned off, we prompted to turn it on,
     * user did not turn it on at that time {@link #initLocationService()} {@link #showLocationAlert()} {@link #onActivityResult(int, int, Intent)}
     * We started watching/observing whenever user turn it on/off {@link #watchLocationService()}
     * We need location call back instance for {@link #fusedLocationClient}
     * Here that instance is.
     * This callback will give location whenever user turns the location service on.
     * Being used in {@link #startLocationRequests()}
     * </p>
     *
     * @since 1.0
     */
    private void onLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    if (Utils.isNotNullNotEmptyLocation(location)) {

                        d(TAG, "MapFragment: onLocationResult: " + location.getLatitude());

                        setMyLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

                        if (Utils.isNotNullNotEmptyLocation(getCurrentLocation(true))) {
                            updateLocationUi();
                        }

                        // sagar : 27/3/19 4:41 PM Remove locationCallback in onPause or in onDestroy if you want to track location change throughout certain intervals
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                        return;
                    }
                }
            }
        };
    }

    private void setMyLatLng(LatLng myLatLng) {
        this.myLatLng = myLatLng;
    }


    /**
     * sagar : 26/3/19 4:40 PM
     * <p>
     * We have location permission, user has location turned on
     * Get location here and update location ui
     * Being used in {@link #updateLocationUi()}
     *
     * @since 1.0
     */
    private Location getCurrentLocation(boolean isCurrentLocation) {

        // sagar : 27/3/19 4:24 PM Get current location if it is null or its latitude or longitude is 0
        if (Utils.isNullOrEmptyLocation(currentLocation)) {
            if (getActivity() != null) {
                LatLng latLng = getMyLatLng(isCurrentLocation);
                Location location = new Location("");
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                currentLocation = location;
                d(TAG, "MapFragment: getCurrentLocation: " + currentLocation.getLatitude());
            }
        }

        return currentLocation;
    }

    /**
     * sagar : 26/3/19 5:00 PM
     * <p>
     * We have location permission, location service is turned on
     * we got the current location {@link #getCurrentLocation(boolean)} ()}
     * We will use that location here
     * </p>
     *
     * @since 1.0
     */
    private void updateLocationUi() {

        if (Utils.isNotNullNotEmptyLocation(getCurrentLocation(true))) {

            if (mapView == null) {
                setMapView();
            }

            addCurrentMarker(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        }
    }
    //endregion

    private LatLng getMyLatLng(boolean isCurrentLatLng) {
        if (myLatLng == null || isCurrentLatLng) {
            myLatLng = GetUserLocation.getCurrentLocation(getActivity(), false);
        }

        return myLatLng;
    }

    //region Simply, setting up the map view here
    private void setMapView() {
        FragmentManager fm = getChildFragmentManager();
        mapView = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapView == null) {
            mapView = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, mapView, "mapFragment");
            //https://medium.com/@elye.project/handling-illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-d4ee8b630066
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
        mapView.getMapAsync(this);


//        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapView.getMapAsync(this);

    }

    private void addCurrentMarker(LatLng latLng) {
        MarkerOptions mMarkerOptions = new MarkerOptions()
                .position(latLng)
//                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin));

        if (mGoogleMap != null) {
            mGoogleMap.addMarker(mMarkerOptions);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
            mGoogleMap.animateCamera(update);
        }
    }

    /**
     * sagar : 26/3/19 4:50 PM
     * <p>
     * First of all we would ask for location permission before any location operation
     *
     * </p>
     * Being used in {@link #handleViews()}
     *
     * @since 1.0
     */
    private void invokeLocationPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                initLocationService();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                // sagar : 27/3/19 4:52 PM Even if we don't have location permission, we should still load the map
                if (mapView == null) {
                    setMapView();
                }
            }
        };

        if (getActivity() != null) {
            TedPermission.with(getActivity())
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage(MarshMallowPermissionMessage.getLocationDeniedMessage(getActivity()))
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        }
    }

    /**
     * sagar : 26/3/19 4:56 PM
     * <p>
     * First thing to do after getting location permission/s
     *
     * </p>
     * Being used in {@link #invokeLocationPermission()}
     *
     * @since 1.0
     */
    private void initLocationService() {
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = isNetworkAvailable(getContext());

        if (!isGpsEnabled) {
            // sagar : 27/3/19 4:53 PM If user has location off, ask to turn it on
            showLocationAlert(); //done (onActivityResult)
        }

        if (!isNetworkEnabled) {
            CustomToastMessage.animRedTextMethod(getActivity(), getString(R.string.error_no_internet));
        }

        if (isGpsEnabled && isNetworkEnabled) {
            // sagar : 27/3/19 4:54 PM Update location ui if both location service and internet are available
            updateLocationUi(); //done (get location and update location ui)
        } else {
            // sagar : 27/3/19 4:54 PM Observe to get response whenever user changes location service status
            watchLocationService(); //done (locationrequest, locationcallback, fusedlocationclient)
        }


        if (mapView == null) {
            setMapView();
        }
    }

    /**
     * sagar : 27/3/19 5:01 PM
     * <p>
     * Because sometimes we don't get response in {@link #watchLocationService()} when user changes the status of location service
     * https://stackoverflow.com/questions/15778807/how-to-detect-when-user-turn-on-off-gps-state
     * </p>
     *
     * @since 1.0
     */
    private void initBroadcastReceiver() {
        /*https://stackoverflow.com/questions/15778807/how-to-detect-when-user-turn-on-off-gps-state*/

        if (getActivity() != null) {
            gpsSwitchStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        if (isGpsEnabled || isNetworkEnabled) {
                            updateLocationUi();
                        } else {
                            watchLocationService();
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
            getActivity().registerReceiver(gpsSwitchStateReceiver, filter);
        }
    }

    @Override
    public void onPauseFragment(FragmentActivity fragmentActivity) {

    }

    @Override
    public void onResumeFragment(FragmentActivity fragmentActivity) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setOnMapClickListener(this);
        if (getMyLatLng(true) != null) {
            addCurrentMarker(myLatLng);
        }
    }

    /**
     * sagar : 26/3/19 4:35 PM
     * <p>
     * User had location off, we prompted to turn it on through dialog, user tapped on yes enable
     * User redirected to settings, user returned - we don't know what user has done in settings
     * We will check it here
     * {@link #showLocationAlert()}
     *
     * @since 1.0
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GPSTracker.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case SETTINGS:
                        if (locationManager != null) {
                            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        }
                        if (getActivity() != null) {
                            isNetworkEnabled = isNetworkAvailable(getActivity());
                            if (isGpsEnabled && isNetworkEnabled) {
                                updateLocationUi();
                            }
                        }
                        break;
                }
                break;
        }
    }

    private void watchLocationService() {
        if (getActivity() != null) {
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                invokeLocationPermission();
                return;
            }
        /*    lm.addGpsStatusListener(new android.location.GpsStatus.Listener() {
                public void onGpsStatusChanged(int event) {
                    switch (event) {
                        case GPS_EVENT_STARTED:
                            // do your tasks
                            d(TAG, "ShopInfoFragment: onGpsStatusChanged: ");
                            startLocationRequests();
                            break;
                        case GPS_EVENT_STOPPED:
                            // do your tasks
                            break;
                    }
                }
            });*/


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lm.registerGnssStatusCallback(new GnssStatus.Callback() {
                    @Override
                    public void onStarted() {
                        super.onStarted();
                        d(TAG, "ShopInfoFragment: onGpsStatusChanged: ");
                        startLocationRequests();
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                    }

                    @Override
                    public void onFirstFix(int ttffMillis) {
                        super.onFirstFix(ttffMillis);
                    }

                    @Override
                    public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
                        super.onSatelliteStatusChanged(status);
                    }
                });
            }
        }
    }

    private void startLocationRequests() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); //in milliseconds
        locationRequest.setFastestInterval(500); //response to location update
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                invokeLocationPermission();
                return;
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }

    /**
     * sagar : 26/3/19 4:50 PM
     * <p>
     * User location service is off, we are prompting to turn it on
     * </p>
     * Being used in {@link #initLocationService()}
     *
     * @since 1.0
     */
    private void showLocationAlert() {
        if (getActivity() != null) {
            locationAlert = new AlertDialog(getActivity()).setClickListener(new AlertDialog.OnClickListener() {
                @Override
                public void onClickLeft(String message) {
                    locationAlert.cancel();
                }

                @Override
                public void onClickRight(String message) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, GPSTracker.REQUEST_CHECK_SETTINGS);
                    getActivity().overridePendingTransition(0, 0);
                    locationAlert.cancel();
                }
            });
            locationAlert.setTitle(getString(R.string.label_device_location_off));
            locationAlert.setContentMessage(getString(R.string.label_content_why_location));

            locationAlert.setLeftBtnText(getString(R.string.label_no_thanks));
            locationAlert.setRightBtnText(getString(R.string.label_enable_location));

            locationAlert.setLeftBtnColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
            locationAlert.setRightBtnColor(ContextCompat.getColor(getActivity(), R.color.colorBlueDark35));

            locationAlert.setLeftBtnTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(getActivity()));
            locationAlert.setRightBtnTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(getActivity()));

            locationAlert.show();
            locationAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    updateLocationUi();
                }
            });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        setMyLatLng(latLng);
        mGoogleMap.clear();
        addCurrentMarker(myLatLng);
    }

    @Override
    public void onEventCallback(View v, int positionTag, Intent intentData, Object... objects) {

    }
}
