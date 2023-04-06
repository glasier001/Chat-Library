package com.commonlib.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatImageView;


public class CustomImageView extends AppCompatImageView {
    public CustomImageView(Context context) {
        super(context);
        init();
    }


    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        setBackgroundResource(outValue.resourceId);
        setClickable(true);
        setFocusable(false);
    }
}
