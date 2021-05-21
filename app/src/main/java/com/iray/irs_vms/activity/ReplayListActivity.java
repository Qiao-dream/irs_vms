package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.DeviceManage;
import com.iray.irs_vms.info.DeviceInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplayListActivity extends AppCompatActivity {
    private final String TAG =  "ReplayListActivity";
    private Context mContext;
    private ConstraintLayout replayListMainLayout;
    private ConstraintLayout replayListTopBar;
    private TextView replayListTvTitle;
    private Button replayListBtnClose;
    private TextView rlTvOrg;
    private Spinner rlSpOrg;
    private TextView rlTvDevice;
    private Spinner rlSpDevice;
    private TextView rlTvStartTime;
    private TextView rlTvSelectStartTime;
    private TextView rlTvEndTime;
    private TextView rlTvSelectEndTime;
    private Button rlBtnFind;
    public ProgressBar pbReplayList;

    DeviceManage deviceManage;
    public ReplayListHandler mReplayListHandler;
    private List<DeviceInfo> mDeviceInfoList;
    private Map<String, ArrayList<String>> mapOrg2Name;
    private ArrayList<String> orgList;
    private String currentOrg;  //当前选择的区域，默认第一个
    private ArrayAdapter<String> orgSpAdapter;
    private ArrayAdapter<String> deviceNameSpAdapter;
    public static final int HANDLER_LIST_ALL_DEVICES = 3001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_list);
        init();
    }

    private void init(){
        mContext = this;
        mDeviceInfoList = new ArrayList<>();
        orgList = new ArrayList<>();
        mapOrg2Name = new HashMap<>();
        mReplayListHandler = new ReplayListHandler(this);
        deviceManage = DeviceManage.getInstance();
        deviceManage.setReplayListActivityWeakReference(this);
        initView();


    }

    private void initView(){
        findView();
        orgSpAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_content, R.id.tv_sp_text);
        orgSpAdapter.setDropDownViewResource(R.layout.spinner_content_drop_down);
        deviceNameSpAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_content, R.id.tv_sp_text);
        deviceNameSpAdapter.setDropDownViewResource(R.layout.spinner_content_drop_down);
        rlSpOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentOrg = (String) rlSpOrg.getItemAtPosition(position);
                setDeviceNameSpAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findView(){
        replayListMainLayout = (ConstraintLayout) findViewById(R.id.replay_list_main_layout);
        replayListTopBar = (ConstraintLayout) findViewById(R.id.replay_list_top_bar);
        replayListTvTitle = (TextView) findViewById(R.id.replay_list_tv_title);
        replayListBtnClose = (Button) findViewById(R.id.replay_list_btn_close);
        rlTvOrg = (TextView) findViewById(R.id.rl_tv_org);
        rlSpOrg = (Spinner) findViewById(R.id.rl_sp_org);
        rlTvDevice = (TextView) findViewById(R.id.rl_tv_device);
        rlSpDevice = (Spinner) findViewById(R.id.rl_sp_device);
        rlTvStartTime = (TextView) findViewById(R.id.rl_tv_start_time);
        rlTvSelectStartTime = (TextView) findViewById(R.id.rl_tv_select_start_time);
        rlTvEndTime = (TextView) findViewById(R.id.rl_tv_end_time);
        rlTvSelectEndTime = (TextView) findViewById(R.id.rl_tv_select_end_time);
        rlBtnFind = (Button) findViewById(R.id.rl_btn_find);
        pbReplayList = (ProgressBar) findViewById(R.id.pb_replay_list);
    }



    @Override
    protected void onResume() {
        super.onResume();
        deviceManage.listAllDeviceRl();
    }

    public void sendReplayHandler(int msgWhat) {
        mReplayListHandler.sendEmptyMessage(msgWhat);
    }

    /**
     * 获取所有设备信息存储到Map中,并获取组织列表
     */
    public void getDeviceInfos(){
        mDeviceInfoList = deviceManage.mDeviceList;
        if (mDeviceInfoList.size() == 0) {
            Toast.makeText(this, getString(R.string.tst_device_list_empty), Toast.LENGTH_SHORT).show();
        } else {
            for(int i = 0; i<mDeviceInfoList.size(); i++){  //遍历获取组织设备map
                String currentOrg = mDeviceInfoList.get(i).getDeviceOrg();
                String currentDeviceName = mDeviceInfoList.get(i).getDeviceName();
                Log.e(TAG, "currentOrg: "+currentOrg +"             currentDeviceName: "+currentDeviceName);
                if(mapOrg2Name.containsKey(currentOrg)){     //已有组织
                    ArrayList<String> deviceNameBuf = mapOrg2Name.get(currentOrg);
                    deviceNameBuf.add(currentDeviceName);
                    mapOrg2Name.put(currentOrg, deviceNameBuf);
                } else {    //新组织
                    orgList.add(currentOrg);
                    ArrayList<String> deviceNameBuf = new ArrayList<>();
                    deviceNameBuf.add(currentDeviceName);
                    mapOrg2Name.put(currentOrg, deviceNameBuf);
                }
            }
        }
    }


    public void setOrgSpAdapter(){
        if(orgList.size()>0) {
            currentOrg = orgList.get(0);
            for (int i = 0; i<orgList.size(); i++){
                orgSpAdapter.add(orgList.get(i));
            }
        } else {
            currentOrg = getString(R.string.rl_sp_org_empty);
            orgSpAdapter.add(currentOrg);
        }
        rlSpOrg.setAdapter(orgSpAdapter);
    }


    public void setDeviceNameSpAdapter(){
        deviceNameSpAdapter.clear();
        if(currentOrg.equals(getString(R.string.rl_sp_org_empty))){

            deviceNameSpAdapter.add(getString(R.string.rl_sp_device_empty));
        } else {
            ArrayList<String> deviceList = mapOrg2Name.get(currentOrg);
            for(int i = 0; i<deviceList.size(); i++){
                deviceNameSpAdapter.add(deviceList.get(i));
            }
        }
        rlSpDevice.setAdapter(deviceNameSpAdapter);
    }



    public static class ReplayListHandler extends Handler {
        private WeakReference<ReplayListActivity> reference;
        private ReplayListActivity activity;

        public ReplayListHandler(ReplayListActivity replayListActivity) {
            super();
            this.reference = new WeakReference<ReplayListActivity>(replayListActivity);
            this.activity = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LIST_ALL_DEVICES:
                    activity.getDeviceInfos();
                    activity.setOrgSpAdapter();
                    activity.setDeviceNameSpAdapter();
                    break;
                default:
                    break;
            }
        }
    }
}
