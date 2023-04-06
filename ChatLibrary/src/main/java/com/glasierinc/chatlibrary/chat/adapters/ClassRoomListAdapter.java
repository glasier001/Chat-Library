package com.glasierinc.chatlibrary.chat.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.util.ClassRooomListItemClickListener;
import com.glasierinc.chatlibrary.webservice.responsepojo.ClassroomListResp;

import java.util.List;
import java.util.Random;

public class ClassRoomListAdapter extends RecyclerView.Adapter<ClassRoomListAdapter.MyViewHolder> {

    private List<ClassroomListResp.RoomList> classRoomList;
    Context context;
    boolean isFromGameBooster;
    ClassRooomListItemClickListener clickListener;
    boolean hidePrice;

    public ClassRoomListAdapter(List<ClassroomListResp.RoomList> moviesList, Context context, boolean isFromGameBooster, ClassRooomListItemClickListener clickListener) {
        this.classRoomList = moviesList;
        this.context = context;
        this.isFromGameBooster = isFromGameBooster;
        this.clickListener = clickListener;
    }

    public void setHidePrice(boolean hidePrice) {
        this.hidePrice = hidePrice;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mock_test_exams_menu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ClassroomListResp.RoomList mExamTipsModel = classRoomList.get(position);
        holder.tv_title.setText(mExamTipsModel.getRoom_name());
        //holder.tv_title.setGravity(Gravity.CENTER);

        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.cv_color.setCardBackgroundColor(currentColor);
        holder.tv_note.setVisibility(View.GONE);
        holder.tv_price.setVisibility(View.GONE);
        holder.cv_test_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position, isFromGameBooster, mExamTipsModel);

            }
        });
    }

    @Override
    public int getItemCount() {
        return classRoomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_note, tv_price;
        CardView cv_color, cv_test_click;
        LinearLayout priceLayout;

        public MyViewHolder(View view) {
            super(view);
            cv_test_click = (CardView) view.findViewById(R.id.cv_test_click);
            cv_color = (CardView) view.findViewById(R.id.cv_color);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_note = (TextView) view.findViewById(R.id.tv_note);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            priceLayout = view.findViewById(R.id.priceLayout);
        }
    }
}