package com.commonlib.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by mauliksantoki on 25/4/17.
 */

public final class Deprecation {

    private Deprecation() {
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
