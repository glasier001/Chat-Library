package com.glasierinc.chatlibrary.chat.model;

import java.util.ArrayList;
import java.util.List;

public class UserRootResp {

    public String roomId;
    public List<RoomUser> roomUser  = new ArrayList<RoomUser>();
    public List<RoomUserList> users_list = new ArrayList<RoomUserList>();

}
