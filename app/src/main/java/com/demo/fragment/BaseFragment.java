package com.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.activity.BaseActivity;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected static String TAG;

    protected abstract int getLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        mRootView = inflater.inflate(getLayout(), container, false);
        TAG = this.getClass().getName();
        return mRootView;
    }

    public void showFragment(Fragment f){
        ((BaseActivity) getActivity()).showFragment(f);
    }
    public void showFragment(Fragment f, boolean isBack){
        ((BaseActivity) getActivity()).showFragment(f, isBack);
    }
}
