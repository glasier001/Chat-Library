package com.commonlib.customdialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;


public abstract class SimpleBaseDialog extends AppCompatDialog {

    public SimpleBaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setLayout());
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        iniControl();
        initlayouts();
        setListener();

    }

    public abstract int setLayout();

    public abstract void iniControl();

    public abstract void initlayouts();

    public abstract void setListener();
}
