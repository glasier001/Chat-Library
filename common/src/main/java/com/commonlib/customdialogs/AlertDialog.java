package com.commonlib.customdialogs;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.commonlib.R;
import com.commonlib.customviews.CustomBtn;
import com.commonlib.customviews.CustomTextView;
import com.commonlib.utils.CustomToastMessage;
import com.commonlib.utils.Limits;
import com.commonlib.utils.ValidationUtil;

import androidx.annotation.NonNull;

public class AlertDialog extends SimpleBaseDialog {
    public OnClickListener clickListener;
    private CustomTextView ctvTitle;
    private CustomTextView ctvContentMsg;
    private CustomBtn btnLeft;
    private CustomBtn btnRight;
    private View viewSeparatorAlert;
    private View viewSeparatorMsg;
    private View viewSeparatorBtns;
    private String title;
    private String leftBtnText;
    private String rightBtnText;
    private String contentMessage;
    private Activity activity;
    private EditText etInputText;
    private boolean isEditText = false;
    private boolean isValidation = false;
    private String validationMessage = "";
    private Typeface leftBtnTypeface;
    private Typeface rightBtnTypeface;
    private int leftBtnColor;
    private int rightBtnColor;

    public AlertDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public Typeface getLeftBtnTypeface() {
        return leftBtnTypeface;
    }

    public AlertDialog setLeftBtnTypeface(Typeface leftBtnTypeface) {
        this.leftBtnTypeface = leftBtnTypeface;
        return this;
    }

    public Typeface getRightBtnTypeface() {
        return rightBtnTypeface;
    }

    public AlertDialog setRightBtnTypeface(Typeface rightBtnTypeface) {
        this.rightBtnTypeface = rightBtnTypeface;
        return this;
    }

    public int getLeftBtnColor() {
        return leftBtnColor;
    }

    public AlertDialog setLeftBtnColor(int leftBtnColor) {
        this.leftBtnColor = leftBtnColor;
        return this;
    }

    public int getRightBtnColor() {
        return rightBtnColor;
    }

    public AlertDialog setRightBtnColor(int rightBtnColor) {
        this.rightBtnColor = rightBtnColor;
        return this;
    }

    @Override
    public int setLayout() {
        return R.layout.dialog_alert;
    }

    @Override
    public void iniControl() {

    }

    @Override
    public void initlayouts() {
        ctvTitle = findViewById(R.id.ctvTitle);
        ctvContentMsg = findViewById(R.id.ctvContentMsg);
        etInputText = findViewById(R.id.etInputText);
        etInputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Limits.USER_COMMENT)});
        viewSeparatorBtns = findViewById(R.id.viewDividerBtns);
        viewSeparatorAlert = findViewById(R.id.viewDivider);

        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);

        if (title.equalsIgnoreCase("")) {
            ctvTitle.setVisibility(View.GONE);
            viewSeparatorAlert.setVisibility(View.GONE);
        }

        if (leftBtnText.equalsIgnoreCase("")) {
            btnLeft.setVisibility(View.GONE);
            viewSeparatorBtns.setVisibility(View.GONE);
        }

        if (rightBtnText.equalsIgnoreCase("")) {
            btnRight.setVisibility(View.GONE);
            viewSeparatorBtns.setVisibility(View.GONE);
        }

        if (isEditText) {
            ctvContentMsg.setVisibility(View.GONE);
            etInputText.setVisibility(View.VISIBLE);
            etInputText.setHint(validationMessage);
        }

        ctvTitle.setText(title);
        ctvContentMsg.setText(contentMessage);
        btnLeft.setText(leftBtnText);
        btnRight.setText(rightBtnText);

        if (leftBtnTypeface != null) {
            btnLeft.setTypeface(leftBtnTypeface);
        }

        if (rightBtnTypeface != null) {
            btnRight.setTypeface(rightBtnTypeface);
        }

        if (leftBtnColor != 0) {
            btnLeft.setTextColor(leftBtnColor);
        }

        if (rightBtnColor != 0) {
            btnRight.setTextColor(rightBtnColor);
        }
    }

    @Override
    public void setListener() {
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (clickListener != null) {
                    clickListener.onClickLeft(etInputText.getText().toString());
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    if (isValidation) {
                        if (ValidationUtil.isEditTextEmpty(etInputText)) {
                            CustomToastMessage.animRedTextMethod(activity, validationMessage);
                        } else {
                            dismiss();
                            clickListener.onClickRight(etInputText.getText().toString());
                        }
                    } else {
                        dismiss();
                        clickListener.onClickRight(etInputText.getText().toString());
                    }
                }
            }
        });

    }

    public void rempoveEtInputText() {
        this.etInputText.setText("");
    }

    public String getTitle() {
        return title;
    }

    public AlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLeftBtnText() {
        return leftBtnText;
    }

    public AlertDialog setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
        return this;
    }

    public String getRightBtnText() {
        return rightBtnText;
    }

    public AlertDialog setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
        return this;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public AlertDialog setContentMessage(String contantMessage) {
        this.contentMessage = contantMessage;
        return this;
    }

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public AlertDialog setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public boolean isEditText() {
        return isEditText;

    }

    public AlertDialog setEditText(boolean editText, boolean isValidation) {
        isEditText = editText;
        this.isValidation = isValidation;
        return this;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public AlertDialog setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
        return this;
    }

    public interface OnClickListener {
        void onClickLeft(String message);

        void onClickRight(String message);

    }
}
