package com.commonlib.customviews;

/**
 * Created by mauliksantoki on 9/5/17.
 */

public class GradientDrawable {

    public static android.graphics.drawable.GradientDrawable setDrawableBackGround(int bgcolor, int brdcolor, int borderStroke, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {

        android.graphics.drawable.GradientDrawable gdDefault = new android.graphics.drawable.GradientDrawable();
        gdDefault.setColor(bgcolor);
        gdDefault.setStroke(borderStroke, brdcolor);
        gdDefault.setCornerRadii(new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius});

        return gdDefault;
    }
}
