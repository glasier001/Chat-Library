package com.commonlib.ui.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import com.commonlib.BuildConfig;
import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.ui.MyApplication;
import com.commonlib.constants.IntentKeys;
import com.commonlib.utils.LogShowHide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

//import Helper.LogShowHide;

/**
 * Created by root on 16/6/16.
 */
public class TakeImgCameraScreen extends Activity {

    /* Media select variable */
    File imageFilePath;
    int pick_img_1_camera_code = 1001;

    public int outputx;
    public int outputy;
    public String imgPath;


    public boolean criculeCrop = false;


    public Activity activity;
    private Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.activity_close_scale);

        activity = TakeImgCameraScreen.this;

        if (getIntent().hasExtra("outputx")) {
            outputx = getIntent().getIntExtra("outputx", 200);
        }
        if (getIntent().hasExtra("outputy")) {
            outputy = getIntent().getIntExtra("outputy", 150);
        }

        if (getIntent().hasExtra(IntentKeys.IK_IS_CIRCULAR)) {
            criculeCrop = getIntent().getBooleanExtra(IntentKeys.IK_IS_CIRCULAR, false);
        }

        inicontrol();
    }

    //region startActivityForResult: Intent to open camera and to save clicked image
    private void inicontrol() {
        //TODO 27/9/17 => sagar =>Note :-if creates problem in nougat, change action flag and grant read uri permission
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*https://developer.android.com/reference/android/support/v4/content/FileProvider#Permissions*/
        grantUriPermission(getPackageName(), setImageUri(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, pick_img_1_camera_code);
        } catch (ActivityNotFoundException e) {
            LogShowHide.LogShowHideMethod(activity, "Edit profile: cannot take picture" + e);
        }
    }
    //endregion

    //region Sagar: Content scope (file provider) has been used instead of file (uriFromFile) scope to support up to Nougat and Onwards
    public Uri setImageUri() {
        imgUri = null;
        try {
            imgUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".provider",
                    createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imgPath = imageFilePath.getAbsolutePath();
        return imgUri;
    }
    //endregion.

    //region Directory to save the clicked image
    private File createImageFile() throws IOException {
        imageFilePath = new File(
                Environment.getExternalStorageDirectory()
                        + File.separator
                        + AppConstants.APPLICATION_DIR_NAME
                        + AppConstants.IMAGES_DIR,
                "Images" + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".jpg"
        );
        return imageFilePath;
    }
    //endregion

    //region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogShowHide.LogShowHideMethod(activity, "result code: " + requestCode + "");
        if (resultCode == RESULT_OK) {
            if (requestCode == pick_img_1_camera_code) {
                String path = imgPath;
                if (path == null) {
                    imageFilePath = null;
                    return;
                }
                imageFilePath = new File(path);
                // FIXME: sagar : 30/7/18 Use new library for cropping
                startSmoothCropping();
//                startCropImage();
            } else if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                Intent intent = new Intent();
                intent.putExtra(IntentKeys.IK_RESULT, result);
                intent.putExtra(IntentKeys.IK_URI, resultUri);
                setResult(RESULT_OK, intent);
                finish();
            }

        } else {
            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.slide_down);
        }
    }
    //endregion

    /**
     * can't set forward result because can't set {@link com.theartofdev.edmodo.cropper.CropImage.ActivityBuilder#mOptions}
     */
    private void startSmoothCropping() {
        com.theartofdev.edmodo.cropper.CropImage.activity(imgUri)
                .setCropShape(criculeCrop ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(criculeCrop ? 1 : outputx, criculeCrop ? 1 : outputy)
                .setAutoZoomEnabled(true)
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAllowCounterRotation(false)
                .start(this);
    }

    public static boolean DeleteImgRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteImgRecursive(child);

        return fileOrDirectory.delete();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed(activity);
    }

}


