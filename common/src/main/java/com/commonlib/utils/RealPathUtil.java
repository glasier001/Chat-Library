package com.commonlib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import static com.commonlib.customdialogs.CustomCameraGalleryDialog.imageFilePath;
import static com.commonlib.utils.LogShowHide.LogShowHideMethod;


public final class RealPathUtil {

    private RealPathUtil(){}

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri); //Gets file type?

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1]; //Gets unique id of that file?

        String[] column = {MediaStore.Images.Media.DATA};     // Accesses data folder?

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?"; // Look all files of data folder?

		/*Simple cursor query with select argument*/

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);// getColumnIndex(String name) gives zero based index for the given column name or -1 if does not exist

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }

        return result;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        return cursor.getString(idx);
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getRealPath(Activity activity, Intent data) {
        if (Build.VERSION.SDK_INT < 19) {
            return RealPathUtil.getRealPathFromURI_API11to18(activity, data.getData());
            // SDK > 19 (Android 4.4)

        } else {
            Bundle dataExtras = data.getExtras();
            String path = dataExtras.getString(imageFilePath);
            LogShowHideMethod(activity,"imageFilePath: " + path + "");
            return path;
        }
    }
}

