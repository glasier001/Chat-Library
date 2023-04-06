package com.commonlib.ui.baseui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.commonlib.R;
import com.commonlib.utils.KeyboardUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.util.Log.d;
import static com.commonlib.constants.AppConstants.TAG;
import static com.commonlib.utils.LogShowHide.LogShowHideMethod;
import static com.commonlib.utils.ScreenUtils.getScreenWidth;


/**
 * Created by sagar on 2/11/17.
 */

// sagar : 31/1/19 10:30 AM Customization:
// https://stackoverflow.com/questions/18536439/dialogfragment-fullscreen-shows-padding-on-sides/18554868#18554868
// https://stackoverflow.com/questions/38160458/dialogfragment-is-not-completely-on-the-right-side-with-gravity-right
// sagar : 31/1/19 10:43 AM Right way to add map fragment in another fragment (Nested Fragment) (Check pasupal)
// https://developer.android.com/about/versions/android-4.2#java
// https://code.i-harness.com/en/q/d6e76e


public abstract class BaseDialogFragment extends DialogFragment {
    private Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayout(), container);
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        setCancelable(true);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        if (window != null) {

            // sagar : 2/2/19 7:59 PM Depending upon configuration and style, android framework may add default space and background surrounding to dialog
            /*window.getLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);*/
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getScreenWidth(getActivity()) * 0.85));
            window.setWindowAnimations(R.style.Animations_SlideWindow);

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialogNoActionBar);

            // sagar : 31/1/19 10:01 AM No Title, No ActionBar == Frame but No Title No ActionBar
//            window.requestFeature(Window.FEATURE_NO_TITLE);
//            window.requestFeature(Window.FEATURE_ACTION_BAR);

            // sagar : 31/1/19 10:00 AM No Frame == No Title
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog);

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            /*window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);*/
            /*https://stackoverflow.com/questions/42877015/dialogfragment-gravity-not-showing-dialog-completely-in-corner*/
            WindowManager.LayoutParams wmlp = window.getAttributes();
//            wmlp.gravity = Gravity.BOTTOM; //You can change the gravity in setWindow(Window window) method

            /*https://stackoverflow.com/questions/29741588/dialogfragment-positioning*/
            wmlp.gravity = Gravity.BOTTOM;

            // sagar : 2/2/19 8:15 PM Strictly saying, window will not resize in below method. It is just for window configuratinos. Use resizeDialog to resize window.
            setWindow(window);
        }
        mUnBinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // sagar : 7/2/19 7:46 PM set your view data values after onResume but-
        // sagar : 7/2/19 3:37 PM Execute only those methods here that you want to re-execute every time fragment gets resumed like onActivityResult
//        makeText(getActivity(), "onResume", LENGTH_SHORT).show();
        d(TAG, "BaseDialogFragment: onResume: ");
    }

    /**
     * Sets layout to be used for DialogFragment
     * <p> Just return R.id.layout_resource
     * </p>
     */
    public abstract int getLayout();

    /**
     * Sets and decors custom window property for DialogFragment
     * <p>
     * <p> This method is executed within onCreateView but before completion of onCreateView.
     * So, just define and set your desired DialogFragment window property here.
     * However, it can be used to decorate and not resize the window property of the DialogFragment.
     * Because window of the DialogFragment is just going to be built but has not yet built.
     * So, we can set window features but we cannot resize it before even it gets drawn.
     * If you want to resize it, define it in resizeDialog() method that will be executed
     * within onResume() but before completion of onResume().
     * </p>
     *
     * @param window The window of the dialog
     */
    public abstract void setWindow(Window window);


    /**
     * sagar : 22/5/19 10:13 AM
     * <p>
     * Will be called before {@link #onDetach()}
     * <p>
     * <p>
     * since 1.0
     */
    @Override
    public void dismiss() {

        LogShowHideMethod(getContext(), "BaseDialogFragment: dismiss: ");

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (getActivity() != null) {
            KeyboardUtils.hideSoftInput(getActivity());
        }
        super.dismiss();
    }

    @Override
    public void onDetach() {

        LogShowHideMethod(getContext(), "BaseDialogFragment: onDetach: ");

        if (getActivity() != null) {
            KeyboardUtils.hideSoftInput(getActivity());
        }
        super.onDetach();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        LogShowHideMethod(getContext(), "BaseDialogFragment: onCancel: ");

        if (getActivity() != null) {
            KeyboardUtils.hideSoftInput(getActivity());
        }
        super.onCancel(dialog);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        d(TAG, "BaseDialogFragment: onActivityCreated: ");
        resizeDialog();
        handleViews();
        initControllers();
        setListeners();
        otherStuffs();
        callApi();
    }

    @Override
    public void onStart() {
        super.onStart();
//        makeText(getActivity(), "onStart", LENGTH_SHORT).show();
        d(TAG, "BaseDialogFragment: onStart: ");
        // sagar : 7/2/19 3:37 PM Execute only those methods here that you want to re-execute every time fragment gets resumed like onActivityResult
    }

    /**
     * Resize the DialogFragment exclusively here.
     * <p>
     * <p> It will be executed within onActivityCreated().
     * </p>
     */
    protected abstract void resizeDialog();

    /**
     * Sets run time changes in views of DialogFragment
     * <p>
     * <p> This method will be executed before DialogFragment becomes visible to the user.
     * So, you can define and set run time view changes here.
     * </p>
     */
    public abstract void handleViews();

    /**
     * Define any controllers to be used here
     */
    public abstract void initControllers();

    /**
     * Define Click listener for any view of the DialogFragment here
     */
    public abstract void setListeners();

    /**
     * Define all other stuff you may want to get started here.
     * <p>
     * <p> Note that it will be executed after callApi() method!
     * </p>
     */
    public abstract void otherStuffs();

    /**
     * Define api call/s here
     */
    public abstract void callApi();
}
