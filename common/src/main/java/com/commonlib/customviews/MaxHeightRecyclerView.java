package com.commonlib.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.commonlib.R;
import com.commonlib.utils.Utils;
import com.commonlib.utils.ScreenUtils;

import androidx.recyclerview.widget.RecyclerView;


public class MaxHeightRecyclerView extends RecyclerView {

    private int maxHeightDp;
    private Context context;

    public MaxHeightRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView, 0, 0);
        try {
            maxHeightDp = a.getInteger(R.styleable.MaxHeightRecyclerView_maxHeightInDp, 0);
        } finally {
            a.recycle();
        }
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    } 
 
    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d("je.homeFrag: ", "mhll: onMeasure: screenHeight: " + ScreenUtils.getScreenHeight(context));
//        Log.d("je.homeFrag: ", "mhll: onMeasure: int: " + heightMeasureSpec);
        int maxHeightPx = Utils.dpToPx(context, maxHeightDp);
//        Log.d("je.homeFrag: ", "mhll: onMeasure: dpTopx: " + maxHeightPx);
        int percentageInt = (int) (ScreenUtils.getScreenHeight(context) * 0.609); //This is working better than intFromDp!
        // FIXME: sagar : 30/7/18 Or may be DP can fit better?
        int dp = Utils.getIntFromDp(context, 350); //This is less accurate than percentageInt
        //1152 for Mi A1 (1920) and 710 for Moto (1184 height - perfect), 768 for 1280 oppo height which is lower than expected!
//        Log.d("je.homeFrag: ", "mhll: onMeasure: percentInt: " + percentageInt);
//        Log.d("je.homeFrag: ", "mhll: onMeasure: intFromDp: " + dp);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(percentageInt, MeasureSpec.AT_MOST); //AT_MOST = Maximum
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    } 
 
    public void setMaxHeightDp(int maxHeightDp) {
        this.maxHeightDp = maxHeightDp;
        invalidate();
    }
}