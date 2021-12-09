package com.demo.model;

import com.sdk.NETDEV_CLOUD_LIMIT_INFO_S;
import com.sdk.NETDEV_CLOUD_MOBILE_INFO_S;
import com.sdk.NetDEVSDK;

public class CloudUser {
    private final String serverUrl;
    private final String userName;
    private final String password;

    public CloudUser(String serverUrl, String userName, String password) {
        this.serverUrl = serverUrl;
        this.userName = userName;
        this.password = password;
    }

    public long login(){
        NetDEVSDK.glpcloudID = NetDEVSDK.NETDEV_LoginCloudEx(serverUrl, userName, password, 0);
        if (NetDEVSDK.glpcloudID != 0) {
            setMobileInfo();
        }
        return NetDEVSDK.glpcloudID;
    }

    public void setMobileInfo(){
        NetDEVSDK.NETDEV_SetClientID("1234567890987654321");          /* ClientID must be Unique! */
        NETDEV_CLOUD_MOBILE_INFO_S stMobileInfo = new NETDEV_CLOUD_MOBILE_INFO_S();
        NETDEV_CLOUD_LIMIT_INFO_S stLimitInfo = new NETDEV_CLOUD_LIMIT_INFO_S();
        stMobileInfo.szMobileModule = "A2017";
        stMobileInfo.szSystemType = "Android";
        stMobileInfo.szSystemVersion = "7.0.0.1";
        stMobileInfo.bDonotDisturb = 1;
        stMobileInfo.bPushBuiltFlag = 0;
        stMobileInfo.szAppName = "SdkDemo";
        stMobileInfo.szAppLanguage = "zh-cn";
        stMobileInfo.szAppVersion = "V0.0.0.1";
        stMobileInfo.bIosEnvir = 1;
        stLimitInfo.udwMaxAppTimeS = 0;
        stLimitInfo.udwMaxDeviceNum = 0;
        stLimitInfo.udwMaxRTSNum = 0;
        NetDEVSDK.NETDEV_ReportMobileInfo(NetDEVSDK.glpcloudID, stMobileInfo, stLimitInfo);
    }
}
