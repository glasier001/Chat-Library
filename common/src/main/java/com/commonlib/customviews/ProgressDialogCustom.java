package com.commonlib.customviews;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.commonlib.R;

public class ProgressDialogCustom extends ProgressDialog {
    public ProgressDialogCustom(Context context) {
        super(context, getStyle());
        setMessage(context.getString(R.string.label_please_wait));
        setCancelable(false);
    }

    private static int getStyle() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            return R.style.AppAlertTheme;
        } else {
            return R.style.AppAlertTheme19;
        }
    }
}
