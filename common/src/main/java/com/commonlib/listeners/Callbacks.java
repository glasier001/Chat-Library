package com.commonlib.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;
import com.alexvasilkov.gestures.views.GestureImageView;
//import com.commonlib.customadapters.PhotoPagerAdapter;
import com.commonlib.models.Photo;

import org.json.JSONException;

import java.util.List;

/**
 * Created by sagar on 15/5/18.
 */
public abstract class Callbacks {

    public interface OnClickListener {
        public void onClickLeft(String message);

        public void onClickRight(String etTextString);
    }

    /*Callback for fragments to parent container with viewpager and tab-layouts*/
    public interface FragmentLifecycle {
        public void onPauseFragment(FragmentActivity fragmentActivity);

        public void onResumeFragment(FragmentActivity fragmentActivity);
    }

    /*General callback for every item click event*/
    public interface OnEventCallbackListener {
        void onEventCallback(View v, int positionTag, Intent intentData, Object... objects);
    }

    public interface OnEventStatusListener {
        void onEventStart(View v, int positionTag, Intent intentData, Object... object);

        void onEventFinish(View v, int positionTag, Intent intentData, Object... object);
    }

    public interface OnEventResultListener {
        void onEventSuccess(View v, int positionTag, Intent intentData, Object... objects) throws JSONException;

        void onEventFail(View v, int positionTag, Intent intentData, Object... objects);
    }

    public interface OnItemChangeListener {
        void onChangeItemData(int positionTag, Object title, Intent intentData);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnPermissionListener {
        void OnPermissionGranted();

        void OnPermissionDenied();
    }

    public interface SignOutListener {
        public void OnSignOut();
    }

    public interface ScrollObserver {

        void onScrollYchange(int directionY);

        void onHeaderHide();

        void onHeaderShow();
    }

    public interface OnScrollViewPager {
        void onSwipeRight();

        void onSwipeLeft();

        void onScrollStop();
    }

    public interface SetCodeListener {

        void codeReceive(String sms_sent, String verification_code);

    }

    public interface OnGetLocationListener {

        void getLatitude(double latitude);

        void getLongitude(double longitude);
    }

    public interface AppAdapter {

        int getPageNumber();

        void setPageNumber(int pageNumber);

        String getApi();

        void setApi(String api);

        String getSourceScreen();

        void setSourceScreen(String sourceScreen);

        void addList(int pageNumber, List list, Intent intent);

        void setTags(RecyclerView.ViewHolder viewHolder, int position);

        void bindViews(RecyclerView.ViewHolder viewHolder, int position);

        void setImage(RecyclerView.ViewHolder viewHolder, Object object);

        void setData(RecyclerView.ViewHolder viewHolder, Object object);

        int getItemType();

        void setItemType(int type);

        void updateItem(String sourceScreen, String api, int positionTag, Object object); //Intent?

        void removeItem(String sourceScreen, String api, int positionTag, Object object); //Intent?

        void removeItem(int positionTag, Object object); //Intent?

        void clearData();
    }

    public interface NetworkListener {
        void onConnectionChanged(boolean isConnected);
    }

    public interface OnAttachActivity {
        void onGetActivity(FragmentActivity activity);
    }

    public interface RvScrollCallbacks {
        void onItemTouch();

        void onReleaseTouch();

        void onScrollUp();

        void onScrollDown();

        void onScrollLeft();

        void onScrollRight();

        void onScrollStop();

        void onRvDrag();

        void onRvFling();

        void onTop();

        void onBottom();

        void onScroll(RecyclerView recyclerView, int dx, int dy);

        void onGetPercentageScroll(RecyclerView recyclerView, float percentageScrolled);
    }

    public interface RvAdapterPositionCallbacks {

        void onReachFirstItem();

        void onReachLastItem();
    }

    public interface OnClickGesturePhoto {
        void onClickGesturePhoto(RecyclerView rvPhoto, RecyclerView.Adapter adapter, List<Photo> photoList, int clickedPhotoPosition);
    }

    public interface OnBottomSheetStateChanged {

        void onCollapsed(Intent intentData, Object... object);

        void onDragging(Intent intentData, Object... object);

        void onHalfExpand(Intent intentData, Object... object);

