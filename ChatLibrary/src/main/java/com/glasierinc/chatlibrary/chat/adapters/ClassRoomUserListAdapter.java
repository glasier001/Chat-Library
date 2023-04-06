package com.glasierinc.chatlibrary.chat.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.model.RoomUserList;
import com.glasierinc.chatlibrary.chat.util.CircleImageView;
import com.glasierinc.chatlibrary.chat.util.ClassRoomUserListItemClickListener;

import java.util.List;

public class ClassRoomUserListAdapter extends RecyclerView.Adapter<ClassRoomUserListAdapter.MyViewHolder> {

    private List<RoomUserList> classRoomList;
    Context context;
    boolean isFromGameBooster;
    ClassRoomUserListItemClickListener clickListener;
    boolean hidePrice;
    String mUserId;

    public ClassRoomUserListAdapter(List<RoomUserList> moviesList, Context context, boolean isFromGameBooster,String mUserId, ClassRoomUserListItemClickListener clickListener) {
        this.classRoomList = moviesList;
        this.context = context;
        this.isFromGameBooster = isFromGameBooster;
        this.mUserId = mUserId;
        this.clickListener = clickListener;
    }

    public void setHidePrice(boolean hidePrice) {
        this.hidePrice = hidePrice;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        RoomUserList mExamTipsModel = classRoomList.get(position);

        if (mExamTipsModel.user_id.equalsIgnoreCase(mUserId)) {
            holder.tv_title.setText("You");
            holder.tv_UserType.setText(mExamTipsModel.type);
            holder.imgStatus.setVisibility(View.GONE);
        } else {
            holder.tv_title.setText(mExamTipsModel.user_name);
            holder.tv_UserType.setText(mExamTipsModel.type);

            if (mExamTipsModel.status) {
                holder.imgStatus.setVisibility(View.VISIBLE);
            } else {
                holder.imgStatus.setVisibility(View.GONE);
            }
        }






    }

    @Override
    public int getItemCount() {
        return classRoomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_UserType;
        CircleImageView imgUser,imgStatus;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.txtUserName);
            tv_UserType = (TextView) view.findViewById(R.id.txtUserType);
            imgUser =   view.findViewById(R.id.imgUser);
            imgStatus =   view.findViewById(R.id.imgStatus);

        }
    }
}
