package com.commonlib.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sagar on 13/3/18.
 */

public class Media implements Parcelable {

    private String mediaPath;
    private String mediaSize;
    private String mediaName;
    private boolean isSelected;
    private boolean isImage;
    private boolean isVideo;
    private boolean isMedia;

    public Media(String mediaPath, String mediaSize, String mediaName, boolean isSelected, boolean isImage) {
        this.mediaPath = mediaPath;
        this.mediaSize = mediaSize;
        this.mediaName = mediaName;
        this.isSelected = isSelected;
        this.isImage = isImage;
    }

    public Media() {
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean isMedia() {
        return isMedia;
    }

    public void setMedia(boolean media) {
        isMedia = media;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mediaPath);
        dest.writeString(this.mediaSize);
        dest.writeString(this.mediaName);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isImage ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isMedia ? (byte) 1 : (byte) 0);
    }

    protected Media(Parcel in) {
        this.mediaPath = in.readString();
        this.mediaSize = in.readString();
        this.mediaName = in.readString();
        this.isSelected = in.readByte() != 0;
        this.isImage = in.readByte() != 0;
        this.isVideo = in.readByte() != 0;
        this.isMedia = in.readByte() != 0;
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
