package com.commonlib.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public final class AnimUtils {

    private AnimUtils() {
    }

    private static final String TAG = "animUtils";

    public static List<Float> getCenter(View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        Log.d(TAG, "moveToBesideOf: locationOnScreen: " + location[1] + " " + location[0]);
        List<Float> centerList = new ArrayList<>();
        float parentCenterX = view.getX() + view.getWidth() / 2;
        float parentCenterY = view.getY() + view.getHeight() / 2;
//        centerList.add(0, (float) location[0]);
//        centerList.add(1, (float) location[1]);
        centerList.add(0, parentCenterX);
        centerList.add(1, parentCenterY);
        Log.d(TAG, "getCenter: x: " + centerList.get(0) + " :y: " + centerList.get(1));
        return centerList;
    }

    public static void moveToCenterOf(View referenceView, View... views) {
        for (View clickedView : views) {
            clickedView.animate()
                    .translationX(getCenter(referenceView).get(0) - (clickedView.getX() + (clickedView.getWidth() / 2)))
                    .translationY(getCenter(referenceView).get(1) - (clickedView.getY() + (clickedView.getHeight() / 2)))
                    .setDuration(500)
                    .setInterpolator(new OvershootInterpolator());
        }
    }

    public static void moveToBesideOf(View referenceView, View... views) {
        for (View clickedView : views) {
            clickedView.animate()
                    .y(getCenter(referenceView).get(1) - ((clickedView.getHeight() / 2)))
                    .setDuration(500)
                    .setInterpolator(new AnticipateInterpolator());
        }
    }

    public static void moveToBesideOf(View referenceView, View targetView) {
        Log.d(TAG, "moveToBesideOf: initialY: " + targetView.getY() + " :traversal : " + String.valueOf(getCenter(referenceView).get(1) - targetView.getHeight() / 2));
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.Y, getCenter(referenceView).get(1) - targetView.getHeight() / 2);
        final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(targetView, pvhY);
        animation.setDuration(500);
        animation.setInterpolator(new LinearOutSlowInInterpolator());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            animation.setAutoCancel(true);
        }
        animation.start();
    }
}
