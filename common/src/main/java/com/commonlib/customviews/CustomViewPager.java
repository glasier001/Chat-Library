package com.commonlib.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.commonlib.listeners.Callbacks;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    // Stores the starting X position of the ACTION_DOWN event
    float downX;
    private boolean isPagingEnabled = false;
    private Callbacks.OnScrollViewPager onScrollViewPager;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Callbacks.OnScrollViewPager getOnScrollViewPager() {
        return onScrollViewPager;
    }

    public void setOnScrollViewPager(Callbacks.OnScrollViewPager onScrollViewPager) {
        this.onScrollViewPager = onScrollViewPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        sendCallback(event);
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    private void sendCallback(MotionEvent event) {
        // sagar : 23/10/18 https://stackoverflow.com/questions/24721539/swipe-direction-in-viewpager
        if (onScrollViewPager != null) {
            boolean wasSwipedToRight = wasSwipeToRightEvent(event);
            if (wasSwipedToRight) {
                onScrollViewPager.onSwipeRight();
            } else {
                onScrollViewPager.onSwipeLeft();
            }
        }
    }

    /**
     * Checks the X position value of the event and compares it to
     * previous MotionEvents. Returns a true/false value based on if the
     * event was an swipe to the right or a swipe to the left.
     *
     * @param event -   Motion Event triggered by the ViewPager
     * @return -   True if the swipe was from left to right. False otherwise
     */
    private boolean wasSwipeToRightEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                return false;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                return downX - event.getX() > 0;

            default:
                return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sendCallback(event);
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    public void setPagingEnabled(boolean scrollable) {
        this.isPagingEnabled = scrollable;
    }
}