package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.commonlib.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.commonlib.utils.ViewUtils.showErrorMessage;

public final class ValidationUtil {

    public static final double BYTE = 1024, KB = BYTE, MB = KB * BYTE, GB = MB * BYTE;

    ;
    public static Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    public static FragmentActivity context;

    public static DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
    static boolean isException = false;

    private ValidationUtil() {
    }

    public ValidationUtil(Context context) {
        ValidationUtil.context = (FragmentActivity) context;
    }

    public static String convertBytesToSuitableUnit(long bytes) {
        String bytesToSuitableUnit = bytes + " B";
        if (bytes >= GB) {
            double tempBytes = bytes / GB;
            bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " GB";
            return bytesToSuitableUnit;
        }
        if (bytes >= MB) {
            double tempBytes = bytes / MB;
            bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " MB";
            return bytesToSuitableUnit;
        }
        if (bytes >= KB) {
            double tempBytes = bytes / KB;
            bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " kB";
            return bytesToSuitableUnit;
        }
        return bytesToSuitableUnit;
    }


    public static boolean isNullValue(String value) {
        boolean isNull = false;
        if (value == null) {
            isNull = true;
        } else if (value.trim().equalsIgnoreCase("")) {
            isNull = true;
        }
        return isNull;
    }

    public static int isInt(String txt) {
        if (txt != null && txt.length() > 0) {
            return Integer.parseInt(txt);
        }
        return 0;
    }

    public static boolean isEmpty(String text) {
        text = text.trim();
        return text.isEmpty();
    }


