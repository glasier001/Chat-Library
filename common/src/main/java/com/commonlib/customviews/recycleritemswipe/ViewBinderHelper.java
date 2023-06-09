package com.commonlib.customviews.recycleritemswipe;

import android.os.Bundle;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


// TODO: Bhargav Savsasani : 22/11/18 if u need this sources show below link

/**
 * Description
 *
 * <p> Additional Description
 * </p>
 * Use {@link #()} to Usage.
 *
 * @param
 * @param
 * @param
 * @param
 * @return
 * @throws
 * @see <a href="https://github.com/chthai64/SwipeRevealLayout">Read more about this class</a>
 * @sine a
 */
public class ViewBinderHelper {

    private static final String BUNDLE_MAP_KEY = "ViewBinderHelper_Bundle_Map_Key";

    private Map<String, Integer> mapStates = Collections.synchronizedMap(new HashMap<String, Integer>());
    private Map<String, CustomSwipeRevealLayout> mapLayouts = Collections.synchronizedMap(new HashMap<String, CustomSwipeRevealLayout>());
    private Set<String> lockedSwipeSet = Collections.synchronizedSet(new HashSet<String>());

    private volatile boolean openOnlyOne = false;
    private final Object stateChangeLock = new Object();

    /**
     * Help to save and restore open/close state of the swipeLayout. Call this method
     * when you bind your view holder with the data object.
     *
     * @param swipeLayout swipeLayout of the current view.
     * @param id a string that uniquely defines the data object of the current view.
     */
    public void bind(final CustomSwipeRevealLayout swipeLayout, final String id) {
        if (swipeLayout.shouldRequestLayout()) {
            swipeLayout.requestLayout();
        }

        mapLayouts.values().remove(swipeLayout);
        mapLayouts.put(id, swipeLayout);

        swipeLayout.abort();
        swipeLayout.setDragStateChangeListener(new CustomSwipeRevealLayout.DragStateChangeListener() {
            @Override
            public void onDragStateChanged(int state) {
                mapStates.put(id, state);

                if (openOnlyOne) {
                    closeOthers(id, swipeLayout);
                }
            }
        });

        // first time binding.
        if (!mapStates.containsKey(id)) {
            mapStates.put(id, CustomSwipeRevealLayout.STATE_CLOSE);
            swipeLayout.close(false);
        }

        // not the first time, then close or open depends on the current state.
        else {
            int state = mapStates.get(id);

            if (state == CustomSwipeRevealLayout.STATE_CLOSE || state == CustomSwipeRevealLayout.STATE_CLOSING ||
                    state == CustomSwipeRevealLayout.STATE_DRAGGING) {
                swipeLayout.close(false);
            } else {
                swipeLayout.open(false);
            }
        }

        // set lock swipe
        swipeLayout.setLockDrag(lockedSwipeSet.contains(id));
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        if (outState == null)
            return;

        Bundle statesBundle = new Bundle();
        for (Map.Entry<String, Integer> entry : mapStates.entrySet()) {
            statesBundle.putInt(entry.getKey(), entry.getValue());
        }

        outState.putBundle(BUNDLE_MAP_KEY, statesBundle);
    }


    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void restoreStates(Bundle inState) {
        if (inState == null)
            return;

        if (inState.containsKey(BUNDLE_MAP_KEY)) {
            HashMap<String, Integer> restoredMap = new HashMap<>();

            Bundle statesBundle = inState.getBundle(BUNDLE_MAP_KEY);
            Set<String> keySet = statesBundle.keySet();

            if (keySet != null) {
                for (String key : keySet) {
                    restoredMap.put(key, statesBundle.getInt(key));
                }
            }

            mapStates = restoredMap;
        }
    }

    /**
     * Lock swipe for some layouts.
     * @param id a string that uniquely defines the data object.
     */
    public void lockSwipe(String... id) {
        setLockSwipe(true, id);
    }

    /**
     * Unlock swipe for some layouts.
     * @param id a string that uniquely defines the data object.
     */
    public void unlockSwipe(String... id) {
        setLockSwipe(false, id);
    }

    /**
     * @param openOnlyOne If set to true, then only one row can be opened at a time.
     */
    public void setOpenOnlyOne(boolean openOnlyOne) {
        this.openOnlyOne = openOnlyOne;
    }

    /**
     * Open a specific layout.
     * @param id unique id which identifies the data object which is bind to the layout.
     */
    public void openLayout(final String id) {
        synchronized (stateChangeLock) {
            mapStates.put(id, CustomSwipeRevealLayout.STATE_OPEN);

            if (mapLayouts.containsKey(id)) {
                final CustomSwipeRevealLayout layout = mapLayouts.get(id);
                layout.open(true);
            }
            else if (openOnlyOne) {
                closeOthers(id, mapLayouts.get(id));
            }
        }
    }

    /**
     * Close a specific layout.
     * @param id unique id which identifies the data object which is bind to the layout.
     */
    public void closeLayout(final String id) {
        synchronized (stateChangeLock) {
            mapStates.put(id, CustomSwipeRevealLayout.STATE_CLOSE);

            if (mapLayouts.containsKey(id)) {
                final CustomSwipeRevealLayout layout = mapLayouts.get(id);
                layout.close(true);
            }
        }
    }

    /**
     * Close others swipe layout.
     * @param id layout which bind with this data object id will be excluded.
     * @param swipeLayout will be excluded.
     */
    private void closeOthers(String id, CustomSwipeRevealLayout swipeLayout) {
        synchronized (stateChangeLock) {
            // close other rows if openOnlyOne is true.
            if (getOpenCount() > 1) {
                for (Map.Entry<String, Integer> entry : mapStates.entrySet()) {
                    if (!entry.getKey().equals(id)) {
                        entry.setValue(CustomSwipeRevealLayout.STATE_CLOSE);
                    }
                }

                for (CustomSwipeRevealLayout layout : mapLayouts.values()) {
                    if (layout != swipeLayout) {
                        layout.close(true);
                    }
                }
            }
        }
    }

    private void setLockSwipe(boolean lock, String... id) {
        if (id == null || id.length == 0)
            return;

        if (lock)
            lockedSwipeSet.addAll(Arrays.asList(id));
        else
            lockedSwipeSet.removeAll(Arrays.asList(id));

        for (String s : id) {
            CustomSwipeRevealLayout layout = mapLayouts.get(s);
            if (layout != null) {
                layout.setLockDrag(lock);
            }
        }
    }

    private int getOpenCount() {
        int total = 0;

        for (int state : mapStates.values()) {
            if (state == CustomSwipeRevealLayout.STATE_OPEN || state == CustomSwipeRevealLayout.STATE_OPENING) {
                total++;
            }
        }

        return total;
    }
}
