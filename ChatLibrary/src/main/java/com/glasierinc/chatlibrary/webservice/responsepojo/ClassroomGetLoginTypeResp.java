package com.glasierinc.chatlibrary.webservice.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassroomGetLoginTypeResp {

    @SerializedName("status")
    @Expose
    public boolean status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("user")
    @Expose
    public List<UserList> result = null;

    public List<UserList> getResult() {
        return result;
    }

    public void setResult(List<UserList> result) {
        this.result = result;
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



    public class UserList {

        @SerializedName("user_id")
        @Expose
        public String user_id;

        @SerializedName("type")
        @Expose
        public String type;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
