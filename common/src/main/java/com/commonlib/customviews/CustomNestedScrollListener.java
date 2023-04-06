package com.commonlib.customviews;

import android.util.Log;

import com.commonlib.listeners.Callbacks;
import com.commonlib.utils.Utils;

import androidx.core.widget.NestedScrollView;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;

/**
 * Created by sagar on 28/12/17.
 */

public class CustomNestedScrollListener implements NestedScrollView.OnScrollChangeListener, CustomNestedScrollView.NestedScrollViewScrollStateListener {
    private int childHeight;
    private Callbacks.ScrollObserver scrollObserver;

    // com: 10/4/18  sagar: Get value in constructor to be excluded from child according to margin/padding +-...
    public CustomNestedScrollListener(CustomNestedScrollView customNestedScrollView, CustomTextView child) {
        int initialX = (int) child.getX();
        int initialY = (int) child.getY();
        int childWidth = child.getWidth();
        this.childHeight = child.getHeight() - Utils.getIntFromDp(child.getContext(), 12);
        Log.v("cnsl.childHeight ", String.valueOf(child.getHeight()));
        customNestedScrollView.setScrollStateListener(this);
    }

    public void setScrollObserver(Callbacks.ScrollObserver scrollObserver) {
        this.scrollObserver = scrollObserver;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int newY;
        if (scrollY > oldScrollY) {
            Log.v("cnsl.scrollingDown", "Scroll DOWN");
            Log.v("cnsl.scrollY: ", String.valueOf(scrollY));
            Log.v("cnsl.oldScrollY: ", String.valueOf(oldScrollY));
            newY = scrollY;
            screenTitleDisappeared(newY);
        }

        if (scrollY < oldScrollY) {
            Log.v("cnsl.scrollingUp", "Scroll UP");
            Log.v("cnsl.scrollY: ", String.valueOf(scrollY));
            Log.v("cnsl.oldScrollY: ", String.valueOf(oldScrollY));
            newY = scrollY;
            screenTitleAppeared(newY);
        }

        if (scrollY == 0) {
            Log.v("cnsl.topReached", "TOP of SCROLL");
            Log.v("cnsl.scrollY: ", String.valueOf(scrollY));
            Log.v("cnsl.oldScrollY: ", String.valueOf(oldScrollY));
            newY = scrollY;
            screenTitleAppeared(newY);
        }

        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            Log.v("cnsl.bottomReached", "BOTTOM of SCROLL");
            Log.v("cnsl.scrollY: ", String.valueOf(scrollY));
            Log.v("cnsl.oldScrollY: ", String.valueOf(oldScrollY));
            newY = scrollY;
            screenTitleDisappeared(newY);
        }
    }

    private void screenTitleDisappeared(int newY) {
        if (newY >= childHeight) {
            //screen title disappeared
            screenTitleDisappeared();
        } else {
            //screen title appeared
            screenTitleAppeared();
        }
    }

    private void screenTitleAppeared(int newY) {
        if (newY <= childHeight) {
            //screen title appeared
            screenTitleAppeared();
        } else {
            //screen title disappeared
            screenTitleDisappeared();
        }
    }

    private void screenTitleDisappeared() {
        if (scrollObserver != null) {
            Log.v("cnsl.hide: ", "true");
            scrollObserver.onHeaderHide();
        }
    }

    private void screenTitleAppeared() {
        if (scrollObserver != null) {
            Log.v("cnsl.show: ", "true");
            scrollObserver.onHeaderShow();
        }
    }

    @Override
    public void onNestedScrollViewStateChanged(int state, float velocityY) {
        // com: 28/12/17  sagar: set either top or bottom by force to avoid middle state
        Log.v("cnsl.vY: ", String.valueOf(velocityY));
        if (state == SCROLL_STATE_FLING) {
            if (velocityY > 0) {
                screenTitleDisappeared();
            } else {
                screenTitleAppeared();
            }
        }
    }
}

