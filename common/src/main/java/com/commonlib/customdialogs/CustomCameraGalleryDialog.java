package com.commonlib.customdialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.constants.IntentKeys;
import com.commonlib.customviews.CustomTextView;
import com.commonlib.ui.activities.TakeImgCameraScreen;
import com.commonlib.ui.activities.TakeImgGalleryScreen;
import com.commonlib.utils.Utils;
import com.commonlib.utils.getfile.FileUtils;

import java.util.List;

import static com.commonlib.utils.MarshMallowPermissionMessage.getCameraPermissionMessage;


public class CustomCameraGalleryDialog extends Dialog implements View.OnClickListener {

    public static String imageFilePath = "IMAGEFILEPATH";
    public Activity activity;
    public int requestCode;
    private CustomTextView ctvOptionTwo;
    private CustomTextView ctvOptionOne;
    private int outputx;
    private int outputy;
    private String firstOption;
    private String secondOption;
    private boolean isCircularCrop;

    public CustomCameraGalleryDialog(Activity a, int code, int cropOutputx,
                                     int cropOutputy, boolean isCirculeCrop) {
        super(a);
        this.activity = a;
        this.requestCode = code;
        this.outputx = cropOutputx;
        this.outputy = cropOutputy;
        isCircularCrop = isCirculeCrop;

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                FileUtils.createApplicationFolder();
                show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }

        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getCameraPermissionMessage(activity))
                .setPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_camera_gallery);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Animations_SlideWindow);
        inicontrol();
    }

    private void inicontrol() {
        ctvOptionTwo = (CustomTextView) findViewById(R.id.ctv_option_two);
        ctvOptionOne = (CustomTextView) findViewById(R.id.ctv_option_one);
        CustomTextView ctvOptionThree = (CustomTextView) findViewById(R.id.ctv_option_three);
        CardView layoutCardCancel = findViewById(R.id.layout_tv_dialog_cg_cancel);
        LinearLayout llPrimaryOptions = (LinearLayout) findViewById(R.id.ll_dialog_cg);
        View line2 = findViewById(R.id.devider_view2);
        int radius = AppConstants.DIALOG_BORDER_RADIUS;
        llPrimaryOptions.setBackground(Utils.setDrawableBackground(ContextCompat.getColor(activity, R.color.ThemeWhite), 0, 0, radius, radius, radius, radius));
        ctvOptionTwo.setOnClickListener(this);
        ctvOptionOne.setOnClickListener(this);
        layoutCardCancel.setOnClickListener(this);

        if (firstOption != null && secondOption != null) {
            setOptions(firstOption, secondOption);
        }
    }

    //This has been set to public to change text labels dynamically
    public CustomCameraGalleryDialog setOptions(String firstOption, String secondOption) {
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        if (ctvOptionOne != null && ctvOptionTwo != null) {
            ctvOptionOne.setText(firstOption);
            ctvOptionTwo.setText(secondOption);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ctv_option_two) {
            startCameraToClickPicture();
        } else if (i == R.id.ctv_option_one) {
            startGalleryToSelectPicture();
        } else if (i == R.id.layout_tv_dialog_cg_cancel) {
            dismiss();
        }
    }

    private void startCameraToClickPicture() {
        Intent cameraIntent = new Intent(activity, TakeImgCameraScreen.class);
        cameraIntent.putExtra("outputx", outputx);
        cameraIntent.putExtra("outputy", outputy);
        cameraIntent.putExtra(IntentKeys.IK_IS_CIRCULAR, this.isCircularCrop);
        if (resolveActivity(cameraIntent)) {
            activity.startActivityForResult(cameraIntent, requestCode);
            activity.overridePendingTransition(R.anim.slide_up, 0);
            dismiss();
        }
    }

    private void startGalleryToSelectPicture() {
        Intent galleryIntent = new Intent(activity, TakeImgGalleryScreen.class);
        galleryIntent.putExtra("outputx", outputx);
        galleryIntent.putExtra("outputy", outputy);
        galleryIntent.putExtra(IntentKeys.IK_IS_CIRCULAR, isCircularCrop);
        if (resolveActivity(galleryIntent)) {
            activity.startActivityForResult(galleryIntent, requestCode);
            activity.overridePendingTransition(0, 0);
            dismiss();
        }
    }

    /**
     * Checks if there is any app or activity that can perform action set in Intent
     */
    private boolean resolveActivity(Intent intent) {
        return intent.resolveActivity(activity.getPackageManager()) != null;
    }
}