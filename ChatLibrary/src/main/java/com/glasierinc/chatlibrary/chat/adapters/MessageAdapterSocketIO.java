package com.glasierinc.chatlibrary.chat.adapters;

import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_CALL;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_IMAGE;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_PDF;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_VIDEO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.activity.FullImageViewActivity;
import com.glasierinc.chatlibrary.chat.model.GetMessageListData;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapterSocketIO extends RecyclerView.Adapter<MessageAdapterSocketIO.MyViewHolder> {

    private LayoutInflater inflater;
    private List<GetMessageListData> messages = new ArrayList<>();
    Context context;
    String mUserId;

    public MessageAdapterSocketIO(Context context, List<GetMessageListData> messages, String mUserId) {
        this.inflater = ((Activity) context).getLayoutInflater();
        this.messages = messages;
        this.context = context;
        this.mUserId = mUserId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        GetMessageListData message = messages.get(position);
        Log.e("LoginUserId",mUserId);
        Log.e("LoginUserIdRec",message.senderId);


        try {
            if (message.senderId.equalsIgnoreCase(mUserId)) {
                //Login user
//                holder.textViewSelfName.setVisibility(View.VISIBLE);
              //  holder.textViewSelfName.setText(message.senderId);

                if (message.messageType.equalsIgnoreCase(TYPE_IMAGE)) {
                    holder.imageViewSelfImg.setVisibility(View.VISIBLE);
                    holder.imageViewOtherImg.setVisibility(View.GONE);



                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();



                    Glide.with(context).load(message.message).placeholder(circularProgressDrawable).into(holder.imageViewSelfImg);

                } else if (message.messageType.equalsIgnoreCase(TYPE_PDF)) {
                    holder.imageViewSelfImg.setVisibility(View.VISIBLE);
                    holder.imageViewOtherImg.setVisibility(View.GONE);

                    holder.imageViewSelfImg.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.imageViewSelfImg.setImageDrawable(context.getResources().getDrawable(R.drawable.pdf));

                } else if (message.messageType.equalsIgnoreCase(TYPE_VIDEO)) {
                    holder.imageViewSelfImg.setVisibility(View.VISIBLE);
                    holder.imageViewOtherImg.setVisibility(View.GONE);

                    holder.imageViewSelfImg.setBackgroundColor(context.getResources().getColor(R.color.white));
                   // holder.imageViewSelfImg.setImageDrawable(context.getResources().getDrawable(R.drawable.video));
                    Glide.with(context).load(message.message).placeholder(R.drawable.video).into(holder.imageViewSelfImg);
                    holder.item_img_play_r.setVisibility(View.VISIBLE);

                } else if (message.messageType.equalsIgnoreCase(TYPE_CALL)) {

//                    holder.itemCallDuration.setVisibility(View.VISIBLE);
//                    if(message.message.equalsIgnoreCase("Missed Call")){
//                        holder.itemCallDuration.setText(message.message);
//
//                    }else {
//                        holder.itemCallDuration.setText("Call duration "+message.message);
//
//                    }

                    holder.imageViewSelfImg.setVisibility(View.VISIBLE);
                    holder.imageViewOtherImg.setVisibility(View.GONE);

                    holder.imageViewSelfImg.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.imageViewSelfImg.setImageDrawable(context.getResources().getDrawable(R.drawable.audio_file));
                    holder.item_img_play_r.setVisibility(View.VISIBLE);


                } else {
                    //message
                    holder.itemCallDuration.setVisibility(View.GONE);

                    holder.textViewSelfMsg.setVisibility(View.VISIBLE);
                    holder.textViewOtherMsg.setVisibility(View.GONE);

                    holder.textViewSelfMsg.setText(message.message);

                }


            } else {


                holder.textViewOtherName.setVisibility(View.VISIBLE);
                holder.textViewOtherName.setText(message.senderId);


                if (message.messageType.equalsIgnoreCase(TYPE_IMAGE)) {
                    holder.imageViewOtherImg.setVisibility(View.VISIBLE);
                    holder.imageViewSelfImg.setVisibility(View.GONE);

                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();

                    Glide.with(context).load(message.message).placeholder(circularProgressDrawable).into(holder.imageViewOtherImg);

                } else if (message.messageType.equalsIgnoreCase(TYPE_PDF)) {
                    holder.ll_Left.setBackgroundColor(Color.WHITE);
                    holder.imageViewOtherImg.setVisibility(View.VISIBLE);
                    holder.imageViewSelfImg.setVisibility(View.GONE);

                    holder.imageViewOtherImg.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.imageViewOtherImg.setImageDrawable(context.getResources().getDrawable(R.drawable.pdf));

                } else if (message.messageType.equalsIgnoreCase(TYPE_VIDEO)) {
                    holder.ll_Left.setBackgroundColor(Color.WHITE);
                    holder.imageViewOtherImg.setVisibility(View.VISIBLE);
                    holder.imageViewSelfImg.setVisibility(View.GONE);

                    holder.imageViewOtherImg.setBackgroundColor(context.getResources().getColor(R.color.white));
                 //   holder.imageViewOtherImg.setImageDrawable(context.getResources().getDrawable(R.drawable.video));
                    Glide.with(context).load(message.message).placeholder(R.drawable.video).into(holder.imageViewOtherImg);
                    holder.item_img_play_l.setVisibility(View.VISIBLE);
                } else if (message.messageType.equalsIgnoreCase(TYPE_CALL)) {
                    holder.ll_Left.setBackgroundColor(Color.WHITE);
//
//                    holder.itemCallDuration.setVisibility(View.VISIBLE);
//                    if(message.message.equalsIgnoreCase("Missed Call")){
//                        holder.itemCallDuration.setText(message.message);
//
//                    }else {
//                        holder.itemCallDuration.setText("Call duration "+message.message);
//
//                    }

                    holder.imageViewOtherImg.setVisibility(View.VISIBLE);
                    holder.imageViewSelfImg.setVisibility(View.GONE);

                    holder.imageViewOtherImg.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.imageViewOtherImg.setImageDrawable(context.getResources().getDrawable(R.drawable.audio_file));
                    holder.item_img_play_l.setVisibility(View.VISIBLE);

                } else {
                    //message
                    holder.itemCallDuration.setVisibility(View.GONE);

                    holder.textViewOtherMsg.setVisibility(View.VISIBLE);
                    holder.textViewSelfMsg.setVisibility(View.GONE);

                    holder.textViewOtherMsg.setText(message.message);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.imageViewOtherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.messageType.equalsIgnoreCase(TYPE_PDF)){
                    String url= "https://docs.google.com/gview?embedded=true&url="+message.message;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }else {
                    context.startActivity(new Intent(context, FullImageViewActivity.class).putExtra("image", message.message).putExtra("type", message.messageType));
                }
            }
        });
        holder.imageViewSelfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.messageType.equalsIgnoreCase(TYPE_PDF)){
                    String url= "https://docs.google.com/gview?embedded=true&url="+message.message;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }else {
                    context.startActivity(new Intent(context, FullImageViewActivity.class).putExtra("image", message.message).putExtra("type", message.messageType));

                }

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOtherName;
        private TextView itemCallDuration;
        private TextView textViewOtherMsg;
        private ImageView imageViewOtherImg;
        private ImageView item_img_play_r;
        private ImageView item_img_play_l;
        private TextView textViewSelfName;
        private TextView textViewSelfMsg;
        private ImageView imageViewSelfImg;
        private RelativeLayout layoutLeft;
        private RelativeLayout layoutRight;
        private LinearLayoutCompat ll_Left;

        MyViewHolder(View itemView) {
            super(itemView);

            itemCallDuration = itemView.findViewById(R.id.itemCallDuration);
            textViewOtherName = itemView.findViewById(R.id.item_name_l);
            textViewOtherMsg = itemView.findViewById(R.id.item_msg_l);
            imageViewOtherImg = itemView.findViewById(R.id.item_img_l);
            item_img_play_r = itemView.findViewById(R.id.item_img_play_r);
            item_img_play_l = itemView.findViewById(R.id.item_img_play_l);
            ll_Left = itemView.findViewById(R.id.ll_Left);
            layoutLeft = itemView.findViewById(R.id.item_layout_l);


            textViewSelfName = itemView.findViewById(R.id.item_name_r);
            textViewSelfMsg = itemView.findViewById(R.id.item_msg_r);
            imageViewSelfImg = itemView.findViewById(R.id.item_img_r);
            layoutRight = itemView.findViewById(R.id.item_layout_r);
        }
    }

}