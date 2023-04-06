package com.commonlib.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.commonlib.R;
import com.commonlib.countrycodeinfo.CountryCodeInfoController;
import com.commonlib.typefacehandler.TypefaceUtils;
import com.commonlib.utils.Limits;

import java.text.NumberFormat;
import java.util.Locale;


public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

    NumberFormat numberFormat;
    private boolean etIsPhone;
    private boolean etIsCountryCode;
    private int etBackgroundColor = 0;
    private int etBorderColor = 0;
    private int etBorderStroke = 8;
    private float etLeftTopRadius = 8;
    private float etRightTopRadius = 8;
    private float etRightBottomRadius = 8;
    private float etLeftBottomRadius = 8;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomEditText,
                0, 0);

        etBorderColor = a.getColor(R.styleable.CustomEditText_etBorderColor, 0);
        etBackgroundColor = a.getColor(R.styleable.CustomEditText_etBackgroundColor, 0);

        etBorderStroke = a.getInt(R.styleable.CustomEditText_etBorderStroke, 0);
        etLeftTopRadius = a.getDimension(R.styleable.CustomEditText_etLeftTopRadius, 0);
        etRightTopRadius = a.getDimension(R.styleable.CustomEditText_etRightTopRadius, 0);
        etRightBottomRadius = a.getDimension(R.styleable.CustomEditText_etRightBottomRadius, 0);
        etLeftBottomRadius = a.getDimension(R.styleable.CustomEditText_etLeftBottomRadius, 0);
        int etTypeFace = a.getInt(R.styleable.CustomEditText_etTypeFace, 0);
        etIsCountryCode = a.getBoolean(R.styleable.CustomEditText_etIsCountryCode, false);
        etIsPhone = a.getBoolean(R.styleable.CustomEditText_etIsPhone, false);

        //One tap solution if all radius are same
        float etRadius = a.getDimension(R.styleable.CustomEditText_etRadius, 0);
//        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etRadius, etRadius, etRadius, etRadius));

        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etLeftTopRadius, etRightTopRadius, etRightBottomRadius, etLeftBottomRadius));
        setCustomEditTextTypeface(context, etTypeFace);

        if (etIsCountryCode) {
            setCountryCode();
        }

        if (etIsPhone) {
            setPhone();
        }
    }

    private void setCustomEditTextTypeface(Context context, int typeface) {
        if (typeface == TypefaceUtils.INT_CODE_BOLD_FONT) {
            setTypeface(TypefaceUtils.getInstance().getBoldTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_REGULAR_FONT) {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightTypeface(context));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_ITALIC_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightItalicTypeface(context));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_DEMI_BOLD) {
            setTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM) {
            setTypeface(TypefaceUtils.getInstance().getMediumTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM_ITALIC) {
//            setTypeface(TypefaceUtils.getInstance().getMediumItalicTypeface(context));
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        } else {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        }
    }

    public void setCountryCode() {
        setMaxLines(1);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.COUNTRY_CODE_TXT_LIMIT)});
        CountryCodeInfoController codeInfoController = new CountryCodeInfoController(getContext());
        setText(String.format("+%s", codeInfoController.getUserCountryCode().getDialingCode()));
        setSelection(getText().length());
        setInputType(InputType.TYPE_CLASS_NUMBER);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+")) {
                    setText(String.format("+%s", getText().toString().replace("+", "")));
                    setSelection(getText().length());
                }
            }
        });
    }

    public void setPhone() {
        setMaxLines(1);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.PHONE_NUMBER_TXT_LIMIT)});
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isEtIsPhone() {
        return etIsPhone;
    }

    public void setEtIsPhone(boolean etIsPhone) {
        this.etIsPhone = etIsPhone;
        if (etIsPhone) {
            setPhone();
        }
    }

    public boolean isEtIsCountryCode() {
        return etIsCountryCode;
    }

    public void setEtIsCountryCode(boolean etIsCountryCode) {
        this.etIsCountryCode = etIsCountryCode;
        if (etIsCountryCode) {
            setCountryCode();
        }
    }

    public float getEtLeftTopRadius() {
        return etLeftTopRadius;
    }

    @SuppressWarnings("SameParameterValue")
    public void setEtLeftTopRadius(float etLeftTopRadius) {
        this.etLeftTopRadius = etLeftTopRadius;
        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etLeftTopRadius, etRightTopRadius, etRightBottomRadius, etLeftBottomRadius));
    }

    public float getEtRightTopRadius() {
        return etRightTopRadius;
    }

    public void setEtRightTopRadius(float etRightTopRadius) {
        this.etRightTopRadius = etRightTopRadius;
        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etLeftTopRadius, etRightTopRadius, etRightBottomRadius, etLeftBottomRadius));
    }

    public float getEtRightBottomRadius() {
        return etRightBottomRadius;
    }

    public void setEtRightBottomRadius(float etRightBottomRadius) {
        this.etRightBottomRadius = etRightBottomRadius;
        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etLeftTopRadius, etRightTopRadius, etRightBottomRadius, etLeftBottomRadius));
    }

    public float getEtLeftBottomRadius() {
        return etLeftBottomRadius;
    }

    @SuppressWarnings("SameParameterValue")
    public void setEtLeftBottomRadius(float etLeftBottomRadius) {
        this.etLeftBottomRadius = etLeftBottomRadius;
        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etLeftTopRadius, etRightTopRadius, etRightBottomRadius, etLeftBottomRadius));
    }

    public void setRadius(float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {
        this.etLeftTopRadius = leftTopRadius;
        this.etRightTopRadius = rightTopRadius;
        this.etRightBottomRadius = rightBottomRadius;
        this.etLeftBottomRadius = leftBottomRadius;
        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius));
    }

    public void setRadius(float etRadius) {
        setBackground(GradientDrawable.setDrawableBackGround(etBackgroundColor, etBorderColor, etBorderStroke, etRadius, etRadius, etRadius, etRadius));
    }

    public String getNumberFormatText(String prise) {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);
        }
        return (numberFormat.format(Double.parseDouble(prise)));
    }

    public void setMultiline(boolean isCapWords) {
        setSingleLine(false);
        setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        setMaxLines(500);
        if (isCapWords) {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        } else {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        }
    }

    public void setAutoCap() {
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    }

    public void changeInputType(int inputType) {
        setInputType(inputType);
    }
}
