package com.demo.activity;

import android.os.Bundle;

import com.demo.R;
import com.demo.fragment.LoginFragment;
import com.demo.fragment.PhoneFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showFragment(new PhoneFragment());
    }
}