package com.demo.model;

import com.sdk.AlarmMessCallBackV30;
import com.sdk.ExceptionCallBack_PF;
import com.sdk.NETDEV_CLOUD_DEV_INFO_S;
import com.sdk.NETDEV_CLOUD_DEV_LOGIN_INFO_S;
import com.sdk.NETDEV_CLOUD_DEV_LOGIN_S;
import com.sdk.NetDEVSDK;

import java.util.ArrayList;
import java.util.List;

public class Device {

    public interface OnLoginListener{
        void onPreExecute();
        void onPostExecute(long id);
    }

    private String ipAddress;
    private String username;
    private String serialNumber;
    private String id;
    private String name;
    private String model;
    private int port;
    private List<Channel> channels;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Device> getDevices(){
        List<Device> devList = new ArrayList<Device>();
        long dwFileHandle = NetDEVSDK.NETDEV_FindCloudDevList(NetDEVSDK.glpcloudID);
        if (dwFileHandle != 0) {
            NETDEV_CLOUD_DEV_INFO_S stclouddeviceinfo = new NETDEV_CLOUD_DEV_INFO_S();
            for (int i = 0; i < 10; i++) {
                int iRet = NetDEVSDK.NETDEV_FindNextCloudDevInfo(dwFileHandle, stclouddeviceinfo);
                if (0 == iRet) {
                    break;
                } else {
                    Device device = new Device();
                    device.id = stclouddeviceinfo.szIPAddr;
                    device.username = stclouddeviceinfo.szDevUserName;
                    device.serialNumber = stclouddeviceinfo.szDevSerialNum;
                    device.name = stclouddeviceinfo.szDevName;
                    device.model = stclouddeviceinfo.szDevModel;
                    device.port = stclouddeviceinfo.dwDevPort;
                    devList.add(device);
                }
            }
        } else {
            return null;
        }
        return devList;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void login(OnLoginListener handle){
        handle.onPreExecute();
        NETDEV_CLOUD_DEV_LOGIN_S stCloudDevInfo = new NETDEV_CLOUD_DEV_LOGIN_S();
        stCloudDevInfo.szDeviceName = NetDEVSDK.strDevName;
        stCloudDevInfo.szDevicePassword = "";
        stCloudDevInfo.dwT2UTimeout = 10;

        NETDEV_CLOUD_DEV_LOGIN_INFO_S stCloudInfo = new NETDEV_CLOUD_DEV_LOGIN_INFO_S();

        stCloudInfo.szDeviceName = NetDEVSDK.strDevName;
        stCloudInfo.dwT2UTimeout = 15;
        stCloudInfo.dwConnectMode = 2;

        NetDEVSDK.lpUserID = NetDEVSDK.NETDEV_LoginCloudDevice_V30(NetDEVSDK.glpcloudID,stCloudInfo);
        if (0 == NetDEVSDK.lpUserID) {
            stCloudInfo.dwConnectMode = 3;
            NetDEVSDK.lpUserID = NetDEVSDK.NETDEV_LoginCloudDevice_V30(NetDEVSDK.glpcloudID,stCloudInfo);
            if(0 == NetDEVSDK.lpUserID) {
                handle.onPostExecute(0);
                return;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmMessCallBackV30 pfAlarmCallBackV30 = new AlarmMessCallBackV30();
                NetDEVSDK.NETDEV_Android_SetAlarmCallBack_V30(NetDEVSDK.lpUserID, pfAlarmCallBackV30, 0);
                ExceptionCallBack_PF pfExceptionCallBack = new ExceptionCallBack_PF();
                NetDEVSDK.NETDEV_Android_SetExceptionCallBack(pfExceptionCallBack, 0);
            }
        }).start();

        handle.onPostExecute(NetDEVSDK.lpUserID);
    }
}
