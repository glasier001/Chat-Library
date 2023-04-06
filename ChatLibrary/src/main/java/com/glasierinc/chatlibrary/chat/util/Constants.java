package com.glasierinc.chatlibrary.chat.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    public static String fcm_token = "fcm_token";
    public static String unique_id = "unique_id";
    public static String userNameKey = "user_name";
    public static String userImage = "user_image";
    public static String passwordKey = "password";
    public static String userIdKey = "userId";
    public static String testTitle = "testTitle";
    public static String testTime = "testTime";
    public static String audioLink = "audioLink";
    public static String quizId = "quizId";
    public static String supportId = "supportId";
    public static String cbtTips = "cbtTips";
    public static String termsAndConditions = "termsAndConditions";
    public static String privacyPolicy= "privacyPolicy:";

    public static boolean isInternetAvailable(Context context) {
        try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                return cm.getActiveNetworkInfo() != null;

        } catch (Exception e) {
            return false;
        }
    }
    //hide keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        //  String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String getVideoId(@NonNull String videoUrl) {
        String videoId = "";
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if(matcher.find()){
            videoId = matcher.group(1);
        }
        return videoId;
    }


}
