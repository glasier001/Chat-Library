package com.commonlib.ui.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.constants.IntentKeys;
import com.commonlib.ui.MyApplication;
import com.commonlib.utils.CustomToastMessage;
import com.commonlib.utils.getfile.FileUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;


//import Helper.LogShowHide;

/**
 * Created by root on 16/6/16.
 */
public class TakeImgGalleryScreen extends Activity {
    /*image get variablers start*/
    int pick_img_1_gallery_code = 1002;
    File imageFilePath;

    public Activity activity;
    public int outputx;
    public int outputy;
    public boolean criculeCrop = false;

    private Uri imgUri;

    /*image get variablers end*/
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.activity_close_scale);
        String state = Environment.getExternalStorageState();

        activity = TakeImgGalleryScreen.this;

        if (getIntent().hasExtra("outputx")) {
            outputx = getIntent().getIntExtra("outputx", 100);
        }
        if (getIntent().hasExtra("outputy")) {
            outputy = getIntent().getIntExtra("outputy", 150);
        }
        if (getIntent().hasExtra(IntentKeys.IK_IS_CIRCULAR)) {
            criculeCrop = getIntent().getBooleanExtra(IntentKeys.IK_IS_CIRCULAR, false);
        }
        initcontrole();
    }

    private void initcontrole() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), pick_img_1_gallery_code);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    String fielx;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == pick_img_1_gallery_code) {
                try {

                    fielx = FileUtils.getExtension(FileUtils.getPath(activity, data.getData()));
                    imageFilePath = new File(
                            Environment.getExternalStorageDirectory()
                                    + File.separator
                                    + AppConstants.APPLICATION_DIR_NAME
                                    + AppConstants.IMAGES_DIR,
                            "Images" + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ((fielx != null && !fielx.equalsIgnoreCase("")) ? fielx : ".png"));
                    imgUri = FileProvider.getUriForFile(this,
                            getPackageName() + ".provider",
                            imageFilePath);

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFilePath);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    // FIXME: sagar : 30/7/18 Replace the code with new library
                    startSmoothCropping();
//                    startCropImage();
                } catch (Exception e) {
                    e.printStackTrace();
                    CustomToastMessage.animRedTextMethod(activity, getString(R.string.error_msg_something_went_wrong_with_image));
                    finish();
                    overridePendingTransition(R.anim.activity_open_scale, R.anim.slide_down);
                }
            }  else if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                com.theartofdev.edmodo.cropper.CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
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

    private void startFlexibleCropping() {
        com.theartofdev.edmodo.cropper.CropImage.activity(imgUri)
                .setCropShape(criculeCrop ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE)
                .setAutoZoomEnabled(true)
                .start(this);
    }

    /**
     * can't set forward result because can't set {@link com.theartofdev.edmodo.cropper.CropImage.ActivityBuilder#mOptions}
     */
    private void startSmoothCropping() {
        com.theartofdev.edmodo.cropper.CropImage.activity(imgUri)
                .setCropShape(criculeCrop ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE)
//                .setAspectRatio(criculeCrop ? 1 : outputx, criculeCrop ? 1 : outputy)
                .setAutoZoomEnabled(true)
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAllowCounterRotation(false)
                .start(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}