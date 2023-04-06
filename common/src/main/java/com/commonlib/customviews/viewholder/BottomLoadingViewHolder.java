package com.commonlib.customviews.viewholder;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;


public class BottomLoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public BottomLoadingViewHolder(View itemView) {
        super(itemView);
//        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
//        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(AppConstants.THEME_COLOR), PorterDuff.Mode.MULTIPLY);
    }
}