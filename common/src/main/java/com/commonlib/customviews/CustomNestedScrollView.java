package com.commonlib.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;


public class CustomNestedScrollView extends NestedScrollView {

    private int mState = RecyclerView.SCROLL_STATE_IDLE;
    private NestedScrollViewScrollStateListener mScrollListener;
    private int dyConsumed;

    public CustomNestedScrollView(Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollStateListener(NestedScrollViewScrollStateListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    @Override
    public boolean startNestedScroll(int axes) {
        boolean superScroll = super.startNestedScroll(axes);
        Log.v("ns_dragging: ", "dispatched");
        dispatchScrollState(SCROLL_STATE_TOUCH_SCROLL, 0); //dragging
        return superScroll;
    }

    //Bug workaround
    @SuppressLint("RestrictedApi")
    @Override
    public int computeVerticalScrollExtent() {
        Log.v("ns_cverticalExtent: ", String.valueOf(super.computeVerticalScrollExtent()));
        return super.computeVerticalScrollExtent();
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
        Log.v("ns_idle: ", "dispatched");
        dispatchScrollState(SCROLL_STATE_IDLE, 0);
    }

    /**
     * This method determines whether fling or scroll state (motion event) has resulted into Y (or X) travelling or not by dyConsumed.
     *
     *<p> Case Scenario: suppose the content is short enough and not able to show scroll.
     * Now if user executes fling, other method will show data even though there will be no actual travelling.
     * However, from the value of dyConsumed, we can determine whether the travelling has happened or not and send our response according to that.
     *</p>
     * Use {@link #dispatchScrollState(int, float)} to Usage.
     *
     * @param dxConsumed X travelling. Horizontal distance in pixels consumed by this view during this scroll step
     * @param dyConsumed Y travelling. Vertical distance in pixels consumed by this view during this scroll step.
     * @param dxUnconsumed  Horizontal scroll distance in pixels not consumed by this view
     * @param dyUnconsumed  Vertical scroll distance in pixels not consumed by this view
     * @return true if the event was dispatched, false if it could not be dispatched.
     * @see <a href="https://developer.android.com/reference/android/support/v4/view/NestedScrollingChild.html#dispatchNestedScroll(int,%20int,%20int,%20int,%20int[])">Read more about NestesdScrollView</a>
     * @since 1.0
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        Log.v("ns_dns_dyConsumed: ", String.valueOf(dyConsumed));
        Log.v("ns_dns_dyUnConsumed: ", String.valueOf(dyUnconsumed));
        Log.v("ns_dns_offsetInWindow: ", offsetInWindow != null ? String.valueOf(offsetInWindow.length) : "null");
        Log.v("ns_dns_type: ", String.valueOf(type));
        this.dyConsumed = dyConsumed;
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        Log.v("ns_dnps_dyConsumed: ", String.valueOf(dy));
        Log.v("ns_dnps_offset: ", offsetInWindow != null ? String.valueOf(offsetInWindow.length) : "null");
        Log.v("ns_dnps_type: ", String.valueOf(type));
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        boolean superCall = super.dispatchNestedFling(velocityX, velocityY, consumed);
        Log.v("ns_dispatchNfling: ", "dispatched");
        Log.v("ns_dispatchNflingY: ", String.valueOf(velocityY));
        dispatchScrollState(SCROLL_STATE_FLING, velocityY);
        return superCall;
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        boolean superCall = super.dispatchNestedPreFling(velocityX, velocityY);
        Log.v("ns_dnprefling: ", "dispatched");
        Log.v("ns_dnpreflingY: ", String.valueOf(velocityY));
        dispatchScrollState(SCROLL_STATE_FLING, velocityY);
        return superCall;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.v("ns_ons_dyCon: ", String.valueOf(dyConsumed));
        Log.v("ns_ons_dyUnCon: ", String.valueOf(dyUnconsumed));
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    //Bug workaround
    @SuppressLint("RestrictedApi")
    @Override
    public int computeVerticalScrollOffset() {
        Log.v("ns_cVerticalOffset: ", String.valueOf(super.computeVerticalScrollOffset()));
        return super.computeVerticalScrollOffset();
    }

    private void dispatchScrollState(int state, float velocityY) {
        if (mScrollListener != null && mState != state && dyConsumed != 0) {
            mScrollListener.onNestedScrollViewStateChanged(state, velocityY);
            mState = state;
        }
    }

    public interface NestedScrollViewScrollStateListener {
        void onNestedScrollViewStateChanged(int state, float velocityY);
    }
}
