package com.commonlib.utils;

import android.app.Activity;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.R;
import com.commonlib.customviews.CustomImageView;
import com.commonlib.customviews.CustomTextView;
import com.commonlib.typefacehandler.TypefaceUtils;

import androidx.core.content.ContextCompat;


/**
 * Created by root on 16/5/16.
 */
public final class CustomToastMessage {

    static final int LONG_DURATION_TIMEOUT = 1000;
    private static boolean showRedMessage = true;
    private static boolean showGreenMessage = true;
    private static boolean exitToast = true;
    private static SpannableStringBuilder spannableStringBuilder;

    private CustomToastMessage() {
    }

    public static void animRedTextMethod(Activity activity, final String text) {
        if (!text.isEmpty()) {
            spannableStringBuilder = new SpannableStringBuilder();
            if (!showRedMessage) {
                return;
            }
//        TypefaceUtils typefaceUtils = new TypefaceUtils(context);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showRedMessage = true;
                }
            }, LONG_DURATION_TIMEOUT);


            LayoutInflater inflater = activity.getLayoutInflater();

            View customToastroot = inflater.inflate(R.layout.custom_toast_layout, null);

            CustomImageView civIcon = (CustomImageView) customToastroot.findViewById(R.id.civ_icon);
            CustomTextView ctvTitle = (CustomTextView) customToastroot.findViewById(R.id.ctv_title);
            CustomTextView ctvText = (CustomTextView) customToastroot.findViewById(R.id.ctv_text);

            civIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_toast_cross));
            ctvTitle.setText(activity.getString(R.string.label_error));
            ctvText.setText(text);

            Toast customtoast = new Toast(activity);
            customtoast.setView(customToastroot);
            customtoast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
            customtoast.setDuration(Toast.LENGTH_SHORT);

            customtoast.show();

            showRedMessage = false;
        /*Alerter.create(activity)
                .setTitle("Error")
                .setText(text)
                .setIcon(ContextCompat.getDrawable(activity,R.drawable.ic_toast_cross))
                .setIconColorFilter(0) // Optional - Removes white tint
                .setBackgroundColorInt(Color.BLACK)
                .setTextTypeface(TypefaceUtils.getInstance().getRegularTypeface(activity))
                .setTitleTypeface(TypefaceUtils.getInstance().getBoldTypeface(activity))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alerter.hide();
                    }
                })
                .show();*/
        }
    }

    public static void animGreenTextMethod(Activity activity, final String text) {
        if (!text.isEmpty()) {
            if (!showGreenMessage) {
                return;
            }
//        TypefaceUtils typefaceUtils = new TypefaceUtils(context);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGreenMessage = true;
                }
            }, LONG_DURATION_TIMEOUT);

            LayoutInflater inflater = activity.getLayoutInflater();

            View customToastroot = inflater.inflate(R.layout.custom_toast_layout, null);

            CustomImageView civIcon = (CustomImageView) customToastroot.findViewById(R.id.civ_icon);
            CustomTextView ctvTitle = (CustomTextView) customToastroot.findViewById(R.id.ctv_title);
            CustomTextView ctvText = (CustomTextView) customToastroot.findViewById(R.id.ctv_text);

            civIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_toast_correct));
            ctvTitle.setText(activity.getString(R.string.lable_success));
            ctvText.setText(text);
            Toast customtoast = new Toast(activity);

            customtoast.setView(customToastroot);
            customtoast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
            customtoast.setDuration(Toast.LENGTH_SHORT);
            customtoast.show();

            showGreenMessage = false;

       /* Alerter.create(activity)
                .setTitle("Success")
                .setText(text)
                .setIcon(ContextCompat.getDrawable(activity,R.drawable.ic_toast_correct))
                .setIconColorFilter(0) // Optional - Removes white tint
                .setBackgroundColorInt(Color.BLACK)
                .setTextTypeface(TypefaceUtils.getInstance().getRegularTypeface(activity))
                .setTitleTypeface(TypefaceUtils.getInstance().getBoldTypeface(activity))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alerter.hide();
                    }
                })
                .show();*/
        }
    }

    public static void exitToast(Activity context, final String message) {
        if (!exitToast) {
            return;
        }
//        TypefaceUtils typefaceUtils = new TypefaceUtils(context);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitToast = true;
            }
        }, LONG_DURATION_TIMEOUT);

        LayoutInflater inflater = context.getLayoutInflater();

        View customToastroot = inflater.inflate(R.layout.custom_exit_toast, null);
        TextView toast_txtvw = (TextView) customToastroot.findViewById(R.id.toast_txtvw);
        toast_txtvw.setGravity(Gravity.CENTER_HORIZONTAL);
        toast_txtvw.setText(message);
        toast_txtvw.setTypeface(TypefaceUtils.getInstance().getRegularTypeface(context));
        Toast customtoast = new Toast(context);
        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 5);
        customtoast.setMargin(0, 0);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();

        exitToast = false;
    }
}
