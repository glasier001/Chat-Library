package com.commonlib.ui.baseui;

import android.graphics.drawable.Drawable;

public interface BaseActivityView {
    /**
     * This method is use to check device connected with internet or not.
     *
     * @return if device connected then return true other wise return false
     * @version V1.0
     */
    public boolean isNetworkConnected();

    /**
     * this method is use to hide Keybord
     *
     * @version V1.0
     */
    public void hideKeyboard();

    /**
     * This method is use for get color from resource file.
     *
     * @param id pass resource id
     * @return return color.
     * @version V1.0
     */
    public int getResColor(int id);

    /**
     * This method is use for get drawable from resouce files
     *
     * @param id pass resource id
     * @return if device connected then return true other wise return false
     * @version V1.0
     */
    public Drawable getResDrawable(int id);


    public int getLayout();

}