        void onExpand(Intent intentData, Object... object);

        void onHide(Intent intentData, Object... object);

        void onSettling(Intent intentData, Object... object);

        void onStateUnknown(Intent intentData, Object... object);

        void onSlide(View bottomSheet, float offset, Intent intentData, Object... object);
    }

    public interface addGesturePositionListener {
        void onGesturePositionUpdate(float position, boolean isLeaving);
    }

    public interface ListScreen {

        // sagar : 2/5/19 1:32 PM setLayoutManager, setRecycleViewAdapter, setLoadMoreListener, showListUi, showNoDataUi, setLoaded

        void setLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager);

        void setLoadMoreListener(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager);

        void setRecycleViewAdapter(RecyclerView recyclerView);

        void showListUi();

        void showNoDataUi();

        void setLoaded();
    }

    /*NewsFeedFragment*/
    public interface SingleListGesture {

        /*call one by methods within this method*/
        void onClickImageItem(ImageView imageView, String urlPath);

        /* animator = GestureTransitions.from(imageView).into(mBinding.giv);*/
        void initAnimator(ViewsTransitionAnimator animator, ImageView fromImageView, GestureImageView toGestureImageView);

        /*animator.addPositionUpdateListener(this);*/
        void addPositionUpdateListener(ViewsTransitionAnimator animator);

        /* if (mBinding.giv.getDrawable() == null) {
            mBinding.giv.setImageDrawable(imageView.getDrawable());
        }*/
        void setDrawable(GestureImageView gestureImageView, ImageView imageView);

        /* ViewUtils.loadGestureImage(getActivity(), news.getPhoto(), R.drawable.ic_banner_placeholder, R.drawable.ic_banner_placeholder, mBinding.giv);*/
        void loadImage(GestureImageView gestureImageView, String urlPhoto);

        /*mBinding.giv.getController().resetState();*/
        void resetState(GestureImageView gestureImageView);

        /*if (animator != null) {
            animator.enterSingle(true);
        }*/
        void enterAnimation(ViewsTransitionAnimator enterAnimator);

        /*if (animator != null && !animator.isLeaving()) {
            animator.exit(true);
        }*/
        void exitAnimation(ViewsTransitionAnimator animator);
    }

    /*DiscoverActivity*/
    public interface RvToViewPagerGesture {

        void onClickImageItem(RecyclerView rvPhoto, RecyclerView.Adapter rvAdapter, List<Photo> photoList, int clickedPhotoPosition);

        /*   photoPagerAdapter = new PhotoPagerAdapter(this, imageViewPager, photoList);
            imageViewPager.setAdapter(photoPagerAdapter);*/
//        PhotoPagerAdapter getPhotoPagerAdapter(Context context, ViewPager imageViewPager, List<Photo> photoList);
//
//        //imageViewPager.setAdapter(getPhotoPagerAdapter)
//        void setUpPhotoPagerAdapter(ViewPager imageViewPager);
//
//        SimpleTracker getSimpleListTracker(RecyclerView rvPhotos, RecyclerView.Adapter rvPhotoAdapter, ImageView nestedListImageView);
//
//        SimpleTracker getSimplePagerTracker(PhotoPagerAdapter photoPagerAdapter);

        /*     mAnimator = GestureTransitions.from(rvPhotos, getSimpleListTracker).into(viewPager, getSimplePagerTracker);*/
        void initRvToViewPagerAnimator(RecyclerView fromRvPhotos, SimpleTracker fromSimpleListTracker, ImageView nestedListImageView, ViewPager intoViewPager, SimpleTracker intoSimplePagerTracker);

        /*mAnimator.addPositionUpdateListener((pos, isLeaving)*/
        void addPositionUpdateListener(ViewsTransitionAnimator<Integer> animator);

        /* if (mAnimator != null) {
            mAnimator.enter(clickedPhotoPosition, true);
        }*/
        void enterAnimation(int clickedPhotoPosition, ViewsTransitionAnimator<Integer> animator);

        /* if (mAnimator != null && !mAnimator.isLeaving()) {
            mAnimator.exit(true);
        }*/
        void exitAnimation(ViewsTransitionAnimator<Integer> animator);
    }
}
