package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iray.irs_vms.R;
import com.iray.irs_vms.adapter.DeviceListAdapter;
import com.iray.irs_vms.httpUtils.DeviceManage;
import com.iray.irs_vms.info.DeviceInfo;
import com.iray.irs_vms.widget.RecycleViewDivider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DeviceListActivity extends AppCompatActivity {

    public static DeviceListActivity deviceListActivity;
    private TextView deviceListTvTitle;
    private Button deviceListBtnClose;
    private Button deviceListBtnSearch;
    public ProgressBar pbDeviceList;
    private RecyclerView rcDeviceList;
    private DeviceListAdapter adapter;
    private List<DeviceInfo> mDeviceInfoList;

    DeviceManage deviceManage;
    public DeviceListHandler mDeviceHandler;
    public static final int HANDLER_LIST_ALL_DEVICES = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        init();
    }

    private void init() {
        initView();
        mDeviceInfoList = new ArrayList<>();
        mDeviceHandler = new DeviceListHandler(this);
        deviceManage = DeviceManage.getInstance();
        deviceManage.setDeviceListActivityWeakReference(this);
    }

    private void initView() {
        findView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcDeviceList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.divide_gray_color)));
        rcDeviceList.setLayoutManager(linearLayoutManager);
    }

    private void findView() {
        deviceListTvTitle = (TextView) findViewById(R.id.device_list_tv_title);
        deviceListBtnClose = (Button) findViewById(R.id.device_list_btn_close);
        deviceListBtnSearch = (Button) findViewById(R.id.device_list_btn_search);
        rcDeviceList = (RecyclerView) findViewById(R.id.rc_device_list);
        pbDeviceList = findViewById(R.id.pb_device_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceManage.listAllDevice();
    }

    public void sendDeviceHandler(int msgWhat) {
        mDeviceHandler.sendEmptyMessage(msgWhat);
    }


    public void listDevicesInfo() {
        mDeviceInfoList = deviceManage.mDeviceList;
        if (mDeviceInfoList.size() == 0) {
            Toast.makeText(this, getString(R.string.tst_device_list_empty), Toast.LENGTH_SHORT).show();
        } else {
            adapter = new DeviceListAdapter(this, mDeviceInfoList);
            rcDeviceList.setAdapter(adapter);
        }
    }


    private static class DeviceListHandler extends Handler {
        private WeakReference<DeviceListActivity> reference;
        private DeviceListActivity activity;

        public DeviceListHandler(DeviceListActivity deviceListActivity) {
            super();
            this.reference = new WeakReference<DeviceListActivity>(deviceListActivity);
            this.activity = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LIST_ALL_DEVICES:
                    activity.listDevicesInfo();
                    break;
                default:
                    break;
            }
        }
    }
}
