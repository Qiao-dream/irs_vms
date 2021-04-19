package com.iray.irs_vms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.DeviceManage;

public class DeviceListActivity extends AppCompatActivity {

    public static DeviceListActivity deviceListActivity;
    private TextView deviceListTvTitle;
    private Button deviceListBtnClose;
    private Button deviceListBtnSearch;
    public ProgressBar pbDeviceList;
    private RecyclerView rcDeviceList;

    DeviceManage deviceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        init();
    }

    private void init(){
        initView();
        deviceManage = DeviceManage.getInstance();
        deviceManage.setDeviceListActivityWeakReference(this);
    }

    private void initView(){
        findView();
    }

    private void findView(){
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
}
