package com.commonlib.utils;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.commonlib.R;

import androidx.core.content.ContextCompat;


/**
 * Created by dilip on 1/9/17.
 */

public class MySpannable extends ClickableSpan {
    Activity activity;
    private boolean isUnderline = false;

    /**
     * Constructor
     */
    public MySpannable(Activity activity, boolean isUnderline) {
        this.activity = activity;
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
//        ds.setColor(Color.parseColor("#DA251E"));
        ds.setColor(ContextCompat.getColor(activity, R.color.close_red));

    }

    @Override
    public void onClick(View widget) {

    }
}
