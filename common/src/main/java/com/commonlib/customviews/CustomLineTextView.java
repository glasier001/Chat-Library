package com.commonlib.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.commonlib.R;

import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * Created by mauliksantoki on 23/8/17.
 */

public class CustomLineTextView extends ConstraintLayout {

    private float textSize = 10;
    private String text = "";
    public CustomTextView textView;

    public CustomLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intt(attrs);
    }


    public CustomLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intt(attrs);
    }


    private void intt(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomLineTextView,
                0, 0);

        textSize = a.getDimension(R.styleable.CustomLineTextView_txttextSize, 10);
        text = a.getString(R.styleable.CustomLineTextView_txttext);


        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_line_textview, this, true);

        textView = view.findViewById(R.id.textView);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }


}
