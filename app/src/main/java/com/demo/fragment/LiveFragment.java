package com.demo.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.utils.Utils;
import com.demo.view.CPlayView;
import com.sdk.AlarmMessCallBackV30;
import com.sdk.ExceptionCallBack_PF;
import com.sdk.NETDEV_AUDIO_SAMPLE_PARAM_S;
import com.sdk.NETDEV_CLOUD_DEV_INFO_S;
import com.sdk.NETDEV_CLOUD_DEV_LOGIN_INFO_S;
import com.sdk.NETDEV_CLOUD_DEV_LOGIN_S;
import com.sdk.NETDEV_DECODE_AUDIO_DATA_CALLBACK_PF;
import com.sdk.NETDEV_DEVICE_INFO_S;
import com.sdk.NETDEV_DEV_BASIC_INFO_S;
import com.sdk.NETDEV_DEV_CHN_ENCODE_INFO_S;
import com.sdk.NETDEV_LOGIN_INFO_S;
import com.sdk.NETDEV_PREVIEWINFO_S;
import com.sdk.NETDEV_VIDEO_CHL_DETAIL_INFO_S;
import com.sdk.NetDEVSDK;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.sdk.NetDEVSDK.gastLoginDeviceInfo;

public class LiveFragment extends BaseFragment{

    private Spinner NameSpinner;
    private Spinner m_oChannelList;
    private Button m_oStartLiveViewBtn;
    private Button m_oStopLiveViewBtn;
    private ArrayAdapter<String> m_oChnAdapter;
    static Map<String, Long> nameLpUserIDMap = new HashMap<String, Long>();
    static Map<String, List<NETDEV_VIDEO_CHL_DETAIL_INFO_S>> NameChannelMap = new HashMap<String, List<NETDEV_VIDEO_CHL_DETAIL_INFO_S>>();
    private int m_dwChannelID;

    private static String strCloudDevName = null;

    public CPlayView m_oPlayer;        //Live GLsurfaceADMIN12345
    public CPlayView m_oPlayer1;        //Live GLsurfaceADMIN12345
    public CPlayView m_oPlayer2;        //Live GLsurfaceADMIN12345
    public CPlayView m_oPlayer3;

    public static long lpLiveViewHandle[] = new long[4];     //live video ID
    public static float scaleRatio[] = new float[4];     //live video ID

    @Override
    protected int getLayout() {
        return R.layout.fragment_live;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        ArrayList<String> szDevNameList = bundle.getStringArrayList("szDevName");
        long[] IDList  = bundle.getLongArray("cloudLpUserID");
        ArrayList<Long> cloudLpUserIDList = new ArrayList<Long>();
        for (long e : IDList){
            cloudLpUserIDList.add(e);
        }

        m_oChannelList = (Spinner) mRootView.findViewById(R.id.channelList);
        NameSpinner = (Spinner) mRootView.findViewById(R.id.device_spinner);
        m_oStartLiveViewBtn = (Button) mRootView.findViewById(R.id.startLiveView);
        m_oPlayer = (CPlayView) mRootView.findViewById(R.id.liveview_View);
        m_oPlayer1 = (CPlayView) mRootView.findViewById(R.id.liveview1_View);
        m_oPlayer2 = (CPlayView) mRootView.findViewById(R.id.liveview2_View);
        m_oPlayer3 = (CPlayView) mRootView.findViewById(R.id.liveview3_View);
        m_oStopLiveViewBtn = (Button) mRootView.findViewById(R.id.stopLiveView);

        m_oPlayer.m_dwWinIndex = 1;
        m_oPlayer1.m_dwWinIndex = 2;
        m_oPlayer2.m_dwWinIndex = 3;
        m_oPlayer3.m_dwWinIndex = 4;
        m_oPlayer.m_bCanDrawFrame = false;
        m_oPlayer1.m_bCanDrawFrame = false;
        m_oPlayer2.m_bCanDrawFrame = false;
        m_oPlayer3.m_bCanDrawFrame = false;
        NetDEVSDK.gdwWinIndex = 1;
        LiveView();/*initial main interface*/

        scaleRatio[0] = 1.0f;
        scaleRatio[1] = 1.0f;
        scaleRatio[2] = 1.0f;
        scaleRatio[3] = 1.0f;

        ArrayAdapter<String> m_oNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, szDevNameList);
        NameSpinner.setAdapter(m_oNameAdapter);

        if (szDevNameList.size() > 0) {
            NameSpinner.setSelection(szDevNameList.size() - 1, true);
        }
        strCloudDevName = szDevNameList.get(szDevNameList.size() - 1);
        Long value = cloudLpUserIDList.get(cloudLpUserIDList.size()-1);
        nameLpUserIDMap.put(strCloudDevName, value);

