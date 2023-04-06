package com.commonlib.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.commonlib.R;
import com.commonlib.constants.AppCodes;
import com.commonlib.constants.AppConstants;
import com.commonlib.customadapters.ViewPagerAdapter;
import com.commonlib.ui.baseui.GlideApp;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import static android.util.Log.d;
import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

public final class ViewUtils {

    private static final String TAG = "dd: ViewUtils";

    ;

    private ViewUtils() {
    }

    public static void setOnClickListener(Activity activity, boolean hasProgressiveRipple, View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setClickable(!hasProgressiveRipple);
                view.setEnabled(true);
                try {
                    view.setOnClickListener((View.OnClickListener) activity);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "implement listener", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(activity, "NPE: No Views", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNotNullNotEmpty(View... views) {
        return views != null && views.length > 0;
    }

    public static void setOnClickListener(Fragment fragment, boolean hasProgressiveRipple, View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setClickable(!hasProgressiveRipple);
                view.setEnabled(true);
                try {
                    view.setOnClickListener((View.OnClickListener) fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Toast.makeText(fragment.getContext(), "implement listener", Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        try {
                            Toast.makeText(fragment.getActivity(), "implement listener", Toast.LENGTH_SHORT).show();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            Toast.makeText(view.getContext(), "implement listener", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } else {
            try {
                Toast.makeText(fragment.getContext(), "NPE: No Views", Toast.LENGTH_SHORT).show();
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(fragment.getActivity(), "NPE: No Views", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public static void setEnable(boolean isEnable, View... views) {
        if (isEnable) {
            setEnable(views);
        } else {
            setDisable(views);
        }
    }

    public static void setEnable(View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setEnabled(true);
            }
        } else {
            d(TAG, "setDisable: NPE: No Views");
        }
    }

    public static void setDisable(View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setEnabled(false);
            }
        } else {
            d(TAG, "setDisable: NPE: No Views");
        }
    }

    public static void showViews(boolean show, View... views) {
        if (show) {
            toggleVisibility(View.VISIBLE, views);
        } else {
            toggleVisibility(View.GONE, views);
        }
    }

    public static void toggleVisibility(int visibility, View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setVisibility(visibility);
                view.invalidate();
                view.requestLayout();
            }
        } else {
            d(TAG, "toggleVisibility: NPE: No Views");
        }
    }

    public static void showHideViews(boolean show, View view, View... viewsToShowOrHide) {
        showView(show, view);
        if (show) {
            toggleVisibility(View.GONE, viewsToShowOrHide);
        } else {
            toggleVisibility(View.VISIBLE, viewsToShowOrHide);
        }
    }

    public static void showView(boolean show, View viewToShow) {
        if (viewToShow != null) {
            if (show) {
                viewToShow.setVisibility(View.VISIBLE);
            } else {
                viewToShow.setVisibility(View.GONE);
            }
        }
    }

    public static void toggleVisibility(int visibility, View view) {
        if (isNotNull(view)) {
            view.setVisibility(visibility);
        } else {
            d(TAG, "toggleVisibility: NPE: No Views");
        }
    }

    public static boolean isNotNull(View view) {
        return view != null;
    }

    public static void showHideViews(boolean show, View viewToShow, boolean hide, View viewToHide) {
        if (viewToShow != null) {
            if (show) {
                viewToShow.setVisibility(View.VISIBLE);
            } else {
                viewToShow.setVisibility(View.GONE);
            }
        }
        if (viewToHide != null) {
            if (hide) {
                viewToHide.setVisibility(View.GONE);
            } else {
                viewToHide.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void setAlpha(float alpha, View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setAlpha(alpha);
            }
        }
    }

    public static void setDrawable(Context context, ImageView imageView, int drawableId, int colorId) {
        if (context != null && drawableId != 0 && ContextCompat.getDrawable(context, drawableId) != null) {
            imageView.setImageDrawable(
                    ContextCompat.getDrawable(context, drawableId));
            imageView.setColorFilter(ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void setDrawableTint(Context context, ImageView v, boolean isSelected, int drawableId, int selectedColorId, int unselectedColorId) {
        toggleDrawableTint(context, v, isSelected, drawableId, selectedColorId, unselectedColorId);
    }

    public static void toggleDrawableTint(Context context, ImageView v, boolean isSelected, int drawableId, int selectedColorId, int unselectedColorId) {
        v.setImageDrawable(
                ContextCompat.getDrawable(context, drawableId));
        if (isSelected) {
            v.setColorFilter(ContextCompat.getColor(context, selectedColorId), PorterDuff.Mode.SRC_ATOP);
        } else {
            v.setColorFilter(ContextCompat.getColor(context, unselectedColorId), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void collapseToolbar(Context context, final AppBarLayout appBarLayout, boolean isExpanded, int limit) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        final AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            int topBottomOffset = behavior.getTopAndBottomOffset();
            d("je.detailScreen: ", "appBarOffset: " + String.valueOf(topBottomOffset));
            d(AppConstants.TAG, "ViewUtils: collapseToolbar: " + isExpanded);
            if (isExpanded) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt();
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        d("je.detailScreen: ", "appBarOffset: onAnimationUpdate: " + String.valueOf(animation.getAnimatedValue()));
                        behavior.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
                        appBarLayout.requestLayout();
                    }
                });
                // TODO: 7/9/17 Get current offset of appbar and set it to the top of screen calculating screen size
                if (limit == 0 || limit == -1) {
                    valueAnimator.setIntValues(topBottomOffset, ScreenUtils.getScreenHeight(context) * -1);
                } else {
                    valueAnimator.setIntValues(topBottomOffset, limit * -1);
                }
                valueAnimator.setDuration(800);
                valueAnimator.start();
            }
        }
    }

    public static int getLocationY(View view) {
        int y = 0;
        if (view != null) {
            int[] originalPos = new int[2];
            view.getLocationInWindow(originalPos);
            //or view.getLocationOnScreen(originalPos)
            int x = originalPos[0];
            y = originalPos[1];
            return y;
        }
        return y;
    }

    public static int getLocationX(View view) {
        int x = 0;
        if (view != null) {
            int[] originalPos = new int[2];
            view.getLocationInWindow(originalPos);
            //or view.getLocationOnScreen(originalPos)
            x = originalPos[0];
            int y = originalPos[1];
            return x;
        }
        return x;
    }

    public static int getLocationRight(View view) {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null) {
            return loc.right;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + view.getWidth();
        loc.bottom = loc.top + view.getHeight();
        return loc.right;
    }

    public static boolean isLastRecyclerViewItem(int dy) {
        return dy == 1;
    }

    public static boolean isFirstRecyclerViewItem(int dy) {
        return dy == -1;
    }

    public static boolean isRecyclerViewScrolling(int dy) {
        return dy == 1 || dy == -1;
    }

    public static boolean isRecyclerViewScrolling(int dy, int state) {
        return dy == 1 || dy == -1 || state != RecyclerView.SCROLL_STATE_IDLE;
    }

    public static boolean isRecyclerViewTopReached(RecyclerView recyclerView) {
        return !recyclerView.canScrollVertically(-1);
    }

    public static boolean isRecyclerViewBottomReached(RecyclerView recyclerView) {
        return !recyclerView.canScrollVertically(1);
    }

    public static void loadImage(Context context, int resourceId, int placeHolderId, int errorResId, ImageView imageView) {
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(resourceId)
                    .placeholder(placeHolderId)
                    .centerCrop()
                    .error(errorResId)
                    .into(imageView);
        }
    }

    public static void loadImage(Context context, boolean isThumbnail, String resourceUrl, int placeHolderId, int errorResId, ImageView imageView) {
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(isThumbnail ? GetThumbImage.getThumbImage(resourceUrl) : resourceUrl)
                    .placeholder(placeHolderId)
                    .centerCrop()
                    .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .error(errorResId)
                    .into(imageView);
        }
    }

    public static void loadImageCenterInside(Context context, boolean isThumbnail, String resourceUrl, int placeHolderId, int errorResId, ImageView imageView) {
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(isThumbnail ? GetThumbImage.getThumbImage(resourceUrl) : resourceUrl)
                    .placeholder(placeHolderId)
                    .fitCenter()
                    .error(errorResId)
                    .into(imageView);
        }
    }

    public static void loadSimpleImage(Context context, boolean isThumbnail, String resourceUrl, int placeHolderId, int errorResId, ImageView imageView) {
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(isThumbnail ? GetThumbImage.getThumbImage(resourceUrl) : resourceUrl)
                    .placeholder(placeHolderId)
                    .centerCrop()
                    .error(errorResId)
                    .into(imageView);
        }
    }

    public static void loadCircularImage(Context context, boolean isThumbnail, String resourceUrl, int placeHolderId, int errorResId, ImageView imageView) {
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(isThumbnail ? GetThumbImage.getThumbImage(resourceUrl) : resourceUrl)
                    .placeholder(placeHolderId)
                    .centerCrop()
                    .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .apply(RequestOptions.circleCropTransform())
                    .error(errorResId)
                    .into(imageView);
        }
    }

    public static void optimizeRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
        }
    }

    public static void setClickable(View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setVisibility(View.VISIBLE);
                view.setClickable(true);
            }
        }
    }

    public static void setFocusable(View... views) {
        if (isNotNullNotEmpty(views)) {
            for (View view : views) {
                view.setVisibility(View.VISIBLE);
                view.setFocusable(true);
            }
        }
    }

    public static void setRadioButtonTypeface(RadioButton radioButton, Typeface typeface) {
        if (radioButton != null && typeface != null) {
            radioButton.setTypeface(typeface);
        }
    }

    public static void setCheckBoxTypeface(CheckBox checkBox, Typeface typeface) {
        if (checkBox != null && typeface != null) {
            checkBox.setTypeface(typeface);
        }
    }

    public static void showPassword(EditText et, boolean showPassword) {
        if (et != null) {
            if (showPassword) {
                et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    // sagar : 5/12/18 Both resId and color because lib module cannot catch app module res
    // sagar : 8/2/19 7:14 PM If you are getting unexpected letter space, give boolean true and set value: 0f to remove that space!
    public static void setTabText(Context context, TabLayout tabLayout, int selectedTab,
                                  int selectedTextColorResId, int unselectedTextColorResId,
                                  int selectedColor, int unSelectedColor,
                                  Typeface selectedTextTypeface, Typeface unselectedTextTypeface,
                                  boolean isTextCap, boolean hasLetterSpace, float letterSpace
    ) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        int j;
        for (j = 0; j < tabsCount; j++) {
            int i;
            ViewGroup vgTab;
            int tabChildCount;
            View tabViewChild;
            if (j != selectedTab) {
                vgTab = (ViewGroup) vg.getChildAt(j); //All tabViews one by one
                tabChildCount = vgTab.getChildCount(); //Each tab has 2 children, ImageView and TextView
                for (i = 0; i < tabChildCount; i++) {
                    tabViewChild = vgTab.getChildAt(i);
                    // sagar : 5/12/18 We don't want to go ahead if there is no text customization to be set
                    if (unselectedTextColorResId != 0 || unselectedTextTypeface != null) {
                        if (tabViewChild instanceof TextView) {
                            ((TextView) tabViewChild).setSelected(false);
                            // sagar : 5/12/18 Why activated?
                            /*https://stackoverflow.com/questions/36556512/how-to-change-textview-text-color-on-selection*/
                            ((TextView) tabViewChild).setActivated(false);
                            if (unselectedTextTypeface != null) {
                                ((TextView) tabViewChild).setTypeface(unselectedTextTypeface);
                            }
                            if (unselectedTextColorResId != 0) {
                                ((TextView) tabViewChild).setTextColor(ContextCompat.getColor(context, unselectedTextColorResId));
                            }
                            if (unSelectedColor != 0) {
                                ((TextView) tabViewChild).setTextColor(unSelectedColor);
                            }
                            if (hasLetterSpace) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    ((TextView) tabViewChild).setLetterSpacing(letterSpace);
                                }
                            }
                            ((TextView) tabViewChild).setAllCaps(isTextCap);
                        }
                    }
                }
            } else {
                vgTab = (ViewGroup) vg.getChildAt(selectedTab); //Particular (selected) TabView
                tabChildCount = vgTab.getChildCount(); //Each tab has 2 children, ImageView and TextView
                for (i = 0; i < tabChildCount; i++) {
                    tabViewChild = vgTab.getChildAt(i);
                    // sagar : 5/12/18 We don't want to go ahead if there is no text customization to be set
                    if (selectedTextColorResId != 0 || selectedTextTypeface != null) {
                        if (tabViewChild instanceof TextView) {
                            ((TextView) tabViewChild).setSelected(true);
                            // sagar : 5/12/18 Why activated?
                            /*https://stackoverflow.com/questions/36556512/how-to-change-textview-text-color-on-selection*/
                            ((TextView) tabViewChild).setActivated(true);
                            if (selectedTextTypeface != null) {
                                ((TextView) tabViewChild).setTypeface(selectedTextTypeface);
                            }
                            if (selectedTextColorResId != 0) {
                                ((TextView) tabViewChild).setTextColor(ContextCompat.getColor(context, selectedTextColorResId));
                            }
                            if (selectedColor != 0) {
                                ((TextView) tabViewChild).setTextColor(selectedColor);
                            }
                            if (hasLetterSpace) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    ((TextView) tabViewChild).setLetterSpacing(0f);
                                }
                            }
                            ((TextView) tabViewChild).setAllCaps(isTextCap);
                        }
                    }
                }
            }
        }
    }

    public static void setSelectedTab(Context context, TabLayout tabLayout, int selectedTab,
                                      Typeface selectedTextTypeface, int selectedColorResId, int selectedColor) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        int j;
        int i;
        ViewGroup vgTab;
        int tabChildCount;
        View tabViewChild;

        for (j = 0; j < tabsCount; j++) {
            if (j != selectedTab) {
                vgTab = (ViewGroup) vg.getChildAt(selectedTab); //Particular TabView
                tabChildCount = vgTab.getChildCount(); //Each tab has 2 children, ImageView and TextView
                for (i = 0; i < tabChildCount; i++) {
                    tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        if (selectedTextTypeface != null) {
                            ((TextView) tabViewChild).setTypeface(selectedTextTypeface);
                        }
                        if (selectedColorResId != 0) {
                            ((TextView) tabViewChild).setTextColor(ContextCompat.getColor(context, selectedColorResId));
                        }
                        if (selectedColor != 0) {
                            ((TextView) tabViewChild).setTextColor(selectedColor);
                        }
                    }
                }
            }
        }
    }

    public static void setLoadMoreOnGridLayout(GridLayoutManager gridLayoutManager, RecyclerView.Adapter adapter, int columns) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case AppCodes.VIEW_TYPE_ITEM:
                        return 1; //Covers 2 columns
                    case AppCodes.VIEW_TYPE_PROGRESS_BAR:
                        return columns;
                    default:
                        return 1;
                }
            }
        });
    }

    public static void setUnSelectedTab(Context context, TabLayout tabLayout, int unSelectedTab,
                                        Typeface unselectedTextTypeface, int unselectedColorResId, int unSelectedColor) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        int j;
        int i;
        ViewGroup vgTab;
        int tabChildCount;
        View tabViewChild;
        for (j = 0; j < tabsCount; j++) {
            if (j == unSelectedTab) {
                vgTab = (ViewGroup) vg.getChildAt(unSelectedTab); //Particular TabView
                tabChildCount = vgTab.getChildCount(); //Each tab has 2 children, ImageView and TextView
                for (i = 0; i < tabChildCount; i++) {
                    tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        if (unselectedTextTypeface != null) {
                            ((TextView) tabViewChild).setTypeface(unselectedTextTypeface);
                        }
                        if (unselectedColorResId != 0) {
                            ((TextView) tabViewChild).setTextColor(ContextCompat.getColor(context, unselectedColorResId));
                        }
                        if (unSelectedColor != 0) {
                            ((TextView) tabViewChild).setTextColor(unSelectedColor);
                        }
                    }
                }
            }
        }
    }

    public static void setStrikeThrough(TextView... tvs) {
        if (tvs != null && tvs.length > 0) {
            for (TextView textView : tvs) {
                textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

    public static void selectTab(int tabIndex, TabLayout tabLayout) {
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        if (tab != null) {
            tab.select();
        }
    }

    public static void setTextColor(Context context, int colorId, TextView... tvs) {
        if (context != null && tvs != null && tvs.length > 0) {
            int color = ContextCompat.getColor(context, colorId);
            for (TextView tv : tvs) {
                tv.setTextColor(color);
            }
        }
    }

    public static void setScrollFlagToCollapsingToolbar(CollapsingToolbarLayout collapsingToolbarLayout, int flag) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        /*params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);*/ // list other flags here by |
        params.setScrollFlags(flag);
        collapsingToolbarLayout.setLayoutParams(params);
    }

    public static void setScrollableCollapsingToolbar(AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout) {

        resetCollapseToolbarFlags(appBarLayout, collapsingToolbarLayout);

        AppBarLayout.LayoutParams params = getAppBarLayoutParams(collapsingToolbarLayout);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        params.setScrollFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        collapsingToolbarLayout.setLayoutParams(params);

        appBarLayout.requestLayout();
        collapsingToolbarLayout.requestLayout();
    }

    public static void resetCollapseToolbarFlags(AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout) {
        AppBarLayout.LayoutParams params = getAppBarLayoutParams(collapsingToolbarLayout);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        params.setScrollFlags(0);
        collapsingToolbarLayout.setLayoutParams(params);
        appBarLayout.requestLayout();
        collapsingToolbarLayout.requestLayout();
    }

    public static AppBarLayout.LayoutParams getAppBarLayoutParams(CollapsingToolbarLayout collapsingToolbarLayout) {
        return (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
    }

    public static void setSnapCollapsingToolbar(AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout) {

        resetCollapseToolbarFlags(appBarLayout, collapsingToolbarLayout);

        AppBarLayout.LayoutParams params = getAppBarLayoutParams(collapsingToolbarLayout);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        params.setScrollFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        collapsingToolbarLayout.setLayoutParams(params);

        appBarLayout.requestLayout();
        collapsingToolbarLayout.requestLayout();
    }

    public static void setPercentage(TextView tv, String percentage) {
        if (tv != null && StringUtils.isNotNullNotEmpty(percentage)) {
            tv.setText(String.format("%s%%", percentage));
        }
    }

    public static void tempDisable(View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> {
            if (view != null) {
                view.setEnabled(true);
            }
        }, Limits.EXIT_RESET_TIME);
    }

    public static void tempDisable(View... views) {
        if (ViewUtils.isNotNullNotEmpty(views)) {
            for (View view : views) {
                if (view != null) {
                    view.setEnabled(false);
                }
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ViewUtils.isNotNullNotEmpty(views)) {
                    for (View view : views) {
                        if (view != null) {
                            view.setEnabled(true);
                        }
                    }
                }
            }
        }, Limits.EXIT_RESET_TIME);
    }

    public static void setGradientColor(View view, String startColorHex, String endColorHex) {

        //Color.parseColor() method allow us to convert
        // a hexadecimal color string to an integer value (int color)
        int[] colors = {Color.parseColor(startColorHex), Color.parseColor(endColorHex)};

        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setCornerRadius(0f);
        //apply the button background to newly created drawable gradient
        view.setBackground(gd);
    }

    public static int[] getGradientColor(String startColorHex, String endColorHex) {
        int[] colors = {Color.parseColor(startColorHex), Color.parseColor(endColorHex)};
        return colors;
    }


    public static void setRippleBackground(View view, int pressedColor, Drawable drawable) {
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackground(getRippleBackground(pressedColor, drawable));
            } else {
                // If we're running on Honeycomb or newer, then we can use the Theme's
                // selectableItemBackground to ensure that the View has a pressed state
                TypedValue outValue = new TypedValue();
                view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                view.setBackgroundResource(outValue.resourceId);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static RippleDrawable getRippleBackground(int pressedColor, Drawable backgroundDrawable) {
        return new RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null);
    }

    public static ColorStateList getPressedState(int pressedColor) {
        return new ColorStateList(new int[][]{new int[]{}}, new int[]{pressedColor});
    }

    public static void addAsterisk(Context context, TextView tv, String baseValue) {
        if (tv != null) {
            tv.setHint(String.format("%s%s", baseValue, context.getString(R.string.label_asterisk)));
        }
    }

    public static void appendText(TextView tv, String baseValue, String append) {
        if (tv != null) {
            tv.setHint(String.format("%s%s", baseValue, append));
        }
    }

    public static void appendText(String append, boolean allowDuplication, TextView... tvs) {

        if (tvs != null && tvs.length > 0) {
            if (!StringUtils.isNotNullNotEmpty(append)) {
                append = "";
            }
            for (TextView textView : tvs) {
                if (textView != null) {
                    if (allowDuplication) {
                        textView.append(append);
                    } else {
                        if (!textView.getText().toString().trim().endsWith(append)) {
                            textView.append(append);
                        }
                    }
                }
            }
        }
    }

    public static void replace(String oldChar, String newChar, TextView... tvs) {

        if (tvs != null && tvs.length > 0) {
            if (StringUtils.isNotNullNotEmpty(newChar)) {
                for (TextView textView : tvs) {
                    if (textView != null && textView.getText() != null && textView.getText().toString().length() > 0) {
                        String old = textView.getText().toString();
                        String newString = old.replace(oldChar, newChar);
                        textView.setText(newString);
                    }
                }
            }
        }
    }

    public static void customizeSearchView(Context context, SearchView searchView, int hintTextColorResId, int inputTxtColorResId, int svPlateBgcolorResId, Typeface typeface) {

        //region search plate
        // sagar : 22/1/19 10:32 AM Code for remove line in search edittext

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);

        if (searchPlate == null) {
            searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        }

        searchPlate.setBackgroundColor(ContextCompat.getColor(context, svPlateBgcolorResId));
        //endregion

        //region Search Text
        int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
        if (searchText == null) {
            searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        }
        if (searchText != null) {
            // sagar : 22/1/19 10:32 AM code for set typeface to search bar
            // sagar : 22/1/19 10:32 AM If it is not textView, may be it is editText
            searchText.setBackgroundColor(ContextCompat.getColor(context, svPlateBgcolorResId));
            searchText.setTypeface(typeface);
            searchText.setTextColor(ContextCompat.getColor(context, inputTxtColorResId));
            searchText.setHintTextColor(ContextCompat.getColor(context, hintTextColorResId));
        }
        //endregion

        /*Sets the default or resting state of the search field. If true, a single search icon is shown by default and expands to show the text field and other buttons when pressed. Also, if the default state is iconified, then it collapses to that state when the close button is pressed. Changes to this property will take effect immediately.*/
        searchView.setIconified(false);
        searchView.clearFocus();
    }

    public static void setTypeface(Typeface typeface, TextView... tvs) {
        if (isNotNullNotEmpty(tvs)) {
            for (TextView tv : tvs) {
                if (tv != null) {
                    tv.setTypeface(typeface);
                }
            }
        }
    }

    public static void invalidateAndRequestLayout(View v) {
        if (v != null) {
            v.invalidate();
            v.requestLayout();
        }
    }


    public static void setTabPage(ViewPager viewPager, int index, boolean smoothScroll, boolean increasingPage) {
        if (index != -1 && viewPager != null && viewPager.getAdapter() != null && viewPager.getAdapter().getCount() >= index) {
            if (viewPager.getAdapter().getCount() > index) {
                viewPager.setCurrentItem(index);
            } else if (increasingPage) {
                // sagar : 31/1/19 5:40 PM By default, setting up at last available index position
                viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
            } else {
                viewPager.setCurrentItem(0);
            }
        }
    }

    public static void setTabPage(TabLayout tabLayout, int index, boolean smoothScroll, boolean increasingPage) {
        if (index != -1 && tabLayout != null && tabLayout.getTabCount() >= index)
            if (tabLayout.getTabCount() > index && tabLayout.getTabAt(index) != null) {
                try {
                    tabLayout.getTabAt(index).select();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (increasingPage) {
                // sagar : 31/1/19 5:40 PM By default, setting up at last available index position
                try {
                    tabLayout.getTabAt(tabLayout.getTabCount() - 1).select();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                tabLayout.getTabAt(0).select();
            }
    }

    public static void setEmail(Context context, TextInputLayout tilCountryCode, TextInputLayout tilEmailPhone, boolean isEmail) {
        if (isEmail) {
            tilCountryCode.setVisibility(View.GONE);
            tilEmailPhone.setHint(context.getString(R.string.label_email));
            EditTextUtils.setEmail(tilEmailPhone.getEditText());
        }
    }

    public static void setEmailPhone(Context context, TextInputLayout tilCountryCode, TextInputLayout tilEmailPhone) {
        tilCountryCode.setVisibility(View.GONE);
        tilEmailPhone.setHint(context.getString(R.string.label_email_mobile_number));
    }

    public static void setEmailInputTypeface(TextInputLayout tilEmailPhone, Typeface tilTypeface, Typeface etTypeface) {
        tilEmailPhone.invalidate();
        tilEmailPhone.setTypeface(tilTypeface);
        if (tilEmailPhone.getEditText() != null) {
            tilEmailPhone.getEditText().setTypeface(etTypeface);
        }
        tilEmailPhone.invalidate();
    }

    public static void showErrorMessage(Activity activity, String message) {
        Utils.showErrorMessage(activity, message);
    }

    public static void showSuccessMessage(Activity activity, String message) {
        Utils.showSuccessMessage(activity, message);
    }

    public static void showToastMessage(Activity activity, String message, boolean isError) {
        Utils.showToastMessage(activity, message, isError);
    }

    public static void openCodeVerificationScreen(Activity activity, String smsSentStatus, String verificationCode, Class phoneVerificationClass) {

        LatLng latLng = GetUserLocation.getCurrentLocation(activity, true);

        Intent intent = new Intent(activity, phoneVerificationClass);
        intent.putExtra(com.commonlib.constants.IntentKeys.SOURCE_SCREEN, activity.getClass().getSimpleName());
        intent.putExtra(com.commonlib.constants.IntentKeys.IK_SMS_SENT, smsSentStatus);
        intent.putExtra(com.commonlib.constants.IntentKeys.IK_VERIFICATION_CODE, verificationCode);
        activity.startActivity(intent);
    }

    public static void openCodeVerificationScreen(Activity activity, String sourceClassName, String smsSentStatus, String verificationCode, Class phoneVerificationClass) {

        LatLng latLng = GetUserLocation.getCurrentLocation(activity, true);

        Intent intent = new Intent(activity, phoneVerificationClass);
        intent.putExtra(com.commonlib.constants.IntentKeys.SOURCE_SCREEN, sourceClassName);
        intent.putExtra(com.commonlib.constants.IntentKeys.IK_SMS_SENT, smsSentStatus);
        intent.putExtra(com.commonlib.constants.IntentKeys.IK_VERIFICATION_CODE, verificationCode);
        activity.startActivity(intent);
    }

    public static void loadGestureImage(Context context, String url, int placeHolderId, int errorHolderId, ImageView targetImage) {
        // We don't want Glide to crop or resize our image
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontTransform()
                .placeholder(placeHolderId)
                .error(errorHolderId)
                .into(targetImage);


        /* // We don't want Glide to crop or resize our image
        Glide.with(getActivity())
                .load(directSubMenu.getPhoto())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontTransform()
                .placeholder(R.drawable.placeholder_banner)
                .error(R.drawable.placeholder_banner)
                .into(fullImage);

        // Resetting to initial image state
        fullImage.getController().resetState();
        outletPhotoAnimator.enterSingle(true);*/
    }

    public static void setText(TextView textView, boolean forceVisibility, String value) {
        if (textView != null) {
            if (forceVisibility) {
                textView.setVisibility(View.VISIBLE);
            }
            textView.setText(value);
        }
    }

    public static void setText(boolean forceVisibility, String commonValue, TextView... textViews) {
        if (textViews != null && textViews.length > 0) {
            for (TextView textView : textViews) {
                setText(textView, forceVisibility, commonValue);
            }
        }
    }

    private void addRemoveViewPagerTabFragments(TabLayout tabLayout, ViewPagerAdapter viewPagerAdapter, List<Fragment> fragmentsToAdd, Bundle bundle, boolean showTabLayout, int showTabIndex, Integer... removeTabIndexes) {
        // sagar : 8/2/19 3:10 PM This method has been added for the flexibility to show only desired tab
        if (tabLayout != null && viewPagerAdapter != null) {

            // sagar : 8/2/19 3:25 PM We first remove given tabIndexes and then we add fragments if any
            if (Utils.isNotNullNotEmpty(viewPagerAdapter.getFragmentList())) {
                if (viewPagerAdapter.getFragmentList().size() > showTabIndex) {
                    try {
                        tabLayout.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(showTabIndex).select();
                        Utils.removeElements(viewPagerAdapter.getFragmentList(), removeTabIndexes);
                        viewPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        // sagar : 8/2/19 2:53 PM Handle exception
                        e.printStackTrace();
                    }
                }
            }

            // FIXME: sagar : 8/2/19 We first set selected some tab index and now we are adding some fragments?
            // sagar : 8/2/19 3:19 PM Code to add fragments
            if (Utils.isNotNullNotEmpty(fragmentsToAdd)) {
                for (Fragment fragment : fragmentsToAdd) {
                    if (bundle != null) {
                        fragment.setArguments(bundle);
                    }
                    viewPagerAdapter.addFragment(fragment);
                }
            }

            // sagar : 8/2/19 3:22 PM Code to show/hide tabLayout
            if (showTabLayout) {
                tabLayout.setVisibility(View.VISIBLE);
            } else {
                tabLayout.setVisibility(View.GONE);
            }

        }
    }

     public static void disableAppbarScrolling(CollapsingToolbarLayout toolbarLayout) {
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
        p.setScrollFlags(0);
        toolbarLayout.setLayoutParams(p);
    }

    public static void enableAppBarScrolling(CollapsingToolbarLayout toolbarLayout) {
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
        p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        toolbarLayout.setLayoutParams(p);
    }

    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }
}
