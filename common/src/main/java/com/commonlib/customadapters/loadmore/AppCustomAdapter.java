package com.commonlib.customadapters.loadmore;

import java.util.List;


/**
 * Created by mauliksantoki on 30/8/17.
 */

public abstract class AppCustomAdapter extends LoadMoreAdapter {

    public List classList;

    public void registerList(List list) {
        this.classList = list;
    }

    public abstract void addLoadMore();

    public abstract void removeLoadMore();

    public static class AdpterViewType {
        public static final int ITEM_VIEW_TYPE_LIST = 1;
        public static final int ITEM_VIEW_TYPE_LOADING = 2; //footer, bottom progress bar
        public static final int ITEM_VIEW_TYPE_SHIMMER = 3;
        public static final int ITEM_VIEW_TYPE_HEADER = 4;
    }

    public void addBottomProgress() {
        if (classList != null) {
            classList.add(null);
            notifyItemInserted(classList.size());
        }
    }


    public void removeBottomProgress() {
        if (classList != null && classList.size() > 0) {
            classList.remove(classList.size() - 1);
        }
        notifyDataSetChanged();
    }

}
