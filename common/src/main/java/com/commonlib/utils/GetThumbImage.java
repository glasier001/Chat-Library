package com.commonlib.utils;

import android.net.Uri;

import java.util.List;

/**
 * Created by mauliksantoki on 10/5/17.
 */

public final class GetThumbImage {

    private GetThumbImage() {
    }

    public static final String THUMB_PREFIX = "thumb_";

    public static String getThumbImage(String url) {
        if (url != null) {
            Uri data = Uri.parse(url);
            if (data != null) {
                List<String> params = data.getPathSegments();
                if (params != null && params.size() > 0) {
                    String imgname = params.get(params.size() - 1); // "imgname"
                    url = url.replace(imgname, THUMB_PREFIX + imgname);
                    return url;
                }
            }
        }
        return "";
    }


}
