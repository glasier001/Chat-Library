package com.commonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.commonlib.constants.AppStrings;
import com.commonlib.constants.IntentKeys;
import com.google.gson.Gson;


/**
 * Created by srdpatel on 2/25/2018.
 * Uses reflection-proof, serialization-proof, thread safe (AsyncTask, AsyncLoader), Double check lock, volatile and lazy (initialization) singleton pattern for SharedPreferences and SharedPreferences.Editor objects
 *
 * @see <a href="https://www.ibm.com/developerworks/library/j-dcl/index.html">http://google.com</a>
 * @since 1.0
 */

public final class SharedPrefs {

    private static volatile SharedPrefs mSharedPrefs;
    private static volatile SharedPreferences mSharedPreferences;
    private static volatile SharedPreferences.Editor mEditor;

    //Private constructor
    private SharedPrefs() {
        //Prevent from the reflection
        if (mSharedPrefs != null) {
            throw new RuntimeException(AppStrings.STR_MSG_ERROR_SHARED_PREF_REFLECTION);
        }
        //Prevent from the reflection
        if (mSharedPreferences != null) {
            throw new RuntimeException(AppStrings.STR_MSG_ERROR_SHARED_PREF_REFLECTION);
        }
        //Prevent from the reflection
        if (mEditor != null) {
            throw new RuntimeException(AppStrings.STR_MSG_ERROR_SHARED_PREF_REFLECTION);
        }
    }

    public static String getSharedPrefString(Context context, String key, String defaultValue) {
        return SharedPrefs.getSharedPref(context).getString(key, defaultValue);
    }

    /**
     * Gives SharedPreferences with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @param mContext Context
     * @return singleton and volatile SharedPreferences
     * see {@link #mSharedPreferences}
     * @since 1.0
     */
    public static SharedPreferences getSharedPref(Context mContext) {
        //Double check locking
        if (mSharedPreferences == null) { //Checking for the first time
            synchronized (SharedPrefs.class) {
                if (mSharedPreferences == null) { //Check for second time
                    mSharedPreferences = mContext.getSharedPreferences(AppStrings.STR_SHARED_PREF,
                            Context.MODE_PRIVATE); //Create new instance only if there is no instance ever created before
                }
            }
        }
        return mSharedPreferences;
    }

    public static SharedPreferences.Editor getPrivateEditor(Context mContext) {
        return getPrivateSharedPref(mContext).edit();
    }

    public static SharedPreferences getPrivateSharedPref(Context mContext) {
        return mContext.getSharedPreferences(AppStrings.STR_PRIVATE_PREF,
                Context.MODE_PRIVATE); //Create new instance only if there is no instance ever created before
    }

    public static void savePrefs(Context context, String key, String value) {
        SharedPreferences.Editor editor = SharedPrefs.getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Gives SharedPreferences.Editor with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @param mContext Context
     * @return singleton and volatile SharedPreferences.Editor
     * see {@link #mEditor}
     * @since 1.0
     */
    public static SharedPreferences.Editor getEditor(Context mContext) {
        //Double check locking
        if (mEditor == null) { //Checking for the first time
            synchronized (SharedPrefs.class) {
                if (mEditor == null) { //Check for second time
                    mEditor = getSharedPref(mContext).edit();
                }
            }
        }
        return mEditor;
    }

    public static void clearKeyValueStringData(Context context, String key) {
        SharedPreferences.Editor editor = SharedPrefs.getEditor(context);
        editor.remove(key);
        editor.putString(key, "");
        editor.apply();
    }

    public static void clearKeyValueStringData(Context context, String... keys) {
        SharedPreferences.Editor editor = SharedPrefs.getEditor(context);
        if (Utils.isNotNullNotEmpty(keys)) {
            for (String key : keys) {
                editor.remove(key);
                editor.putString(key, "");
                editor.apply();
            }
        }
    }


    public static void clearSharedPrefs(Context context) {
        SharedPreferences.Editor editor = SharedPrefs.getEditor(context);
        editor.clear();
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return SharedPrefs.getSharedPref(context).getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = SharedPrefs.getEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveParcelableObject(Context context, Object o) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(IntentKeys.PARCEL, json);
        editor.apply();
    }

    public static Object getParcelableObject(Context context, Class objectClass) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPref(context);
        String json = sharedPreferences.getString(IntentKeys.PARCEL, "");
        return gson.fromJson(json, objectClass);
    }

    /**
     * Protects from serialization and deserialization
     *
     * @since 1.0
     */
    protected SharedPrefs readResolve() {
        return getInstance();
    }

    /**
     * Gives SharedPrefs instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile SharedPrefs
     * see {@link #mSharedPrefs}
     * @since 1.0
     */
    public static SharedPrefs getInstance() {
        //Double check locking
        if (mSharedPrefs == null) { //Checking for the first time
            synchronized (SharedPrefs.class) {
                if (mSharedPrefs == null) { //Check for second time
                    mSharedPrefs = new SharedPrefs(); //Create new instance only if there is no instance ever created before
                }
            }
        }
        return mSharedPrefs;
    }
}