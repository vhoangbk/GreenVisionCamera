package com.demo.ui.auth;

import com.demo.model.CloudUser;

import androidx.lifecycle.ViewModel;

public class OTPConfirmViewModel extends ViewModel {

    public interface OnCloudLoginListener{
        void onPreExecute();
        void onPostExecute(long id);
    }

    public void cloudLogin(OnCloudLoginListener handler){
        handler.onPreExecute();
        String cloudServerUrl = "uvision-tech.net";
        String cloudUserName = "quyetnisoka@gmail.com";
        String cloudPassword = "123456@";
        CloudUser cloudUser = new CloudUser(cloudServerUrl, cloudUserName, cloudPassword);
        long id = cloudUser.login();
        if (id != 0){
            handler.onPostExecute(id);
        } else {
            handler.onPostExecute(0);
        }
    }
}
