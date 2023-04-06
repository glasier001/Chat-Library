package com.commonlib.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by bhavika on 22/11/16.
 */

public class ReleaseMemory {
    File dir;
    String[] children;

    public ReleaseMemory(Context context) {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        trimCache(context);
        dir = null;
        children = null;
    }


    public void trimCache(Context context) {
        try {
            dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                boolean cache = deleteDir(dir);
                Log.e("After pressing Exit ", "cache memory clear result is::  " +
                        cache);
            }
        } catch (Exception e) {
// TODO: handle exception
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }
}
