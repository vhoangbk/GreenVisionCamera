package com.demo.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.demo.R;
import com.demo.activity.MainActivity;
import com.demo.utils.Utils;
import com.sdk.NETDEV_CLOUD_LIMIT_INFO_S;
import com.sdk.NETDEV_CLOUD_MOBILE_INFO_S;
import com.sdk.NetDEVSDK;

import androidx.annotation.Nullable;

public class LoginFragment extends BaseFragment {

    private EditText etServerUrl, etName, etPasword;
    private Button btnLogin;

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NetDEVSDK oNetSDKDemo = new NetDEVSDK();
        oNetSDKDemo.NETDEV_Init();
        NetDEVSDK.NETDEV_SetT2UPayLoad(800);

        this.etServerUrl = mRootView.findViewById(R.id.et_server_url);
        this.etName = mRootView.findViewById(R.id.et_name);
        this.etPasword = mRootView.findViewById(R.id.et_password);
        this.btnLogin = mRootView.findViewById(R.id.btn_login);

        this.etServerUrl.setText("uvision-tech.net");
        this.etName.setText("quyetnisoka@gmail.com");
        this.etPasword.setText("123456@");

        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cloudLogin();
            }
        });
    }

//    class LoginHanlde extends AsyncTask<Void,Void,Void>{
//
//        private ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = new ProgressDialog(getActivity());
//            dialog.setMessage("Logging, please wait.");
//            dialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            String strCloudUserName = etName.getText().toString();
//            String strCloudPassword = etPasword.getText().toString();
//            String strCloudServerUrl = etServerUrl.getText().toString();
//            int  iNoAccountFlag = 0;
//            if ((0 == strCloudUserName.length()) && (0 == strCloudPassword.length()))
//            {
//                iNoAccountFlag = 1;
//            }
//            NetDEVSDK.glpcloudID = NetDEVSDK.NETDEV_LoginCloudEx(strCloudServerUrl, strCloudUserName, strCloudPassword, iNoAccountFlag);
//            if(0 != NetDEVSDK.glpcloudID)
//            {
//                /* Client info as demo. Must be filled by real data while actual development */
//                NetDEVSDK.NETDEV_SetClientID("1234567890987654321");          /* ClienID must be Unique! */
//                NETDEV_CLOUD_MOBILE_INFO_S stMobileInfo = new NETDEV_CLOUD_MOBILE_INFO_S();
//                NETDEV_CLOUD_LIMIT_INFO_S stLimitInfo = new NETDEV_CLOUD_LIMIT_INFO_S();
//                stMobileInfo.szMobileModule = "A2017";
//                stMobileInfo.szSystemType = "Andriod";
//                stMobileInfo.szSystemVersion = "7.0.0.1";
//                stMobileInfo.bDonotDisturb = 1;
//                stMobileInfo.bPushBuiltFlag = 0;
//                stMobileInfo.szAppName = "SdkDemo";
//                stMobileInfo.szAppLanguage = "zh-cn";
//                stMobileInfo.szAppVersion = "V0.0.0.1";
//                stMobileInfo.bIosEnvir = 1;
//                stLimitInfo.udwMaxAppTimeS = 0;
//                stLimitInfo.udwMaxDeviceNum = 0;
//                stLimitInfo.udwMaxRTSNum = 0;
//                int iRet = NetDEVSDK.NETDEV_ReportMobileInfo(NetDEVSDK.glpcloudID, stMobileInfo, stLimitInfo);
//                if(0 == iRet)
//                {
//                    Utils.showAlert(getActivity(),"Fail", "Report Mobile Info Fail.");
//                }
//                showFragment(new MainFragment());
//            }else {
//                Utils.showAlert(getActivity(),"Fail", "Login failed, please check if the input is correct.");
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//
//            dialog.dismiss();
//        }
//    }

    private void cloudLogin(){

//        new LoginHanlde().execute();


    }
}
