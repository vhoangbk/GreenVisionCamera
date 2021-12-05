package com.demo.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.utils.Utils;
import com.sdk.AlarmMessCallBackV30;
import com.sdk.ExceptionCallBack_PF;
import com.sdk.NETDEV_CLOUD_DEV_INFO_S;
import com.sdk.NETDEV_CLOUD_DEV_LOGIN_INFO_S;
import com.sdk.NETDEV_CLOUD_DEV_LOGIN_S;
import com.sdk.NETDEV_DEVICE_INFO_S;
import com.sdk.NETDEV_LOGIN_INFO_S;
import com.sdk.NetDEVSDK;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainFragment extends BaseFragment{

    private Button btnDevList, btnDevLogin;
    private TextView tvDevName;
    ArrayList<Long> cloudLpUserIDList = new ArrayList<Long>();
    ArrayList<String> cloudDevNameList = new ArrayList<String>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDevList = mRootView.findViewById(R.id.btn_dev_list);
        btnDevLogin = mRootView.findViewById(R.id.btn_dev_login);
        tvDevName = mRootView.findViewById(R.id.tv_dev_name);

        btnDevList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDevList();
            }
        });

        btnDevLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devLogin();
            }
        });
    }

    private void getDevList(){
        String strTag = "FindCloudDevList";
        String[] szDevList = new String[10];

        long dwFileHandle = NetDEVSDK.NETDEV_FindCloudDevList(NetDEVSDK.glpcloudID);
        if (0 == dwFileHandle) {
            Log.i(strTag, "Query cloud device list failed.");
        } else {
            AlertDialog.Builder oBuilder = new AlertDialog.Builder(getActivity());
            oBuilder.setTitle("Device List");
            int dwCount = 0;

            NETDEV_CLOUD_DEV_INFO_S stclouddeviceinfo = new NETDEV_CLOUD_DEV_INFO_S();
            for (int i = 0; i < 10; i++) {
                String strMeg = "";
                String strOut = "";
                int iRet = NetDEVSDK.NETDEV_FindNextCloudDevInfo(dwFileHandle, stclouddeviceinfo);
                if (0 == iRet) {
                    break;
                } else {
                    strMeg = "IP:" + stclouddeviceinfo.szIPAddr + "\n";
                    strOut += strMeg;
                    strMeg = "User Name:" + stclouddeviceinfo.szDevUserName + "\n";
                    strOut += strMeg;
                    strMeg = "Serial Num:" + stclouddeviceinfo.szDevSerialNum + "\n";
                    strOut += strMeg;
                    strMeg = "Dev Name:" + stclouddeviceinfo.szDevName + "\n";
                    strOut += strMeg;
                    strMeg = "Dev Model:" + stclouddeviceinfo.szDevModel + "\n";
                    strOut += strMeg;
                    strMeg = "Dev Port:" + String.valueOf(stclouddeviceinfo.dwDevPort) + "";
                    strOut += strMeg;

                    szDevList[dwCount++] = strOut;
                }
                Log.i(strTag, strOut);
            }

            String[] szDevListArr = new String[dwCount];
            System.arraycopy(szDevList, 0, szDevListArr, 0, dwCount);
            oBuilder.setSingleChoiceItems(szDevListArr, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int witch) {
                    String str = szDevList[witch];
                    String strUserName = str.substring(str.indexOf("User Name:") + 10, str.indexOf("Serial Num:")-1);
                    tvDevName.setText(strUserName);
                    NetDEVSDK.strDevName = strUserName;

                    String strDeDeviceType = str.substring(str.indexOf("Dev Model:") + 10, str.indexOf("Dev Port:"));
                    boolean bIsVMS = strDeDeviceType.contains("VMS");
                    if(true == bIsVMS)
                    {
                        NetDEVSDK.dwDeviceType = 1;
                    }
                }
            });
            oBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    int dwFileHandle = NetDEVSDK.NETDEV_FindCloseCloudDevList(NetDEVSDK.glpcloudID);
                    if (0 == dwFileHandle) {
                        Log.i(strTag, "Find Close cloud device list failed.");
                    }

                }
            });
            oBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    int dwFileHandle = NetDEVSDK.NETDEV_FindCloseCloudDevList(NetDEVSDK.glpcloudID);
                    if (0 == dwFileHandle) {
                        Log.i(strTag, "Find Close cloud device list failed.");
                    }

                }
            });
            oBuilder.show();
        }
    }

    private void devLogin(){
        new DevLoginHandle().execute();
    }

    class DevLoginHandle extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("login device, please wait.");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            NETDEV_CLOUD_DEV_LOGIN_S stCloudDevInfo = new NETDEV_CLOUD_DEV_LOGIN_S();
            NETDEV_DEVICE_INFO_S stDeviceInfo = new NETDEV_DEVICE_INFO_S();
            stCloudDevInfo.szDeviceName = NetDEVSDK.strDevName;
            stCloudDevInfo.szDevicePassword = "";
            stCloudDevInfo.dwT2UTimeout = 10;

            if (null == stCloudDevInfo.szDeviceName) {
                Utils.showAlert(getActivity(), "", "Username is empty!");
            } else {
                if (stCloudDevInfo.szDevicePassword.isEmpty()) {
                    NETDEV_LOGIN_INFO_S stLoginInfo = new NETDEV_LOGIN_INFO_S();
                    NETDEV_CLOUD_DEV_LOGIN_INFO_S stCloudInfo = new NETDEV_CLOUD_DEV_LOGIN_INFO_S();

                    stCloudInfo.szDeviceName = NetDEVSDK.strDevName;
                    stCloudInfo.dwT2UTimeout = 15;
                    stCloudInfo.dwConnectMode = 2;

                    if(1 == NetDEVSDK.dwDeviceType)
                    {
                        if (cloudLpUserIDList.size() >0)
                        {
                            Toast.makeText(getActivity(), "Login VMS allows only one!", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                        stCloudInfo.dwLoginProto = 1;
                    }

                    NetDEVSDK.lpUserID = NetDEVSDK.NETDEV_LoginCloudDevice_V30(NetDEVSDK.glpcloudID,stCloudInfo);
                    if (0 == NetDEVSDK.lpUserID) {
                        stCloudInfo.dwConnectMode = 3;
                        NetDEVSDK.lpUserID = NetDEVSDK.NETDEV_LoginCloudDevice_V30(NetDEVSDK.glpcloudID,stCloudInfo);
                        if(0 == NetDEVSDK.lpUserID)
                        {
                            Log.i("Login Device", "NETDEV_LoginCloudDevice_V30 failed! ");
                            return null;
                        }

                    }
                    if (0 != NetDEVSDK.lpUserID) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AlarmMessCallBackV30 pfAlarmCallBackV30 = new AlarmMessCallBackV30();
                                NetDEVSDK.NETDEV_Android_SetAlarmCallBack_V30(NetDEVSDK.lpUserID, pfAlarmCallBackV30, 0);
                                ExceptionCallBack_PF pfExceptionCallBack = new ExceptionCallBack_PF();
                                NetDEVSDK.NETDEV_Android_SetExceptionCallBack(pfExceptionCallBack, 0);
                            }
                        }).start();
                        Log.i("wbtest", "startActivity");
                        if (!cloudDevNameList.contains(stCloudDevInfo.szDeviceName)){
                            cloudLpUserIDList.add(NetDEVSDK.lpUserID);
                            cloudDevNameList.add(stCloudDevInfo.szDeviceName);
                        }

                        Bundle bundle = new Bundle();

                        long[] lids = new long[cloudLpUserIDList.size()];
                        for(int i = 0; i<cloudLpUserIDList.size(); i++){
                            lids[i] = cloudLpUserIDList.get(i);
                        }

                        bundle.putLongArray("cloudLpUserID", lids);
                        bundle.putStringArrayList("szDevName",cloudDevNameList);
                        bundle.putBoolean("bLocalDevFlag",false);

                        LiveFragment f = new LiveFragment();
                        f.setArguments(bundle);
                        showFragment(f, true);
                    }
                } else {
                    Utils.showAlert(getActivity(), "","Login was failed!");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dialog.dismiss();
        }
    }
}
