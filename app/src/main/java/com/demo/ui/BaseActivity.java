package com.demo.ui;

import android.os.Bundle;

import com.demo.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
    }

    public void showFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_content, fragment)
                .commit();
    }

    public void showFragment(Fragment fragment, boolean isBack){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_content, fragment);
        if (isBack) {
            ft.addToBackStack(fragment.toString());
        }
        ft.commit();
    }

    public void showFragmentToTop(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
        fm.beginTransaction().replace(R.id.frame_content, fragment)
                .commit();
    }
}
