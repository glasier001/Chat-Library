package com.commonlib.utils.behaviors;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dilip on 28/7/17.
 */

public class NestedScrollViewBehavior extends AppBarLayout.Behavior {

    // Lower value means fling action is more easily triggered
    static final int MIN_DY_DELTA = 4;
    // Lower values mean less velocity, higher means higher velocity
    static final int FLING_FACTOR = 20;

    int mTotalDy;
    int mPreviousDy;
    WeakReference<AppBarLayout> mPreScrollChildRef;

    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;
    private boolean isNestedSvReachedTop;
    private Map<RecyclerView, RecyclerViewScrollListener> scrollListenerMap = new HashMap<>(); //keep scroll listener map, the custom scroll listener also keeps the current scroll Y position.
    private Map<NestedScrollView, NestedScrollViewListener> nestedSvListenerMap = new HashMap<>();

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                  View target, int dx, int dy, int[] consumed) {
        Log.e("onNestedPreScroll", "invoked");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        isPositive = dy > 0;
        // Reset the total fling delta distance if the user starts scrolling back up
        if (dy < 0) {
            mTotalDy = 0;
        }
        // Only track move distance if the movement is positive (since the bug is only present
        // in upward flings), equal to the consumed value and the move distance is greater
        // than the minimum difference value
        if (dy > 0 && consumed[1] == dy && MIN_DY_DELTA < Math.abs(mPreviousDy - dy)) {
            mPreScrollChildRef = new WeakReference<>(child);
            mTotalDy += dy * FLING_FACTOR;
        }
        mPreviousDy = dy;
        Log.e("==mPreScrollChildRef", String.valueOf(mPreScrollChildRef));
        Log.e("==mPreviousDy", String.valueOf(mPreviousDy));
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        Log.e("onStartNestedScroll", "invoked");
        // Stop any previous fling animations that may be running
        onNestedFling(parent, child, target, 0, 0, false);
        Log.e("==TO-onNestedFling", "");
        // Scroll starting? onNestedScroll
//        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout parent, AppBarLayout abl, View target) {
        Log.e("onStopNestedScroll", "invoked");
        Log.e("==mPreScrollChildRef", String.valueOf(mPreScrollChildRef));
        if ((mTotalDy > 0 && mPreScrollChildRef != null && mPreScrollChildRef.get() != null)
                ) {
            // Programmatically trigger fling if all conditions are met
            onNestedFling(parent, mPreScrollChildRef.get(), target, 0, mTotalDy, false);
            mTotalDy = 0;
            mPreviousDy = 0;
            mPreScrollChildRef = null;
        } else if (target instanceof NestedScrollView) {
            final NestedScrollView nestedScrollView = (NestedScrollView) target;
            if (nestedSvListenerMap.get(nestedScrollView).isNestedSvTop) {
                onNestedFling(parent, abl, target, 0, mTotalDy, false);
            }
        }
        //Scroll stops
        super.onStopNestedScroll(parent, abl, target);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("onNestedFling", "invoked");
        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
            velocityY = velocityY * -1;
        }
        if (target instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) target;
            if (scrollListenerMap.get(recyclerView) == null) {
                RecyclerViewScrollListener recyclerViewScrollListener = new RecyclerViewScrollListener(coordinatorLayout, child, this);
                scrollListenerMap.put(recyclerView, recyclerViewScrollListener);
                recyclerView.addOnScrollListener(recyclerViewScrollListener);
            }
            scrollListenerMap.get(recyclerView).setVelocity(velocityY);
            consumed = scrollListenerMap.get(recyclerView).getScrolledY() > 0; //recyclerView only consume the fling when it's not scrolled to the top
        } else if (target instanceof NestedScrollView) {
            final NestedScrollView nestedScrollView = (NestedScrollView) target;
            if (nestedSvListenerMap.get(nestedScrollView) == null) {
                NestedScrollViewListener nestedScrollViewListener = new NestedScrollViewListener(coordinatorLayout, child, this);
                nestedSvListenerMap.put(nestedScrollView, nestedScrollViewListener);
                nestedScrollView.setOnScrollChangeListener(nestedScrollViewListener);
                Log.e("==setNsvListener?", "true");
            }
            nestedSvListenerMap.get(nestedScrollView).setVelocity(velocityY * -1);
            Log.e("==nsvVelocity", String.valueOf(velocityY));
            if (nestedSvListenerMap.get(nestedScrollView).isNestedSvTop && velocityY < 0) {
                Log.e("insideCondition", String.valueOf(nestedSvListenerMap.get(nestedScrollView).isNestedSvTop));
                nestedSvListenerMap.get(nestedScrollView).isNestedSvTop = false;
                //onNestedFling(coordinatorLayout, child, target, 0, 0, false);
                return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, false);
            }
        } else if (target instanceof NestedScrollingChild) {
            consumed = false;
        }
        Log.e("===consumed===", String.valueOf(consumed) + "==velocityY==" + String.valueOf(velocityY)
                + "==target" + String.valueOf(target));
        //fling started
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private static class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private int scrolledY;
        private boolean dragging;
        private float velocity;
        private WeakReference<CoordinatorLayout> coordinatorLayoutRef;
        private WeakReference<AppBarLayout> childRef;
        private WeakReference<NestedScrollViewBehavior> behaviorWeakReference;

        public RecyclerViewScrollListener(CoordinatorLayout coordinatorLayout, AppBarLayout child, NestedScrollViewBehavior barBehavior) {
            coordinatorLayoutRef = new WeakReference<CoordinatorLayout>(coordinatorLayout);
            childRef = new WeakReference<AppBarLayout>(child);
            behaviorWeakReference = new WeakReference<NestedScrollViewBehavior>(barBehavior);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            dragging = newState == RecyclerView.SCROLL_STATE_DRAGGING;
        }

        public void setVelocity(float velocity) {
            this.velocity = velocity;
        }

        public int getScrolledY() {
            return scrolledY;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.e("onScrolled", "invoked");
            scrolledY += dy;
            if (scrolledY <= 0 && !dragging && childRef.get() != null && coordinatorLayoutRef.get() != null && behaviorWeakReference.get() != null) {
                //manually trigger the fling when it's scrolled at the top
                behaviorWeakReference.get().onNestedFling(coordinatorLayoutRef.get(), childRef.get(), recyclerView, 0, velocity, false);
            }
        }
    }

    private static class NestedScrollViewListener implements NestedScrollView.OnScrollChangeListener {
        private boolean isNestedSvTop;
        private WeakReference<CoordinatorLayout> coordinatorLayoutRef;
        private WeakReference<AppBarLayout> childRef;
        private WeakReference<NestedScrollViewBehavior> behaviorWeakReference;
        private float nsvVelocity;

        public NestedScrollViewListener(CoordinatorLayout coordinatorLayout, AppBarLayout child, NestedScrollViewBehavior barBehavior) {
            coordinatorLayoutRef = new WeakReference<CoordinatorLayout>(coordinatorLayout);
            childRef = new WeakReference<AppBarLayout>(child);
            behaviorWeakReference = new WeakReference<NestedScrollViewBehavior>(barBehavior);
        }

        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollY > oldScrollY) {
                Log.e("scrollingDown", "Scroll DOWN");
                Log.e("scrollY: ", String.valueOf(scrollY));
                Log.e("oldScrollY: ", String.valueOf(oldScrollY));
            }
            if (scrollY < oldScrollY) {
                Log.e("scrollingUp", "Scroll UP");
                Log.e("scrollY: ", String.valueOf(scrollY));
                Log.e("oldScrollY: ", String.valueOf(oldScrollY));
            }

            if (scrollY == 0) {
                Log.e("topReached", "TOP of SCROLL");
                Log.e("scrollY: ", String.valueOf(scrollY));
                Log.e("oldScrollY: ", String.valueOf(oldScrollY));
                isNestedSvTop = true;
                if (coordinatorLayoutRef != null && childRef != null && behaviorWeakReference != null) {
                    Log.e("insideTop", "");
                    /*TODO: Get and transfer velocity*/
                    behaviorWeakReference.get().onNestedFling(coordinatorLayoutRef.get(),
                            childRef.get(), v, 0, -2000, false);
                }
//                        nestedScrollViewCallback.isReachedTop(true);
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                Log.e("bottomReached", "BOTTOM SCROLL");
                Log.e("scrollY: ", String.valueOf(scrollY));
                Log.e("oldScrollY: ", String.valueOf(oldScrollY));
            }
        }

        public void setVelocity(float velocity) {
            this.nsvVelocity = velocity;
        }
    }
}

