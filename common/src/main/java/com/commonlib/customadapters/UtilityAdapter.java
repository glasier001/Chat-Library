package com.commonlib.customadapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.constants.AppCodes;
import com.commonlib.listeners.Callbacks;
import com.commonlib.models.Title;
import com.commonlib.constants.IntentKeys;
import com.commonlib.utils.Limits;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sagar on 1/11/17.
 */

public class UtilityAdapter extends RecyclerView.Adapter implements Callbacks.OnItemClickListener {

    private Activity activity;
    private List<Title> selectedObjList = new ArrayList<>();
    private List<Title> objectList = new ArrayList<>();
    private String apiId;
    private int lastSelected = -1;
    private Title temp;
    private Callbacks.OnItemChangeListener onItemChangeListener;

    public UtilityAdapter(Activity activity, String apiId, Callbacks.OnItemChangeListener onItemChangeListener) {
        this.activity = activity;
        this.apiId = apiId;
        this.onItemChangeListener = onItemChangeListener;
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
            if (objectList.get(position).isSelected()) {
                lastSelected = position;
                temp = objectList.get(lastSelected);
                addToSelectedItemList(temp);
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
        objectList.get(position).setPositionTag(position);
    }*/

    @Override
    public int getItemViewType(int position) {
        return objectList.get(position) == null ? AppCodes.VIEW_TYPE_PROGRESS_BAR : AppCodes.VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    /*private void bindViews(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.ctvTitle.setText(objectList.get(position).getTitle());
    }*/

    public void alterSelection(int position) {
        if (objectList.get(position).isSelected()) {
            objectList.get(position).setSelected(false);
            notifyItemChanged(position);
            sendItemUpdate(position, objectList.get(position), false);
//                        updateSelectedList(false, position);
        } else {
            objectList.get(position).setSelected(true);
            notifyItemChanged(position);
            sendItemUpdate(position, objectList.get(position), true);
//                        updateSelectedList(true, position);
        }
    }

    private void sendItemUpdate(int positionTag, Title title, boolean isSelected) {
        if (onItemChangeListener != null) {
            onItemChangeListener.onChangeItemData(positionTag, title, new Intent());
        }
    }

    public void alterItemSelection(Title title, boolean isSelected) {
        if (isSelected) {
            title.setSelected(true);
        } else {
            title.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void addAllDataList(int pageNumber, List<Title> searchJobList) {
        if (this.objectList.size() >= Limits.PAGENUMBER_LIMIT) {
            removeLoadingView();
        }
        if (pageNumber == 1) {
            this.objectList.clear();
        }
        this.objectList.addAll(searchJobList);
        notifyDataSetChanged();
    }
    //endregion

    public void removeLoadingView() {
        //Remove loading item
        objectList.remove(objectList.size() - 1); //Removes progress bar loading
        notifyItemRemoved(objectList.size()); //Notifies an adapter for that to refresh the data list
    }

    public List<Title> getSelectedTitles() {
        return selectedObjList;
    }

    public Title getSelectedTitle() {
        return temp;
    }

    public void setSelectedTitle(Title title) {
        this.temp = title;
    }

    @Override
    public void onItemClick(View v, int position) {
        // TODO: 12/4/18  sagar: This can have switch case for multiple views with different calls (bookmark, share, edit etc...)
        if (apiId.equalsIgnoreCase(IntentKeys.MULTIPLE_SELECTION)) {
            selectMultipleItem(v, position);
        } else if (apiId.equalsIgnoreCase(IntentKeys.SINGLE_SELECTION)) {
            selectSingleItem(v, position);
        }
    }

    public void selectMultipleItem(View v, int position) {
        temp = objectList.get((Integer) v.getTag());
        if (temp.isSelected()) {
            
            // TODO: 12/4/18  sagar: You may want to move everything inside dialog or api response
            showAlertDialog(temp, v, position);
            callApi(temp, v, position, false);
            
            temp.setSelected(false);
            removeFromSelectedItemList(temp);
            
            onItemChangeListener.onChangeItemData(position, temp, new Intent());
            
        } else {
            // TODO: 12/4/18  sagar: You may want to move everything inside dialog or api response
            callApi(temp, v, position, true);
            
            temp.setSelected(true);
            setSelectedTitle(temp);
            addToSelectedItemList(temp);
            
            onItemChangeListener.onChangeItemData(position, temp, new Intent());
        }
        sendItemUpdate(position, temp, temp.isSelected());
        notifyDataSetChanged();
    }

    //region Clear any prior selection and set selected true clicked item
    private void selectSingleItem(View view, int position) {

        if (lastSelected < objectList.size() && lastSelected != (Integer) view.getTag()) {
            temp = objectList.get(lastSelected);
            temp.setSelected(false);
            removeFromSelectedItemList(temp);
            onItemChangeListener.onChangeItemData(position, objectList.get(lastSelected), new Intent());
        }

        lastSelected = (Integer) view.getTag();
        temp = objectList.get(lastSelected);

        // TODO: 12/4/18  sagar: You may want to move everything inside dialog or api response
        callApi(temp, view, position, true);

        //Set selected item true
        temp.setSelected(true);
        setSelectedTitle(temp);
        addToSelectedItemList(temp);
        onItemChangeListener.onChangeItemData(position, temp, new Intent());
        //Need to notify for this change
        notifyDataSetChanged();
    }

    private void showAlertDialog(Title temp, View v, int position) {

    }

    private void callApi(Title temp, View v, int position, boolean isSelected) {

    }

    public void removeFromSelectedItemList(Title temp) {
        for (Title title : selectedObjList) {
            if (title.getId().equalsIgnoreCase(temp.getId()) && title.getTitle().equalsIgnoreCase(temp.getTitle())) {
                selectedObjList.remove(title);
            }
        }
    }

    public void addToSelectedItemList(Title temp) {
        selectedObjList.add(temp);
    }

    public void removeItemFromCurrentList(Title temp, View v, int position) {
        int index = (Integer) v.getTag();
        if (index != -1 && index < objectList.size()) {
            objectList.remove(index);
            notifyItemRemoved(index);
            // TODO: 12/4/18  sagar: If above notifyItemRemoved(index) does not work properly, replace it by below method.
//            notifyDataSetChanged();
        }
    }

    public void updateItem(Title title, View v, int position) {
        Title item = objectList.get((Integer) v.getTag());
        item.setSelected(title.isSelected());
        notifyItemChanged((Integer) v.getTag());
        // TODO: 12/4/18  sagar: If above notifyItemChanged(index) does not work properly, replace it by below method.
//        notifyDataSetChanged();
    }

    public void addItem(Title title, View v, int position) {
        if (position != 0 && position < objectList.size()) {
            objectList.add(position, title);
            notifyItemInserted(position);
            // TODO: 12/4/18  sagar: If above notifyItemInserted(index) does not work properly, replace it by below method.
//        notifyDataSetChanged();
        } else if (v != null && (Integer) v.getTag() != 0 && (Integer) v.getTag() < objectList.size()) {
            objectList.add((Integer) v.getTag(), title);
            notifyItemInserted((Integer) v.getTag());
            // TODO: 12/4/18  sagar: If above notifyItemInserted(index) does not work properly, replace it by below method.
//        notifyDataSetChanged();
        } else if (title != null) {
            objectList.add(title);
            notifyDataSetChanged();
        }
    }
}
