package com.commonlib.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

/**
 * Created by sagar on 1/11/17.
 */

@Keep
public class Title implements Parcelable {
    private String title;
    private String id;
    private boolean isSelected;
    private int positionTag;

    public int getPositionTag() {
        return positionTag;
    }

    public void setPositionTag(int positionTag) {
        this.positionTag = positionTag;
    }

    public Title() {

    }

    public Title(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.positionTag);
    }

    protected Title(Parcel in) {
        this.title = in.readString();
        this.id = in.readString();
        this.isSelected = in.readByte() != 0;
        this.positionTag = in.readInt();
    }

    public static final Creator<Title> CREATOR = new Creator<Title>() {
        @Override
        public Title createFromParcel(Parcel source) {
            return new Title(source);
        }

        @Override
        public Title[] newArray(int size) {
            return new Title[size];
        }
    };
}
