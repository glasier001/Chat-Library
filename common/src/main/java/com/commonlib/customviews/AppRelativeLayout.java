package com.commonlib.customviews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by mauliksantoki on 4/10/17.
 */

public class AppRelativeLayout extends RelativeLayout {
    public AppRelativeLayout(Context context) {
        super(context);
        initView();
    }


    public AppRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AppRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public AppRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        //TODO MK Change Version V1.1  Fix RTL bug on Android 4.2 (for Arabic and Hebrew)
        if (Build.VERSION.SDK_INT == 17 &&
                getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            // Force a left-to-right layout
            setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

}
