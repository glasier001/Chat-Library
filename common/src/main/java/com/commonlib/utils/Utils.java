package com.commonlib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.commonlib.R;
import com.commonlib.constants.AppCodes;
import com.commonlib.constants.IntentKeys;
import com.commonlib.constants.JsonKeys;
import com.commonlib.constants.PreferencesKey;
import com.commonlib.countrycodeinfo.CountryCodeInfoController;
import com.commonlib.customadapters.loadmore.RecyclerViewLoadMoreScroll;
import com.commonlib.models.Media;
import com.commonlib.models.Photo;
import com.commonlib.typefacehandler.CustomTypefaceSpan;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.pm.PackageManager.GET_META_DATA;
import static android.os.Build.VERSION_CODES.P;
import static android.util.Log.d;
import static com.commonlib.constants.AppConstants.TAG;
import static com.commonlib.utils.PrepareMultipartBody.prepareFilePart;
import static com.commonlib.utils.StringUtils.convertToUpperCase;

/**
 * Created by sagar on 27/12/17.
 */

public final class Utils {

    private static String uniqueID = null;

    public static final double BYTE = 1024, KB = BYTE, MB = KB * BYTE, GB = MB * BYTE;

    public static DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

    private Utils() {
    }

    public static String getDeviceID(Context context, TelephonyManager phonyManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return "";
            }
        }
        String id = phonyManager.getDeviceId(); /*"";*/
        if (id == null) {
            id = "not available";
        }

        int phoneType = phonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_NONE:
                return "None: " + id;

            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM: IMEI=" + id;

            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA: MEID/ESN=" + id;

            default:
                return "UNKNOWN: ID=" + id;
        }
    }

    public static void EditTextEditFalse(View view) {
        view.setFocusable(false);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);
    }

    public static double Round_price(double total2) {
        // TODO Auto-generated method stub

        double factor = (float) (Math.pow(10, 2) / 5);
        double total = Math.round(total2 * factor) / factor;

        return total;
    }

    public static boolean DeleteImgRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteImgRecursive(child);

        return fileOrDirectory.delete();
    }


    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 5;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    public static final Drawable getRoundedBitmapDrawable(Activity activity, Bitmap img) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return new BitmapDrawable(activity.getResources(), img);
        } else {
            return new BitmapDrawable(img);
        }
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.GetResources(), img);
//        final float roundPx = (float) img.getWidth() * 0.06f;
//        roundedBitmapDrawable.setCornerRadius(roundPx);
//        return roundedBitmapDrawable;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + 30) + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static GradientDrawable setDrawableBackground(int bgcolor, int brdcolor, int borderStroke, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {

        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(bgcolor);
        gdDefault.setStroke(borderStroke, brdcolor);
        gdDefault.setCornerRadii(new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius});

        return gdDefault;
    }


    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = activity.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(color);
        }
    }

    public static boolean isSearchApiCall(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.has(JsonKeys.SEARCH_KEY)) {
                String searchKey = jsonObject.get(JsonKeys.SEARCH_KEY).getAsString();
                jsonObject.addProperty(JsonKeys.SEARCH_KEY, searchKey.trim());
                if (jsonObject.get(JsonKeys.SEARCH_KEY).isJsonNull() || jsonObject.get(JsonKeys.SEARCH_KEY).getAsString().isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    public static void applyFontToMenuItem(Activity activity, MenuItem mi, float size, Typeface font, int color, boolean isUpperCase) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new RelativeSizeSpan(size), 0, mNewTitle.length(), 0); // set size
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(color), 0, mNewTitle.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        if (isUpperCase) {
            mi.setTitle(convertToUpperCase(mNewTitle));
        } else {
            mi.setTitle(mNewTitle);
        }
    }

    public static void clearFocus(Activity activity, EditText customTextInputLayout) {
        KeyboardUtils.hideSoftInput(activity);
        customTextInputLayout.clearFocus();
    }

    public static void clearList(List classList) {
        if (classList != null && classList.size() > 0) {
            classList.clear();
        }
    }

    public static GradientDrawable getGradientDrawable(int colorStart, int colorEnd, GradientDrawable.Orientation gradientOrientation, float radius) {
        //Color.parseColor() method allow us to convert
        // a hexadecimal color string to an integer value (int color)
        int[] colors = {Color.parseColor("#30C5FF"), Color.parseColor("#25E5E4")};

        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(
                gradientOrientation, colors);
        gd.setCornerRadius(radius);
        return gd;
    }

    public static float getFloatFromDp(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static float getFloatFromSp(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
                context.getResources().getDisplayMetrics());
    }

    public static int getIntFromDp(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static int getIntFromAnyUnit(int unit, float size) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(unit, size, metrics);
    }

    public static void clearAllEditTextFields(ViewGroup group, EditText exception) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText && view != exception) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearAllEditTextFields((ViewGroup) view, exception);
        }
    }

    public static String convertBytesToSuitableUnit(long bytes) {
        String bytesToSuitableUnit = bytes + " B";
        if (bytes >= GB) {
            double tempBytes = bytes / GB;
            bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " GB";
            return bytesToSuitableUnit;
        }
        if (bytes >= MB) {
            double tempBytes = bytes / MB;
            bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " MB";
            return bytesToSuitableUnit;
        }
        if (bytes >= KB) {
            double tempBytes = bytes / KB;
            bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " kB";
            return bytesToSuitableUnit;
        }
        return bytesToSuitableUnit;
    }

    public static boolean isNotNullNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNotNullNotEmpty(Object obj) {
        return obj != null;
    }

    public static List removeElements(List list, Integer... indexes) {
        Arrays.sort(indexes, Collections.reverseOrder());
        for (int i : indexes) {
            if (list.size() > i) {
                list.remove(i);
            }
        }
        return list;
    }

    public static void toggleVisibility(int visibility, View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setVisibility(visibility);
            }
        }
    }

    public static String getCountryCode(Context context, boolean addPlusPrefix) {
        if (addPlusPrefix) {
            return (String.format("+%s", new CountryCodeInfoController(context).getUserCountryCode().getDialingCode()));
        } else {
            return new CountryCodeInfoController(context).getUserCountryCode().getDialingCode();
        }
    }

    public static int dpToPx(Context activity, int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Activity activity, float px) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int pxToDp(Activity activity, int px) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static JsonObject getDefaultPost(SharedPreferences sharedPreferences) {
        JsonObject postData = new JsonObject();
        postData.addProperty(JsonKeys.USER_ID, sharedPreferences.getString(PreferencesKey.USER_ID, ""));
        postData.addProperty(JsonKeys.LOGIN_TOKEN, sharedPreferences.getString(PreferencesKey.LOGIN_TOKEN, ""));
        return postData;
    }

    public static boolean hasApi(Intent intent) {
        return intent != null && intent.hasExtra(IntentKeys.API);
    }

    public static boolean hasApi(Bundle bundle) {
        return bundle != null && bundle.containsKey(IntentKeys.API);
    }

    public static boolean hasSourceScreen(Bundle bundle) {
        return bundle != null && bundle.containsKey(IntentKeys.SOURCE_SCREEN);
    }

    public static boolean hasSourceScreen(Intent intent) {
        return intent != null && intent.hasExtra(IntentKeys.SOURCE_SCREEN);
    }

    public static String getApi(Intent intent) {
        String api = "";
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.API)) {
                return intent.getStringExtra(IntentKeys.API);
            }
        }
        return api;
    }

    public static String getApi(Bundle bundle) {
        String api = "";
        if (bundle != null) {
            if (bundle.containsKey(IntentKeys.API)) {
                return bundle.getString(IntentKeys.API);
            }
        }
        return api;
    }

    public static int getPositionTag(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.POSITION_TAG)) {
                return intent.getIntExtra(IntentKeys.POSITION_TAG, -1);
            }
        }
        return -1;
    }

    public static String getSourceScreen(Intent intent) {
        String sourceScreen = "";
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.SOURCE_SCREEN)) {
                return intent.getStringExtra(IntentKeys.SOURCE_SCREEN);
            }
        }
        return sourceScreen;
    }

    public static String getSourceScreen(Bundle bundle) {
        String sourceScreen = "";
        if (bundle != null) {
            if (bundle.containsKey(IntentKeys.SOURCE_SCREEN)) {
                return bundle.getString(IntentKeys.SOURCE_SCREEN, "");
            }
        }
        return sourceScreen;
    }

    public static String getTitle(Intent intent) {
        String title = "";
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.TITLE)) {
                return intent.getStringExtra(IntentKeys.TITLE);
            }
        }
        return title;
    }

    public static Object getParcel(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.PARCEL)) {
                return intent.getParcelableExtra(IntentKeys.PARCEL);
            }
        }
        return null;
    }

    public static void putParcel(Object parcelable, Intent intent) {
        if (intent != null && parcelable != null) {
            intent.putExtra(IntentKeys.PARCEL, (Parcelable) parcelable);
        }
    }

    public static void putParcelableList(List parcelableList, Intent intent) {
        if (Utils.isNotNullNotEmpty(parcelableList) && intent != null) {
            intent.putParcelableArrayListExtra(IntentKeys.PARCEL_LIST, (ArrayList<? extends Parcelable>) parcelableList);
        }
    }

    public static boolean isNotNullNotEmpty(List list) {
        // sagar : 31/12/18 or you can use isEmpty too
        return list != null && list.size() > 0;
    }

    public static String getString(Bundle bundle, String key, String defaultValue) {
        if (bundle != null) {
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        }
        return defaultValue;
    }

    public static String getString(Intent intent, String key, String defaultValue) {
        if (intent != null) {
            if (intent.hasExtra(key)) {
                return intent.getStringExtra(key);
            }
        }
        return defaultValue;
    }

    public static String getMessage(Intent intent) {
        if (Utils.hasMessage(intent)) {
            return intent.getStringExtra(IntentKeys.IK_MESSAGE);
        }
        return "";
    }

    public static boolean hasMessage(Intent intent) {
        if (intent != null) {
            return intent.hasExtra(IntentKeys.IK_MESSAGE);
        }
        return false;
    }

    public static boolean hasParcel(Intent intent) {
        if (intent != null) {
            return intent.hasExtra(IntentKeys.PARCEL);
        }

        return false;
    }

    public static boolean hasParcel(Bundle bundle) {
        if (bundle != null) {
            return bundle.containsKey(IntentKeys.PARCEL);
        }

        return false;
    }

    public static boolean hasParcelList(Bundle bundle) {
        if (bundle != null) {
            return bundle.containsKey(IntentKeys.PARCEL_LIST);
        }

        return false;
    }

    public static boolean hasParcelList(Intent intent) {
        if (intent != null) {
            return intent.hasExtra(IntentKeys.PARCEL_LIST);
        }

        return false;
    }

    public static List getParcelList(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.PARCEL_LIST)) {
                return intent.getParcelableArrayListExtra(IntentKeys.PARCEL_LIST);
            }
        }
        return null;
    }

    public static Object getParcel(Bundle intent) {
        if (intent != null) {
            if (intent.getParcelable(IntentKeys.PARCEL) != null) {
                return intent.getParcelable(IntentKeys.PARCEL);
            }
        }
        return null;
    }

    public static List getParcelList(Bundle bundle) {
        if (bundle != null) {
            if (bundle.getParcelableArrayList(IntentKeys.PARCEL_LIST) != null) {
                return bundle.getParcelableArrayList(IntentKeys.PARCEL_LIST);
            }
        }
        return null;
    }

    public static String getUserId(SharedPreferences sharedPreferences) {
        String userId = "";
        if (sharedPreferences != null) {
            return sharedPreferences.getString(PreferencesKey.USER_ID, "");
        }
        return userId;
//        return "148";
    }

    public static String getUserPhoto(Context context) {
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.PHOTO, "");
    }

    public static String getUserName(Context context) {
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.FULL_NAME, "");
    }

    public static String getDeviceToken(Context context) {
        return SharedPrefs.getPrivateSharedPref(context).getString(PreferencesKey.DEVICE_TOKEN, "");
    }

    public static void updateDeviceToken(Context context, String token) {
        SharedPrefs.getPrivateEditor(context).putString(PreferencesKey.DEVICE_TOKEN, token).apply();
    }

    public static String getUserCountryCode(Context context) {
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.COUNTRY_CODE, "");
//        return "91";
    }

    public static String getUserPhone(Context context) {
//        return "9429359528";
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.PHONE_NUMBER, "");
    }

    public static String getUserEmail(Context context) {
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.EMAIL, "");
    }

    public static String getUserEmailPhone(Context context) {
//        return "9429359528";
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.EMAIL_PHONE, "");
    }

    public static String getUserPassword(Context context) {
        return "121212";
        /*return SharedPrefs.getSharedPref(context).getString(PreferencesKey.PASSWORD, "");*/
    }

    public static String getUserDeviceToken(Context context) {
        return "KJB543B5B53N54MN3B5";
        /*return AppConstants.DEVICE_TOKEN;*/
    }

    public static String getUserRole(Context context) {
        /*return "5";*/
        return SharedPrefs.getSharedPrefString(context, PreferencesKey.ROLE, "");
    }

    public static String getPrefString(Context context, String key, String defaultValue) {
        return SharedPrefs.getSharedPref(context).getString(key, defaultValue);
    }

    public static String getLoginToken(SharedPreferences sharedPreferences) {
        /*String loginToken = "";
        if (sharedPreferences != null) {
            return sharedPreferences.getString(PreferencesKey.LOGIN_TOKEN, "");
        }
        return loginToken;*/
        return "121212";
    }

    public static String getUserType(SharedPreferences sharedPreferences) {
        String userRole = "";
        if (sharedPreferences != null) {
            return sharedPreferences.getString(PreferencesKey.USER_TYPE, "");
        }
        return userRole;
    }

    public static long getLongFromLongArray(long[] longs) {
        if (longs != null && longs.length > 0) {
            return longs[0];
        }
        return 0;
    }

    public static String getString(Intent intent, String key) {
        if (intent != null) {
            if (intent.hasExtra(key)) {
                String stringExtra = intent.getStringExtra(key);
                if (stringExtra != null) {
                    return stringExtra;
                }
            }
        }
        return "";
    }

    public static String getPhoneNumber(LinkedTreeMap linkedTreeMap) {
        if (linkedTreeMap != null) {
            return String.valueOf(linkedTreeMap.get(JsonKeys.PHONE_NUMBER));
        }
        return "";
    }

    public static String getCountryCode(LinkedTreeMap linkedTreeMap) {
        if (linkedTreeMap != null) {
            return String.valueOf(linkedTreeMap.get(JsonKeys.COUNTRY_CODE));
        }
        return "";
    }

    public static String getPrettyPrinting(String string) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(string));
    }

    public static void showErrorToast(Context activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(DateTimeFormats.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static String getSharedPrefString(Context context, String key, String defaultValue) {
        return SharedPrefs.getSharedPref(context).getString(key, defaultValue);
    }

    public static String getKm(String distance) {
        String finalKm = null;
        int km = 0;
        double dis;
        double decimal;

        if (!StringUtils.isNotNullNotEmpty(distance)) {
            return "0";
        }

        dis = Double.parseDouble(distance);
        decimal = (dis % 1);

        if (decimal == 0) {
            if (dis == 0) {
                km = (int) dis;

                finalKm = String.valueOf(km + " km");
            } else {
                km = (int) dis;

                DecimalFormat formatter = new DecimalFormat("#,###,###");
                formatter.setMinimumFractionDigits(0);
                formatter.setMaximumFractionDigits(1);
                finalKm = String.valueOf(formatter.format(km) + " " + "km");

//                Log.v("cu.deci==0 dis!=0 sform", String.format(Locale.US, "%.1f", decimal));
                finalKm = String.format(Locale.US, "%.1f", dis) + " km";
            }
        } else {
            if (dis > 1) {

//                finalKm = String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(dis) + " " + "km");

//                Log.v("cu.deci!=0 dis>1 sform", String.format(Locale.US, "%.1f", dis));
                finalKm = String.format(Locale.US, "%.1f", dis) + " km";

            } else if (dis < 1) {
                km = (int) (dis * 1000);
                finalKm = String.valueOf(km + " " + "mtr");
            }
        }
        return finalKm;
    }

    public static String convertToKm(String distance) {
        String finalKm = null;
        int km = 0;
        double dis;
        double decimal;

        if (!StringUtils.isNotNullNotEmpty(distance)) {
            return "0";
        }

        dis = Double.parseDouble(distance);
        decimal = (dis % 1);

        if (decimal == 0) {
            if (dis == 0) {
                km = (int) dis;
                finalKm = String.valueOf(km + " " + "km");
            } else {
                km = (int) dis;
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                finalKm = String.valueOf(formatter.format(km) + " " + "km");
            }
        } else {
            if (dis > 1) {
                finalKm = String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(dis) + " " + "km");
            } else if (dis < 1) {
                km = (int) (dis * 1000);
                finalKm = String.valueOf(km + " " + "mtr");
            }
        }
        return finalKm;
    }

    public static Object getNotNullObject(Object yourObject, Object defaultObject) {
        if (yourObject == null) {
            return defaultObject;
        }
        return yourObject;
    }

    public static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    public static List<JsonObject> getJsonObjList(JsonObject jsonObject) {
        List<JsonObject> jsonObjectList = new ArrayList<>();
        if (jsonObject != null) {
            jsonObjectList.add(jsonObject);
            return jsonObjectList;
        }
        return jsonObjectList;
    }

    public static List<JsonObject> getJsonListFromString(String jsonString) {
        if (StringUtils.isNotNullNotEmpty(jsonString)) {
            Gson gson = new Gson();
            Type jsonObjType = new TypeToken<List<JsonObject>>() {
            }.getType();
            return gson.fromJson(jsonString, jsonObjType);
        }
        return null;
    }

    public static String getStringFromJson(Object object) {
        String defaultString = "";
        if (object != null) {
            if (object instanceof JsonObject) {
                return getStringFromJsonObject((JsonObject) object);
            } else if (object instanceof List) {
                // FIXME: sagar : 26/12/18 List can be either of jsonObject or of LinkedTreeMap type
                return getStringFromJsonList((List<JsonObject>) object);
            } else if (object instanceof LinkedTreeMap) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
                return getStringFromJsonObject(jsonObject);
            }
        }
        return defaultString;
    }

    public static String getStringFromJsonObject(JsonObject jsonObject) {
        if (jsonObject != null) {
            Gson gson = new Gson();
            Type jsonObjType = new TypeToken<JsonObject>() {
            }.getType();
            return gson.toJson(jsonObject, jsonObjType);
        }
        return "";
    }

    public static String getStringFromJsonList(List<JsonObject> jsonObjectList) {
        if (jsonObjectList != null) {
            if (jsonObjectList.size() > 0) {
                Gson gson = new Gson();
                Type jsonObjType = new TypeToken<List<JsonObject>>() {
                }.getType();
                return gson.toJson(jsonObjectList, jsonObjType);
            }
        }
        return "";
    }

    public static String getStringFromLinkedMap(LinkedTreeMap treeMap) {
        if (treeMap != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(treeMap).getAsJsonObject();
            return getStringFromJsonObject(jsonObject);
        }
        return "";
    }

    public static JsonObject getJsonObjectFromString(String jsonString) {
        if (StringUtils.isNotNullNotEmpty(jsonString)) {
            Gson gson = new Gson();
            Type jsonObjType = new TypeToken<JsonObject>() {
            }.getType();
            return gson.fromJson(jsonString, jsonObjType);
        }
        return null;
    }

    // sagar : 26/12/18 Use  this method when you do not know whether the key would give LinkedTreeMap, List<LinkedTreeMap> or JsonObject
    public static String getStringFromResponseObject(LinkedTreeMap dataMap, Object key) {
        String defaultString = "";
        if (dataMap != null && key != null) {
            Object object = dataMap.get(key);
            if (object instanceof JsonObject) {
                return Utils.getStringFromJsonObject((JsonObject) object);
            } else if (object instanceof List) {
                if (((List) object).size() > 0) {
                    List list = (List) object;
                    List<JsonObject> jsonObjectList = new ArrayList<>();
                    for (Object o : list) {
                        if (o instanceof LinkedTreeMap) {
                            jsonObjectList.add(Utils.getJsonObjectFromLinkedTreeMap((LinkedTreeMap) o));
                        }
                    }
                    if (jsonObjectList.size() > 0) {
                        return Utils.getStringFromJsonList(jsonObjectList);
                    }
                }
            } else if (object instanceof LinkedTreeMap) {
                return Utils.getStringFromJsonObject(Utils.getJsonObjectFromLinkedTreeMap((LinkedTreeMap) object));
            }
        }
        return defaultString;
    }

    public static JsonObject getJsonObjectFromLinkedTreeMap(LinkedTreeMap treeMap) {
        Gson gson = new Gson();
        return gson.toJsonTree(treeMap).getAsJsonObject();
    }

    public static List<LinkedTreeMap> getLinkedTreeMapListFromJsonArray(String jsonArray) {
        Type listType = new TypeToken<List<LinkedTreeMap>>() {
        }.getType();
        return new Gson().fromJson(jsonArray, listType);
    }

    public static LinkedTreeMap getLinkedTreeMapFromJsonObject(String jsobObject) {
        Type treeMap = new TypeToken<LinkedTreeMap>() {
        }.getType();
        return new Gson().fromJson(jsobObject, treeMap);
    }

    public static String getKeyValue(String key, LinkedTreeMap map) {
        if (StringUtils.isNotNullNotEmpty(key) && map != null) {
            if (map.containsKey(key)) {
                return String.valueOf(map.get(key));
            }
        }

        return "";
    }

    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static String getLoginToken(Context context) {
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.LOGIN_TOKEN, "");
//        return "121212";
    }

    public static boolean hasMoreData(List list) {
        return list != null && list.size() >= Limits.PAGENUMBER_LIMIT;
    }

    public static boolean isDifferentPhoneOrCountryCode(Context context, String countryCode, String phone) {
        return !SharedPrefs.getSharedPrefString(context, PreferencesKey.COUNTRY_CODE, "").equalsIgnoreCase(countryCode)
                || !SharedPrefs.getSharedPrefString(context, PreferencesKey.PHONE_NUMBER, "").equalsIgnoreCase(phone);
    }

    public static boolean hasFlash(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public static int getPercentage(int small, int big) {
        return (small * 100) / big;
    }

    public static String getString(LinkedTreeMap treeMap, String key, String defaultReturn) {
        if (treeMap != null) {
            if (StringUtils.isNotNullNotEmpty(key)) {
                if (treeMap.containsKey(key)) {
                    return String.valueOf(treeMap.get(key));
                } else {
                    return defaultReturn;
                }
            } else {
                return defaultReturn;
            }
        }
        return defaultReturn;
    }

    public static void changeLangauge(Context context, String languageCode) {

        // sagar : 30/1/19 4:22 PM
        /*https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758*/

        Locale defaultLocale = Locale.getDefault();
        String countryCode = defaultLocale.getCountry();

        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }

        resources.updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

    public static void restartApplication(Activity activity, Class targetClass) {
        Intent intent = new Intent(activity, targetClass);
        intent.putExtra(IntentKeys.SOURCE_SCREEN, activity.getClass().getSimpleName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finishAffinity();
//        activity.finish();

        // sagar : 30/1/19 4:45 PM https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758

//        System.exit(0);
    }

    public static List getList(Object... objects) {
        List list = null;
        if (objects != null) {
            if (objects.length > 0) {
                for (Object o : objects) {
                    if (o instanceof List) {
                        list = (List) o;
                        break;
                    }
                }
            }
        }
        return list;
    }

    public static Object getParcel(Object... objects) {
        Object parcelable = null;
        if (Utils.isNotNullNotEmpty(objects)) {
            for (Object o : objects) {
                if (o instanceof Parcelable) {
                    parcelable = o;
                    break;
                }
            }
        }

        return parcelable;
    }

    public static boolean isNotNullNotEmpty(Object... objects) {
        return objects != null && objects.length > 0;
    }

    public static boolean hasParcel(Object... objects) {
        if (Utils.isNotNullNotEmpty(objects)) {
            for (Object o : objects) {
                if (o instanceof Parcelable) {
                    return true;
                }
            }
        }

        return false;
    }

    public static List getList(Object object) {
        List list = null;
        if (object != null) {
            if (object instanceof List) {
                list = (List) object;
                return list;
            }
        }
        return null;
    }

    public static LinkedTreeMap getLinkedTreeMap(Object... objects) {
        LinkedTreeMap ltm = null;
        if (objects != null) {
            if (objects.length > 0) {
                for (Object o : objects) {
                    if (o instanceof LinkedTreeMap) {
                        ltm = (LinkedTreeMap) o;
                        break;
                    }
                }
            }
        }
        return ltm;
    }

    public static LinkedTreeMap getLinkedTreeMap(Object object) {
        LinkedTreeMap ltm = null;
        if (object != null) {
            if (object instanceof LinkedTreeMap) {
                ltm = (LinkedTreeMap) object;
                return ltm;
            }
        }
        return null;
    }

    public static String hexString(Resources res) {
        Object resImpl = getPrivateField("android.content.res.Resources", "mResourcesImpl", res);
        Object o = resImpl != null ? resImpl : res;
        return "@" + Integer.toHexString(o.hashCode());
    }

    public static Object getPrivateField(String className, String fieldName, Object object) {
        try {
            Class c = Class.forName(className);
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(object);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resetActivityTitle(Activity a) {
        try {
            ActivityInfo info = a.getPackageManager().getActivityInfo(a.getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                a.setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static String getTitleCache() {
        // https://developer.android.com/about/versions/pie/restrictions-non-sdk-interfaces
        if (isAtLeastVersion(P)) return "Can't access title cache\nstarting from API 28";
        Object o = getPrivateField("android.app.ApplicationPackageManager", "sStringCache", null);
        Map<?, WeakReference<CharSequence>> cache = (Map<?, WeakReference<CharSequence>>) o;
        if (cache == null) return "";

        StringBuilder builder = new StringBuilder("Cache:").append("\n");
        for (Map.Entry<?, WeakReference<CharSequence>> e : cache.entrySet()) {
            CharSequence title = e.getValue().get();
            if (title != null) {
                builder.append(title).append("\n");
            }
        }
        return builder.toString();
    }

    public static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static Resources getTopLevelResources(Activity a) {
        try {
            return a.getPackageManager().getResourcesForApplication(a.getApplicationInfo());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showToastMessage(Activity activity, String message, boolean isError) {
        if (isError) {
            showErrorMessage(activity, message);
        } else {
            showSuccessMessage(activity, message);
        }
    }

    public static void showErrorMessage(Activity activity, String message) {
        CustomToastMessage.animRedTextMethod(activity, message);
    }

    public static void showSuccessMessage(Activity activity, String message) {
        CustomToastMessage.animGreenTextMethod(activity, message);
    }

    public static boolean isSuccess(String responseMessage) {
        return JsonKeys.SUCCESS.equalsIgnoreCase(responseMessage);
    }

    public static void savePrefs(Context context, String key, String value) {
        SharedPreferences.Editor editor = SharedPrefs.getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    public static String getCurrentLat(Activity activity, boolean showSettings) {
        if (activity != null) {
            LatLng latLng = GetUserLocation.getCurrentLocation(activity, showSettings);
            return Utils.getDefaultLatLng(latLng.latitude);
        }

        return "";
    }

    public static String getDefaultLatLng(Double val) {
        if (val != null && val > 0.0) {
            return String.valueOf(val);
        } else {
            return "";
        }
    }

    public static String getCurrentLng(Activity activity, boolean showSettings) {
        if (activity != null) {
            LatLng latLng = GetUserLocation.getCurrentLocation(activity, showSettings);
            return Utils.getDefaultLatLng(latLng.longitude);
        }

        return "";
    }

    public static void replacePostDataKeyValue(JsonObject jsonObject, String key, String value, boolean allowEmptyValue) {
        replaceJsonKeyValue(jsonObject, key, value, allowEmptyValue);
    }

    public static void replaceJsonKeyValue(JsonObject jsonObject, String key, String value, boolean allowEmptyValue) {
        if (jsonObject != null) {
            if (StringUtils.isNotNullNotEmpty(key, !allowEmptyValue ? value : "temp")) {
                if (jsonObject.has(key)) {
                    jsonObject.remove(key);
                }
                jsonObject.addProperty(key, value);
            }
        }
    }

    private static void invokeCallPermission(Activity activity, String phoneWithoutPlusPrefix) {

        // sagar : 3/4/19 3:39 PM According to google guideline, we will open dial pad only and not perform calling
        /*PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openDialingPadToCall(activity, phoneWithoutPlusPrefix);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(MarshMallowPermissionMessage.getCallDeniedMessage(activity))
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();*/

        openDialingPadToCall(activity, phoneWithoutPlusPrefix);
    }

    public static void openDialingPadToCall(Activity activity, String phoneWithoutPlusPrefix) {
        String uri = "tel:" + "+" + phoneWithoutPlusPrefix.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));

        // sagar : 3/4/19 11:44 AM We may not need any permission to just open dialpad. However, If you are still using ACTION_CALL even after google warning, you will need to ask for permission
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                invokeCallPermission(activity, phoneWithoutPlusPrefix);
                return;
            }
        }*/

        d(TAG, "Utils: openDialingPadToCall: starting");

        activity.startActivity(intent);
    }

    public static void openDialingPadToCall(Activity activity, String phone, boolean addPlusPrefix) {
        String uri = null;
        if (addPlusPrefix) {
            uri = "tel:" + "+" + phone.trim();
        } else {
            uri = "tel:" + phone.trim();
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));

        // sagar : 3/4/19 11:44 AM We may not need any permission to just open dialpad. However, If you are still using ACTION_CALL even after google warning, you will need to ask for permission
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                invokeCallPermission(activity, phoneWithoutPlusPrefix);
                return;
            }
        }*/

        if (ImplicitIntentUtil.resolveActivity(activity, intent)) {
            activity.startActivityForResult(intent, AppCodes.CODE_CALL_PHONE);
        } else {
            showErrorMessage(activity, "No app could be found ready to perform this action");
        }
    }

    public static List<Photo> getActualPhotoList(List<Photo> currentPhotoList) {
        List<Photo> actualPhotoList = new ArrayList<>();
        if (Utils.isNotNullNotEmpty(currentPhotoList)) {
            for (Photo photo : currentPhotoList) {
                if (photo != null && StringUtils.isNotNullNotEmpty(photo.getPhotoUrl())) {
                    actualPhotoList.add(photo);
                }
            }
        }
        return actualPhotoList;
    }

    public static int getLastElementIndex(List list) {
        if (Utils.isNotNullNotEmpty(list)) {
            return list.size() - 1;
        }
        return -1;
    }

    public static void commitFragmentTransaction(FragmentManager fragmentManager, Fragment fragment, Bundle bundle, int containerResId) {
        if (fragmentManager != null && fragment != null && bundle != null && containerResId != -1) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            ft.add(containerResId, fragment);
            ft.commit();
        }
    }

    public static String removeTrailingZeroes(String number) {
        return number.contains(".") ? number.replaceAll("0*$", "").replaceAll("\\.$", "") : number;
    }

    public static List getTypeList(List<LinkedTreeMap> linkedTreeMapList, Class listType) {
        /*https://pavneetsblog.wordpress.com/2017/09/23/retrofit-and-linkedtreemap-class-cast-exception/*/
        if (Utils.isNotNullNotEmpty(linkedTreeMapList)) {
            Gson gson = new Gson();
            String data = Utils.getStringFromLinkedList(linkedTreeMapList);

            try {
                List typeList = gson.fromJson(data, TypeToken.getParameterized(ArrayList.class, listType).getType());

                if (Utils.isNotNullNotEmpty(typeList)) {
                    d(TAG, "Utils: getTypeList: " + typeList.size());
                    return typeList;
                }
            } catch (JsonSyntaxException e) {
                // sagar : 12/2/19 3:59 PM Type cast exception
                e.printStackTrace();
            }
        }
        //endregion

        return null;
    }

    public static String getStringFromLinkedList(List<LinkedTreeMap> linkedTreeMapList) {
        if (linkedTreeMapList != null) {
            if (linkedTreeMapList.size() > 0) {
                Gson gson = new Gson();
                Type jsonObjType = new TypeToken<List<LinkedTreeMap>>() {
                }.getType();
                return gson.toJson(linkedTreeMapList, jsonObjType);
            }
        }
        return "";
    }

    public static boolean isNotNullNotEmpty(LinkedTreeMap linkedTreeMap) {
        return linkedTreeMap != null && linkedTreeMap.size() > 0;
    }

    public static boolean hasNextElement(List list, int currentPosition) {
        if (Utils.isNotNullNotEmpty(list)) {
            if (currentPosition != -1 && list.size() > currentPosition) {
                return list.size() > currentPosition + 1;
            }
        }
        return false;
    }

    public static boolean hasPreviousElement(List list, int currentPosition) {
        if (Utils.isNotNullNotEmpty(list)) {
            if (currentPosition != -1 && currentPosition - 1 >= 0 && list.size() > currentPosition) {
                return currentPosition - 1 >= 0;
            }
        }
        return false;
    }

    public static boolean isLogin(Context context) {
        return StringUtils.isNotNullNotEmpty(Utils.getUserId(context));
    }

    public static String getUserId(Context context) {
        return SharedPrefs.getSharedPref(context).getString(PreferencesKey.USER_ID, "");
//        return "148";
    }

    public static void composeEmail(String subject, Activity activity, String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: " + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        activity.startActivity(emailIntent);
    }

    public static String getImagePath(Intent intent) {
        if (hasImagePath(intent)) {
            return intent.getStringExtra(IntentKeys.IK_IMAGE_PATH);
        }

        return "";
    }

    public static boolean hasImagePath(Intent intent) {
        return intent != null && intent.hasExtra(IntentKeys.IK_IMAGE_PATH);
    }

    public static LatLng getLatLngFromAddress(Context context, String strAddress) {
        d("Utils", "getLatLngFromAddress: -" + strAddress);
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static boolean isNotNullNotEmptyLocation(Location location) {
        return location != null && location.getLatitude() > 0.0 && location.getLongitude() > 0.0;
    }

    public static boolean isNullOrEmptyLocation(Location location) {
        return location == null || location.getLatitude() == 0.0 || location.getLongitude() == 0.0;
    }

    // TODO: 24/1/18  sagar: move to utils
    public static boolean isAppInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void setLoaded(RecyclerViewLoadMoreScroll loadMoreScroll) {
        if (loadMoreScroll != null) {
            loadMoreScroll.setLoaded();
        }
    }

    public static boolean isNotFirstNotLast(int position, List list) {
        return position != -1 && Utils.isNotNullNotEmpty(list) && position != 0 && position != list.size() - 1;
    }

    public static List<Media> getMediaList(List<Uri> uriList, List<String> pathList) {
        List<Media> mediaList = new ArrayList<>();
        if (Utils.isNotNullNotEmpty(uriList) && Utils.isNotNullNotEmpty(pathList)) {
            for (String path : pathList) {
                Media media = new Media();
                media.setMediaPath(path);
                mediaList.add(media);
            }
        }
        return mediaList;
    }

    public static List<MultipartBody.Part> getMultiParts(Activity activity, String name, List<String> mediaPathList) {

        /*post_data={"sender_user_id":"112","receiver_user_id":"89","login_token":"121212","file_type":"1","file_count":"2","next_user_consultation_chat_log_id":"55"}&file_path1=FILE1&file_path2=FILE2*/

        // sagar : 17/4/19 8:21 PM Here, file_path is a name

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < mediaPathList.size(); i++) {
            parts.add(prepareFilePart(activity, name + (i + 1), Uri.parse("file://" + mediaPathList.get(i).replace("file://", ""))));
        }
        return parts;
    }

    public static MultipartBody.Part getMultiPart(String mediaPath) {

        // sagar : 17/4/19 8:22 PM

        MultipartBody.Part body = null;
        if (StringUtils.isNotNullNotEmpty(mediaPath)) {
            // creates RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(mediaPath));
            // MultipartBody.Part is used to send also the actual filename
            body = MultipartBody.Part.createFormData("photo", new File(mediaPath).getName(), requestFile);
        }

        return body;
    }

    public static boolean isOfflineData(Intent intent) {
        if (intent != null && intent.hasExtra(IntentKeys.IS_OFFLINE)) {
            return intent.getBooleanExtra(IntentKeys.IS_OFFLINE, false);
        }
        return false;
    }

    public static boolean getBoolean(Intent intent, String key) {
        if (intent != null && intent.hasExtra(key)) {
            return intent.getBooleanExtra(key, false);
        }

        return false;
    }

    public static boolean containsNull(List mList) {
        if (Utils.isNotNullNotEmpty(mList)) {
            return mList.contains(null);
        }
        return false;
    }

    public static boolean containsNull(List mList, int index) {
        if (Utils.isNotNullNotEmpty(mList) && Utils.hasElement(mList, index)) {
            return mList.get(index) == null;
        }
        return false;
    }

    public static boolean hasElement(List list, int position) {
        return hasIndex(position, list);
    }

    public static boolean hasIndex(int position, List list) {
        return position != -1 && Utils.isNotNullNotEmpty(list) && position < list.size();
    }

    public synchronized static String getUUID(Context context) {
        // Bhargav Savasani : 1/11/19 more info to Visit this Link --> https://medium.com/@ssaurel/how-to-retrieve-an-unique-id-to-identify-android-devices-6f99fd5369eb
        try {
            if (uniqueID == null) {
                uniqueID = SharedPrefs.getPrivateSharedPref(context).getString(PreferencesKey.DEVICE_UUID, null);
                if (uniqueID == null) {
                    uniqueID = UUID.randomUUID().toString();
                    SharedPrefs.getPrivateEditor(context).putString(PreferencesKey.DEVICE_UUID, uniqueID).apply();
                }
            }
            return uniqueID;
        } catch (Exception e) {
            d(TAG, "Utils: getUUID: " + e.getMessage());
            return "";
        }

       /* String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        d("Utils_id", "getUUID :- " + uniqueID);
        d("Utils_id", "getAndroidId :- " + androidId);*/
    }

    public static String getAndroidSecureId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
