package com.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.R;

public class PhoneFragment extends BaseFragment {

    private Button btnPhoneLogin;

    @Override
    protected int getLayout() {
        return R.layout.fragment_phone;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPhoneLogin = mRootView.findViewById(R.id.btn_phone_login);
        btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new OTPConfirmFragment(), true);
            }
        });
    }
}
