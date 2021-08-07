package com.iray.irs_vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.adapter.DeviceListAdapter;
import com.iray.irs_vms.httpUtils.DeviceManage;
import com.iray.irs_vms.info.DeviceInfo;
import com.iray.irs_vms.widget.RecycleViewDivider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DeviceListActivity extends BaseActivity {

    private ConstraintLayout deviceListMainLayout;
    public static ConstraintLayout deviceListSearch;
    private ConstraintLayout deviceListDeviceListTopBar;
    private TextView deviceListTvTitle;
    private Button deviceListBtnClose;
    private Button deviceListBtnSearch,deviceSearch;
    public ProgressBar pbDeviceList;
    private RecyclerView rcDeviceList;
    private DeviceListAdapter adapter;
    public List<DeviceInfo> mDeviceInfoList;
    public List<DeviceInfo> mSearchDeviceInfoList=new ArrayList<DeviceInfo>() ;
    private boolean deviceListSearch_Flag=true;

    DeviceManage deviceManage;
    public DeviceListHandler mDeviceHandler;
    public static final int HANDLER_LIST_ALL_DEVICES = 1001;
    public static final int HANDLER_GET_DEVICE_ID = 1002;
    private List<CharSequence> typeList = null;
    private ArrayAdapter<CharSequence> typeAdapter = null;
    private Spinner typeSpinner= null;
    private EditText searchContent;
    private String TAG="DeviceListActivity";


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
        initLayoutSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcDeviceList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.divide_gray_color)));
        rcDeviceList.setLayoutManager(linearLayoutManager);
        //找到Spinner控件
        typeSpinner = (Spinner)super.findViewById(R.id.typeSpinner);
        typeSpinner.setPrompt("请选择类型");
        typeList = new ArrayList<CharSequence>();
        typeList.add("区域");
        typeList.add("名称");
        typeList.add("型号");
        typeAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        //添加Spinner监听事件
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Log.w(TAG,"选中的类型是："+typeList.get((int)id));
                adapter = new DeviceListAdapter(getApplicationContext(), mDeviceInfoList, mDeviceHandler);
                rcDeviceList.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void findView() {
        deviceListTvTitle = (TextView) findViewById(R.id.device_list_tv_title);
        deviceListBtnClose = (Button) findViewById(R.id.device_list_btn_close);
        deviceListBtnClose.setOnClickListener(mOnClickListener);
        deviceListBtnSearch = (Button) findViewById(R.id.device_list_btn_search);
        deviceSearch=(Button) findViewById(R.id.bt_device_search);
        rcDeviceList = (RecyclerView) findViewById(R.id.rc_device_list);
        pbDeviceList = findViewById(R.id.pb_device_list);
        deviceListDeviceListTopBar = findViewById(R.id.device_list_top_bar);
        deviceListMainLayout = findViewById(R.id.device_list_main_layout);
        deviceListSearch = findViewById(R.id.device_list_search);
        searchContent=findViewById(R.id.et_search_content);
        deviceListBtnSearch.setOnClickListener(mOnClickListener);
        deviceSearch.setOnClickListener(mOnClickListener);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.device_list_btn_search:
                    if(deviceListSearch_Flag) {
                        deviceListSearch.setVisibility(View.VISIBLE);
                        deviceListSearch_Flag=false;
                        deviceListTvTitle.setText("查找");
                        adapter = new DeviceListAdapter(getApplicationContext(), mDeviceInfoList, mDeviceHandler);
                        rcDeviceList.setAdapter(adapter);
                    }else {
                        deviceListSearch_Flag=true;
                        deviceListSearch.setVisibility(View.GONE);
                        deviceListTvTitle.setText("设备列表");
                        adapter = new DeviceListAdapter(getApplicationContext(), mDeviceInfoList, mDeviceHandler);
                        rcDeviceList.setAdapter(adapter);
                    }
                    break;
                case R.id.device_list_btn_close:
                    DeviceListActivity.this.finish();
                    break;
                case R.id.bt_device_search:
                    //开发搜索
                    String selectedItem = (String)typeSpinner.getSelectedItem();
                    String content = searchContent.getText().toString();
                    DeviceListSearch(selectedItem,content);
                    break;

                default:
                    break;
            }
        }
    };

    private void DeviceListSearch(String selectedItem, String content) {
        Log.w(TAG,"selectedItem:"+selectedItem);
        Log.w(TAG,"content:"+content);
        Log.w(TAG,"mDeviceInfoList.size():"+mDeviceInfoList.size());
        mSearchDeviceInfoList.clear();
       for(int i=0;i<mDeviceInfoList.size();i++) {
           DeviceInfo deviceInfo = mDeviceInfoList.get(i);
           if(selectedItem.equals("区域")){
               if(deviceInfo.getDeviceOrg().contains(content)){
                   mSearchDeviceInfoList.add(deviceInfo);
               }
           }
           if(selectedItem.equals("名称")){
               if(deviceInfo.getDeviceName().contains(content)){
                   mSearchDeviceInfoList.add(deviceInfo);
               }
           }
           if(selectedItem.equals("型号")){
               int targetContent = 0;
               if(content.equals("IPC")){
                   targetContent=1;
               }
               if(content.equals("NVR")){
                   targetContent=2;
               }
               if(content.equals("门禁")){
                   targetContent=3;
               }
               if(deviceInfo.getDeviceType()==targetContent){
                   mSearchDeviceInfoList.add(deviceInfo);
               }
           }

           if (mSearchDeviceInfoList.size() == 0) {
               Toast.makeText(this, getString(R.string.search_device_list_empty), Toast.LENGTH_SHORT).show();
           } else {
               adapter = new DeviceListAdapter(this, mSearchDeviceInfoList, mDeviceHandler);
               rcDeviceList.setAdapter(adapter);
           }

       }

    }

    /**
     * 初始化视图尺寸，适配虚拟键和水滴屏
     */
    private void initLayoutSize(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) deviceListDeviceListTopBar.getLayoutParams();
        layoutParams.height = statusBarHeight+ (int)getResources().getDimension(R.dimen.top_bar_origin_height);
        deviceListDeviceListTopBar.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) deviceListMainLayout.getLayoutParams();
        layoutParams1.bottomMargin = navigationBarHeight;
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceManage.listAllDeviceDl();
    }

    public void sendDeviceHandler(int msgWhat) {
        mDeviceHandler.sendEmptyMessage(msgWhat);
    }


    public void listDevicesInfo() {
        mDeviceInfoList = deviceManage.mDeviceList;
        if (mDeviceInfoList.size() == 0) {
            Toast.makeText(this, getString(R.string.tst_device_list_empty), Toast.LENGTH_SHORT).show();
        } else {
            adapter = new DeviceListAdapter(this, mDeviceInfoList, mDeviceHandler);
            rcDeviceList.setAdapter(adapter);
        }
    }

    public static class DeviceListHandler extends Handler {
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
                case HANDLER_GET_DEVICE_ID:
                    if(!msg.getData().getString("id").equals("")){
                        Intent intent = new Intent(activity, PreviewActivity.class);
                        intent.putExtra("id", msg.getData().getString("id"));
                        intent.putExtra("org", msg.getData().getString("org"));
                        intent.putExtra("name", msg.getData().getString("name"));
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.tst_get_device_id_error), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
