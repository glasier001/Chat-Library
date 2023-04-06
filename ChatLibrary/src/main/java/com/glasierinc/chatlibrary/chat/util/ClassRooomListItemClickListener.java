package com.glasierinc.chatlibrary.chat.util;


import android.view.View;

import com.glasierinc.chatlibrary.webservice.responsepojo.ClassroomListResp;


public interface ClassRooomListItemClickListener {
    void onItemClick(View view, int position, boolean isFromGameBooster, ClassroomListResp.RoomList mExamTipsModel);
}