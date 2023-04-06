package com.commonlib.customadapters.loadmore;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.commonlib.listeners.Callbacks;
import com.commonlib.utils.KeyboardUtils;

import static android.util.Log.d;
import static com.commonlib.constants.AppConstants.TAG;


public abstract class RecyclerViewLoadMoreScroll extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity activity;
    private Callbacks.RvScrollCallbacks rvScrollCallbacks;
    private int firstVisibleInListview;
    private int currentFirstVisible;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;
    private boolean listenKeyboard = true;

    public RecyclerViewLoadMoreScroll(FragmentActivity activity, LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.activity = activity;
    }

    public RecyclerViewLoadMoreScroll(FragmentActivity activity, GridLayoutManager layoutManager) {
        this.activity = activity;
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public RecyclerViewLoadMoreScroll(FragmentActivity activity, StaggeredGridLayoutManager layoutManager) {
        this.activity = activity;
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public void setRvScrollCallbacks(Callbacks.RvScrollCallbacks rvScrollCallbacks) {
        this.rvScrollCallbacks = rvScrollCallbacks;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean getLoaded() {
        return isLoading;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (rvScrollCallbacks != null) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                rvScrollCallbacks.onRvDrag();
            } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                rvScrollCallbacks.onRvFling();
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                rvScrollCallbacks.onScrollStop();
            }
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // sagar : 20/12/18 Why do we do return from here?
        // sagar : 22/1/19 6:27 PM Because we don't want our keyboard get closed even if user is not scrolling!
        // sagar : 22/1/19 6:28 PM Useful when we have edit text above (jobsAdBoard, pasupal - Address Pop-up dialog)
        if (rvScrollCallbacks == null) {
            if (dy <= 0) return;
        }

        // TODO: sagar : 20/12/18 Considering the fragment or dialog, Have a view alternative to activity to hide keyboard
        if (listenKeyboard) {
            // KeyboardUtils.hideSoftInput(activity);
            KeyboardUtils.forceCloseKeyboard(recyclerView);
        }


        if (rvScrollCallbacks != null) {
            rvScrollCallbacks.onScroll(recyclerView, dx, dy);
        }


        int offset = recyclerView.computeVerticalScrollOffset();
        d(TAG, "RecyclerViewLoadMoreScroll: getPercentageScrolling: offset: " + offset);
        int extent = recyclerView.computeVerticalScrollExtent();
        d(TAG, "RecyclerViewLoadMoreScroll: getPercentageScrolling: extent: " + extent);
        int range = recyclerView.computeVerticalScrollRange();
        d(TAG, "RecyclerViewLoadMoreScroll: getPercentageScrolling: range: " + range);

        d(TAG, "RecyclerViewLoadMoreScroll: getPercentageScrolling: percentage: " + ((100.0f * offset / (float) (range - extent))));
        if (rvScrollCallbacks != null) {
            rvScrollCallbacks.onGetPercentageScroll(recyclerView, 100.0f * offset / (float) (range - extent));
        }

        // sagar : 20/12/18 Total items currently in listing
        totalItemCount = mLayoutManager.getItemCount();

        // sagar : 20/12/18 Get last visible item
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItem = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        // sagar : 20/12/18 Considering the case when the list is reset
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        d(TAG, "RecyclerViewLoadMoreScroll: onScrolled: isLoading: " + isLoading
                + " :totalItemCount: " + totalItemCount + " :lastVisibleItem: " + lastVisibleItem + " :visibleThresold: " + visibleThreshold);
        d(TAG, "RecyclerViewLoadMoreScroll: onScrolled: isLoadMore: " + (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)));

        // sagar : 5/4/19 5:49 PM For any load more related issue, uncomment below boolean check and call setLoaded(true) method of this class at proper time
        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            currentPage++;
            if (mOnLoadMoreListener != null) {
                d(TAG, "RecyclerViewLoadMoreScroll: onScrolled: calling onLoadMore");
                mOnLoadMoreListener.onLoadMore();
            }
            onLoadMore(currentPage, totalItemCount, recyclerView);
            // sagar : 6/4/19 12:42 PM Don't know why we are making it true again
            isLoading = true;
        }

        if (mLayoutManager instanceof LinearLayoutManager) {
            currentFirstVisible = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            d(TAG, "RecyclerViewLoadMoreScroll: onScrolled: currentFirstVisible: " + currentFirstVisible + " :pos: " + ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition());

            // To check if at the top of recycler view
            if (((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {

                d(TAG, "RecyclerViewLoadMoreScroll: onScrolled: onTop");

                // It is on top
                if (rvScrollCallbacks != null) {
                    rvScrollCallbacks.onTop();
                }
            }

            // To check if at the bottom of recycler view
            if (((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition() == totalItemCount - 1) {
                // It is at bottom
                if (rvScrollCallbacks != null) {
                    rvScrollCallbacks.onBottom();
                }
            }
        }

        if (rvScrollCallbacks != null) {
            if (currentFirstVisible > firstVisibleInListview) {
                rvScrollCallbacks.onScrollUp();
            } else {
                rvScrollCallbacks.onScrollDown();
            }

            d(TAG, "RecyclerViewLoadMoreScroll: onScrolled: dy: " + dy + " dx: " + dx);

            if (dy > 0) {
                d("je.lma: ", "rvOnScroll: down");
                rvScrollCallbacks.onScrollDown();
            } else if (dy < 0) {
                d("je.lma: ", "rvOnScroll: up");
                rvScrollCallbacks.onScrollUp();
            } else {
                d("je.lma: ", "rvOnScroll: no vertical");
            }

            if (dx > 0) {
                d("je.lma: ", "rvOnScroll: right");
                rvScrollCallbacks.onScrollRight();
            } else if (dx < 0) {
                d("je.lma: ", "rvOnScroll: left");
                rvScrollCallbacks.onScrollLeft();
            } else {
                d("je.lma: ", "rvOnScroll: no horizontal");
            }
        }
        firstVisibleInListview = currentFirstVisible;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    private boolean isFirstItemCompletelyVisible(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            return getFirstVisibleItem(positions) == 0;
        } else if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0;
        }
        return false;
    }

    private int getFirstVisibleItem(int[] firstVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < firstVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = firstVisibleItemPositions[i];
            } else if (firstVisibleItemPositions[i] > maxSize) {
                maxSize = firstVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // Call this method whenever performing new searches
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    public void setKeyboardListener(boolean listenKeyboard) {
        this.listenKeyboard = listenKeyboard;
    }
}