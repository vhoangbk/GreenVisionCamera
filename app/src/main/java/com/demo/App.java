package com.demo;

import android.app.Application;

import com.sdk.NetDEVSDK;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NetDEVSDK oNetSDKDemo = new NetDEVSDK();
        oNetSDKDemo.NETDEV_Init();
        NetDEVSDK.NETDEV_SetT2UPayLoad(800);
    }
}
