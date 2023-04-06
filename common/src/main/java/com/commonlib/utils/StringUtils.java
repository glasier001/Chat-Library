package com.commonlib.utils;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.commonlib.customviews.CustomTextInputLayout;
import com.commonlib.customviews.CustomTextView;
import com.commonlib.typefacehandler.CustomTypefaceSpan;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
    }

    public static String getEmijoByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    public static String replaceAll(String str, String[] patterns, String[] replaceStrings) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < patterns.length; i++) {
            while ((e = str.indexOf(patterns[i], s)) >= 0) {
                result.append(str.substring(s, e));
                result.append(replaceStrings[i]);
                s = e + patterns[i].length();
            }
        }

        result.append(str.substring(s));
        return result.toString();
    }

    public static String getNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static Spanned convertToUpperCase(@NonNull Spanned s) {
        Object[] spans = s.getSpans(0,
                s.length(), Object.class);
        SpannableString spannableString = new SpannableString(s.toString().toUpperCase());

        // reapply the spans to the now uppercase string
        for (Object span : spans) {
            spannableString.setSpan(span,
                    s.getSpanStart(span),
                    s.getSpanEnd(span),
                    0);
        }

        return spannableString;
    }

    public static String setEmojiFlagFromCountryCode(String countryIso) {
        int flagOffset = 0x1F1E6;
        int asciiOffset = 0x41;
        int firstChar = Character.codePointAt(countryIso, 0) - asciiOffset + flagOffset;
        int secondChar = Character.codePointAt(countryIso, 1) - asciiOffset + flagOffset;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }

    public static SpannableString getFormattedString(String partOne, String partTwo, String partThree,
                                                     Typeface fontOne, Typeface fontTwo, Typeface fontThree,
                                                     int colorOne, int colorTwo, int colorThree,
                                                     float sizeOne, float sizeTwo, float sizeThree) {

        SpannableString spannableString = new SpannableString(partOne + partTwo + partThree);

        if (fontOne != null) {
            //Set Typeface
            spannableString.setSpan(new CustomTypefaceSpan("", fontOne), 0, partOne.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (fontTwo != null) {
            spannableString.setSpan(new CustomTypefaceSpan("", fontTwo), partOne.length(), partOne.length() + partTwo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (fontThree != null) {
            spannableString.setSpan(new CustomTypefaceSpan("", fontThree), partTwo.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (sizeOne != 0) {
            //Set font size
            spannableString.setSpan(new RelativeSizeSpan(sizeOne), 0, partOne.length(), 0); // set size
        }
        if (sizeTwo != 0) {
            spannableString.setSpan(new RelativeSizeSpan(sizeTwo), partOne.length(), partOne.length() + partTwo.length(), 0); // set size
        }
        if (sizeThree != 0) {
            spannableString.setSpan(new RelativeSizeSpan(sizeThree), partTwo.length(), spannableString.length(), 0); // set size
        }
        if (colorOne != 0) {
            //Set font color
            spannableString.setSpan(new ForegroundColorSpan(colorOne), 0, partOne.length(), 0);// set color
        }
        if (colorTwo != 0) {
            spannableString.setSpan(new ForegroundColorSpan(colorTwo), partOne.length(), partOne.length() + partTwo.length(), 0);// set color
        }
        if (colorThree != 0) {
            spannableString.setSpan(new ForegroundColorSpan(colorThree), partTwo.length(), spannableString.length(), 0);// set color
        }
        return spannableString;
    }

    public static SpannableString getFormattedString(String partOne, String partTwo, String partThree, String partFour, String partFive,
                                                     Typeface fontOne, Typeface fontTwo, Typeface fontThree, Typeface fontFour, Typeface fontFive,
                                                     int colorOne, int colorTwo, int colorThree, int colorFour, int colorFive,
                                                     float sizeOne, float sizeTwo, float sizeThree, float sizeFour, float sizeFive) {
        SpannableString spannableString = new SpannableString(partOne + partTwo + partThree + partFour + partFive);

        /*Typeface*/
        if (fontOne != null) {
            //Set Typeface
            spannableString.setSpan(new CustomTypefaceSpan("", fontOne), 0, partOne.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (fontTwo != null) {
            spannableString.setSpan(new CustomTypefaceSpan("", fontTwo), partOne.length(), partOne.length() + partTwo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (fontThree != null) {
            spannableString.setSpan(new CustomTypefaceSpan("", fontThree), partTwo.length(), partOne.length() + partTwo.length() + partThree.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (fontFour != null) {
            spannableString.setSpan(new CustomTypefaceSpan("", fontFour), partThree.length(), partOne.length() + partTwo.length() + partThree.length() + partFour.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (fontFive != null) {
            spannableString.setSpan(new CustomTypefaceSpan("", fontFive), partFour.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        /*Size*/
        if (sizeOne != 0) {
            //Set font size
            spannableString.setSpan(new RelativeSizeSpan(sizeOne), 0, partOne.length(), 0); // set size
        }
        if (sizeTwo != 0) {
            spannableString.setSpan(new RelativeSizeSpan(sizeTwo), partOne.length(), partOne.length() + partTwo.length(), 0); // set size
        }
        if (sizeThree != 0) {
            spannableString.setSpan(new RelativeSizeSpan(sizeThree), partTwo.length(), partOne.length() + partTwo.length() + partThree.length(), 0); // set size
        }
        if (sizeFour != 0) {
            spannableString.setSpan(new RelativeSizeSpan(sizeFour), partThree.length(), partOne.length() + partTwo.length() + partThree.length() + partFour.length(), 0); // set size
        }
        if (sizeFive != 0) {
            spannableString.setSpan(new RelativeSizeSpan(sizeFive), partFour.length(), spannableString.length(), 0); // set size
        }

        /*Color*/
        if (colorOne != 0) {
            //Set font color
            spannableString.setSpan(new ForegroundColorSpan(colorOne), 0, partOne.length(), 0);// set color
        }
        if (colorTwo != 0) {
            spannableString.setSpan(new ForegroundColorSpan(colorTwo), partOne.length(), partOne.length() + partTwo.length(), 0);// set color
        }
        if (colorThree != 0) {
            spannableString.setSpan(new ForegroundColorSpan(colorThree), partTwo.length(), partOne.length() + partTwo.length() + partThree.length(), 0);// set color
        }
        if (colorFour != 0) {
            spannableString.setSpan(new ForegroundColorSpan(colorFour), partThree.length(), partOne.length() + partTwo.length() + partThree.length() + partFour.length(), 0);// set color
        }
        if (colorFive != 0) {
            spannableString.setSpan(new ForegroundColorSpan(colorFive), partFour.length(), spannableString.length(), 0);// set color
        }

        return spannableString;
    }

    public static SpannableString setFontSize(String string, float size) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new RelativeSizeSpan(size), 0, string.length(), 0); // set size
        return spannableString;
    }

    public static String getDefaultString(String value, String defaultString) {
        if (value != null && !value.trim().isEmpty() && value.trim().length() > 0 && !"null".equalsIgnoreCase(value)) {
            return value;
        } else {
            return defaultString;
        }
    }

    public static SpannableString setTypeface(String title, Typeface fontOne) {
        SpannableString spannableString = new SpannableString(title);
        //Set Typeface
        spannableString.setSpan(new CustomTypefaceSpan("", fontOne), 0, title.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static String setCapSentence(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void setCurrencyFormat(EditText customTextInputLayout, String currency) {
        if (!ValidationUtil.isEditTextEmpty(customTextInputLayout)) {
            if (!customTextInputLayout.getText().toString().startsWith(currency)) {
                customTextInputLayout.setText(
                        String.format("%s %s", currency,
                                GetFormatedNumber.GetFormatedNumber(Double.valueOf(customTextInputLayout.getText().toString()))
                        ));
            }
        }
    }

    public static void setCurrencyFormat(EditText customTextInputLayout) {
        if (!ValidationUtil.isEditTextEmpty(customTextInputLayout)) {
            customTextInputLayout.setText(
                    GetFormatedNumber.GetFormatedNumber(Double.valueOf(customTextInputLayout.getText().toString()))
            );
        }
    }

    public static void removeCurrencyFormat(EditText customTextInputLayout) {
        if (customTextInputLayout.getText().toString().trim().length() == 0) {
            return;
        }
        customTextInputLayout.setText(String.valueOf((long) Double.parseDouble(customTextInputLayout.getText().toString().replaceAll("[^\\d.]", ""))));
    }

    public static String[] removeCommaSeparation(EditText customTextInputLayout) {
        String[] stringArray = new String[]{};
        if (customTextInputLayout.getText().toString().trim().length() > 0) {
            stringArray = customTextInputLayout.getText().toString().split(", ");
        }
        return stringArray;
    }

    public static List<String> removeCommaSeparations(String string) {

        List<String> stringList = new ArrayList<>();

        if (StringUtils.isNotNullNotEmpty(string)) {
            stringList = Arrays.asList(string.split(", "));
        }

        return stringList;
    }

    public static boolean isNotNullNotEmpty(String s) {
        return s != null && !s.isEmpty() && !s.equalsIgnoreCase("null");
    }

    public static String getCommaSeparatedString(List<String> stringList, boolean addSpace) {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String stringElement : stringList) {
            if (StringUtils.isNotNullNotEmpty(stringElement)) {
                stringBuilder.append(stringElement);
                if (!addSpace) {
                    stringBuilder.append(",");
                } else {
                    stringBuilder.append(", ");
                }
            }
        }

        if (stringBuilder.length() > 0) {
            if (!addSpace) {
                string = stringBuilder.substring(0, stringBuilder.length() - 1);
            } else {
                string = stringBuilder.substring(0, stringBuilder.length() - 2);
            }
        }

        return string;
    }

    public static String getSlashSeparatedString(List<String> stringList) {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String stringElement : stringList) {
            stringBuilder.append(stringElement);
            stringBuilder.append("/");
        }

        if (stringBuilder.length() > 0) {
            string = stringBuilder.substring(0, stringBuilder.length() - 1);
        }

        return string;
    }

    public static String getSeparatedString(List<String> stringList, String separation, int removeLastChars) {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String stringElement : stringList) {
            stringBuilder.append(stringElement);
            stringBuilder.append(separation);
        }

        if (stringBuilder.length() > 0) {
            string = stringBuilder.substring(0, stringBuilder.length() - removeLastChars);
        }

        return string;
    }

    public static String getSeparatedString(List<String> stringList, String separation) {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String stringElement : stringList) {
            stringBuilder.append(stringElement);
            stringBuilder.append(separation);
        }

        if (stringBuilder.length() > 0) {
            int removeLastChars = separation.length();
            string = stringBuilder.substring(0, stringBuilder.length() - removeLastChars);
        }

        return string;
    }

    public static String removeCommaSeparation(String string) {
        String s;
        if (string.trim().length() > 0 && string.contains(",")) {
            s = string.substring(0, string.length());
        } else if (string.trim().length() > 0) {
            s = string;
        } else {
            s = "";
        }
        return s;
    }

    public static String getString(EditText cet) {
        return cet != null && cet.getText().toString().trim().length() > 0 ? cet.getText().toString() : "";
    }

    public static String getString(CustomTextInputLayout ctil) {
        return ctil != null && ctil.getEditText() != null && ctil.getEditText().getText().toString().trim().length() > 0
                ? ctil.getEditText().getText().toString() : "";
    }

    public static String getString(CustomTextView ctv) {
        return ctv != null && ctv.getText().toString().trim().length() > 0 ? ctv.getText().toString() : "";
    }

    public static boolean isNotEqualTo(String base, String value) {
        return !base.equalsIgnoreCase(value);
    }

    public static boolean isEqualTo(String base, String value) {
        return base.equalsIgnoreCase(value);
    }

    public static boolean notContains(String value, String base) {
        return !value.contains(base);
    }

    public static boolean hasAnyNullOrEmptyValue(String... strings) {
        if (strings != null && strings.length > 0) {
            for (String s : strings) {
                if (isNullOrEmpty(s)) {
                    return true;
                }
            }
        } else {
            return true;
        }

        return false;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null");
    }

    public static boolean hasAllNullOrEmptyValue(String... strings) {
        List<String> tempList = new ArrayList<>();
        if (strings != null && strings.length > 0) {
            for (String s : strings) {
                if (isNullOrEmpty(s)) {
                    tempList.add(s);
                }
            }
            return tempList.size() == strings.length;
        } else {
            // sagar : 31/1/19 4:56 PM Returning true because having null or empty varArgs means yes the given arguments are null or empty
            return true;
        }
    }

    public static boolean hasString(EditText v) {
        return v != null && ((EditText) v).getText().toString().length() > 0;
    }

    public static boolean hasString(CustomTextInputLayout ctil) {
        return ctil != null && ctil.getEditText() != null && (ctil.getEditText()).getText().toString().length() > 0;
    }

    public static String getString(String baseString, String oldChar, String newChar, String defaultString) {
        if (StringUtils.isNotNullNotEmpty(baseString)) {
            return baseString.replace(oldChar, newChar);
        }

        return defaultString;
    }

    public static boolean isNotNullNotEmpty(CharSequence cs) {
        return cs != null && !cs.toString().isEmpty() && !cs.toString().equalsIgnoreCase("null");
    }

    public static boolean isNotNullNotEmpty(Character cs) {
        return cs != null && !cs.toString().isEmpty() && !cs.toString().equalsIgnoreCase("null");
    }

    public static String removeExtraDoubleQuotations(String baseString, String defaultReturn) {
        if (StringUtils.isNotNullNotEmpty(baseString)) {
            return baseString.replaceAll("^\"|\"$", "");
        }
        return defaultReturn;
    }

    public static String getString(LinkedTreeMap treeMap, String key, String defaultReturn) {
        if (treeMap != null) {
            if (StringUtils.isNotNullNotEmpty(key)) {
                if (treeMap.containsKey(key)) {
                    return String.valueOf(treeMap.get(key));
                } else {
                    return defaultReturn;
                }
            } else {
                return defaultReturn;
            }
        }
        return defaultReturn;
    }

    public static boolean isEqualToAnyOne(String source, String... targets) {
        if (StringUtils.isNotNullNotEmpty(targets)) {
            if (targets.length > 0) {
                for (String target : targets) {
                    if (target.equalsIgnoreCase(source)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isNotNullNotEmpty(String... strings) {
        if (strings != null && strings.length > 0) {
            List<String> stringList = new ArrayList<>();
            for (String s : strings) {
                if (StringUtils.isNotNullNotEmpty(s)) {
                    stringList.add(s);
                }
            }
            return stringList.size() == strings.length;
        }
        return false;
    }

    public static boolean isNotNullNotEmpty(TextView textView) {
        return textView != null && StringUtils.isNotNullNotEmpty(textView.getText().toString());
    }

    public static boolean isNotNullNotEmpty(TextView... textViews) {

        if (textViews != null && textViews.length > 0) {
            for (TextView textView : textViews) {
                if (textView == null || StringUtils.isNullOrEmpty(textView.getText().toString())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public static boolean isAnyNotNullNotEmpty(TextView... textViews) {
        if (textViews != null && textViews.length > 0) {
            for (TextView textView : textViews) {
                if (StringUtils.isNotNullNotEmpty(textView)) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    public static boolean isAllNotNullNotEmpty(TextView... textViews) {
        if (textViews != null && textViews.length > 0) {
            for (TextView textView : textViews) {
                if (textView == null || StringUtils.isNullOrEmpty(textView.getText().toString())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public static boolean isEqualToAll(String source, String... targets) {
        if (StringUtils.isNotNullNotEmpty(targets)) {
            if (targets.length > 0) {
                List<String> equals = new ArrayList<>();
                for (String target : targets) {
                    if (target.equalsIgnoreCase(source)) {
                        equals.add(target);
                    }
                }
                return equals.size() == targets.length;
            }
        }

        return false;
    }

    public static List<String> getStringList(List<String> list, String elementOne, String elementTwo) {
        List<String> stringList = new ArrayList<>();
        if (Utils.isNotNullNotEmpty(list)) {
            stringList = list;
        }
        if (StringUtils.isNotNullNotEmpty(elementOne, elementTwo)) {
            stringList.add(elementOne);
            stringList.add(elementTwo);
            return stringList;
        }
        return stringList;
    }

    public static boolean isNotNullNotEmpty(Editable s) {
        // sagar : 22/1/19 11:19 AM No need of excessive conditions
        return s != null && s.length() > 0 && !String.valueOf(s).isEmpty() && !s.toString().trim().isEmpty();
    }

    public static boolean isNotNullNotEmpty(EditText v) {
        return v != null && v.getText() != null && ((EditText) v).getText().toString().trim().length() > 0;
    }

    public static String replaceSpecialChars(String source, String replacement) {
        return source.replaceAll("[;\\/:*?\"<>|&']", replacement);
    }
}
