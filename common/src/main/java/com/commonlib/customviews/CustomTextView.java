package com.commonlib.customviews;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.constants.PreferencesKey;
import com.commonlib.typefacehandler.TypefaceUtils;
import com.commonlib.utils.LogShowHide;
import com.commonlib.utils.SharedPrefs;

import java.text.NumberFormat;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.ColorUtils;

/**
 * Created by mauliksantoki on 21/8/17.
 */

public class CustomTextView extends AppCompatTextView {

    private NumberFormat distansFormat;
    private NumberFormat numberFormat;
    private boolean isUnderlined;
    private Paint mPaint;
    private Rect mRect;
    private float density;
    private float mStrokeWidth;
    private int[] colors = new int[2];
    private float x_diff;
    private int selectedTextColor;
    private int unselectedTextColor;
    private int pressedColor;

    private Context mContext;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        density = context.getResources().getDisplayMetrics().density;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        SharedPreferences sharedPrefs = SharedPrefs.getSharedPref(getContext());

        int typeface = a.getInt(R.styleable.CustomTextView_txtTypeface, 0);
        int color = a.getColor(R.styleable.CustomTextView_ctvTxtColor, 0);
        int selectedColor = a.getColor(R.styleable.CustomTextView_ctvSelectedTxtColor, 0);
        int pressedColor = a.getColor(R.styleable.CustomTextView_ctvPressedColor, 0);

        if (selectedColor == 0) {
            selectedColor = color;
        }

        if (pressedColor == 0) {
            pressedColor = color;
        }

