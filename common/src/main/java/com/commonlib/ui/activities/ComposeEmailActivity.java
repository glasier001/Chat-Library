package com.commonlib.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
//import androidx.databinding.DataBindingUtil;

import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.countrycodeinfo.CountryCodeInfoController;
//import com.commonlib.databinding.ActivityComposeEmailBinding;
import com.commonlib.utils.GetUserLocation;
import com.commonlib.utils.InfoUtils;
import com.commonlib.utils.MarshMallowPermissionMessage;
import com.commonlib.utils.StringUtils;
import com.commonlib.utils.Utils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.commonlib.utils.getfile.FileUtils.createApplicationFolder;

public class ComposeEmailActivity extends AppCompatActivity {

    // TODO: sagar : 25/2/19 5:31 PM Make this activity transparent and show progress loader if required

    private String deviceInfo;
    private File file;
//    private ActivityComposeEmailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_compose_email);
        invokePermissions();
    }

    private void invokePermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                mBinding.progressBar.setVisibility(View.GONE);
                deviceInfo = getDeviceInfo();
                file = createFile();
                writeDeviceInfo(file, deviceInfo);
                composeEmail(file);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
//                mBinding.progressBar.setVisibility(View.GONE);
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(MarshMallowPermissionMessage.getDefaultMessage(this))
                .setPermissions(Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void composeEmail(File file) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setData(Uri.parse("mailto:")); //Only email client should be able to handle this
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AppConstants.CONTACT_US_EMAIL});
        Uri uri = FileProvider.getUriForFile(this,
                getPackageName() + ".LocalStorageProvider",
                file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent targetedShareIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + "email_to"));
                targetedShareIntent.setType("vnd.android.cursor.dir/email");
//                targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject to be shared");
                targetedShareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AppConstants.CONTACT_US_EMAIL});
                targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), getString(R.string.chooser_email));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
            startActivity(chooserIntent);
            overridePendingTransition(R.anim.slide_up, R.anim.fade_away_zero);
            finish();
        }
    }

    private String getDeviceInfo() {
        GetUserLocation.getCurrentLocation(this, true);

        deviceInfo = getString(R.string.device) + " " + InfoUtils.getDeviceBrandName() + " " + InfoUtils.getDeviceModelId() + "\n" +
                getString(R.string.os_version) + " " + InfoUtils.getDeviceAndroidVersion() + "\n" +
                getString(R.string.app_version) + " " + InfoUtils.getAppVersion(this) + "\n" +
                getString(R.string.location) + " " + getCountryIsoCode() + " (" + GetUserLocation.getCurrentLocation(this, false).latitude
                + ", " + GetUserLocation.getCurrentLocation(this, false).longitude + ")" + "\n" +
                getString(R.string.language) + " " + InfoUtils.getDeviceLanguage() + "\n" +
                getString(R.string.user_id) + " " + Utils.getUserId(this) + "\n" +
                getString(R.string.carrier) + " " + StringUtils.getSlashSeparatedString(InfoUtils.getNetworkOperatorName(this)) + "\n" +
                getString(R.string.connection) + " " + InfoUtils.getConnectionType(this);

        return deviceInfo;
    }

    private String getCountryIsoCode() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(GetUserLocation.getCurrentLocation(this, false).latitude,
                    GetUserLocation.getCurrentLocation(this, false).longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getCountryCode();
        } else {
            CountryCodeInfoController codeInfoController = new CountryCodeInfoController(this);
            return codeInfoController.getUserCountryCode().getIsoCode();
        }
    }

    private void writeDeviceInfo(File file, String deviceInfo) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(deviceInfo);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFile() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            createApplicationFolder();
            file = new File(getAppDirectory(), AppConstants.DEVICE_INFO_TXT_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
        } else {
            invokeStoragePermission();
        }
        return file;
    }

    private void invokeStoragePermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                file = createFile();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(MarshMallowPermissionMessage.getDefaultMessage(this))
                .setPermissions(Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private File getAppDirectory() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + AppConstants.APPLICATION_DIR_NAME);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + AppConstants.APPLICATION_DIR_NAME + AppConstants.DEVICE_DIR);
        f.mkdirs();
        return f;
    }

}
