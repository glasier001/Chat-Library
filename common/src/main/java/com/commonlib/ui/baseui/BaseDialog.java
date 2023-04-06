package com.commonlib.ui.baseui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;

import com.commonlib.R;
import com.commonlib.utils.ScreenUtils;

import static android.util.Log.d;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.commonlib.constants.AppConstants.TAG;


/**
 * Created by sagar on 2/11/17.
 * This dialog is like how our custom gallery image dialog is. iOS style pop-up dialog.
 * Check custom bottom sheet dialog - by overriding bottom_sheet_width, we can get the same effect from bottom sheet
 * https://www.hidroh.com/2016/06/17/bottom-sheet-everything/
 * Prevent background activity to get touch event from dialog
 * https://stackoverflow.com/questions/23126701/prevent-dialog-activity-from-interacting-with-background-activity-when-clickin
 */

public abstract class BaseDialog extends Dialog {

    private boolean cancelOnOutsideTouch;
    private Context context;

    public BaseDialog(Context context, boolean cancelOnOutsideTouch) {
        super(context);
        this.context = context;
        this.cancelOnOutsideTouch = cancelOnOutsideTouch;
        setCanceledOnTouchOutside(cancelOnOutsideTouch);
    }

    public BaseDialog(Context context, int themeResId, boolean cancelOnOutsideTouch) {
        super(context, themeResId);
        this.context = context;
        this.cancelOnOutsideTouch = cancelOnOutsideTouch;
        setCanceledOnTouchOutside(cancelOnOutsideTouch);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        /*if (getOwnerActivity() != null) {
            KeyboardUtils.hideSoftInput(getOwnerActivity());
        }*/
//        KeyboardUtils.closeKeyboard(context);
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // sagar : 21/1/19 12:16 PM Default window customization
        requestWindowFeature(FEATURE_NO_TITLE);

        //region Another way to use data binding in base dialog but this way will not take window theme
        // sagar : 21/1/19 1:01 PM In case of any theme problem, use dialog.getContext() or dialog.getLayoutInflater()
        // https://stackoverflow.com/questions/34967868/how-to-use-data-binding-in-dialog
        /*View view = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayout(), null, false).getRoot();

        setContentView(view);

        // sagar : 21/1/19 12:50 PM To check if this view has already any binding attached to it
        setDataBinding(view);*/
        //endregion

        // FIXME: sagar : 5/2/19 All window ops ideally should be happened before setting up the contentView

        //region Another way to use data binding for base dialog
        setContentView(getLayout());

        // sagar : 21/1/19 12:50 PM To check if this view has already any binding attached to it
        // https://github.com/sagarpatel288/InventoryApp/blob/master/app/src/main/java/com/example/android/inventoryapp/adapters/IproductCursorAdapter.java

        // sagar : 21/1/19 1:01 PM In case of any theme problem, use dialog.getContext() or dialog.getLayoutInflater()
        // https://stackoverflow.com/questions/34967868/how-to-use-data-binding-in-dialog

        // sagar : 4/2/19 3:54 PM Commenting/Removing out this line causes address dialog title to not taking value
//        setDataBinding(DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayout(), null, false).getRoot());
        //endregion


        //region Use butterKnife primary as data binding in dialog but then it will not take theme
//        View view = View.inflate(getContext(), getLayout(), null);
//        setContentView(view);
//        ButterKnife.bind(this, view);
//        setDataBinding(DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayout(), null, false).getRoot());
        //endregion

        Window window = getWindow();

        if (window != null) {
//            window.setGravity(Gravity.BOTTOM);

            // sagar : 4/2/19 3:56 PM This is how we set wrap_content or match_parent attribute normally
//            window.getLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // region sagar : 28/1/19 11:52 AM Setting up below percentage, can remove translucent theme
            /*new WindowManager.LayoutParams(); will remove translucent theme*/
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int) (ScreenUtils.getScreenWidth(context) * 0.97);
            lp.height = (int) (ScreenUtils.getScreenHeight(context) * 0.85);
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            //endregion

            // sagar : 4/2/19 1:09 PM To dismiss the dialog on click of out side, but it is not working!
//            setCancelOnOutsideTouch(window);


            // sagar : 28/1/19 11:52 AM Setting up below percentage is working

            // sagar : 4/2/19 3:57 PM Window is being resized this way (int percentage)
//            window.getLayout((int) (getScreenWidth(context) * 0.97), (int) (getScreenWidth(context) * 0.80));

            window.setWindowAnimations(R.style.Animations_SlideWindow);

            // sagar : 4/2/19 7:33 PM This flag-mask only is making this dialog dismiss on click of outside
            // Make us non-modal, so that others can receive touch events.
//            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


            window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // sagar : 21/1/19 12:17 PM Flexible customization
            setWindow(window);
            resizeDialog(window);
            getAnimation();
            setAnimation(window);
        }

