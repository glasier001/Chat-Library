package com.commonlib.models;

import android.os.Parcel;
import android.os.Parcelable;

// sagar : 7/2/19 11:52 AM Add id field if required here
public class Photo implements Parcelable {

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Photo(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.photoUrl = in.readString();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photoUrl);
    }
}
