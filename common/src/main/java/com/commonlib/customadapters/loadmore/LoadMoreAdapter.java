package com.commonlib.customadapters.loadmore;

import android.util.Log;
import android.view.View;

import com.commonlib.utils.KeyboardUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LoadMoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private int firstVisibleInListview;
    private int currentFirstVisible;
    private boolean isLoading;
    private boolean isError;
    private LoaderCallbacks callbacks;
    private RvScrollCallbacks rvScrollCallbacks;
    private int loadingOffset = 0;
    private final RecyclerView.OnScrollListener scrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
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

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        KeyboardUtils.forceCloseKeyboard(recyclerView);
                    }
                    loadNextItemsIfNeeded(recyclerView);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        currentFirstVisible = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    }
                    if (rvScrollCallbacks != null) {
                        if (currentFirstVisible > firstVisibleInListview) {
                            rvScrollCallbacks.onScrollUp();
                        } else {
                            rvScrollCallbacks.onScrollDown();
                        }

                        if (dy > 0) {
                            Log.d("je.lma: ", "rvOnScroll: down");
                            rvScrollCallbacks.onScrollDown();
                        } else if (dy < 0) {
                            Log.d("je.lma: ", "rvOnScroll: up");
                            rvScrollCallbacks.onScrollUp();
                        } else {
                            Log.d("je.lma: ", "rvOnScroll: no vertical");
                        }

                        if (dx > 0) {
                            Log.d("je.lma: ", "rvOnScroll: right");
                            rvScrollCallbacks.onScrollRight();
                        } else if (dx < 0) {
                            Log.d("je.lma: ", "rvOnScroll: left");
                            rvScrollCallbacks.onScrollLeft();
                        } else {
                            Log.d("je.lma: ", "rvOnScroll: no horizontal");
                        }
                    }

                    firstVisibleInListview = currentFirstVisible;
                }
            };

    public void setRvScrollCallbacks(RvScrollCallbacks rvScrollCallbacks) {
        this.rvScrollCallbacks = rvScrollCallbacks;
    }

    final boolean isLoading() {
        return isLoading;
    }

    final boolean isError() {
        return isError;
    }

    public void setCallbacks(LoaderCallbacks callbacks) {
        this.callbacks = callbacks;
        loadNextItems();
    }

    private void loadNextItems() {
        if (!isLoading && !isError && callbacks != null && callbacks.canLoadNextItems()) {
            isLoading = true;
            onLoadingStateChanged();
            callbacks.loadNextItems();
        }
    }

    protected void onLoadingStateChanged() {
        // No-default-op
    }

    public void setLoadingOffset(int loadingOffset) {
        this.loadingOffset = loadingOffset;
    }

    void reloadNextItemsIfError() {
        if (isError) {
            isError = false;
            onLoadingStateChanged();
            loadNextItems();
        }
    }

    public void onNextItemsLoaded() {
        if (isLoading) {
            isLoading = false;
            isError = false;
            onLoadingStateChanged();
        }
    }

    public void onNextItemsError() {
        if (isLoading) {
            isLoading = false;
            isError = true;
            onLoadingStateChanged();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(scrollListener);
        // TODO: sagar : 25/7/18 We can set onItemTouchListener as well here to detect touch and release
        loadNextItemsIfNeeded(recyclerView);
    }

    private void loadNextItemsIfNeeded(RecyclerView recyclerView) {
        if (!isLoading && !isError) {
            View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            int lastVisiblePos = recyclerView.getChildAdapterPosition(lastVisibleChild);
            int total = getItemCount();
            Log.v("lma.lniin: ", String.valueOf(lastVisiblePos));

            if (lastVisiblePos >= total - loadingOffset) {
                // We need to use runnable, since recycler view does not like when we are notifying
                // about changes during scroll callback.
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        loadNextItems();
                    }
                });
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(scrollListener);
    }


    public interface LoaderCallbacks {
        boolean canLoadNextItems();

        void loadNextItems();
    }

    public interface RvScrollCallbacks {
        void onItemTouch();

        void onReleaseTouch();

        void onScrollUp();

        void onScrollDown();

        void onScrollLeft();

        void onScrollRight();

        void onScrollStop();

        void onRvDrag();

        void onRvFling();
    }
}