        handleViews();
        initControllers();
        setListeners();
        otherStuffs();
        callApi();
    }


    @Override
    public void cancel() {
        /*if (getOwnerActivity() != null) {
            KeyboardUtils.hideSoftInput(getOwnerActivity());
        }*/
//        KeyboardUtils.closeKeyboard(context);
        super.cancel();
    }


    //region Overriding onTouchEvent method can make this dialog forcefully dismissed on outside click otherwise the touch event can be passed in background
    // sagar : 13/2/19 7:45 PM Uncommenting this method gives unexpected heights of treatment dialog!
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // sagar : 13/2/19 12:31 PM To get click event through dialog to the host, copy filter dialog of brandZilla and comment onTouchEvent
        d(TAG, "BaseDialog: onTouchEvent: ");
       /* if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            d("Dilaog Outside Click", "onTouchEvent: ");
            this.dismiss();
        }*/
        dismiss();
        return false;
    }
    //endregion

    /**
     * Sets layout to be used for Dialog
     * <p> Just return R.id.layout_resource
     * </p>
     */
    public abstract int getLayout();

    /**
     * Beta
     *
     * <p> Check if the root parameter here when being used as view for data binding as yourLayoutBinding.bind(root) works or not
     * </p>
     *
     * @param root View to bind
     * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc">Read more about javadoc</a>
     * @since 1.0
     */
    public abstract void setDataBinding(View root);

    /**
     * Sets and decors custom window property for Dialog
     * <p>
     * <p> This method is executed within onCreate.
     * So, just define and set your desired Dialog window property here.
     * It can be used to decorate the window property of the Dialog.
     * </p>
     *
     * @param window The window of the dialog
     */
    public abstract void setWindow(Window window);

    /**
     * Resize the Dialog exclusively here.
     * <p>
     * // sagar : 21/1/19 6:55 PM This is how we can resize the dialog
     * WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
     * lp.width = (int) (ScreenUtils.getScreenWidth(context) * 0.90);
     * lp.height = WindowManager.LayoutParams.MATCH_PARENT;
     * if (window != null) {
     * window.setAttributes(lp);
     * }
     * <p> It will be executed within onCreate()
     * </p>
     *
     * @param window
     */
    protected abstract void resizeDialog(Window window);

    /**
     * This can be used to handle how the dialog should be appeared on screen with animation
     * Return R.style.Animations_SlideWindow
     * The style would look like:
     * <item name="android:windowEnterAnimation">@anim/slide_up</item>
     * <item name="android:windowExitAnimation">@anim/slide_down</item>
     */
    public abstract int getAnimation();

    private void setAnimation(Window window) {
        if (window != null) {
            if (getAnimation() != -1 && getAnimation() != 0) {
                window.setWindowAnimations(getAnimation());
            } else {
                window.setWindowAnimations(R.style.Animations_SlideWindow);
            }

            setCancelOnOutsideTouch(window);
            setCanceledOnTouchOutside(cancelOnOutsideTouch);

        }
    }

    /**
     * Sets run time changes in views of Dialog
     * <p>
     * <p> This method will be executed before Dialog becomes visible to the user.
     * So, you can define and set run time view changes here.
     * </p>
     */
    public abstract void handleViews();

    /**
     * Define any controllers to be used here
     */
    public abstract void initControllers();

    /**
     * Define Click listener for any view of the Dialog here
     */
    public abstract void setListeners();

    /**
     * Define all other stuff you may want to get started here.
     * <p>
     * <p> Note that it will be executed after callApi() method!
     * </p>
     */
    public abstract void otherStuffs();

    /*public boolean onTouchEvent(MotionEvent event) {
        if (cancelOnOutsideTouch) {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                d(TAG, "BaseDialog: onTouchEvent: Touch outside the dialog ******************** ");
                this.dismiss();
            }
        }

        if (isShowing() && (event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(getContext(), event) && getWindow().peekDecorView() != null)) {
           dismiss();
        }

        // super = let the system handle the event
        return false;
    }*/

    /**
     * Define api call/s here
     */
    public abstract void callApi();

    public void setCancelOnOutsideTouch(Window window) {
        if (window != null) {

            // Make us non-modal, so that others can receive touch events.
//            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            // ...but notify us that it happened.
            window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }
}