    public static boolean isFilled(EditText editText) {
        String text = editText.getText().toString();
        if (text.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEditTextEmpty(EditText editText) {
        String text = editText.getText().toString();
        text = text.trim();
        return text.isEmpty();
    }

    public static double isdouble(String txt) {
        if (txt != null && txt.length() > 0) {
            return Double.parseDouble(txt);
        }
        return 0.0;
    }

    public static String isString(String txt) {
        if (txt != null && txt.length() > 0) {
            return txt;
        }
        return "";
    }

    public static boolean isRequiredField(String strText) {
        return strText != null && !strText.trim().isEmpty();
    }

    public static boolean validEmail(EditText editText) {
        Matcher matcher = p.matcher(editText.getText().toString());
        if (editText.getText().length() != 0) {
            return matcher.matches();
        } else {
            return false;
        }
    }

    public static boolean isValidWebsite(CharSequence Website) {
        return Patterns.WEB_URL.matcher(Website).matches();
    }

    public static boolean isValidatePhoneNumberSize(String test) {
        return test.length() >= Limits.PHONENUMBER_LENGTH_MIM_LIMIT;
    }

    public static boolean isLongPhone(String s) {
        return StringUtils.isNotNullNotEmpty(s) && s.length() > Limits.PHONE_NUMBER_TXT_LIMIT;
    }

    public static boolean isPhoneNumber(String phoneNo) {
        return Patterns.PHONE.matcher(phoneNo).matches();
    }

    public static boolean isValidEmailOrPhone(Activity activity, EditText et) {
        String sUser_email_phone = et.getText().toString();

        if (!(Patterns.EMAIL_ADDRESS.matcher(sUser_email_phone).matches()) &&
                !(Patterns.PHONE.matcher(sUser_email_phone).matches())) {
            requestFocus(activity, et);//TODO:Removing may create problem while configuration change (screen rotation)
            return false;

        } else if (Patterns.EMAIL_ADDRESS.matcher(sUser_email_phone).matches() || Patterns.PHONE.matcher(sUser_email_phone).matches()) {
            et.setError(null);
        }
        return true;
    }

    private static void requestFocus(Activity activity, View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphabetic(String strText) {
        return strText.matches("[a-zA-Z ]+");
    }

    public static boolean isMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static float pxFromDp(final float dp) {
        return dp * context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static List<String[]> getLatLongFromZipCode(String zipcode) {

        List<String[]> locations = new ArrayList<String[]>();
        final Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 20);
            if (addresses != null && !addresses.isEmpty()) {
                for (Address address : addresses) {
                    locations.add(new String[]{"" + address.getLatitude(), "" + address.getLongitude()});
                }
            } else {
                // Display appropriate message when Geocoder services are not
                // available
                // toast("Unable to geocode zipcode");
            }
        } catch (IOException e) {
            // handle exception
        }
        return locations;
    }

    public static boolean playVideo(String videopath, Activity activity, VideoView video_challenge_preview) {
        try {
            DisplayMetrics dm;
            MediaController media_Controller;
            media_Controller = new MediaController(activity);
            dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int height = dm.heightPixels;
            int width = dm.widthPixels;
            video_challenge_preview.setMinimumWidth(width);
            video_challenge_preview.setMinimumHeight(height);
            video_challenge_preview.setMediaController(media_Controller);
            video_challenge_preview.setVideoPath(videopath);
            video_challenge_preview.start();
            video_challenge_preview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    isException = true;
                    System.out.println("isException in OnErrorlistener" + isException);
                    return true;
                }
            });
            return isException;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void loadJustifieddata(WebView view, String data) {
        String youtContentStr = String.valueOf(Html.fromHtml("<![CDATA[<body style=\"text-align:justify;color:black;background-color:white; \">" + "" + data + "</body>]]>"));
        view.loadData(youtContentStr, "text/html", "utf-8");
    }

    public static void loadCentrifieddata(WebView view, String data) {
        String youtContentStr = String.valueOf(Html.fromHtml("<![CDATA[<body style=\"text-align:center;color:black;background-color:white; \">" + "" + data + "</body>]]>"));
        view.loadData(youtContentStr, "text/html", "utf-8");
    }

    public static String getDurationMark(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
        } catch (Exception e) {
//            Log.e("getDurationMark", e.toString());
            return "?:??";
        }
        String time = null;

        //fix for the gallery picker crash
        // if it couldn't detect the media file
        try {
//            Log.e("file", filePath);
            time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            Log.e("getDurationMark", ex.toString());
        }

        //fix for the gallery picker crash
        // if it couldn't extractMetadata() of a media file
        //time was null
        time = time == null ? "0" : time.isEmpty() ? "0" : time;
        //bam crash - no more :)
        int timeInMillis = Integer.parseInt(time);
        int duration = timeInMillis / 1000;
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append(":");
        }
        if (minutes < 10) {
            sb.append("0").append(minutes);
        } else {
            sb.append(minutes);
        }
        sb.append(":");
        if (seconds < 10) {
            sb.append("0").append(seconds);
        } else {
            sb.append(seconds);
        }
        return sb.toString();
    }

    public static boolean isValidCountryCode(Activity activity, EditText et) {
        String s = et.getText().toString();

        if (s.trim().equals("") || s.length() > 5 || s.length() == 1) {
            requestFocus(activity, et);
            return false;

        } else {
            et.setError(null);
        }
        return true;
    }

    public static boolean isLongCountryCode(String countryCode) {
        return countryCode != null && countryCode.length() > 5;
    }

    public static String replaceSpecialCharacters(String serverMsg) {
        char lastchar = 0;
        String local = serverMsg.replaceAll("[ ./,()']+", "_");
        while (local.contains("__")) {
            local = local.replace("__", "_");
        }
        if (local.length() > 1) {
            lastchar = local.charAt(local.length() - 1);
        }
        if (String.valueOf(lastchar).equals("_")) {
            local = local.substring(0, local.length() - 1);
        }
        return local;
    }

    /*TODO: following method assumes that "+" sign is placed by force automatically and is not removable*/

    public static String checkUrlStartWith(EditText editText) {
        if (editText.getText().toString().trim().length() > 0 && !editText.getText().toString().trim().startsWith("http://") && !editText.getText().toString().trim().startsWith("https://"))
            return ("http://" + editText.getText().toString().trim());
        return editText.getText().toString().trim();
    }

    public static boolean viewStubValidation(Fragment selectedFragment, Fragment visibleFragmet) {
        return ((selectedFragment.getTag()).equalsIgnoreCase(visibleFragmet.getTag()));
    }

    public static boolean isValidPassword(String password, String confirmPassword) {
        return password != null && confirmPassword != null && isValidPassword(password) && password.equals(confirmPassword);
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() && password.length() >= Limits.PASSWORD_LENGTH_MIM_LIMIT && password.length() <= Limits.PASSWORD_LENGTH_MAX_LIMIT;
    }

    public static boolean isSamePassword(String oldPassword, String newPassword) {
        return StringUtils.isNotNullNotEmpty(oldPassword) && oldPassword.trim().equalsIgnoreCase(newPassword.trim());
    }

    public static boolean isValidCountryCodePhone(Activity activity, TextInputLayout tilCountryCode, TextInputLayout tilEmailPhone) {
        if (tilCountryCode.getEditText() != null && tilEmailPhone.getEditText() != null) {

            EditText etCountryCode = tilCountryCode.getEditText();
            EditText etEmailPhone = tilEmailPhone.getEditText();

            return isValidCountryCodePhone(activity, etCountryCode, etEmailPhone);
        } else {
            return false;
        }
    }

    public static boolean isValidCountryCodePhone(Activity activity, EditText etCountryCode, EditText etPhone) {
        if (etCountryCode.getText().length() <= 1) {
            showErrorMessage(activity, activity.getString(R.string.error_empty_country_code));
            return false;
        } else if (!ValidationUtil.isValidCountryCode(EditTextUtils.getString(etCountryCode))) {
            showErrorMessage(activity, activity.getString(R.string.error_invalid_country_code));
            return false;
        } else if (!ValidationUtil.isValidPhoneNumber(EditTextUtils.getString(etPhone))) {
            showErrorMessage(activity, activity.getString(R.string.error_invalid_phone).replace("####", String.valueOf(Limits.PHONENUMBER_LENGTH_MIM_LIMIT)));
            return false;
        } else if (ValidationUtil.isSmallPhone(EditTextUtils.getString(etPhone))) {
            showErrorMessage(activity, activity.getString(R.string.error_small_phone).replace("####", String.valueOf(Limits.PHONENUMBER_LENGTH_MIM_LIMIT)));
            return false;
        }

        return true;
    }

    public static boolean isValidCountryCode(String countryCode) {
        return countryCode != null && !countryCode.isEmpty() && countryCode.length() > 1 && countryCode.length() < 5;
    }

    public static boolean isValidPhoneNumber(CharSequence number) {
        if (number.toString().matches("[0-9]+")) {
            return true;
        }
        return false;
    }

    public static boolean isSmallPhone(String s) {
        return StringUtils.isNotNullNotEmpty(s) && s.length() < Limits.PHONENUMBER_LENGTH_MIM_LIMIT;
    }

    public static boolean isValidEmailPhone(Activity activity, TextInputLayout tilCountryCode, TextInputLayout tilEmailPhone) {
        if (tilCountryCode.getEditText() != null && tilEmailPhone.getEditText() != null) {

            EditText etCountryCode = tilCountryCode.getEditText();
            EditText etEmailPhone = tilEmailPhone.getEditText();

            return isValidEmailPhone(activity, etCountryCode, etEmailPhone);
        } else {
            return false;
        }
    }

    public static boolean isValidEmailPhone(Activity activity, EditText etCountryCode, EditText etEmailPhone) {
        if (!EditTextUtils.hasText(etEmailPhone)) {
            showErrorMessage(activity, activity.getString(R.string.error_empty_email_phone));
            return false;
        } else if (EditTextUtils.hasText(etEmailPhone) && ValidationUtil.isDigit(EditTextUtils.getString(etEmailPhone))) {
            return isValidCountryCodePhone(activity, etCountryCode, etEmailPhone);
        } else if (etCountryCode.getVisibility() == View.VISIBLE
                && !EditTextUtils.hasText(etEmailPhone)) {
            showErrorMessage(activity, activity.getString(R.string.error_empty_phone));
            return false;
        } else if (EditTextUtils.hasText(etEmailPhone)
                && !ValidationUtil.isValidEmail(EditTextUtils.getString(etEmailPhone))) {
            showErrorMessage(activity, activity.getString(R.string.error_invalid_email));
        }
        return true;
    }

    public static boolean isDigit(String string) {
        if (string.matches("\\d+(?:\\.\\d+)?")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(CharSequence email) {

        Pattern EMAIL_ADDRESS = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,3}" +
                        ")+"
        );


        return EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPinCode(String pincode) {
        if (StringUtils.isNotNullNotEmpty(pincode)) {
            return pincode.length() == Limits.PINCODE;
        }
        return false;
    }

    public static boolean isValidLandLineNumber(Activity activity, String number, boolean isCompulsory) {

        if (isCompulsory && StringUtils.isNullOrEmpty(number)) {
            showErrorMessage(activity, activity.getString(R.string.error_please_enter_landline_number));
        } else if (StringUtils.isNotNullNotEmpty(number)) {
            if (!isValidPhoneNumber(number)) {
                CustomToastMessage.animRedTextMethod(activity, activity.getString(R.string.error_please_enter_valid_landline_number));
                return false;
            } else if (number.trim().length() < Limits.LANDLINE_MIN_LIMIT){
                showErrorMessage(activity, activity.getString(R.string.error_landline_number_cannot_be_less_than_digits).replace("#", String.valueOf(Limits.LANDLINE_MIN_LIMIT)));
                return false;
            } else if (number.trim().length() > Limits.LANDLINE_MAX_LIMIT){
                showErrorMessage(activity, activity.getString(R.string.error_landline_number_cannot_be_more_than_digits).replace("#", String.valueOf(Limits.LANDLINE_MAX_LIMIT)));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}


