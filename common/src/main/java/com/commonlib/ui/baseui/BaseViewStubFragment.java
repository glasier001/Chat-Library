package com.commonlib.ui.baseui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.commonlib.R;
import com.commonlib.constants.IntentKeys;
import com.commonlib.listeners.Callbacks;
import com.commonlib.utils.CustomToastMessage;
import com.commonlib.utils.Utils;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.util.Log.d;
import static androidx.constraintlayout.widget.Constraints.TAG;

public abstract class BaseViewStubFragment extends androidx.fragment.app.Fragment
        implements Callbacks.FragmentLifecycle {

    private Bundle mSavedInstanceState;
    private boolean mHasInflated = false;
    private ViewStub mViewStub;
    private View pb;
    private Unbinder mUnBinder;
    private Callbacks.OnAttachActivity onAttachActivity;
    private FragmentActivity fragmentActivity;
    private Bundle mBundleData;
    private String mApi;

    public String getApi() {
        return mApi;
    }

    public void setApi(String api) {
        this.mApi = api;
    }

    public boolean ismHasInflated() {
        return mHasInflated;
    }

    public void setHasInflated(boolean hasInflated) {
        this.mHasInflated = hasInflated;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //Condition to prevent an attempt to re-inflate viewstub otherwise it can lead crash!
        if (isVisibleToUser && mViewStub != null && !mHasInflated) {
            afterViewStubInflated(mViewStub.inflate()); //It will inflate the layout that has been attached to it with setLayoutResource
        }
    }

    /**
     * @param inflate main view not stub view.
     */
    @CallSuper
    protected void afterViewStubInflated(View inflate) {
        mHasInflated = true;
        if (inflate != null) {
            pb.setVisibility(View.GONE);
            mUnBinder = ButterKnife.bind(this, inflate);
            onViewStubInflated(inflate);
            setApi(Utils.getApi(getArguments()));
            initControllers();
            setCustomTitleBar();
            handleViews();
            // sagar : 20/11/18 After handle views to prevent null view
            setBundleData(getArguments());
            setListeners();
            otherStuffs();
        }
    }

    protected abstract void onViewStubInflated(View inflatedView);

    protected abstract void initControllers();

    protected abstract void setCustomTitleBar();

    protected abstract void handleViews();

    protected abstract void setListeners();

    protected abstract void otherStuffs();
    //endregion

    //region Will be used for dagger 2
//    public abstract void injectComponent(DaggerComponent component);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (onAttachActivity != null) {
            onAttachActivity.onGetActivity((FragmentActivity) context);
            setActivity((FragmentActivity) context);
            onAttachActivity = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (onAttachActivity != null) {
            onAttachActivity.onGetActivity((FragmentActivity) activity);
            setActivity((FragmentActivity) activity);
            onAttachActivity = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        d(TAG, "BaseViewStubFragment: onCreateView: ");
        final View rootView = inflater.inflate(R.layout.common_stub, container, false);
        mViewStub = (ViewStub) rootView.findViewById(R.id.fragmentViewStub);
        pb = rootView.findViewById(R.id.inflateProgressbar);
        mViewStub.setLayoutResource(setLayout()); // This layout will be inflated. It has been set to be inflated but not yet inflated.
        mSavedInstanceState = savedInstanceState;
        //region Will be used for dagger 2
//        if (getActivity() != null) {
//            injectComponent(((ConciergeApp) getActivity().getApplication()).getDaggerComponent());
//        }
        //endregion

        /*
         * Inside onCreateView(), we use getUserVisibleHint() (default is true and that's why, we had to take
         * another our very own boolean field) to check whether
         * the current fragment is already visible to user, if yes, we inflate it directly
         * by calling mViewStub.inflate() else don’t inflate it.
         * */

        if (getUserVisibleHint() && !mHasInflated) {
            afterViewStubInflated(mViewStub.inflate());
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // sagar : 4/12/18 This method ensures that activity is finally inflated and we are ready to use its views, children and fragments
        d(TAG, "BaseViewStubFragment: onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);

    }

    // Thanks to Noa Drach, this will fix the orientation change problem
    @Override
    public void onDetach() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDetach();
        mHasInflated = false;
    }

    @LayoutRes
    protected abstract int setLayout();

    public abstract void setActivity(FragmentActivity activity);

    public Bundle getBundleData() {
        return mBundleData;
    }

    protected void setBundleData(Bundle arguments) {
        this.mBundleData = arguments;
    }

    public void getAvailableActivity(Callbacks.OnAttachActivity onAttachActivity) {
        if (getActivity() == null) {
            this.onAttachActivity = onAttachActivity;
        } else {
            onAttachActivity.onGetActivity(getActivity());
            setActivity(getActivity());
        }
    }

    public void showToastMessage(String message, boolean isError) {
        if (isError) {
            showErrorMessage(message);
        } else {
            showSuccessMessage(message);
        }
    }

    public void showErrorMessage(String message) {
        CustomToastMessage.animRedTextMethod(getActivity(), message);
    }

    public void showSuccessMessage(String message) {
        CustomToastMessage.animGreenTextMethod(getActivity(), message);
    }

    public void showErrorMessage(int stringResId) {
        CustomToastMessage.animRedTextMethod(getActivity(), getString(stringResId));
    }

    public void showSuccessMessage(int stringResId) {
        CustomToastMessage.animGreenTextMethod(getActivity(), getString(stringResId));
    }

    private void setViewStubListener(ViewStub mViewStub) {
        mViewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {

            }
        });
    }

    public void showErrorMessage(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IntentKeys.IK_MESSAGE)) {
                showErrorMessage(intent.getStringExtra(IntentKeys.IK_MESSAGE));
            }
        }
    }
}
