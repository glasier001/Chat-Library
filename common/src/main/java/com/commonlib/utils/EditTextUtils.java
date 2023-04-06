package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.commonlib.R;
import com.commonlib.constants.AppConstants;
import com.commonlib.countrycodeinfo.CountryCodeInfoController;
import com.commonlib.customviews.CustomEditText;
import com.commonlib.customviews.CustomTextInputLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class EditTextUtils {

    private EditTextUtils() {
    }

    public static void setPhone(EditText editText) {
        if (editText != null) {
            editText.setMaxLines(1);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.PHONE_NUMBER_TXT_LIMIT)});
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            editText.setSelection(editText.getText().length());
        }
    }

    public static void setInputLimit(EditText editText, int limit) {
        if (editText != null && limit > -1) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
        }
    }

    public static void setInputLimit(int limit, EditText... editTexts) {
        if (Utils.isNotNullNotEmpty((Object[]) editTexts) && limit > -1) {
            for (EditText editText : editTexts) {
                setInputLimit(limit, editText);
            }
        }
    }

    public static void setTextWatcher(TextWatcher textWatcher, EditText... editTexts) {
        if (Utils.isNotNullNotEmpty((Object[]) editTexts)) {
            for (EditText editText : editTexts) {
                editText.addTextChangedListener(textWatcher);
            }
        }
    }

    public static void setInputTypeNumber(EditText... editTexts) {
        if (Utils.isNotNullNotEmpty((Object[]) editTexts)) {
            for (EditText editText : editTexts) {
                setInputTypeNumber(editText);
            }
        }
    }

    public static void setInputTypeNumber(EditText editText) {
        if (editText != null) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    public static void setCountryCode(EditText editText) {
        if (editText != null) {
            editText.setMaxLines(1);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.COUNTRY_CODE_TXT_LIMIT)});
            CountryCodeInfoController codeInfoController = new CountryCodeInfoController(editText.getContext());
            editText.setText(String.format("+%s", codeInfoController.getUserCountryCode().getDialingCode()));
            editText.setSelection(editText.getText().length());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            if (!editText.getText().toString().startsWith("+")) {
                editText.setText(String.format("+%s", editText.getText().toString().replace("+", "")));
                editText.setSelection(editText.getText().length());
            }
        }
    }

    public static void setText(TextInputLayout til, String value) {
        if (til != null) {
            til.setVisibility(View.VISIBLE);
            if (til.getEditText() != null) {
                til.getEditText().setVisibility(View.VISIBLE);
                til.getEditText().setText(StringUtils.getDefaultString(value, ""));
            }
        }
    }

    public static void setText(CustomTextInputLayout ctil, String value) {
        if (ctil != null) {
            if (ctil.getEditText() != null) {
                EditTextUtils.setText(ctil.getEditText(), value);
            }
        }
    }

    public static void setText(EditText editText, String value) {
        if (editText != null) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(StringUtils.getDefaultString(value, ""));
        }
    }

    public static void setEmail(EditText editText) {
        if (editText != null) {
            editText.setMaxLines(1);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.EMAIL_TXT_LIMIT)});
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//            if (editText.getText() != null && !editText.getText().toString().isEmpty() && editText.getText().toString().length() > 0) {
//                editText.setSelection(editText.getText().length());
//            }
        }
    }

    public static void clearEditText(EditText editText) {
        if (editText != null) {
            editText.setText("");
        }
    }

    public static void clearEditText(EditText... editTexts) {
        if (editTexts != null) {
            for (EditText editText : editTexts) {
                if (editText != null) {
                    editText.setText("");
                }
            }
        }
    }

    public static void clearInputs(CustomTextInputLayout... ctils) {
        if (ctils != null) {
            for (CustomTextInputLayout customTextInputLayout : ctils) {
                if (customTextInputLayout != null) {
                    if (customTextInputLayout.getEditText() != null) {
                        customTextInputLayout.getEditText().setText("");
                    }
                }
            }
        }
    }

    public static void removeFocus(EditText editText, Activity activity) {
        if (editText != null) {
            editText.clearFocus();
            KeyboardUtils.hideSoftInput(activity);
        }
    }

    public static void setHint(EditText editText, String hint) {
        if (editText != null) {
            editText.setHint(hint);
        }
    }

    public static void toggleEditTextDrawable(Context context, View v, boolean hasFocus, int selectedColorId, int unselectedColorId) {
        if (hasFocus) {
            if (v instanceof EditText && ((EditText) v).getCompoundDrawables().length > 0) {
                try {
                    if (context != null) {
                        ((EditText) v).getCompoundDrawables()[0].setColorFilter(ContextCompat.getColor(context, selectedColorId), PorterDuff.Mode.SRC_ATOP);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (v instanceof EditText && ((EditText) v).getCompoundDrawables().length > 0) {
                try {
                    if (context != null) {
                        ((EditText) v).getCompoundDrawables()[0].setColorFilter(ContextCompat.getColor(context, unselectedColorId), PorterDuff.Mode.SRC_ATOP);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setEditTextStateUi(Activity activity, int[] drawableResIds, int[] colorResIds, EditText... editText) {
        if (editText != null && editText.length > 0 && drawableResIds.length == colorResIds.length && colorResIds.length == editText.length) {
            for (int i = 0, l = editText.length; i < l; i++) {
                if (drawableResIds[i] != 0) {
                    editText[i].setCompoundDrawablesWithIntrinsicBounds(drawableResIds[i], 0, 0, 0);
                    if (editText[i].getCompoundDrawables().length > 0 && colorResIds[i] != 0) {
                        try {
                            editText[i].getCompoundDrawables()[0].setColorFilter(ContextCompat.getColor(activity, colorResIds[i]), PorterDuff.Mode.SRC_ATOP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void setEditTextFocusListener(Activity activity, View... views) {
        if (isNotNullNotEmpty(views) && activity != null) {
            for (View view : views) {
                try {
                    view.setOnFocusChangeListener((View.OnFocusChangeListener) activity);
                } catch (Exception e) {
                    CustomToastMessage.animRedTextMethod(activity, "you need to implement the interface in host");
                }
            }
        }
    }

    private static boolean isNotNullNotEmpty(View... editTexts) {
        return editTexts != null && editTexts.length > 0;
    }

    public static void setEditTextValue(String commonValue, EditText... editTexts) {
        if (isNotNullNotEmpty(editTexts) && commonValue != null) {
            for (EditText editText : editTexts) {
                editText.setText(commonValue);
            }
        }
    }

    public static void setEditTextValue(String commonValue, CustomTextInputLayout... ctils) {
        if (isNotNullNotEmpty(ctils) && commonValue != null) {
            for (CustomTextInputLayout ctil : ctils) {
                if (null != ctil.getEditText()) {
                    ctil.getEditText().setText(commonValue);
                }
            }
        }
    }

    public static void setKeyboardActionListener(Activity activity, EditText... views) {
        if (isNotNullNotEmpty(views) && activity != null) {
            for (EditText view : views) {
                try {
                    view.setOnEditorActionListener((EditText.OnEditorActionListener) activity);
                } catch (Exception e) {
                    CustomToastMessage.animRedTextMethod(activity, "you need to implement the interface in host");
                }
            }
        }
    }

    public static void setKeyboardActionListener(Fragment fragment, EditText... views) {
        if (isNotNullNotEmpty(views) && fragment != null) {
            for (EditText view : views) {
                try {
                    view.setOnEditorActionListener((EditText.OnEditorActionListener) fragment);
                } catch (Exception e) {
                    CustomToastMessage.animRedTextMethod(fragment.getActivity(), "you need to implement the interface in host");
                }
            }
        }
    }

    public static void setEditTextFocusListener(Fragment fragment, View... views) {
        if (isNotNullNotEmpty(views) && fragment != null) {
            for (View view : views) {
                try {
                    view.setOnFocusChangeListener((View.OnFocusChangeListener) fragment);
                } catch (Exception e) {
                    CustomToastMessage.animRedTextMethod(fragment.getActivity(), "you need to implement the interface in host");
                }
            }
        }
    }

    public static void toggleSelection(boolean isSelected, int selectedColor, int unselectedColor, EditText... views) {
        if (views != null) {
            if (views.length > 0) {
                if (isSelected) {
                    for (EditText view : views) {
                        try {
                            view.setCompoundDrawablesWithIntrinsicBounds(((EditText) view).getCompoundDrawables()[0], null, null, null);
                            view.getCompoundDrawables()[0].setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
                            view.requestLayout();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (EditText v : views) {
                        try {
                            ((EditText) v).setCompoundDrawablesWithIntrinsicBounds(((EditText) v).getCompoundDrawables()[0], null, null, null);
                            ((EditText) v).getCompoundDrawables()[0].setColorFilter(unselectedColor, PorterDuff.Mode.SRC_ATOP);
                            v.requestLayout();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void setCursor(EditText... editTexts) {
        if (editTexts != null && editTexts.length > 0) {
            for (EditText editText : editTexts) {
                Log.d(AppConstants.TAG, "setCursor: " + editText.getText().length());
                editText.setSelection(editText.getText().length());
            }
        }
    }

    public static void setSelected(int selectedColor, View... views) {
        if (views != null) {
            if (views.length > 0) {
                for (View view : views) {
                    setSelected(view, selectedColor);
                }
            }
        }
    }

    public static void setSelected(View v, int selectedColor) {
        if (v instanceof EditText && ((EditText) v).getCompoundDrawables().length > 0) {
            try {
                ((EditText) v).getCompoundDrawables()[0].setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setUnselected(View v, int unselectedColor) {
        // sagar : 25/10/18 string length parameter is based on design format
        if (v instanceof EditText && ((EditText) v).getText().toString().length() == 0 && ((EditText) v).getCompoundDrawables().length > 0) {
            // sagar : 25/10/18 According to new understanding, now filled fields will be selected in addition to focused one
            try {
                ((EditText) v).getCompoundDrawables()[0].setColorFilter(unselectedColor, PorterDuff.Mode.SRC_ATOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setCountryCodeSelected(Context context, EditText countryCode, int countryDrawableResId, boolean hasFocus, int selectedColorResId, int unselectedColorResId, EditText emailPhone) {
        EditTextUtils.setEditTextDrawable(context, countryCode, countryDrawableResId, hasFocus, selectedColorResId, unselectedColorResId);
        EditTextUtils.removeDrawables(emailPhone);
    }

    public static void setEditTextDrawable(Context context, EditText editText, int drawableResId, boolean hasFocus, int activeColorId, int disableColorId) {
        editText.setCompoundDrawablesWithIntrinsicBounds(drawableResId, 0, 0, 0);
        if (drawableResId != 0) {
            if (hasFocus) {
                if (editText.getCompoundDrawables().length > 0) {
                    try {
                        editText.getCompoundDrawables()[0].setColorFilter(ContextCompat.getColor(context, activeColorId), PorterDuff.Mode.SRC_ATOP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (editText.getCompoundDrawables().length > 0) {
                    try {
                        editText.getCompoundDrawables()[0].setColorFilter(ContextCompat.getColor(context, disableColorId), PorterDuff.Mode.SRC_ATOP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void removeDrawables(EditText editText) {
        if (editText != null) {
            if (editText.getCompoundDrawables().length > 0) {
                editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
//            editText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
    }

    public static boolean hasText(CustomTextInputLayout ctil) {
        return ctil != null && ctil.getEditText() != null && (ctil.getEditText()).getText().toString().trim().length() > 0;
    }

    public static String getText(EditText v) {
        return getString(v);
    }

    public static String getString(EditText v) {
        if (v != null && v.getText() != null && ((EditText) v).getText().toString().trim().length() > 0) {
            return ((EditText) v).getText().toString();
        } else {
            return "";
        }
    }

    public static String getString(TextInputLayout til) {
        if (til != null && til.getEditText() != null && til.getEditText().getText().toString().trim().length() > 0) {
            return til.getEditText().getText().toString();
        } else {
            if (til != null && til.getContext() != null) {
                LogShowHide.LogShowHideMethod(til.getContext(), "returning empty blank value from: " + til);
            }
            return "";
        }
    }

    public static String getCountryCode(TextInputLayout til) {
        if (til != null && til.getEditText() != null && til.getEditText().getText().toString().trim().length() > 0) {
            return til.getEditText().getText().toString().trim().replace("+", "");
        }
        return "";
    }

    public static String getCountryCode(EditText et) {
        if (hasText(et)) {
            return et.getText().toString().trim().replace("+", "");
        } else {
            return "";
        }
    }

    public static boolean hasText(EditText v) {
        return v != null && v.getText() != null && ((EditText) v).getText().toString().trim().length() > 0;
    }

    public static boolean isNotNullNotEmpty(TextInputLayout til) {
        return til != null && til.getEditText() != null;
    }

    public static boolean isNotNullNorEmpty(EditText et) {
        return et != null && et.getText() != null;
    }

    public static void setAutoCapWords(CustomEditText... editTexts) {
        if (editTexts != null) {
            if (editTexts.length > 0) {
                for (CustomEditText editText : editTexts) {
                    editText.setAutoCap();
                }
            }
        }
    }

    public static void setAutoCapWords(CustomTextInputLayout... ctil) {
        if (ctil != null) {
            if (ctil.length > 0) {
                for (CustomTextInputLayout mCtil : ctil) {
                    if (mCtil.getEditText() != null) {
                        mCtil.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    }
                }
            }
        }
    }

    public static void setAutoCapSentence(CustomTextInputLayout... ctil) {
        if (ctil != null) {
            if (ctil.length > 0) {
                for (CustomTextInputLayout mCtil : ctil) {
                    if (mCtil.getEditText() != null) {
                        mCtil.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    }
                }
            }
        }
    }

    public static void setTextWatcher(Activity activity, CustomTextInputLayout... ctil) {
        if (activity != null) {
            if (ctil != null && ctil.length > 0) {
                for (CustomTextInputLayout customTextInputLayout : ctil) {
                    if (customTextInputLayout.getEditText() != null) {
                        try {
                            customTextInputLayout.getEditText().addTextChangedListener((TextWatcher) activity);
                        } catch (Exception e) {
                            CustomToastMessage.animRedTextMethod(activity, "you need to implement the interface in host");
                        }
                    }
                }
            }
        }
    }


    public static void setTextWatcher(Fragment fragment, CustomTextInputLayout... ctil) {
        if (fragment != null) {
            if (ctil != null && ctil.length > 0) {
                for (CustomTextInputLayout customTextInputLayout : ctil) {
                    if (customTextInputLayout.getEditText() != null) {
                        try {
                            customTextInputLayout.getEditText().addTextChangedListener((TextWatcher) fragment);
                        } catch (Exception e) {
                            CustomToastMessage.animRedTextMethod(fragment.getActivity(), "you need to implement the interface in host");
                        }
                    }
                }
            }
        }
    }

    public static void setHintColor(Context context, CustomTextInputLayout ctil, boolean hasText, int colorEnabledResId, int colorDisabledResId) {
        /*https://stackoverflow.com/questions/35683379/programmatically-set-textinputlayout-hint-text-color-and-floating-label-color*/
        // sagar : 1/12/18 Getting super class because our class extends original class that contains our reflection interest
        try {
            Field field = ctil.getClass().getSuperclass().getDeclaredField("focusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[0];
            if (hasText) {
                colors = new int[]{
                        ContextCompat.getColor(context, colorEnabledResId)
                };
            } else {
                colors = new int[]{
                        ContextCompat.getColor(context, colorDisabledResId)
                };
            }
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(ctil, myList);

            Method method = ctil.getClass().getSuperclass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(ctil, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disableEditTexts(CustomTextInputLayout... ctils) {
        if (ctils != null && ctils.length > 0) {
            for (CustomTextInputLayout ctil : ctils) {
                if (ctil.getEditText() != null) {
                    disableEditTexts(ctil.getEditText());
                }
            }
        }
    }

    public static void disableEditTexts(EditText... editTexts) {
        if (editTexts != null && editTexts.length > 0) {
            for (EditText editText : editTexts) {
                editText.setClickable(true);
                editText.setFocusable(false);
//                editText.setEnabled(false);
                editText.setCursorVisible(false);
                editText.setKeyListener(null);
                editText.setFocusableInTouchMode(false);
//        editText.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public static void addAsterisk(Context context, CustomTextInputLayout ctil, String baseValue) {
        if (ctil != null) {
            ctil.setHint(String.format("%s%s", baseValue, context.getString(R.string.label_asterisk)));
        }
    }

    public static void appendText(String append, CustomTextInputLayout... ctils) {
        if (ctils != null && ctils.length > 0) {
            if (!StringUtils.isNotNullNotEmpty(append)) {
                append = "";
            }
            for (CustomTextInputLayout ctil : ctils) {
                if (ctil != null && ctil.getEditText() != null) {
                    ctil.getEditText().append(append);
                }
            }
        }
    }

    public static void appendText(String append, EditText... ets) {
        if (ets != null && ets.length > 0) {
            if (!StringUtils.isNotNullNotEmpty(append)) {
                append = "";
            }
            for (EditText et : ets) {
                if (et != null) {
                    et.append(append);
                }
            }
        }
    }

    public static void requestFocus(Context context, EditText editText) {
        if (context != null && editText != null) {
            KeyboardUtils.showSoftInput(editText, context);
        }
    }

    public static void requestFocus(Context context, TextInputLayout til) {
        if (context != null && til != null) {
            KeyboardUtils.showSoftInput(til, context);
        }
    }
}
