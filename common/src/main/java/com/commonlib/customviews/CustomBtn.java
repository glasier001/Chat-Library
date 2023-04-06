package com.commonlib.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import com.commonlib.R;
import com.commonlib.typefacehandler.TypefaceUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.ColorUtils;

/**
 * Created by mauliksantoki on 19/8/17.
 */

public class CustomBtn extends AppCompatTextView {

    private int deafultColor;

    public CustomBtn(Context context) {
        super(context);
        initAttrs(null);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomBtn,
                0, 0);

        deafultColor = a.getInt(R.styleable.CustomBtn_btnTextColor, Color.BLACK);
        setTextColor(getPressedColorSelector(deafultColor, getTransparentColor(deafultColor)));
        setTypeface(a.getInt(R.styleable.CustomBtn_btnTypeface, 0));

        /*code to  Remove shadow from Login gradient button*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStateListAnimator(null);
        }
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                normalColor
                        }
        );
    }

    private int getTransparentColor(int color) {
        return ColorUtils.setAlphaComponent(color, 128);
    }

    public void setTypeface(int typeface) {
        if (typeface == TypefaceUtils.INT_CODE_BOLD_FONT) {
            setTypeface(TypefaceUtils.getInstance().getBoldTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_REGULAR_FONT) {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightTypeface(getContext()));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_ITALIC_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightItalicTypeface(getContext()));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_DEMI_BOLD) {
            setTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM) {
            setTypeface(TypefaceUtils.getInstance().getMediumTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM_ITALIC) {
//            setTypeface(TypefaceUtils.getInstance().getMediumItalicTypeface(getContext()));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        } else {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        }
    }

    public CustomBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CustomBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    public void setTextColor(int deafultColor) {
        this.deafultColor = deafultColor;
        setTextColor(getPressedColorSelector(deafultColor, getTransparentColor(deafultColor)));
    }
}
