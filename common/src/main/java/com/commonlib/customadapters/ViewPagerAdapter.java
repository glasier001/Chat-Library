package com.commonlib.customadapters;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by sagar on 18/7/17.
 * FragmentStatePagerAdapter will destroy last fragment
 * FragmentPagerAdapter will keep the state of last loaded fragment
 * Extend any one of it according to requirement
 *
 * Properly manage fragment for the case: put your app in background for few minutes, open nested fragments, null fragments/activity
 * https://stackoverflow.com/questions/7951730/viewpager-and-fragments-whats-the-right-way-to-store-fragments-state/9646622#9646622
 *
 * Dynamically add or remove views
 * https://stackoverflow.com/questions/13664155/dynamically-add-and-remove-view-to-viewpager/44281299
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Map<Integer, String> mTags = new HashMap<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitle = new ArrayList<>();
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private Bundle bundle;

    public List<Fragment> getFragmentList(){
        return mFragmentList;
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public static void onRestoreInstanceState(Bundle savedInstanceState) {
        Bundle mSavedInstanceState = savedInstanceState != null ? savedInstanceState : new Bundle();
    }

    public List<String> getTitles() {
        return mFragmentTitle;
    }

    @Override
    public Fragment getItem(int position) {
        if (bundle != null) {
            mFragmentList.get(position).setArguments(bundle);
        }
        return mFragmentList.get(position);
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
    // to add the page to the container, which is normally the ViewPager itself.  Since
    // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
    // page should be displayed, from left-to-right.  If the page no longer exists,
    // return POSITION_NONE. This prevents crash when removing item.
    @Override
    public int getItemPosition(@NonNull Object object) {
        int index = mFragmentList.indexOf(object);

       /* if (index == -1) {
            return POSITION_NONE;
        } else {
            return index;
        }*/

        if (index >= 0) {
            return index;
        } else {
            // sagar : 12/1/19 7:33 PM Returning position none may cause fragment recreation!
            return POSITION_NONE;
        }
    }

    /*public int getItemPosition(Object item) {
        MyFragment fragment = (MyFragment)item;
        String title = fragment.getTitle();
        int position = titles.indexOf(title);

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }
*/
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }

    /**
     * sagar : 8/2/19
     * <p>
     * If we set {{@link #bundle}} value by incoming arguments, then as a result, last argument will be set for all fragments because
     * after this method, {@link #getItem(int)} will be called, which uses {{@link #bundle}} to set as an argument for fragment!
     *
     * @param fragment fragment to display
     * @param title Title for fragment (generally, what we see as a tab title)
     * @param bundle Argument for fragment
     * @see <a href="https://en.wikipedia.org/wiki/Javadoc#Overview_of_Javadoc">Read more about javadoc</a>
     * @since 1.0
     */
    public void addFragment(Fragment fragment, String title, Bundle bundle) {
        /*this.bundle = bundle;*/ // sagar : 8/2/19 5:58 PM Because it will set last bundle for all items.
        fragment.setArguments(bundle);
        // sagar : 6/4/19 3:05 PM Added this method to check empty map view for recreation after few hours of back stack
//        fragment.setRetainInstance(true);
        mFragmentList.add(fragment);
        mFragmentTitle.add(title);
        notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        for (Map.Entry<Integer, String> entry : mTags.entrySet()) {
            outState.putString(makeTagName(entry.getKey()), entry.getValue());
        }
    }

    private static String makeTagName(int position) {
        return ViewPagerAdapter.class.getName() + ":" + position;
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
