package com.demo.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.databinding.FragmentPhoneBinding;
import com.demo.ui.BaseActivity;
import com.demo.ui.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class PhoneFragment extends BaseFragment {

    private PhoneViewModel viewModel;
    private FragmentPhoneBinding binding;

    public PhoneFragment(BaseActivity activity) {
        super(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(mActivity).get(PhoneViewModel.class);
        binding = FragmentPhoneBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnPhoneLogin.setOnClickListener(v -> {
            mActivity.showFragment(new OTPConfirmFragment(mActivity), true);
        });


    }

}
