package com.commonlib.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseModel implements Parcelable {

    /*"user_id":"107",
"full_name":"shop owner 1",
"photo":"",
"address":"B 614, EBH",
"village_city":"Ranasan",
"taluka":"Vijapur",
"district":"Mahesana",
"pincode":"380060",
"lat":"23.58984540",
"lng":"72.68914440",
"shop_name":"shop 1",
"phone_number":"9429353458",
"distance":"54.23"*/

    /*"user_id":"146",
"full_name":"Sam",
"photo":"",
"address":"Prahladnagar",
"village_city":"Prahalad Gadh",
"taluka":"Gadhada",
"district":"Botad",
"pincode":"380060",
"lat":"23.07225329",
"lng":"72.51079134",
"shop_name":"Sampai",
"phone_number":"7016362475",
"distance":"39.15",
"role":"5"*/

    private String typeId;
    private String userId;
    private String name;
    private String photo;
    private String countryCode;
    private String phoneNumber;
    private String email;
    private String address;
    private String pincode;
    private String lat;
    private String lng;
    private boolean isSelected;

    public BaseModel() {
    }

    protected BaseModel(Parcel in) {
        this.typeId = in.readString();
        this.userId = in.readString();
        this.name = in.readString();
        this.photo = in.readString();
        this.countryCode = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.pincode = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
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
        dest.writeString(this.typeId);
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.countryCode);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.address);
        dest.writeString(this.pincode);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }
}
