package com.demo.utils;

import android.app.Activity;
import android.app.AlertDialog;

public class Utils {

    public static void showAlert(Activity activity, String title, String message){
        AlertDialog.Builder oBuilder =new  AlertDialog.Builder(activity);
        oBuilder.setTitle(title);
        oBuilder.setMessage(message);
        oBuilder.setPositiveButton("OK" ,  null );
        oBuilder.show();
    }
}
