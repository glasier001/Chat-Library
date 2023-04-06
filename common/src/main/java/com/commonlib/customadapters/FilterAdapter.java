package com.commonlib.customadapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;


import com.commonlib.listeners.Callbacks;
import com.commonlib.models.Title;
import com.commonlib.constants.IntentKeys;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class FilterAdapter extends RecyclerView.Adapter implements Callbacks.OnItemClickListener {

    private Activity activity;
    private List<Title> titleList = new ArrayList<>();
    private View rootView;
    private List<Title> selectedTitleList = new ArrayList<>();
    private List<Title> filterList = new ArrayList<>();
    private String apiId;
    private int lastSelected = -1;
    private Title temp;
    private String filterTexts;
    private Callbacks.OnItemChangeListener onItemChangeListener;

    public FilterAdapter(Activity activity, String apiId, Callbacks.OnItemChangeListener onItemChangeListener) {
        this.activity = activity;
        this.apiId = apiId;
        this.onItemChangeListener = onItemChangeListener;
    }

    public void unCheck(Title title) {
        refreshTitleList(title, false);
    }

    public void refreshTitleList(Title title, boolean isChecked) {
        for (int i = 0; i < titleList.size(); i++) {
            if (titleList.get(i).getTitle().equalsIgnoreCase(title.getTitle())) {
                titleList.get(i).setSelected(isChecked);
            }
        }
    }

    public void updateFilterList(Title title) {

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mutable_choice, parent, false);
//        return new ItemViewHolder(rootView, this);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            setTags(itemViewHolder, position);
                *//*We are using cloned list to get position of selected item because position of selected item in
                filter list can be changed due to filter process. Also, getAdapterPosition works for visible items only, not with filter*//*
            //This will be useful for single selection. Multiple selection will have last selected value.
            if (filterList.get(itemViewHolder.getAdapterPosition()).isSelected()) {
                temp = filterList.get(itemViewHolder.getAdapterPosition());
                lastSelected = titleList.indexOf(temp);
                itemViewHolder.ctvTitle.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(activity, R.drawable.ic_radio_check), null, null, null);
            } else {
                itemViewHolder.ctvTitle.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(activity, R.drawable.ic_radio_uncheck), null, null, null);
            }

            bindViews(itemViewHolder, position);
        }*/
    }

    /*private void setTags(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.itemView.setTag(position);
        itemViewHolder.ctvTitle.setTag(position);
        filterList.get(position).setPositionTag(position);
    }*/

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    /*private void bindViews(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.ctvTitle.setText(filterList.get(position).getTitle());
    }*/

    private void sendItemUpdate(int positionTag, Title title, boolean isSelected) {
        if (onItemChangeListener != null) {
            onItemChangeListener.onChangeItemData(positionTag, title, new Intent());
        }
    }

    public void alterSelection(int position) {
        if (filterList.get(position).isSelected()) {
            filterList.get(position).setSelected(false);
            refreshTitleList(filterList.get(position), false);
            notifyItemChanged(position);
            sendItemUpdate(position, filterList.get(position), false);
//                        updateSelectedList(false, position);
        } else {
            filterList.get(position).setSelected(true);
            refreshTitleList(filterList.get(position), true);
            notifyItemChanged(position);
            sendItemUpdate(position, filterList.get(position), true);
//                        updateSelectedList(true, position);
        }
    }

    public void alterSelection(Title title, boolean isSelected) {
        if (isSelected) {
            title.setSelected(true);
            refreshTitleList(title, true);
            notifyDataSetChanged();
        } else {
            title.setSelected(false);
            refreshTitleList(title, false);
            notifyDataSetChanged();
        }
    }

    //region Clear any prior selection from all lists
    private void selectItem(int position, View view) {
        temp = filterList.get((Integer) view.getTag());

        //Clear any prior selection from list
        resetList();
        //Need to notify for this change
        notifyDataSetChanged();
        //Set selected item true
        temp.setSelected(true);
        onItemChangeListener.onChangeItemData(position, temp, new Intent());
        //To sustain filter mode?
        filter(filterTexts);
        //Update title list but no need to send callback as we already did that above
        refreshTitleList(temp, true);
        //Need to notify for this change
        notifyDataSetChanged();
    }
    //endregion

    /**
     * Case: lastSelection 90, close, re-open, filter: select 120th item.
     * <p>
     * <p> lastSelected item will not updated in onBindViewHolder until it gets visibility and in such case,
     * lastSelected item will be considered as -1 only. So, getSelected item will give two items from which
     * we are taking first selected item assuming there will be always one item!
     * Hence, we simply deselect all items using for loop!
     * </p>
     * //     * @param position Adapter position or positionTag of the relevant item
     */
    private void resetList() {

        //While in filter mode, otherwise will be outOfIndex
        if (filterList.size() < titleList.size()) {
            filterList.clear();
            filterList.addAll(titleList);
        }

        //Either notify through filterList or through titleList but not from both as it will create null pointer exception or outOfIndex
        // FIXME: 9/4/18  sagar: Should be done by particular position
        for (Title title : filterList) {
            title.setSelected(false);
            onItemChangeListener.onChangeItemData(title.getPositionTag(), title, new Intent());
        }

        for (Title title : titleList) {
            title.setSelected(false);
        }

        if (selectedTitleList != null && selectedTitleList.size() > 0) {
            selectedTitleList.clear();
        }
    }

    public void addAll(List<Title> List) {

        if (List == null) {
            titleList = new ArrayList<>();
            filterList = new ArrayList<>();
        } else {
            this.filterList = List;
            titleList.clear();
            titleList.addAll(filterList);
        }

        notifyDataSetChanged();
    }

    public List<Title> getSelectedTitles() {
        if (selectedTitleList != null && selectedTitleList.size() > 0) {
            selectedTitleList.clear();
        } else {
            selectedTitleList = new ArrayList<>();
        }

//        filter("");
        //filterList can be replaced by titleList as filterList can have less data while in filter mode?
        for (Title title : titleList) {
            if (title.isSelected()) {
                selectedTitleList.add(title);
            }
        }
        return selectedTitleList;
    }

    public Title getSelectedTitle() {
        return temp;
    }

    public Title setSelectedTitle(Title title) {
        this.temp = title;
        return title;
    }

    private void setSelection(List<Title> titleList, List<Title> filterList) {
        if (filterList != null && filterList.size() > 0) {
            for (int i = 0; i < titleList.size(); i++) {
                Title title = titleList.get(i);
                for (int j = 0; j < filterList.size(); j++) {
                    Title selectedItem = filterList.get(j);
                    if (selectedItem.getTitle().equalsIgnoreCase(title.getTitle())
                            && selectedItem.isSelected()) {
                        title.setSelected(true);
                    }
                }
            }
        }
    }

    // Do Search...
    public void filter(final String text) {
        this.filterTexts = text;
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list. It also clears all selected records!?!
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(titleList); //This titleList contains no selection!?!

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (Title title : titleList) {
                        if (title.getTitle().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched jobTitles
                            filterList.add(title);
                        }
                    }
                }

                // Set on UI Thread
                (activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    @Override
    public void onItemClick(View v, int position) {
        if (apiId.equalsIgnoreCase(IntentKeys.MULTIPLE_SELECTION)) {
            temp = filterList.get((Integer) v.getTag());
            if (temp.isSelected()) {
                temp.setSelected(false);
                onItemChangeListener.onChangeItemData(position, temp, new Intent());
            } else {
                temp.setSelected(true);
                setSelectedTitle(temp);
                onItemChangeListener.onChangeItemData(position, temp, new Intent());
            }
            refreshTitleList(temp, temp.isSelected());
            sendItemUpdate(position, temp, temp.isSelected());

        } else if (apiId.equalsIgnoreCase(IntentKeys.SINGLE_SELECTION)) {
            selectItem(position, v);
        }

        notifyDataSetChanged();
    }
}
