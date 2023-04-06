package com.commonlib.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.commonlib.customadapters.PhotoPagerAdapter;
import com.commonlib.listeners.Callbacks;
import com.commonlib.models.Photo;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;
import static com.commonlib.constants.AppConstants.TAG;

public class GestureUtil implements ViewPositionAnimator.PositionUpdateListener {

    private boolean isCircularImage;
    private String imagePath;
    private ImageView sourceImageView;
    private GestureImageView gestureImageView;

    // sagar : 9/4/19 4:22 PM Usually, black background
    private View backgroundView;

    private List<View> viewsToHideOnFullScreen = new ArrayList<>();
    private List<View> viewsToShowOnFullScreen = new ArrayList<>();
    private Context context;
    private boolean isThumbnail;
    private ViewsTransitionAnimator animator;
    private ViewsTransitionAnimator<Integer> nestedListAnimator;
    private boolean isSingleAnimation;
    private boolean isSingleListAnimation;
    private boolean isNestedListAnimation;
    private boolean isPhotoListAnimation;

    private List<Photo> photoList = new ArrayList<>();

    private View viewToCloseAnimation;
    private PhotoPagerAdapter photoPagerAdapter;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private RecyclerView.Adapter rvAdapter;

    public void setGesturePositionListener(Callbacks.addGesturePositionListener gesturePositionListener) {
        this.gesturePositionListener = gesturePositionListener;
    }

    private Callbacks.addGesturePositionListener gesturePositionListener;

    public void resetUtil() {
        /*https://github.com/alexvasilkov/GestureViews/issues/136*/
        gestureImageView = null;
        backgroundView = null;
        context = null;
        if (animator != null) {
            animator.enterSingle(false);
            animator.exit(false);
            animator.removePositionUpdateListener(this);
        }
        animator = null;
        if (nestedListAnimator != null) {
            nestedListAnimator.enterSingle(false);
            nestedListAnimator.exit(false);
            nestedListAnimator.removePositionUpdateListener(this);
        }
        nestedListAnimator = null;
        photoPagerAdapter = null;
        recyclerView = null;
        viewPager = null;
        rvAdapter = null;
    }

    public GestureUtil(Context context) {
        resetUtil();
        if (context != null) {
            this.context = context;
        }
    }

    public GestureUtil forSingleListAnimation(View viewToCloseAnimation, View backgroundView,
                                              List<View> viewsToHideOnFullScreen, List<View> viewsToShowOnFullScreen) {
        this.isSingleListAnimation = true;

        return init(getContext(), backgroundView, viewToCloseAnimation, viewsToHideOnFullScreen, viewsToShowOnFullScreen);
    }

    public GestureUtil init(Context context, View backgroundView, View viewToCloseAnimation, List<View> viewsToHideOnFullScreen, List<View> viewsToShowOnFullScreen) {

        if (context != null) {
            this.context = context;
        }

        if (backgroundView != null) {
            this.backgroundView = backgroundView;
        }

        if (Utils.isNotNullNotEmpty(viewsToHideOnFullScreen)) {
            this.viewsToHideOnFullScreen = viewsToHideOnFullScreen;
        }

        if (Utils.isNotNullNotEmpty(viewsToShowOnFullScreen)) {
            this.viewsToShowOnFullScreen = viewsToShowOnFullScreen;
        }

        if (viewToCloseAnimation != null) {
            this.viewToCloseAnimation = viewToCloseAnimation;
            viewToCloseAnimation.setOnClickListener(v -> exitFullScreenImageView());
        }

        return this;
    }