        setCustomTextColor(color, pressedColor, selectedColor);
        setTextTypeface(typeface);
        setClickable(isClickable());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setStateListAnimator(null);
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(context, R.animator.sla_lift_up);
            setStateListAnimator(sla);
        }


        isUnderlined = a.getBoolean(R.styleable.CustomTextView_isUnderlined, false);

        if (isUnderlined) {
            Drawable drawable = a.getDrawable(R.styleable.CustomTextView_underlineBackground);
            mStrokeWidth = a.getDimension(R.styleable.CustomTextView_underlineHeight, density * 2);
            a.recycle();
            //region Gradient creation programmatically
            colors[0] = Color.parseColor("#E84C57");
            colors[1] = Color.parseColor("#FF6633");

            LinearGradient gradient = new LinearGradient(0, 0, 0, 400, colors[0], colors[1], Shader.TileMode.CLAMP);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setShader(gradient);
            mPaint.setStrokeWidth(mStrokeWidth);
            //endregion
        }
    }

    public void setCustomTextColor(int color, int pressedColor, int selectedColor) {
        setTextColor(getPressedColorSelector(color, isClickable() ? getTransparentColor(color) : pressedColor != 0 ? pressedColor : color, selectedColor));
    }

    public void setTextTypeface(int typeface) {
        if (typeface == TypefaceUtils.INT_CODE_BOLD_FONT) {
            setTypeface(TypefaceUtils.getInstance().getBoldTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_REGULAR_FONT) {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightTypeface(getContext()));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_ITALIC_FONT) {
            setTypeface(TypefaceUtils.getInstance().getRegularItalicTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_DEMI_BOLD) {
            setTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM) {
            setTypeface(TypefaceUtils.getInstance().getMediumTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM_ITALIC) {
            setTypeface(TypefaceUtils.getInstance().getRegularItalicTypeface(getContext()));
        } else if (typeface == TypefaceUtils.INT_CODE_REGULAR_ITALIC) {
            setTypeface(TypefaceUtils.getInstance().getRegularItalicTypeface(getContext()));
        } else {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        }
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor, int selectedTextColor) {
        // sagar : 5/12/18 Orders are so important here.
        /*https://stackoverflow.com/questions/15543186/how-do-i-create-colorstatelist-programmatically*/
        // sagar : 5/12/18 Check comment of ToolMakerSteve there under accepted answer of Caner
        // sagar : 5/12/18 Consider color state list as if-else block only
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{android.R.attr.state_selected},
                                new int[]{android.R.attr.state_enabled},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                selectedTextColor,
                                selectedTextColor,
                                normalColor,
                                normalColor
                        }
        );
    }

    private int getTransparentColor(int color) {
        return ColorUtils.setAlphaComponent(color, 128);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;
        initAttrs(context, attrs);
    }

    public float getUnderlineHeight() {
        return mStrokeWidth;
    }

    public void setUnderlineHeight(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

    public void setTypeface(int typeface) {
        if (typeface == TypefaceUtils.INT_CODE_BOLD_FONT) {
            setTypeface(TypefaceUtils.getInstance().getBoldTypeface(getContext()));
        } else {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
        }
    }

    public void setDistance(String distance, String endString) {
        intNumberFormate();
        try {
            setDistance(Double.parseDouble(distance), endString);
        } catch (Exception e) {
            LogShowHide.LogShowHideMethod(getContext(), e.getMessage());
            setDistance(0.0, endString);
        }
    }

    private void intNumberFormate() {
        if (distansFormat == null) {
            distansFormat = NumberFormat.getInstance(Locale.ENGLISH);
            distansFormat.setMinimumFractionDigits(1);
            distansFormat.setMaximumFractionDigits(1);
        }
    }

    public void setDistance(double distance, String endString) {
        intNumberFormate();
        setText(String.format("%s%s", distansFormat.format(distance), endString));
    }

    public void setNumberFormatBasePriseText(String prise) {
        try {
            setNumberFormatBasePriseText(Double.parseDouble(prise));
        } catch (Exception e) {
            setNumberFormatText(0.0, null);
        }
    }

    public void setNumberFormatBasePriseText(Double prise) {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);
        }
        setText(String.format("%s %s %s", "from in ctv class", SharedPrefs.getSharedPref(mContext).getString(PreferencesKey.CURRENCY_SYMBOL, ""), numberFormat.format(prise)));
    }

    public void setNumberFormatText(Double prise, String currencySymbol) {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);
        }
        setText(String.format("%s %s", (currencySymbol == null) ? "currency symbol in ctv class" : currencySymbol, numberFormat.format(prise)));
    }

    public void setNumberFormatText(String prise, String currencySymbol) {
        try {
            setNumberFormatText(Double.parseDouble(prise), currencySymbol);
        } catch (Exception e) {
            setNumberFormatText(0.0, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isUnderlined) {
            //region View gradient to end line upto last char only
            int lineCount = getLineCount();
            Log.e("lineCount", String.valueOf(lineCount));
            final Layout layout = getLayout();
            int baseLine = getLineBounds(lineCount - 1, mRect);
            Log.e("baseLine", String.valueOf(baseLine));
            int lastCharInLine = layout.getLineStart(lineCount);
            // FIXME: 27/9/17  => Sagar: Note: Might be different b/w below API 21 Lollipop and above API 21
            Log.e("lineStartOffset", String.valueOf(lastCharInLine));
            Log.e("lineEndOffset", String.valueOf(0));
//            firstCharInLine = layout.getLineEnd(lineCount); //Chars after last char! Always 0 in our case!
            int firstCharInLine = 0;
            float x_start = layout.getPrimaryHorizontal(firstCharInLine);
//            x_diff = layout.getPrimaryHorizontal(lastCharInLine + 1) - x_start;
            float x_stop = layout.getPrimaryHorizontal(lastCharInLine);

            //region Test area
            //TODO 28/9/17 => sagar =>Note :- Will be removed once the issue is fixed
            float distance = (float) (baseLine + (AppConstants.REF_SCREEN_HEIGHT * 0.0135));
            Log.e("screenHeight", String.valueOf(AppConstants.REF_SCREEN_HEIGHT));
            Log.e("distance", String.valueOf(distance));
            Log.e("baseLine", String.valueOf(baseLine));
            Log.e("StaticDistance", String.valueOf(baseLine + 10));
            //endregion

            canvas.drawLine(x_start, (float) (baseLine + (AppConstants.REF_SCREEN_HEIGHT * 0.0135)), x_stop, (float) (baseLine + (AppConstants.REF_SCREEN_HEIGHT * 0.0135)), mPaint);
//            canvas.drawLine(x_start, (float) (baseLine + (AppUrls.screenHeight * 0.0135)), x_stop, (float) (baseLine + (AppUrls.screenHeight * 0.0135)), mPaint);
            //endregion
        }
        super.onDraw(canvas);
    }

    //TODO MK Change Version V1.1 get Truck currency code...

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public float dpToFloatPx(int dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public String getCurrency() {
        return !SharedPrefs.getSharedPref(mContext).getString(PreferencesKey.CURRENCY_SYMBOL, "").equalsIgnoreCase("")
                ? SharedPrefs.getSharedPref(mContext).getString(PreferencesKey.CURRENCY_SYMBOL, "") :
                "My Currency Code in CustomTextView Class";
    }
}
