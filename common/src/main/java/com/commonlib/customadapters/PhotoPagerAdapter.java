package com.commonlib.customadapters;

import android.content.Context;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.commonlib.R;
import com.commonlib.models.Photo;
import com.commonlib.utils.Utils;
import com.commonlib.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class PhotoPagerAdapter extends RecyclePagerAdapter<PhotoPagerAdapter.ViewHolder> {

    private final ViewPager viewPager;
    private List<Photo> photoList;
    private Context context;

    public PhotoPagerAdapter(Context context, ViewPager pager, List<Photo> photoList) {
        this.context = context;
        this.viewPager = pager;
        this.photoList = photoList;
    }

    public static GestureImageView getImageView(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }

    public void removeItem(int position) {
        if (Utils.isNotNullNotEmpty(photoList)) {
            if (position != -1) {
                if (photoList.size() > position) {
                    photoList.remove(position);
                    notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    public void addList(int pageNumber, List modelList) {
        if (modelList == null) {
            modelList = new ArrayList<>();
        }

        if (this.photoList == null) {
            this.photoList = modelList;
        }

        if (pageNumber == 1) {
            this.photoList.clear();
        }
        this.photoList.addAll(modelList);
        notifyDataSetChanged();
    }

    public void clearDataList(){
        if (photoList != null){
            photoList.clear();
            notifyDataSetChanged();
        }
    }

    public void replaceDataList(List<Photo> photoList){
        if (photoList != null){
            this.photoList = photoList;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        ViewHolder holder = new ViewHolder(container);
        holder.image.getController().enableScrollInViewPager(viewPager);
        holder.image.getController().getSettings().setMaxZoom(10f).setDoubleTapZoom(3f);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        ViewUtils.loadImage(context, false, photo.getPhotoUrl(), R.drawable.ic_banner_placeholder, R.drawable.ic_banner_placeholder, holder.image);
    }

    static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        final GestureImageView image;

        ViewHolder(ViewGroup container) {
            super(new GestureImageView(container.getContext()));
            image = (GestureImageView) itemView;
        }
    }
}
