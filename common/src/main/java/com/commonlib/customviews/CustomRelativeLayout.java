package com.commonlib.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import com.commonlib.R;
import com.commonlib.utils.LayoutDirection;

import static com.commonlib.customviews.GradientDrawable.setDrawableBackGround;

public class CustomRelativeLayout extends AppRelativeLayout {

    private int rrDeafultBgColor;
    private int rrPressBgColor;
    private int rrBorderColor = 0;
    private int rrBorderStroke = 8;

    private float rrLeftTopRadius = 8;
    private float rrRightTopRadius = 8;
    private float rrRightBottomRadius = 8;
    private float rrLeftBottomRadius = 8;

    private boolean rrIsSupportRTL;


    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomRelativeLayout,
                0, 0);


        rrBorderColor = a.getColor(R.styleable.CustomRelativeLayout_rvBorderColor, 0);
        rrDeafultBgColor = a.getColor(R.styleable.CustomRelativeLayout_rvDeafultBgColor, 0);
        rrPressBgColor = a.getColor(R.styleable.CustomRelativeLayout_rvPressBgColor, 0);

        rrBorderStroke = a.getInt(R.styleable.CustomRelativeLayout_rvBorderStroke, 0);
        rrLeftTopRadius = a.getDimension(R.styleable.CustomRelativeLayout_rvLeftTopRadius, 0);
        rrRightTopRadius = a.getDimension(R.styleable.CustomRelativeLayout_rvRightTopRadius, 0);
        rrRightBottomRadius = a.getDimension(R.styleable.CustomRelativeLayout_rvRightBottomRadius, 0);
        rrLeftBottomRadius = a.getDimension(R.styleable.CustomRelativeLayout_rvLeftBottomRadius, 0);

        rrIsSupportRTL = a.getBoolean(R.styleable.CustomRelativeLayout_rrIsSupportRTL, false);
        setBackGroundStates();
//        ChangeLayoutDirection();

    }

    public void ChangeLayoutDirection() {
        if (LayoutDirection.getLayoutDirection(getContext()) == android.util.LayoutDirection.RTL && rrIsSupportRTL) {
            setRotationY(180);
        }
    }


    private void setBackGroundStates() {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, setDrawableBackGround(rrPressBgColor, rrBorderColor, rrBorderStroke, rrLeftTopRadius, rrRightTopRadius, rrRightBottomRadius, rrLeftBottomRadius));
        states.addState(new int[]{android.R.attr.state_selected}, setDrawableBackGround(rrPressBgColor, rrBorderColor, rrBorderStroke, rrLeftTopRadius, rrRightTopRadius, rrRightBottomRadius, rrLeftBottomRadius));
        states.addState(new int[]{}, setDrawableBackGround(rrDeafultBgColor, rrBorderColor, rrBorderStroke, rrLeftTopRadius, rrRightTopRadius, rrRightBottomRadius, rrLeftBottomRadius));
        setBackground(states);

    }


    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRrIsSupportRTL(boolean rrIsSupportRTL) {
        this.rrIsSupportRTL = rrIsSupportRTL;
        ChangeLayoutDirection();
    }
}
