package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.commonlib.R;
import com.commonlib.constants.AppConstants;


/**
 * Created by mauliksantoki on 10/7/17.
 */

public final class MarshMallowPermissionMessage {

    private MarshMallowPermissionMessage() {
    }

    public static String getCameraPermissionMessage(Activity activity) {
        if (!checkWriteExternalPermission(activity, android.Manifest.permission.CAMERA)) {
            return activity.getResources().getString(R.string.msg_no_permission_photos_and_videos).replace("#", AppConstants.APP_TITLE);
        }
        return activity.getResources().getString(R.string.msg_storage).replace("#", AppConstants.APP_TITLE);
    }

    private static boolean checkWriteExternalPermission(Activity activity, String permission) {
        return (activity.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static String getLocationDeniedMessage(Context context) {
        return context.getResources().getString(R.string.msg_no_permission_location).replace("#", AppConstants.APP_TITLE);
    }

    public static String getCallDeniedMessage(Context context) {
        return context.getResources().getString(R.string.msg_no_permission_call_phone).replace("#", AppConstants.APP_TITLE);
    }

    public static String getImageReadWriteMessage(Context context){
        return context.getResources().getString(R.string.msg_no_permission_image_read_write);
    }

    public static String getPhoneStateDeniedMessage(Activity activity) {
        return activity.getResources().getString(R.string.permission_denied_msg);
    }

    public static String getDefaultMessage(Context context){
        return context.getString(R.string.permission_denied_msg);
    }
}
