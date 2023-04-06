package com.commonlib.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.countrycodeinfo.CountryCodeInfoController;
import com.commonlib.typefacehandler.TypefaceUtils;
import com.commonlib.utils.Limits;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;


public class CustomTextInputLayout extends TextInputLayout {

    private TextWatcher passwordWatcher;
    private boolean showPasswordToggle = true;

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        addCustomView(context, attrs);
    }

    private void addCustomView(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextInputLayout,
                0, 0);

        int typeface = a.getInt(R.styleable.CustomTextInputLayout_etTypeface, 0);
        int inputType = a.getInt(R.styleable.CustomTextInputLayout_inputType, InputType.TYPE_CLASS_TEXT);
        String hintText = a.getString(R.styleable.CustomTextInputLayout_hintString);
        int textColor = a.getColor(R.styleable.CustomTextInputLayout_textColor, Color.BLACK);
        int hintColor = a.getColor(R.styleable.CustomTextInputLayout_hintTextColor, Color.BLACK);
        int floatColor = a.getColor(R.styleable.CustomTextInputLayout_floatColor, Color.BLACK);
        int textCursorColor = a.getColor(R.styleable.CustomTextInputLayout_textCursorColor,
                ContextCompat.getColor(context, R.color.colorAccent));
        int textSize = (int) a.getDimension(R.styleable.CustomTextInputLayout_textSize, 10);
        int hintTextSize = (int) a.getDimension(R.styleable.CustomTextInputLayout_hintTextSize, 10);
        boolean clickable = a.getBoolean(R.styleable.CustomTextInputLayout_clickable, true);
        boolean focusable = a.getBoolean(R.styleable.CustomTextInputLayout_focusable, true);
        boolean isCountryCode = a.getBoolean(R.styleable.CustomTextInputLayout_isCountryCode, false);
        boolean isPhone = a.getBoolean(R.styleable.CustomTextInputLayout_isPhone, false);
        boolean isEnable = a.getBoolean(R.styleable.CustomTextInputLayout_tilEtEnable, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        initMyEditText(context, typeface, inputType, hintText, textColor, hintColor, floatColor,
                textCursorColor, textSize, hintTextSize, clickable, focusable, isCountryCode, isPhone, isEnable);
    }

    private void initMyEditText(Context context, int typeface, int inputType, String hintText,
                                int textColor, int hintColor, int floatColor, int textCursorColor,
                                int textSize, int hintTextSize, boolean clickable, boolean focusable,
                                boolean isCountryCode, boolean isPhone, boolean isEnable) {

        AppCompatEditText appCompatEditText = new AppCompatEditText(context);

        setAutoCap(appCompatEditText);
        setMyInputType(appCompatEditText, inputType);
        setPasswordVisibilityToggleTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorAccent)));

        appCompatEditText.setHint(hintText);
        appCompatEditText.setHintTextColor(hintColor);
//        appCompatEditText.setBackground(ContextCompat.getDrawable(context, R.drawable.til_bottomline));
        appCompatEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        appCompatEditText.setTextColor(textColor);

        setCursorColor(appCompatEditText, textCursorColor);

        appCompatEditText.setPadding(appCompatEditText.getPaddingLeft(), appCompatEditText.getPaddingTop(), appCompatEditText.getPaddingRight(), (int) (appCompatEditText.getPaddingBottom() + getContext().getResources().getDimension(R.dimen.view_space_4)));
        appCompatEditText.setClickable(clickable);
        appCompatEditText.setFocusable(focusable);

        if (isCountryCode) {
            setCountryCode(appCompatEditText);
        }

        if (isPhone) {
            setPhone(appCompatEditText);
        }

        if (!isEnable) {
            appCompatEditText.setEnabled(false);
        }

        setMyTypeface(context, typeface, appCompatEditText);
        appCompatEditText.setCursorVisible(true);

        addView(appCompatEditText);
    }

    public void setAutoCap(EditText editText) {
        if (editText != null) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }
    }

    private void setMyInputType(AppCompatEditText appCompatEditText, int inputType) {
        appCompatEditText.setInputType(inputType);
        if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            appCompatEditText.setMaxLines(1);
            appCompatEditText.setSingleLine(true);
            setPasswordVisibilityToggleEnabled(false);
            appCompatEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            appCompatEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.PASSWORD_LENGTH_MAX_LIMIT)});
