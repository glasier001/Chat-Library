package com.commonlib.utils;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by mauliksantoki on 6/5/17.
 */

public final class GetGeocoderAddress {

    private GetGeocoderAddress() {
    }

    private List<Address> addresses = null;
    private Geocoder geocoder;
    private GoogleApiClient googleApiClient;

    private Activity activity;
    double latitude;
    double longitude;

    public GetGeocoderAddress(Activity activity) {
        geocoder = new Geocoder(activity, Locale.getDefault());
        this.activity = activity;
    }

    public interface setAddressListener {
        public void Address(String StreetName, String StreetName2, String City, String State, String PostalCode, String CoutryName, String CoutryCode, double latitude, double longitude);

        public void fail();
    }


    public void getAddress(final setAddressListener setAddressListener) {
        // In this sample, get just a single address.
        try {
            latitude = new GetUserLocation().getCurrentLocation(activity, false).latitude;
            longitude = new GetUserLocation().getCurrentLocation(activity, false).longitude;

////            latitude = 24.5525760650635;
////            longitude = 46.6768531799316;
////
////            latitude = 24.7135517;
////            longitude = 46.67529569999999;
//
////            latitude = 21.2854067;
////            longitude = 39.23755069999993;
//
//            latitude = 24.742953;
//            longitude = 24.742953;


            addresses = geocoder.getFromLocation(latitude, longitude, 1);


            String cityName = (addresses.get(0).getLocality() != null) ? addresses.get(0).getLocality() : "";
            String postalCode = (addresses.get(0).getPostalCode() != null) ? addresses.get(0).getPostalCode() : "";
            String state = (addresses.get(0).getAdminArea() != null) ? addresses.get(0).getAdminArea() : "";
            String coutry = (addresses.get(0).getCountryName() != null) ? addresses.get(0).getCountryName() : "";
            String coutrySN = (addresses.get(0).getCountryCode() != null) ? addresses.get(0).getCountryCode() : "";
            String subStreet = (addresses.get(0).getThoroughfare() != null) ? addresses.get(0).getThoroughfare() : addresses.get(0).getSubThoroughfare();
            String subStreet2 = (addresses.get(0).getSubLocality() != null) ? addresses.get(0).getSubLocality() : "";

            setAddressListener.Address(subStreet, subStreet2, cityName, state, postalCode, coutry, coutrySN, latitude, longitude);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            setAddressListener.fail();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
//                        LogShowHide.LogShowHideMethod("autocomplet", "Latitude = " + location.latitude + ", Longitude = " + location.longitude, illegalArgumentException);
            setAddressListener.fail();
        }
    }


}
