package com.commonlib.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.commonlib.constants.AppCodes;
import com.commonlib.constants.IntentKeys;
import com.commonlib.listeners.Callbacks;

import com.commonlib.utils.Limits;
import com.commonlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;
import static com.commonlib.constants.AppConstants.TAG;

// sagar : 22/1/19 1:07 PM All helper methods can be from interface to help new developers about the idea
public abstract class AppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List mList;
    private Object mObject;
    private String mSourceScreen;
    private String mApi;
    private boolean showShimmer;
    private Intent mIntent;
    private Callbacks.OnEventCallbackListener onEventCallbackListener;
    private int itemViewType;
    private RecyclerView recyclerView;

    public AppAdapter(Context mContext, RecyclerView recyclerView, List mList, String mSourceScreen, String mApi, Callbacks.OnEventCallbackListener onEventCallbackListener) {
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        this.mList = mList;
        this.mSourceScreen = mSourceScreen;
        this.mApi = mApi;
        this.onEventCallbackListener = onEventCallbackListener;
        mIntent = new Intent();
        mIntent.putExtra(IntentKeys.SOURCE_SCREEN, mSourceScreen);
        mIntent.putExtra(IntentKeys.API, mApi);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public Intent getAdapterIntent(Object parcelableObjectToPut) {
        mIntent = getAdapterIntent();
        mIntent.putExtra(IntentKeys.PARCEL, (Parcelable) parcelableObjectToPut);
        return mIntent;
    }

    public Intent getAdapterIntent() {
        return mIntent;
    }

    public void setAdapterIntent(Intent mIntent) {
        this.mIntent = mIntent;
    }

    public Intent getAdapterIntent(List parcelableList) {
        mIntent = getAdapterIntent();
        mIntent.putParcelableArrayListExtra(IntentKeys.PARCEL_LIST, (ArrayList<? extends Parcelable>) parcelableList);
        return mIntent;
    }

    public void updateList(List list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        if (position != -1) {
            if (mList != null) {
                if (mList.size() > position) {
                    return mList.get(position);
                }
            }
        }

        return null;
    }

    public List getModelList() {
        if (mList != null) {
            return mList;
        } else {
            return new ArrayList();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = showShimmer ? AppCodes.VIEW_TYPE_SHIMMER
                /*: mList == null || mList.isEmpty() ? AppCodes.VIEW_TYPE_SHIMMER*/
                : mList != null && mList.size() > 0 && mList.get(position) == null ? AppCodes.VIEW_TYPE_PROGRESS_BAR
                : AppCodes.VIEW_TYPE_ITEM;
        d(TAG, "AppAdapter: getItemViewType: " + itemViewType);
        return itemViewType;
    }

    @Override
    public int getItemCount() {
        d(TAG, "AppAdapter: getItemCount: " + (showShimmer ? 20 : mList != null ? mList.size() : 0));
        return showShimmer ? 20 : mList != null ? mList.size() : 0;
    }

    /**
     * // sagar : 22/1/19 3:09 PM
     * <p>
     * Add list to adapter
     *
     * <p>
     * The purpose of this method is to stop loading process if required (remove shimmer, load more) and
     * after adding the list, add load more if required. So that developer does not need to manage it through
     * controller, host class and adapter with several methods those end up in adapter in the end.
     * </p>
     *
     * @param pageNumber     Server page number
     * @param modelList      List for adapter
     * @param adjustLoadMore true if we want to add load more in the end of the list
     * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc">Read more about javadoc</a>
     * @since 1.0
     */
    public void addList(int pageNumber, List modelList, boolean adjustLoadMore) {

        d(TAG, "AppAdapter: addList: page: " + pageNumber + " list size: " + modelList.size());
        // sagar : 22/1/19 3:04 PM Why can't we remove loading progress at the time we add list?

        stopLoadingProgress();

        // sagar : 22/1/19 3:07 PM We may need to do next ops in separate thread

        /*new Handler().post(new Runnable() {
            @Override
            public void run() {
                // FIXME: sagar : 22/1/19 If required, put all below code inside this thread
            }
        });*/

        boolean addLoadMore = false;

        if (modelList == null) {
            modelList = new ArrayList<>();
        }

        // sagar : 22/1/19 3:00 PM Why can't we add load more directly here?
        // sagar : 22/1/19 3:02 PM We may need to get booolean before list gets merged (before both lists become one, unique, equal)

        if (adjustLoadMore) {
            if (modelList.size() >= Limits.PAGENUMBER_LIMIT) {
                addLoadMore = true;
            }
        }

        if (this.mList == null) {
            this.mList = modelList;
        }

        if (pageNumber == 1) {
            this.mList.clear(); // clearing this list clears modelList too if it is same reference
        }





        this.mList.addAll(modelList);


        d(TAG, "AppAdapter: addList: list size: " + this.mList.size());
        setItemViewType(AppCodes.VIEW_TYPE_ITEM);
        notifyDataSetChanged();

        if (addLoadMore) {
            addLoadMore();
        }
    }

    /**
     * The purpose of this method is to have single method to stop loading progress despite of the acknowledgement of progress nature being shimmer or bottom loading progress bar
     *
     * <p> Improve this method if required
     * </p>
     *
     * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc">Read more about javadoc</a>
     * @since 1.0
     */
    public void stopLoadingProgress() {
        stopShimmer();
        removeLoadMore();
    }

    public void stopLoadingProgress(boolean isFromTop) {
        stopShimmer();
        removeLoadMore(isFromTop);
    }

    public void addLoadMore() {
        // sagar : 22/1/19 1:26 PM Case: Remove load more, notifyDataSetChanged, Add Load More during data being notified
        // FIXME: sagar : 22/1/19 Remove separate thread if it is not working as expected
        new Handler().post(() -> {

            /*if (mList == null) {
                mList = new ArrayList();
            }*/

            // TODO: sagar : 4/4/19 12:32 PM Add support to add load more either on top or in bottom to support chat like ui
            if (mList != null) {
                mList.add(null);
                setItemViewType(AppCodes.VIEW_TYPE_PROGRESS_BAR);
                notifyItemInserted(mList.size());
            }
        });
    }

    public void stopShimmer() {
        if (showShimmer || (mList == null) || (mList.isEmpty())) {
            showShimmer = false;
            notifyDataSetChanged();
        }
    }

    public void removeLoadMore() {
        d(TAG, "AppAdapter: removeLoadMore: item type: " + getItemViewType());
        if (mList != null && mList.size() > 0 && getItemViewType() != AppCodes.VIEW_TYPE_SHIMMER && !showShimmer) {

//            mList.remove(mList.size() - 1);

            // sagar : 22/1/19 1:35 PM Case: It is shimmer or footer?
            // FIXME: sagar : 22/1/19 If it is not working as expected, uncomment above line and comment this block
            if (mList.size() > 0) {
                int lastItem = mList.size() - 1;
                Object object = mList.get(lastItem);
                if (object == null) {
                    // sagar : 22/1/19 1:37 PM If it is null, that means it is load more progress bar
                    d(TAG, "AppAdapter: removeLoadMore: removing last item");
                    mList.remove(lastItem);
                }
            }
        }
        setItemViewType(AppCodes.VIEW_TYPE_ITEM);
        notifyDataSetChanged();
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public void removeLoadMore(boolean isFromTop) {
        d(TAG, "AppAdapter: removeLoadMore: item type: " + getItemViewType());
        if (mList != null && mList.size() > 0 && getItemViewType() != AppCodes.VIEW_TYPE_SHIMMER && !showShimmer) {

//            mList.remove(mList.size() - 1);

            // sagar : 22/1/19 1:35 PM Case: It is shimmer or footer?
            // FIXME: sagar : 22/1/19 If it is not working as expected, uncomment above line and comment this block

            if (mList.size() > 0) {

                if (isFromTop) {
                    Object object = mList.get(0);
                    if (object == null) {
                        // sagar : 22/1/19 1:37 PM If it is null, that means it is load more progress bar
                        mList.remove(object);
                    }
                } else {
                    int lastItem = mList.size() - 1;
                    Object object = mList.get(lastItem);
                    if (object == null) {
                        // sagar : 22/1/19 1:37 PM If it is null, that means it is load more progress bar
                        mList.remove(lastItem);
                    }
                }
            }
        }
        setItemViewType(AppCodes.VIEW_TYPE_ITEM);
        notifyDataSetChanged();
    }

    public void addList(int pageNumber, List modelList, boolean addToTop, boolean adjustLoadMore) {
        d(TAG, "ChatAdapter: addList: page: " + pageNumber + " list size: " + modelList.size());
        // sagar : 22/1/19 3:04 PM Why can't we remove loading progress at the time we add list?

        stopLoadingProgress(addToTop);

        // sagar : 22/1/19 3:07 PM We may need to do next ops in separate thread

        /*new Handler().post(new Runnable() {
            @Override
            public void run() {
                // FIXME: sagar : 22/1/19 If required, put all below code inside this thread
            }
        });*/

        boolean addLoadMore = false;

        if (modelList == null) {
            modelList = new ArrayList<>();
        }

        // sagar : 22/1/19 3:00 PM Why can't we add load more directly here?
        // sagar : 22/1/19 3:02 PM We may need to get booolean before list gets merged (before both lists become one, unique, equal)

        if (adjustLoadMore) {
            if (modelList.size() >= Limits.PAGENUMBER_LIMIT) {
                addLoadMore = true;
            }
        }

        if (this.mList == null) {
            this.mList = modelList;
        }

        if (pageNumber == 1) {
            this.mList.clear(); // clearing this list clears modelList too if it is same reference
            d(TAG, "ChatAdapter: addList: this list: " + this.mList.size() + "input list: " + modelList.size());
        }

        if (addToTop) {
            this.mList.addAll(0, modelList);
        } else {
            this.mList.addAll(modelList);
        }

        setItemViewType(AppCodes.VIEW_TYPE_ITEM);
        notifyDataSetChanged();

        if (addLoadMore) {
            addLoadMore(pageNumber, addToTop);
        }
    }

    public void addLoadMore(int pageNumber, boolean addToTop) {
        // sagar : 22/1/19 1:26 PM Case: Remove load more, notifyDataSetChanged, Add Load More during data being notified
        // FIXME: sagar : 22/1/19 Remove separate thread if it is not working as expected
        new Handler().post(() -> {

            /*if (mList == null) {
                mList = new ArrayList();
            }*/

            // TODO: sagar : 4/4/19 12:32 PM Add support to add load more either on top or in bottom to support chat like ui
            if (mList != null) {
                if (addToTop) {

                    if (!Utils.containsNull(mList, 0)) {
                        mList.add(0, null);
                        setItemViewType(AppCodes.VIEW_TYPE_PROGRESS_BAR);
                        notifyDataSetChanged();
                    }

                    if (pageNumber == 1) {
                        if (getRecyclerView() != null) {
                            getRecyclerView().scrollToPosition(getList().size() == 1 ? getList().size() - 1 : getList().size());
                        }
                    }

                } else {

                    if (!Utils.containsNull(mList, mList.size() - 1)) {
                        mList.add(null);
                        setItemViewType(AppCodes.VIEW_TYPE_PROGRESS_BAR);
                        notifyItemInserted(mList.size());
                    }
                }

            }
        });
    }

    public void showShimmer(boolean show) {
        this.showShimmer = show;
        notifyDataSetChanged();
    }

    public void startShimmer() {
        showShimmer = true;
        notifyDataSetChanged();
    }

    public void clearData() {
        resetList();
    }

    public void resetList() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object mObject) {
        this.mObject = mObject;
    }

    public String getSourceScreen() {
        return mSourceScreen;
    }

    public void setSourceScreen(String mSourceScreen) {
        this.mSourceScreen = mSourceScreen;
    }

    public String getApi() {
        return mApi;
    }

    public void setApi(String mApi) {
        this.mApi = mApi;
    }

    public boolean isShowShimmer() {
        return showShimmer;
    }

    public void setShowShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
    }

    public void addItem(Object mObject) {
        List mList = getList();
        mList.add(mObject);
//        notifyItemInserted(mList.size());
        notifyDataSetChanged();
    }

    public List getList() {
        if (mList != null) {
            return mList;
        } else {
            return new ArrayList();
        }
    }

    public void setList(List mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addItem(Object mObject, int position) {
        List mList = getList();
        if (mList != null) {
            mList.add(position, mObject);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        List mList = getList();
        if (mList != null) {
            if (mList.size() > position) {
                mList.remove(position);

                /*if (position != mList.size() - 1 || position != 0) {
                    notifyItemRemoved(position);
                } else {
                    notifyDataSetChanged();
                }*/

                /*if (mList.size() == 1) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRemoved(position);
                }*/

                // sagar : 4/2/19 1:02 PM notifyItemRemoved(position) is causing layout problem in Moto E6 when Rv has EditText Items!

                notifyDataSetChanged();
            }
        }
    }

    public void removeItem(Object mObject) {
        List mList = getList();
        if (mList != null) {
            try {
                mList.remove(mObject);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateItem(Object mObject, int position) {

        List mList = getList();
        if (mList != null) {
            if (mList.size() > position) {
                mList.set(position, mObject);
                notifyDataSetChanged();
            }
        }
    }

    public void onItemClick(View v, int positionTag, Intent intentData, Object... objects) {
        if (getOnEventCallbackListener() != null) {
            getOnEventCallbackListener().onEventCallback(v, positionTag, intentData, objects);
        }
    }

    public Callbacks.OnEventCallbackListener getOnEventCallbackListener() {
        return onEventCallbackListener;
    }

    public void setOnEventCallbackListener(Callbacks.OnEventCallbackListener onEventCallbackListener) {
        this.onEventCallbackListener = onEventCallbackListener;
    }
}