//            if (showPasswordToggle) {
//                setPasswordvisibilitytoggle(appCompatEditText);
//            }
        }
    }

    /**
     * Description
     *
     * @see <a href="https://stackoverflow.com/questions/25996032/how-to-change-programmatically-edittext-cursor-color-in-android/25996411">if not work this method setCursorColor</a>
     * @since 1.0
     */
    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    public void setCountryCode(final AppCompatEditText appCompatEditText) {
        appCompatEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.COUNTRY_CODE_TXT_LIMIT)});
        CountryCodeInfoController codeInfoController = new CountryCodeInfoController(getContext());
        appCompatEditText.setText(String.format("+%s", codeInfoController.getUserCountryCode().getDialingCode()));
//        appCompatEditText.setSelection(0);
        appCompatEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+")) {
                    appCompatEditText.setText("+" + appCompatEditText.getText().toString().replace("+", ""));

                    appCompatEditText.setSelection(0);
                }




            }
        });
    }

    public void setPhone(AppCompatEditText appCompatEditText) {
        appCompatEditText.setMaxLines(1);
        appCompatEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.PHONE_NUMBER_TXT_LIMIT)});
        appCompatEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        appCompatEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    private void setMyTypeface(Context context, int typeface, AppCompatEditText appCompatEditText) {

        if (typeface == TypefaceUtils.INT_CODE_BOLD_FONT) {
            setTypeface(TypefaceUtils.getInstance().getBoldTypeface(getContext()));
            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getBoldTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_REGULAR_FONT) {
            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightTypeface(getContext()));
//            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getLightTypeface(context));

            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));

        } else if (typeface == TypefaceUtils.INT_CODE_LIGHT_ITALIC_FONT) {
//            setTypeface(TypefaceUtils.getInstance().getLightItalicTypeface(getContext()));
//            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getLightItalicTypeface(context));

            setTypeface(TypefaceUtils.getInstance().getRegularTypeface(getContext()));
            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));

        } else if (typeface == TypefaceUtils.INT_CODE_MEDIUM) {
            setTypeface(TypefaceUtils.getInstance().getMediumTypeface(getContext()));
            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getMediumTypeface(context));
        } else if (typeface == TypefaceUtils.INT_CODE_DEMI_BOLD) {
            setTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(getContext()));
            appCompatEditText.setTypeface(TypefaceUtils.getInstance().getDemiBoldTypeface(context));
        }

        // sagar : 18/1/19 6:05 PM Setting up medium typeface for all floating hints
        setTypeface(TypefaceUtils.getInstance().getMediumTypeface(getContext()));
        // sagar : 18/1/19 6:06 PM Setting up regular typeface for all edit text hints
        appCompatEditText.setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
    }

    private void setPasswordvisibilitytoggle(AppCompatEditText appCompatEditText) {
        passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {

                    setPasswordVisibilityToggleEnabled(true);
                } else {

                    setPasswordVisibilityToggleEnabled(false);
                }


                if (editable.length() == 0) {

                    appCompatEditText.setHint(getHint());

                }

            }
        };

        appCompatEditText.addTextChangedListener(passwordWatcher);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void setMultiline(EditText editText, boolean isCapWords) {
        if (editText != null) {
            editText.setSingleLine(false);
            editText.setMaxLines(500);
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            if (isCapWords) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
        }
    }

    public void setAutoCap(TextInputLayout til) {
        if (til != null && til.getEditText() != null) {
            til.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }
    }

    public boolean isShowPasswordToggle() {
        return showPasswordToggle;
    }

    public void setShowPasswordToggle(boolean showPasswordToggle) {
        this.showPasswordToggle = showPasswordToggle;
        if (!showPasswordToggle) {
            if (getEditText() != null) {
                if (passwordWatcher != null) {
                    getEditText().removeTextChangedListener(passwordWatcher);
                }
            }
        }
    }
}
