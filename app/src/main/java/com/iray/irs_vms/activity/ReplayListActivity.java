package com.iray.irs_vms.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.DeviceManage;
import com.iray.irs_vms.info.DeviceInfo;
import com.iray.irs_vms.widget.datepicker.CustomDatePicker;
import com.iray.irs_vms.widget.datepicker.DateFormatUtils;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplayListActivity extends BaseActivity {
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

    private CustomDatePicker beginTimePicker, endTimePicker;

    DeviceManage deviceManage;
    public ReplayListHandler mReplayListHandler;
    private List<DeviceInfo> mDeviceInfoList;
    private Map<String, ArrayList<String[]>> mapOrg2Name;   //组织-设备名、设备id字符串数组的list 其中字符串数组[0]表示设备名 [1]表示设备id
    private ArrayList<String> orgList;
    private String currentOrg;  //当前选择的区域，默认第一个
    private String currentDeviceName;
    public String currentDeviceId;
    public String currentStartTime;
    public String currentEndTime;
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
        initLayoutSize();
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

        rlSpDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String[]> deviceList = mapOrg2Name.get(currentOrg);
                currentDeviceName = deviceList.get(position)[0];
                currentDeviceId = deviceList.get(position)[1];
                Log.e(TAG, "currentDeviceName: "+currentDeviceName+"         currentDeviceId: "+currentDeviceId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initBeginTimerPicker();
        initEndTimerPicker();
//        //获取当前时间
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
//        Date date = new Date(System.currentTimeMillis());
//        rlTvSelectStartTime.setText(simpleDateFormat.format(date));
//        rlTvSelectEndTime.setText(simpleDateFormat.format(date));

    }

    private void findView(){
        replayListMainLayout = (ConstraintLayout) findViewById(R.id.replay_list_main_layout);
        replayListTopBar = (ConstraintLayout) findViewById(R.id.replay_list_top_bar);
        replayListTvTitle = (TextView) findViewById(R.id.replay_list_tv_title);
        replayListBtnClose = (Button) findViewById(R.id.replay_list_btn_close);
        replayListBtnClose.setOnClickListener(mOnClickListener);
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

        rlTvSelectStartTime.setOnClickListener(mOnClickListener);
        rlTvSelectEndTime.setOnClickListener(mOnClickListener);
        rlBtnFind.setOnClickListener(mOnClickListener);
    }



    @Override
    protected void onResume() {
        super.onResume();
        deviceManage.listAllDeviceRl();
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_tv_select_start_time:
                    beginTimePicker.show(rlTvSelectStartTime.getText().toString());
                    break;
                case R.id.rl_tv_select_end_time:
                    endTimePicker.show(rlTvSelectEndTime.getText().toString());
                    break;
                case R.id.rl_btn_find:
                    //Qiaoxp
                    boolean dateAvailable = false;
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat dateFormatDes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String startTimeStr = rlTvSelectStartTime.getText().toString() + ":00";
                        Date startDate = dateFormat.parse(startTimeStr);
                        currentStartTime = dateFormatDes.format(startDate);
                        String endTimeStr = rlTvSelectEndTime.getText().toString() + ":00";
                        Date endDate = dateFormat.parse(endTimeStr);
                        currentEndTime = dateFormatDes.format(endDate);
                        if(startDate.compareTo(endDate)<0){
                            dateAvailable = true;
                        }
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    if(dateAvailable){
                        deviceManage.getReplayList();
                    } else {
                        Toast.makeText(mContext, getString(R.string.rl_tst_unavailable_date), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.replay_list_btn_close:
                    finish();
                    break;

            }
        }
    };

    private void initLayoutSize() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) replayListTopBar.getLayoutParams();
        layoutParams.height = statusBarHeight + (int) getResources().getDimension(R.dimen.top_bar_origin_height);
        replayListTopBar.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) replayListMainLayout.getLayoutParams();
        layoutParams1.bottomMargin = navigationBarHeight;
//        previewMainLayout.setLayoutParams(layoutParams1);
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
                String currentDeviceId = mDeviceInfoList.get(i).getDeviceId();
                Log.e(TAG, "currentOrg: "+currentOrg +"             currentDeviceName: "+currentDeviceName);
                if(mapOrg2Name.containsKey(currentOrg)){     //已有组织
                    ArrayList<String[]> deviceNameIdStrsBuf = mapOrg2Name.get(currentOrg);
                    deviceNameIdStrsBuf.add(new String[]{currentDeviceName, currentDeviceId});
                    mapOrg2Name.put(currentOrg, deviceNameIdStrsBuf);
                } else {    //新组织
                    orgList.add(currentOrg);
                    ArrayList<String[]> deviceNameIdStrsBuf = new ArrayList<>();
                    deviceNameIdStrsBuf.add(new String[]{currentDeviceName, currentDeviceId});
                    mapOrg2Name.put(currentOrg, deviceNameIdStrsBuf);
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
            ArrayList<String[]> deviceList = mapOrg2Name.get(currentOrg);
            currentDeviceName = deviceList.get(0)[0];
            currentDeviceId = deviceList.get(0)[1];
            for(int i = 0; i<deviceList.size(); i++){
                deviceNameSpAdapter.add(deviceList.get(i)[0]);
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


    private void initBeginTimerPicker() {
        String beginTime = "2018-0-0 00:00";

        Date date = new Date();
        //时间选择器下边界-当前天数加一天
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(date);
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1);
        String endTime = DateFormatUtils.long2Str(calendarEnd.getTimeInMillis(), true);

        //tv起始时间-当前时间减一天
        Calendar tv_begin_calendar = Calendar.getInstance();
        tv_begin_calendar.setTime(date);
        tv_begin_calendar.add(Calendar.DAY_OF_MONTH, -1);
        rlTvSelectStartTime.setText(DateFormatUtils.long2Str(tv_begin_calendar.getTimeInMillis(), true));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        beginTimePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                rlTvSelectStartTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        beginTimePicker.setCancelable(true);
        // 显示时和分
        beginTimePicker.setCanShowPreciseTime(true);
        // 不允许循环滚动
        beginTimePicker.setScrollLoop(false);
        // 允许滚动动画
        beginTimePicker.setCanShowAnim(true);
    }

    private void initEndTimerPicker() {
        String beginTime = "2018-0-0 00:00";

        Date date = new Date();
        //时间选择器下边界-当前天数加一天
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(date);
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1);
        String endTime = DateFormatUtils.long2Str(calendarEnd.getTimeInMillis(), true);

        //tv起始时间-当前时间
        rlTvSelectEndTime.setText(DateFormatUtils.long2Str(System.currentTimeMillis(), true));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        endTimePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                rlTvSelectEndTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        endTimePicker.setCancelable(true);
        // 显示时和分
        endTimePicker.setCanShowPreciseTime(true);
        // 不允许循环滚动
        endTimePicker.setScrollLoop(false);
        // 允许滚动动画
        endTimePicker.setCanShowAnim(true);
    }
}
