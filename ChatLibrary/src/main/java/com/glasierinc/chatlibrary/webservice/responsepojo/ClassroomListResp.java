package com.glasierinc.chatlibrary.webservice.responsepojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassroomListResp {

    @SerializedName("status")
    @Expose
    public boolean status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("room_list")
    @Expose
    public List<RoomList> room = null;



    public List<RoomList> getResult() {
        return room;
    }

    public void setResult(List<RoomList> result) {
        this.room = result;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class RoomList {

        @SerializedName("room_id")
        @Expose
        public String room_id;

        @SerializedName("room_name")
        @Expose
        public String room_name;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }
    }

}

