package com.glasierinc.chatlibrary.webservice.responsepojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassroomGetUserList {

    @SerializedName("status")
    @Expose
    public boolean status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("user_list")
    @Expose
    public List<RoomUserList> userList = null;


    public List<RoomUserList> getResult() {
        return userList;
    }

    public void setResult(List<RoomUserList> result) {
        this.userList = result;
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


    public class RoomUserList {

        @SerializedName("user_id")
        @Expose
        public String user_id;

        @SerializedName("user_name")
        @Expose
        public String user_name;


        @SerializedName("type")
        @Expose
        public String type;


        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}

