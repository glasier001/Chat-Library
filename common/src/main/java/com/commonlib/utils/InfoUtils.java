package com.commonlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.commonlib.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by sagar on 10/1/18.
 */

public class InfoUtils {

    /*System.getProperty("os.version"); // OS version
    android.os.Build.VERSION.SDK      // API Level
    android.os.Build.DEVICE           // Device
    android.os.Build.MODEL            // Model
    android.os.Build.PRODUCT          // Product*/


    /*String  details =  "VERSION.RELEASE : "+Build.VERSION.RELEASE
            +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
		    +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
		    +"\nBOARD : "+Build.BOARD
		    +"\nBOOTLOADER : "+Build.BOOTLOADER
		    +"\nBRAND : "+Build.BRAND
		    +"\nCPU_ABI : "+Build.CPU_ABI
		    +"\nCPU_ABI2 : "+Build.CPU_ABI2
		    +"\nDISPLAY : "+Build.DISPLAY
		    +"\nFINGERPRINT : "+Build.FINGERPRINT
		    +"\nHARDWARE : "+Build.HARDWARE
		    +"\nHOST : "+Build.HOST
		    +"\nID : "+Build.ID
		    +"\nMANUFACTURER : "+Build.MANUFACTURER
		    +"\nMODEL : "+Build.MODEL
		    +"\nPRODUCT : "+Build.PRODUCT
		    +"\nSERIAL : "+Build.SERIAL
		    +"\nTAGS : "+Build.TAGS
		    +"\nTIME : "+Build.TIME
		    +"\nTYPE : "+Build.TYPE
		    +"\nUNKNOWN : "+Build.UNKNOWN
		    +"\nUSER : "+Build.USER;*/

    /*private String[] getAndroidHardWare(){
        String androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        String androidVersion = android.os.Build.VERSION.RELEASE;
        String androidBrand = android.os.Build.BRAND;
        String androidManufacturer = android.os.Build.MANUFACTURER;
        String androidModel = android.os.Build.MODEL;
        return new String[]{androidSDK, androidVersion, androidBrand, androidManufacturer, androidModel};
    }*/

    private static TelephonyManager telephonyManager = null;

    //Not gives multiple sim network carrier info
//    public static String getNetworkOperatorName(Context context) {
//        return getTelephonyManager(context).getNetworkOperatorName();
//    }

    //Not gives multiple sim network carrier info
    public static String getNetworkOperator(Context context) {
        return getTelephonyManager(context).getNetworkOperator();
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        if (telephonyManager == null) {
            return (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        } else {
            return telephonyManager;
        }
    }

    public static String getSimOperator(Context context) {
        return getTelephonyManager(context).getSimOperator();
    }

    public static String getSimOperatorName(Context context) {
        return getTelephonyManager(context).getSimOperatorName();
    }

    public static String getDeviceBrandName() {
        return Build.BRAND;
    }

    public static String getDeviceManufacturerName() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceModelId() {
        return Build.MODEL;
    }

    public static String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceSdkVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public static String getAppVersion(Context context) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(versionCode);
    }

    public static String getDeviceLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

    public static String getConnectionType(Context context) {
        if (NetworkUtils.isConnectedWifi(context)) {
            return "WiFi";
        } else {
            return "Mobile Data";
        }
    }

    public static List<SubscriptionInfo> getSubscriberInfo(Context context) {
        List<SubscriptionInfo> subscriptionInfoList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (subscriptionManager != null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    askNetworkCarrierPermission((Activity) context);
                    return null;
                }
                subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            }
            //region How to use returned subscriptionInfoList
            if (subscriptionInfoList != null && subscriptionInfoList.size() > 0) {
                for (SubscriptionInfo info : subscriptionInfoList) {
                    String carrierName = info.getCarrierName().toString();
                    String mobileNo = info.getNumber();
                    String countyIso = info.getCountryIso();
                    int dataRoaming = info.getDataRoaming();

                }

            }
            //endregion
        }

        return subscriptionInfoList;
    }

    public static String getNetworkCarrier(Activity activity) {
        // TODO: 10/1/18  sagar: Old method. Get network operator from mcc and mnc from cellIdentity.getMcc() and compare?!
        String networkCarrier = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Get information about all radio modules on device board
            // and check what you need by calling #getCellIdentity.
            final List<CellInfo> allCellInfo;

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                askNetworkCarrierPermission(activity);
                return null;
            }
            allCellInfo = getTelephonyManager(activity).getAllCellInfo();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoGsm) {
                    CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                } else if (cellInfo instanceof CellInfoWcdma) {
                    CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                } else if (cellInfo instanceof CellInfoLte) {
                    CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                } else if (cellInfo instanceof CellInfoCdma) {
                    CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
                }
            }
        }
        return networkCarrier;
    }

    private static void askNetworkCarrierPermission(final Activity activity) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(MarshMallowPermissionMessage.getPhoneStateDeniedMessage(activity))
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();

    }

    public static List<String> getNetworkOperatorName(final Activity context) {
        List<String> carrierNames = new ArrayList<>();
        try {
            final String permission = Manifest.permission.READ_PHONE_STATE;
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) && (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                final List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                for (int i = 0; i < subscriptionInfos.size(); i++) {
                    carrierNames.add(subscriptionInfos.get(i).getCarrierName().toString());
                }
            } else if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) && (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)) {
                askNetworkCarrierPermission(context);
            } else {

                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                // Get carrier name (Network Operator Name)
                if (telephonyManager != null) {
                    carrierNames.add(telephonyManager.getNetworkOperatorName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return carrierNames;
    }
}