    public void exitFullScreenImageView() {
        d(TAG, "GestureUtil: exitFullScreenImageView: ");
        if (animator != null && !animator.isLeaving()) {
            animator.exit(true);
        } else if (nestedListAnimator != null && !nestedListAnimator.isLeaving()) {
            nestedListAnimator.exit(true);
        }
        resetUtil();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public GestureUtil forSingleImageAnimation(ImageView sourceImageView, GestureImageView gestureImageView, View viewToCloseAnimation, View backgroundView, List<View> viewsToHideOnFullScreen, List<View> viewsToShowOnFullScreen) {

        this.isSingleAnimation = true;
        return init(backgroundView, sourceImageView, gestureImageView, viewToCloseAnimation, viewsToHideOnFullScreen, viewsToShowOnFullScreen);
    }

    public GestureUtil init(View backgroundView, ImageView sourceImageView, GestureImageView gestureImageView, View viewToCloseAnimation, List<View> viewsToHideOnFullScreen, List<View> viewsToShowOnFullScreen) {

        if (backgroundView != null) {
            this.backgroundView = backgroundView;
        }

        if (Utils.isNotNullNotEmpty(viewsToHideOnFullScreen)) {
            this.viewsToHideOnFullScreen = viewsToHideOnFullScreen;
        }

        if (Utils.isNotNullNotEmpty(viewsToShowOnFullScreen)) {
            this.viewsToShowOnFullScreen = viewsToShowOnFullScreen;
        }

        if (viewToCloseAnimation != null) {
            this.viewToCloseAnimation = viewToCloseAnimation;
            viewToCloseAnimation.setOnClickListener(v -> exitFullScreenImageView());
        }

        if (isSingleAnimation && ViewUtils.isNotNullNotEmpty(sourceImageView, gestureImageView)) {
            initSingleImageAnimator(sourceImageView, gestureImageView);
        }

        return this;
    }

    private void initSingleImageAnimator(ImageView sourceImageView, GestureImageView gestureImageView) {
        if (sourceImageView != null && gestureImageView != null) {

            this.sourceImageView = sourceImageView;
            this.gestureImageView = gestureImageView;

            animator = GestureTransitions.from(sourceImageView).into(gestureImageView);
            animator.addPositionUpdateListener(this);
        }
    }

    public GestureUtil forNestedListAnimation(View viewToCloseAnimation, View backgroundView, List<View> viewsToShowOnFullScreen, List<View> viewsToHideOnFullScreen) {
        this.isNestedListAnimation = true;
        // sagar : 19/4/19 4:49 PM Refer: PrescriptionListAdapter -> AvailMyRequestFragment -> DiscoverActivity

        return init(getContext(), backgroundView, viewToCloseAnimation, viewsToHideOnFullScreen, viewsToShowOnFullScreen);
    }

    public GestureUtil forPhotoListAnimation() {
        this.isPhotoListAnimation = true;
        // sagar : 9/4/19 7:14 PM Refer: yc_ambassador -> photosFragment
        return this;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView.Adapter getRvAdapter() {
        return rvAdapter;
    }

    public void setRvAdapter(RecyclerView.Adapter rvAdapter) {
        this.rvAdapter = rvAdapter;
    }

    // sagar : 9/4/19 6:02 PM Call this method to set the resting normal image for single image animation.
    // It will handle the click event, will perform the enter animation on click of that imageView,
    // It will toggle visibility for given viewsToShowOnFullScreen and viewsToHideOnFullScreen,
    // and will close the animation on click of given viewToCloseAnimation.
    public void setSingleImageAnimation(ImageView sourceImageView, String url, boolean isCircular, boolean isThumbnail,
                                        int placeholderResId, int errorResId) {

        if (getContext() != null && StringUtils.isNotNullNotEmpty(url) && sourceImageView != null) {

            this.isSingleAnimation = true;
            this.isCircularImage = isCircular;
            this.imagePath = url;
            this.isThumbnail = isThumbnail;
            this.sourceImageView = sourceImageView;

            if (isCircular) {
                ViewUtils.loadCircularImage(getContext(), isThumbnail, url, placeholderResId, errorResId, sourceImageView);
            } else {
                ViewUtils.loadGestureImage(getContext(), url, placeholderResId, errorResId, sourceImageView);
            }

            // sagar : 9/4/19 4:18 PM single image animation
            sourceImageView.setOnClickListener(v ->
                    onClickImageForSingleAnimation(sourceImageView, gestureImageView, url, placeholderResId, errorResId));
        }
    }

    public void onClickImageForSingleAnimation(ImageView sourceImageView, GestureImageView gestureImageView, String photoUrl, int placeholderResId, int errorResId) {
        if (ViewUtils.isNotNullNotEmpty(sourceImageView, gestureImageView)) {

            this.isSingleAnimation = true;
            this.sourceImageView = sourceImageView;
            this.gestureImageView = gestureImageView;

            // Setting image drawable from 'from' view to 'to' to prevent flickering
            BitmapDrawable bitmapDrawable = (BitmapDrawable) sourceImageView.getDrawable();
            gestureImageView.setImageDrawable(bitmapDrawable);

            // sagar : 9/4/19 5:51 PM Comment this line if it is unnecessary
            ViewUtils.loadGestureImage(context, photoUrl, placeholderResId, errorResId, gestureImageView);

            // Updating gesture image settings
            loadSingleAnimation(gestureImageView);
        }
    }

    public void loadSingleAnimation(GestureImageView gestureImageView) {
        // Updating gesture image settings
        gestureImageView.getController().resetState();
        if (animator != null) {
            animator.enterSingle(true);
        }
    }

    public void onClickImageForSingleListAnimation(ImageView sourceImageView, String url, GestureImageView gestureImageView, int placeholderId, int errorPlaceholderId) {
        if (ViewUtils.isNotNullNotEmpty(sourceImageView, gestureImageView)) {

            this.sourceImageView = sourceImageView;
            this.gestureImageView = gestureImageView;

            // sagar : 9/4/19 6:00 PM For single list animation, we have to re-initialize animator for each new imageView position
            animator = GestureTransitions.from(sourceImageView).into(gestureImageView);
            animator.addPositionUpdateListener(this);

            gestureImageView.setImageDrawable(sourceImageView.getDrawable());

            ViewUtils.loadGestureImage(getContext(), url, placeholderId, errorPlaceholderId, gestureImageView);

            loadSingleAnimation(gestureImageView);
        }
    }

    @Override
    public void onPositionUpdate(float position, boolean isLeaving) {

        d(TAG, "GestureUtil: onPositionUpdate: " + position + " isLeaving: " + isLeaving);

        if (gesturePositionListener != null) {
            gesturePositionListener.onGesturePositionUpdate(position, isLeaving);
        }

        if (backgroundView != null) {
            d(TAG, "GestureUtil: onPositionUpdate: bgv is not null");
            backgroundView.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
            backgroundView.getBackground().setAlpha((int) (255 * position));
        }

        if (gestureImageView != null) {
            gestureImageView.setVisibility(position == 0f && isLeaving ? View.INVISIBLE : View.VISIBLE);
        }

        View[] viewsToShowOnFullScreenArray = viewsToShowOnFullScreen.toArray(new View[0]);
        View[] viewsToHideOnFullScreenArray = viewsToHideOnFullScreen.toArray(new View[0]);

        d(TAG, "GestureUtil: onPositionUpdate: viewsToShowOnFullScreen: " + viewsToShowOnFullScreenArray.length);
        d(TAG, "GestureUtil: onPositionUpdate: viewsToHideOnFullScreen: " + viewsToHideOnFullScreenArray.length);

        ViewUtils.setAlpha(position, viewsToShowOnFullScreenArray);
        ViewUtils.toggleVisibility(position == 0f && isLeaving ? View.INVISIBLE : View.VISIBLE, viewsToShowOnFullScreenArray);

        if (getViewToCloseAnimation() != null) {
            ViewUtils.setAlpha(position, getViewToCloseAnimation());
            ViewUtils.toggleVisibility(position == 0f && isLeaving ? View.INVISIBLE : View.VISIBLE, getViewToCloseAnimation());
        }

        ViewUtils.setAlpha(1 - position, viewsToHideOnFullScreenArray);
        ViewUtils.toggleVisibility(position == 0f ? View.VISIBLE : View.GONE, viewsToHideOnFullScreenArray);

        // com: sagar : 1/5/19 6:18 PM nullify on exit animation position float value
        if (position == 0f && isLeaving) {
            resetUtil();
        }
    }

    public View getViewToCloseAnimation() {
        return viewToCloseAnimation;
    }

    public void setViewToCloseAnimation(View viewToCloseAnimation) {
        this.viewToCloseAnimation = viewToCloseAnimation;
    }

    // sagar : 9/4/19 6:44 PM Call this method when you have nestedListImageView, i.e., a recyclerView in which each item can have individual recyclerView to show photo list. Here, the nestedListImageView must have some url set on it.
    // sagar : 19/4/19 4:49 PM Refer: PrescriptionListAdapter -> AvailMyRequestFragment -> DiscoverActivity
    public void onClickImageForNestedAnimation(RecyclerView recyclerView, ViewPager imageViewPager, ImageView nestedListImageView, List<Photo> photoList, int clickedPhotoPosition) {

        // sagar : 9/4/19 6:45 PM These methods must be called in order to make the animator work properly
        setUpPhotoPagerAdapter(getContext(), imageViewPager, photoList);
        initNestedListAnimator(recyclerView, nestedListImageView, imageViewPager);
        loadNestedListAnimator(clickedPhotoPosition);
    }

    public void setUpPhotoPagerAdapter(Context context, ViewPager imageViewPager, List<Photo> photoList) {
        if (context != null && imageViewPager != null && Utils.isNotNullNotEmpty(photoList)) {
            photoPagerAdapter = new PhotoPagerAdapter(context, imageViewPager, photoList);
            imageViewPager.setAdapter(photoPagerAdapter);
        }
    }

    /**
     * sagar : 9/4/19 6:27 PM
     * See DiscoverActivity#initializeAnimator method to know what is nestedListImageView and how to get it
     * We get it from RvPhotoAdapter using getImageView(int position) method.
     * This nestedListImageView must have some photo url set on it.
     *
     * @since 1.0
     */
    public void initNestedListAnimator(RecyclerView rvPhotos, ImageView nestedListImageView, ViewPager viewPager) {
        // Initializing images animator. It requires us to provide FromTracker and IntoTracker items
        // that are used to find images views for particular item IDs in the list and in the pager
        // to keep them in sync.
        // In this example we will use SimpleTracker which will track images by their positions,
        // if you have a more complicated case see further examples.
        final SimpleTracker listTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                RecyclerView.ViewHolder holder = rvPhotos.findViewHolderForLayoutPosition(position);
                return holder == null ? null
                        : nestedListImageView;
            }
        };

        final SimpleTracker pagerTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                RecyclePagerAdapter.ViewHolder holder = photoPagerAdapter.getViewHolder(position);
                return holder == null ? null : PhotoPagerAdapter.getImageView(holder);
            }
        };

        nestedListAnimator = GestureTransitions.from(rvPhotos, listTracker).into(viewPager, pagerTracker);

        // Setting up background animation during image transition
        nestedListAnimator.addPositionUpdateListener(this);
    }

    // sagar : 9/4/19 6:57 PM Call this method for photoListAnimation
    public void loadNestedListAnimator(int clickedPhotoPosition) {
        if (nestedListAnimator != null) {
            nestedListAnimator.enter(clickedPhotoPosition, true);
        }
    }

    public void onClickImageForSingleToPagerAnimation(RecyclerView recyclerView, ViewPager imageViewPager, ImageView nestedListImageView, List<Photo> photoList, int clickedPhotoPosition) {

        if (imageViewPager != null && nestedListImageView != null && Utils.isNotNullNotEmpty(photoList)) {
            // sagar : 9/4/19 6:45 PM These methods must be called in order to make the animator work properly
            setUpPhotoPagerAdapter(getContext(), imageViewPager, photoList);


            final SimpleTracker pagerTracker = new SimpleTracker() {
                @Override
                public View getViewAt(int position) {
                    RecyclePagerAdapter.ViewHolder holder = photoPagerAdapter.getViewHolder(position);
                    return holder == null ? null : PhotoPagerAdapter.getImageView(holder);
                }
            };

            // sagar : 9/4/19 6:00 PM For single list animation, we have to re-initialize animator for each new imageView position

            animator.addPositionUpdateListener(this);

            loadNestedListAnimator(clickedPhotoPosition);
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public boolean isFullScreenImageAnimation() {
        return (animator != null && !animator.isLeaving()) || (nestedListAnimator != null && !nestedListAnimator.isLeaving());
    }
}
