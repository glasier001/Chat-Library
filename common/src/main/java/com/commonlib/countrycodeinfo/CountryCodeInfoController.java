package com.commonlib.countrycodeinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.utils.StringUtils;
import com.commonlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CountryCodeInfoController {
    private Context context;
    private List<CountryModel> countryCodeInfoControllerListArray = new ArrayList<CountryModel>();
    private String[] info;

    public CountryCodeInfoController(Context con) {
        context = con;
        intArray();
    }

    public void intArray() {
        String[] rl = context.getResources().getStringArray(R.array.full_country_info);
        for (String s : rl) {
            info = s.split(",", 3);
            countryCodeInfoControllerListArray.add(new CountryModel(
                    info[0],
                    info[1],
                    info[2]
            ));
        }
    }

    public List<CountryModel> getCountryArrayList(Activity con) {
        if (context == null) {
            context = con;
        }

        if (countryCodeInfoControllerListArray == null || countryCodeInfoControllerListArray.size() <= 0) {
            intArray();
        }
        return countryCodeInfoControllerListArray;
    }

    public CountryModel getUserCountryCode() {
        return getData(getUserCountry());
    }

    public CountryModel getData(String value) {
        if (value != null) {
            for (CountryModel model : countryCodeInfoControllerListArray) {
                if (model.getDialingCode().equalsIgnoreCase(value) || model.getIsoCode().equalsIgnoreCase(value)) {
                    return model;
                }
            }
        }
        return new CountryModel();
    }

    private String getUserCountry() {
        String countryCode = AppConstants.DEFAULT_COUNTRY_CODE;
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country
                // code is available
                countryCode = simCountry.toUpperCase(Locale.US);
                if (StringUtils.isNotNullNotEmpty(countryCode)) {
                    return countryCode;
                } else {
                    try {
                        countryCode = tm.getNetworkCountryIso();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (StringUtils.isNotNullNotEmpty(countryCode)) {
                        return countryCode;
                    }
                }
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device
                // is not 3G(would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network
                    // country code is available
                    return networkCountry.toUpperCase(Locale.US);
                }
            } else {
                return Locale.getDefault().getCountry().toUpperCase(Locale.US);

            }
        } catch (Exception e) {
            Utils.showErrorToast(context, e.getMessage());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
            if (StringUtils.isNotNullNotEmpty(countryCode)) {
                return countryCode;
            }
        } else {
            countryCode = context.getResources().getConfiguration().locale.getCountry();
            if (StringUtils.isNotNullNotEmpty(countryCode)) {
                return countryCode;
            }
        }

        return countryCode;
    }

}
