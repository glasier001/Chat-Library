package com.commonlib.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;


import com.commonlib.R;
import com.commonlib.utils.LayoutDirection;

import static com.commonlib.customviews.GradientDrawable.setDrawableBackGround;


public class CustomLinearLayout extends LinearLayout {

    private int llDeafultBgColor;
    private int llPressBgColor;
    private int llBorderColor = 0;
    private int llBorderStroke = 8;

    private float llLeftTopRadius = 8;
    private float llRightTopRadius = 8;
    private float llRightBottomRadius = 8;
    private float llLeftBottomRadius = 8;

    private boolean rrIsSupportRTL;

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomLinearLayout,
                0, 0);


        llBorderColor = a.getColor(R.styleable.CustomLinearLayout_llBorderColor, 0);
        llDeafultBgColor = a.getColor(R.styleable.CustomLinearLayout_llDeafultBgColor, 0);
        llPressBgColor = a.getColor(R.styleable.CustomLinearLayout_llPressBgColor, 0);

        llBorderStroke = a.getInt(R.styleable.CustomLinearLayout_llBorderStroke, 0);
        llLeftTopRadius = a.getDimension(R.styleable.CustomLinearLayout_llLeftTopRadius, 0);
        llRightTopRadius = a.getDimension(R.styleable.CustomLinearLayout_llRightTopRadius, 0);
        llRightBottomRadius = a.getDimension(R.styleable.CustomLinearLayout_llRightBottomRadius, 0);
        llLeftBottomRadius = a.getDimension(R.styleable.CustomLinearLayout_llLeftBottomRadius, 0);
        rrIsSupportRTL = a.getBoolean(R.styleable.CustomLinearLayout_llIsSupportRTL, false);


        setBackGroundStates();
//        ChangeLayoutDirection();
    }


    private void ChangeLayoutDirection() {
        if (LayoutDirection.getLayoutDirection(getContext()) == android.util.LayoutDirection.RTL && rrIsSupportRTL) {
            setRotationY(180);
        }
    }

    private void setBackGroundStates() {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, setDrawableBackGround(llPressBgColor, llBorderColor, llBorderStroke, llLeftTopRadius, llRightTopRadius, llRightBottomRadius, llLeftBottomRadius));
        states.addState(new int[]{android.R.attr.state_selected}, setDrawableBackGround(llPressBgColor, llBorderColor, llBorderStroke, llLeftTopRadius, llRightTopRadius, llRightBottomRadius, llLeftBottomRadius));
        states.addState(new int[]{}, setDrawableBackGround(llDeafultBgColor, llBorderColor, llBorderStroke, llLeftTopRadius, llRightTopRadius, llRightBottomRadius, llLeftBottomRadius));
        setBackground(states);

    }


    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRadius(float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {
        this.llLeftTopRadius = leftTopRadius;
        this.llRightTopRadius = rightTopRadius;
        this.llRightBottomRadius = rightBottomRadius;
        this.llLeftBottomRadius = leftBottomRadius;
    }

    public void setRrIsSupportRTL() {
        ChangeLayoutDirection();
    }
}
