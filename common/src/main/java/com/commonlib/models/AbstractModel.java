package com.commonlib.models;

import java.util.ArrayList;

public class AbstractModel {


    private String title;

    private String message;

    private ArrayList<AbstractModel> singleItemModelArrayList;

    public AbstractModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public AbstractModel(String title, String message, ArrayList<AbstractModel> singleItemModelArrayList) {
        this.title = title;
        this.message = message;
        this.singleItemModelArrayList = singleItemModelArrayList;
    }


    public AbstractModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AbstractModel> getSingleItemArrayList() {
        return singleItemModelArrayList;
    }

    public void setSingleItemArrayList(ArrayList<AbstractModel> singleItemModelArrayList) {
        this.singleItemModelArrayList = singleItemModelArrayList;
    }
}
