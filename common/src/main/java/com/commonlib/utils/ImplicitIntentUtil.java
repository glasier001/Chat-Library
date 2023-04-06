package com.commonlib.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.commonlib.R;
import com.commonlib.listeners.Callbacks;
import com.commonlib.utils.getfile.FileUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


/**
 * This class is use to perform implicit intent actions.
 *
 * @author Sagar
 * @version V1.0
 * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc" >Read more about javadoc</a>
 */


public final class ImplicitIntentUtil implements Callbacks.OnPermissionListener {

    private Activity activityRedirectToCall;
    private String phoneNumberRedirectToCall;

    private ImplicitIntentUtil() {
    }

    /**
     * This method is use to redirect to google map.
     *
     * @param activity
     * @param lat      lat location
     * @param lng      lng location
     * @version V1.0
     */
    public static void redirectToMap(Activity activity, Double lat, Double lng) {
        try {
            boolean isGoogleMapInstalled = appInstalledOrNot(activity, "com.google.android.apps.maps");
            if (isGoogleMapInstalled) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "," + lng));
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.error_google_map_not_installed), Toast.LENGTH_SHORT).show();
                try {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
                }
            }
        } catch (ActivityNotFoundException ex) {
            CustomToastMessage.animRedTextMethod(activity, activity.getResources().getString(R.string.error_google_map_failed_please_try_again_later));
        }
    }

    /**
     * This method is check application us install or not.
     *
     * @param context application context
     * @param uri     redirect url.
     * @version V1.0
     */
    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * This method is use to redirect to google map with direction on map
     *
     * @param activity
     * @param lat      lat location
     * @param lng      lng location
     * @version V1.0
     */
    public static void directionToMap(Activity activity, Double lat, Double lng) {
        try {
            boolean isGoogleMapInstalled = appInstalledOrNot(activity, "com.google.android.apps.maps");
            if (isGoogleMapInstalled) {
                LogShowHide.LogShowHideMethod(activity, "http://maps.google.com/maps?saddr=&daddr=" + lat + "," + lng + "&directionsmode=driving");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + lat + "," + lng + "&directionsmode=driving"));
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.error_google_map_not_installed), Toast.LENGTH_SHORT).show();
                try {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
                }
            }
        } catch (ActivityNotFoundException ex) {
            CustomToastMessage.animRedTextMethod(activity, activity.getResources().getString(R.string.error_google_map_failed_please_try_again_later));
        }
    }

    /**
     * This method is use to redirect to playstore
     *
     * @param context application context
     * @version V1.0
     */
    public static void redirectToPlayStore(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with play store back stack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    /**
     * This method exclusively fires implicit intent for API 19 onwards to select file having given mimeType/s
     * <p>
     * <p> Additional Description
     * </p>
     *
     * @param activity    Current activity
     * @param mimeTypes   Given mimeTypes as String[] to set as intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
     * @param requestCode An int requestCode depending on which we can perform certain operation in onActivityResult
     */
    public static void accessStorageApi19(Activity activity, String[] mimeTypes, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * This method exclusively fires implicit intent for API below 19 to select file having given mimeType/s
     * <p>
     * <p> Multiple mime can be set as for e.g. String mimes = "application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/msword";
     * </p>
     *
     * @param activity    Current activity
     * @param mimes       Given mimeTypes as String to set as intent.setType(mimes);
     * @param requestCode An int requestCode depending on which we can perform certain operation in onActivityResult
     */
    public static void accessStorageBelowApi19(Activity activity, String mimes, int requestCode) {
        // 1. on Upload click, call ACTION_GET_CONTENT intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 2. pick pdf, doc, docx only
//        intent.setType(mimes);
        intent.setType("*/*");
//            intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 3. start activity
        activity.startActivityForResult(intent, requestCode);
        // define onActivityResult to do something with picked image
    }

    public static void share(Context context, String contents) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, contents);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.label_share)));
    }

    /**
     * Create a chooser intent to select the source to get image from.<br>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br>
     * All possible sources are added to the intent chooser.
     *
     * @param context          used to access Android APIs, like content resolve, it is your
     *                         activity/fragment/widget.
     * @param title            the title to use for the chooser UI
     * @param includeDocuments if to include KitKat documents activity containing all sources
     * @param includeCamera    if to include camera intents
     */
    public static Intent getImageIntent(
            @NonNull Context context,
            CharSequence title,
            boolean includeDocuments,
            boolean includeCamera,
            boolean isMultipleSelection
    ) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents if Camera permission is available and includeCamera is true
        if (includeCamera) {
            allIntents.addAll(getCameraIntents(context, packageManager, true, false));
        }

        List<Intent> galleryIntents =
                getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments, true);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments, true);
        }
        allIntents.addAll(galleryIntents);

        // sagar : 5/4/19 12:13 PM Why removing last one?
        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get all Camera intents for capturing image using device camera apps.
     * Sets Intent for camera action and Creates file that will store image captured by camera
     * <p>
     * By setting up explicit uri and EXTRA_OUTPUT, return intent will be null but then we
     * will have pre-defined path to use full size photo
     * However, if we want to use only thumbnail, then do not set uri and EXTRA_OUTPUT on intent.
     * Instead,
     * <p>
     *
     * @see <a href="https://developer.android.com/training/camera/photobasics.html#TaskPhotoView">https://developer.android.com/training/camera/photobasics.html#TaskPhotoView</a>
     * @since 1.0
     */
    public static List<Intent> getCameraIntents(
            @NonNull Context context, @NonNull PackageManager packageManager, boolean isImage, boolean isVideo) {

        List<Intent> allIntents = new ArrayList<>();

        // Determine Uri of camera image to  save.
        // sagar : 5/4/19 11:34 AM We give the file path to save the captured image and then we get the uri from content provider to get that uri from saved image file path. For that, as a pre-requisite, preparation we first create directory where we want to save the image.
        // TODO: sagar : 5/4/19 1:48 PM if-else for image or video
        Uri outputFileUri = null;
        if (isImage) {
            outputFileUri = FileUtils.getUri(context, FileUtils.getImageFile());
        } else {
            outputFileUri = FileUtils.getUri(context, FileUtils.getVideoFile());
        }

        Intent captureIntent = null;


        if (isImage) {
            captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        } else {
            /*https://developer.android.com/training/camera/videobasics*/
            captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        return allIntents;
    }

    /**
     * Get all Gallery intents for getting image from one of the apps of the device that handle
     * images.
     */
    public static List<Intent> getGalleryIntents(
            @NonNull PackageManager packageManager, String action, boolean includeDocuments, boolean isMultipleSelection) {

        List<Intent> intents = new ArrayList<>();
        Intent galleryIntent =
                action == Intent.ACTION_GET_CONTENT
                        ? new Intent(action)
                        : new Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        if (isMultipleSelection) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }

        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intents.add(intent);
        }

        // remove documents intent
        if (!includeDocuments) {
            for (Intent intent : intents) {
                if (intent
                        .getComponent()
                        .getClassName()
                        .equals("com.android.documentsui.DocumentsActivity")) {
                    intents.remove(intent);
                    break;
                }
            }
        }
        return intents;
    }

    /**
     * Checks if there is any app or activity that can perform action set in Intent
     */
    public static boolean resolveActivity(Context context, Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    public static Intent getVideoIntent(Context context, String chooserTitle, boolean includeCamera) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents if Camera permission is available and includeCamera is true
        if (includeCamera) {
            allIntents.addAll(getCameraIntents(context, packageManager, false, true));
        }

        /*https://stackoverflow.com/questions/4922037/android-let-user-pick-image-or-video-from-gallery/27282932*/

        /*https://developer.android.com/training/camera/videobasics*/

        /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);*/
        /*List<Intent> videoIntentList = getVideoIntentList(packageManager, MediaStore.ACTION_VIDEO_CAPTURE);*/
        List<Intent> videoIntentList = getVideoIntentList(packageManager, Intent.ACTION_GET_CONTENT);

        if (Utils.isNullOrEmpty(videoIntentList)) {
            videoIntentList = getVideoIntentList(packageManager, Intent.ACTION_PICK);
        }

        allIntents.addAll(videoIntentList);

        // sagar : 5/4/19 12:13 PM Why removing last one?
        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, chooserTitle);

        // Add all other intents
        chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;

    }

    public static List<Intent> getVideoIntentList(PackageManager packageManager, String action) {


        List<Intent> intents = new ArrayList<>();
        Intent galleryIntent =
                action == Intent.ACTION_GET_CONTENT
                        ? new Intent(action)
                        : new Intent(action, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("video/*");

        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intents.add(intent);
        }

        return intents;

    }

    /**
     * this method is use to call.
     *
     * @param activity    pass current page activity.
     * @param phoneNumber call phone number
     * @return true if the move is valid, otherwise false
     * @version V1.0
     */
    public void redirectToCall(final Activity activity, final String phoneNumber) {


        activityRedirectToCall = activity;
        phoneNumberRedirectToCall = phoneNumber;

//        PermissionUtils permissionUtils=new PermissionUtils(activity,null,this);

//        permissionUtils.isAllPermissionsGranted(activity,new String[]{Manifest.permission.CAMERA},activity.getString(R.string.msg_no_permission_call_phone));

    }

    @Override
    public void OnPermissionGranted() {
        if (isSimSupport(activityRedirectToCall)) {
            onCall(activityRedirectToCall, phoneNumberRedirectToCall);
        } else {
            CustomToastMessage.animRedTextMethod(activityRedirectToCall, activityRedirectToCall.getResources().getString(R.string.error_mobile_network_not_available));
        }
    }

    /**
     * check sim card available
     *
     * @param activity pass activity.
     * @return retutn true if the sim card inserted, otherwise false
     * @version V1.0
     */
    public boolean isSimSupport(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }

    /**
     * this method is use to call.
     *
     * @param activity    pass current page activity.
     * @param phoneNumber call phone number
     * @return true if the move is valid, otherwise false
     * @version V1.0
     */
    private void onCall(final Activity activity, String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            //callIntent.setPackage("com.android.dialer");
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // sagar : 3/4/19 3:39 PM According to google guideline, we will open dial pad only and not perform calling
            /*if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissionshere to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                return;
            }*/

            activity.startActivity(callIntent);
            //dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnPermissionDenied() {

    }

}

