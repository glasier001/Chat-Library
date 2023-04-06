package com.commonlib.ui.baseui;


import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.commonlib.R;
import com.commonlib.customviews.CustomBtn;
import com.commonlib.customviews.CustomTextView;
import com.commonlib.utils.KeyboardUtils;
import com.commonlib.utils.NetworkUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity implements BaseActivityView {

    private Toolbar toolbar;

    private CustomTextView ctv_title;
    private CustomBtn btnRight;
    private CustomBtn btnLeft;
    /**
     * This variable is used to hold bindview (bind and unbind view) so that when activity is onDestroy that time it unbinds all views.
     *
     * @version V1.0
     */
    private Unbinder mBinder;
    private boolean isToolbar = false;
    private Drawable homeAsUpIndicator;

    public CustomTextView getCtv_title() {
        return ctv_title;
    }

    public void setCtv_title(CustomTextView ctv_title) {
        this.ctv_title = ctv_title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setStatusBarGradiant();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayout());
        mBinder = ButterKnife.bind(this);
        iniControl();
        isToolbar = setToolbarVisible();
        if (isToolbar) {
            toolbar = findViewById(R.id.toolbar);
            //ctv_title = toolbar.findViewById(R.id.ctv_toolbar_title);
            setUpToolbar();
        }
        setCustomTitleBar();
        setListeners();
        //setting up handleViews after setting up listener so that views with value change listener can work and notify changes properly
        handleViews();
        otherStuffs();
    }

    //Sets gradient status bar only if no soft navigation bar
    public void setStatusBarGradiant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.ThemeColor));
            }
        }
    }



    public abstract int getLayout();

    public abstract boolean setToolbarVisible();

    public abstract void iniControl();

    private void setUpToolbar() {
        if (isToolbar) {
            setTitle(""); //Deletes application title
//            setCustomBackArrow(R.drawable.ic_back_arrow);
            setSupportActionBar(toolbar); //Sets toolbar as actionbar
            if (getSupportActionBar() != null) {
                ctv_title.setText(this.setTitle());
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Displays splace_screen_back arrow
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicator);

                if (setRightText() != null && setRightText().trim().length() > 0) {
//                    btnRight = findViewById(R.id.btn_right);
                    btnRight.setVisibility(View.VISIBLE);
                    btnRight.setText(setRightText());
                }

                if (setLeftText() != null && setLeftText().trim().length() > 0) {
//                    btnLeft = findViewById(R.id.btn_left);
                    btnLeft.setVisibility(View.VISIBLE);
                    btnLeft.setText(setLeftText());
                }
            }
        }
    }

    public abstract void setCustomTitleBar();

    public abstract void handleViews();

    public abstract void setListeners();

    public abstract void otherStuffs();

    public void setCustomBackArrow(int id) {
        if (isToolbar) {
            homeAsUpIndicator = ContextCompat.getDrawable(this, id);
//            homeAsUpIndicator.setColorFilter(ContextCompat.getColor(this, R.color.appThemeColor), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public abstract String setTitle();

    public abstract String setRightText();

    public abstract String setLeftText();

    @Override
    protected void onDestroy() {
        if (mBinder != null) {
            mBinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public abstract void showToastMessage(String message, boolean isError);

    public abstract void showErrorMessage(String message);

    public abstract void showSuccessMessage(String message);

    public abstract void showErrorMessage(int stringResId);

    public abstract void showSuccessMessage(int stringResId);

    public void hideSoftInput() {
        View view = this.getCurrentFocus();
        if (view != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                InputMethodManager inputManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkAvailable(this);
    }

    @Override
    public void hideKeyboard() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public int getResColor(int id) {
        return ContextCompat.getColor(this, id);
    }

    @Override
    public Drawable getResDrawable(int id) {
        return ContextCompat.getDrawable(this, id);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public CustomBtn getBtnRight() {
        if (btnRight == null) {
            Toast.makeText(this, "Set your right btn splace_screen_text here method setRightText()", Toast.LENGTH_SHORT).show();
        }
        return btnRight;
    }


}

