package com.demo.activity;

import android.os.Bundle;

import com.demo.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    public void showFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_content, fragment)
                .commit();
    }

    public void showFragment(Fragment fragment, boolean isBack){
        FragmentManager fm = getSupportFragmentManager();
        if (isBack) {
            fm.beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(fragment.toString())
                    .commit();
        } else{
            fm.beginTransaction().replace(R.id.frame_content, fragment)
                    .commit();
        }
    }
}