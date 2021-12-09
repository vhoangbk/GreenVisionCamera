package com.demo.ui;

import android.os.Bundle;

import com.demo.R;
import com.demo.ui.auth.PhoneFragment;

public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        showFragment(new PhoneFragment(this));
    }
}