        if (0 == NetDEVSDK.dwDeviceType)
        {
            List<NETDEV_VIDEO_CHL_DETAIL_INFO_S> chlList = new ArrayList<NETDEV_VIDEO_CHL_DETAIL_INFO_S>();
            chlList = NetDEVSDK.NETDEV_QueryVideoChlDetailList(nameLpUserIDMap.get(strCloudDevName), 64);
            m_oChnAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
            m_oChannelList.setAdapter(m_oChnAdapter);
            for (int i = 0; i < (chlList == null ? 0 : chlList.size()); i++) {
                NETDEV_VIDEO_CHL_DETAIL_INFO_S tmp = chlList.get(i);
                System.out.println(i + " Status -" + ((tmp.enStatus == 1) ? "Online" : "Offline"));
                m_oChnAdapter.add("Channel - " + tmp.dwChannelID + ": Status -" + ((tmp.enStatus == 1) ? "Online" : "Offline") + "  Ptz -" + ((tmp.bPtzSupported != 0) ? "Yes" : "No"));
            }
            NameChannelMap.put(szDevNameList.get(szDevNameList.size() - 1), chlList);
        }

    }


    private void LiveView() {

        /* channel list */
        m_oChannelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String strItem = arg0.getItemAtPosition(arg2).toString();
                String strTemp = "Channel - ";
                m_dwChannelID = Integer.parseInt(strItem.substring(strItem.indexOf(strTemp)+strTemp.length(), strItem.indexOf(": Status")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        /*start live view */
        m_oStartLiveViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NETDEV_PREVIEWINFO_S stPreviewInfo = new NETDEV_PREVIEWINFO_S();
                stPreviewInfo.dwChannelID = m_dwChannelID;                        //  the value can be modified at the interface
                stPreviewInfo.dwLinkMode = 1;                                // 1,/* TCP */             2,/* UDP */
                stPreviewInfo.dwStreamIndex = 0;                            // 0,/*   Main stream */       1,/*   Sub stream */       2,/*  Third stream */
                if (1 == NetDEVSDK.gdwWinIndex) {
                    m_oPlayer.m_bCanDrawFrame = true;
                    NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer.m_dwWinIndex - 1], m_oPlayer.m_dwWinIndex);
                } else if (2 == NetDEVSDK.gdwWinIndex) {
                    m_oPlayer1.m_bCanDrawFrame = true;
                    NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer1.m_dwWinIndex - 1], m_oPlayer1.m_dwWinIndex);
                } else if (3 == NetDEVSDK.gdwWinIndex) {
                    m_oPlayer2.m_bCanDrawFrame = true;
                    NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer2.m_dwWinIndex - 1], m_oPlayer2.m_dwWinIndex);
                } else if (4 == NetDEVSDK.gdwWinIndex) {
                    m_oPlayer3.m_bCanDrawFrame = true;
                    NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer3.m_dwWinIndex - 1], m_oPlayer3.m_dwWinIndex);
                }

                lpLiveViewHandle[NetDEVSDK.gdwWinIndex - 1] = NetDEVSDK.NETDEV_RealPlay(nameLpUserIDMap.get(strCloudDevName), stPreviewInfo, NetDEVSDK.gdwWinIndex);

                scaleRatio[NetDEVSDK.gdwWinIndex - 1] = 1.0f;
                NetDEVSDK.Scale(scaleRatio[NetDEVSDK.gdwWinIndex - 1], 0, 0, NetDEVSDK.gdwWinIndex);
            }
        });

        m_oStopLiveViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLive();
            }
        });

    }


    private void stopLive() {
        int iRet = -1;
        if (true == m_oPlayer.m_bCanDrawFrame) {
            m_oPlayer.m_bCanDrawFrame = false;
            iRet = NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer.m_dwWinIndex - 1], m_oPlayer.m_dwWinIndex);
        }
        if (true == m_oPlayer1.m_bCanDrawFrame) {
            m_oPlayer1.m_bCanDrawFrame = false;
            iRet = NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer1.m_dwWinIndex - 1], m_oPlayer1.m_dwWinIndex);
        }
        if (true == m_oPlayer2.m_bCanDrawFrame) {
            m_oPlayer2.m_bCanDrawFrame = false;
            iRet = NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer2.m_dwWinIndex - 1], m_oPlayer2.m_dwWinIndex);
        }
        if (true == m_oPlayer3.m_bCanDrawFrame) {
            m_oPlayer3.m_bCanDrawFrame = false;
            iRet = NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[m_oPlayer3.m_dwWinIndex - 1], m_oPlayer3.m_dwWinIndex);
        }

        lpLiveViewHandle[0] = 0;
        lpLiveViewHandle[1] = 0;
        lpLiveViewHandle[2] = 0;
        lpLiveViewHandle[3] = 0;
        System.out.println("----------------------" + iRet);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        int iRet = NetDEVSDK.NETDEV_StopRealPlay(lpLiveViewHandle[0], 1);
        lpLiveViewHandle[0] = 0;
    }

}
