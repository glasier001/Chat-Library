package com.commonlib.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.commonlib.R;


public class CustomRoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    private RectF rect;
    private Path path = new Path();
    private float DEFAULT_RADIUS = 20f;
    private float radius = DEFAULT_RADIUS;
    private final ScaleType scaleType = ScaleType.CENTER_CROP;

    public CustomRoundImageView(Context context) {
        super(context);
        setScaleType(scaleType);
        init();
    }

    public CustomRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setScaleType(scaleType);
        init();
    }

    public CustomRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(scaleType);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundImageView, defStyleAttr, 0);
        radius = a.getFloat(R.styleable.CustomRoundImageView_rounded_radius, DEFAULT_RADIUS);
        init();
    }

    public int roundRadius() {
        return (int) radius;
    }

    public void setRoundedRadius(int radius) {
        this.radius = radius;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    private void init() {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CCW);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }
}
