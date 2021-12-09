package com.demo.ui;

import android.view.View;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    protected View mRootView;
    protected static String TAG;
    protected BaseActivity mActivity;

    public BaseFragment(BaseActivity activity) {
        this.mActivity = activity;
    }
}
