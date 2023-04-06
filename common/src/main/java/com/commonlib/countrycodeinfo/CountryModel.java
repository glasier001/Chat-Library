package com.commonlib.countrycodeinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class CountryModel implements Parcelable {
    private String dialingCode="";
    private String isoCode="";
    private String coutryName="";
    private String header="";

    public long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(long headerId) {
        this.headerId = headerId;
    }

    private long headerId;
    private ArrayList<String> countryData;

    public CountryModel() {
    }

    public CountryModel(String dialingCode, String isoCode, String coutryName) {
        this.dialingCode = dialingCode;
        this.isoCode = isoCode;
        this.coutryName = coutryName;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getCoutryName() {
        return coutryName;
    }

    public void setCoutryName(String coutryName) {
        this.coutryName = coutryName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dialingCode);
        dest.writeString(this.isoCode);
        dest.writeString(this.coutryName);
        dest.writeString(this.header);
        dest.writeLong(this.headerId);
        dest.writeStringList(this.countryData);
    }

    protected CountryModel(Parcel in) {
        this.dialingCode = in.readString();
        this.isoCode = in.readString();
        this.coutryName = in.readString();
        this.header = in.readString();
        this.headerId = in.readLong();
        this.countryData = in.createStringArrayList();
    }

    public static final Creator<CountryModel> CREATOR = new Creator<CountryModel>() {
        @Override
        public CountryModel createFromParcel(Parcel source) {
            return new CountryModel(source);
        }

        @Override
        public CountryModel[] newArray(int size) {
            return new CountryModel[size];
        }
    };
}
