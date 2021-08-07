package com.iray.irs_vms.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.adapter.KeyDeviceListAdapter;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.httpUtils.KeyDeviceManage;
import com.iray.irs_vms.httpUtils.UserUtils;
import com.iray.irs_vms.info.KeyDeviceInfo;
import com.iray.irs_vms.service.AlarmService;
import com.iray.irs_vms.utils.FileUtils;
import com.iray.irs_vms.widget.RecycleViewDivider;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private ConstraintLayout mainLayout;
    private ConstraintLayout topMenuLayout;
    private EditText etSearchBox;
    private Button btnSearch;
    private Button btnPlusMenu;
    private TextView tvPosition;
    private ConstraintLayout topBannerLayout;
    private ConstraintLayout mainButtonLayout;
    private Button btnAccessControl;
    private TextView tvAccessControl;
    private Button btnVideoSurveillance;
    private TextView tvVideoSurveillance;
    private Button btnInspection;
    private TextView tvInspection;
    private Button btnTempScreening;
    private TextView tvTempScreening;
    private Button btnParkTrack;
    private TextView tvParkTrack;
    private Button btnStatistics;
    private TextView tvStatistics;
    private Button btnAbnormalAlarm;
    private TextView tvAbnormalAlarm;
    private Button btnMore;
    private TextView tvMore;
    private ConstraintLayout mainNewsLayout;
    private ConstraintLayout favoriteCamsLayout;
    private TextView tvFavoriteCamsTitle;
    private Button btnFavoriteCamsManage;
    private ConstraintLayout bottomMenuLayout;
    private Button btnHome;
    private TextView tvHome;
    private Button btnAlarm;
    private TextView tvAlarm;
    private Button btnUser;
    private Button btnReplay;
    private TextView tvUser;
    public ProgressBar pbDeviceList;


    private SharedPreferences userSp;
    public static boolean tagLogin = false;    //登录标记
    //Handler
    public MainHandler mHandler;
    public final static int LOGIN_RESULT = 201;
    public static final int HANDLER_LIST_ALL_DEVICESB = 1004;
    private RecyclerView rcDeviceList;
    private List<KeyDeviceInfo> mDeviceInfoList;
    KeyDeviceManage keyDeviceManage;
    private KeyDeviceListAdapter adapter;
    public MainActivity.MainHandler mDeviceHandler;

    MZBannerView mMZBanner;
    List<Integer> list;
    public static final int[] RES = new int[]{R.mipmap.banner_image1, R.mipmap.banner_image2, R.mipmap.banner_image3, R.mipmap.banner_image4, R.mipmap.banner_image5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBanner();
        mHandler = new MainHandler(this);
        initLogin();
        initDirs();
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMZBanner.start();
        keyDeviceManage.listAllDeviceDlB();


    }

    public void sendDeviceHandler(int msgWhat) {
        mDeviceHandler.sendEmptyMessage(msgWhat);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMZBanner.pause();
    }

    private void initView() {
        mainLayout = findViewById(R.id.main_layout);
        topMenuLayout = (ConstraintLayout) findViewById(R.id.top_menu_layout);
        etSearchBox = (EditText) findViewById(R.id.et_search_box);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnPlusMenu = (Button) findViewById(R.id.btn_plus_menu);
        tvPosition = (TextView) findViewById(R.id.tv_position);
        topBannerLayout = (ConstraintLayout) findViewById(R.id.top_banner_layout);
        mainButtonLayout = (ConstraintLayout) findViewById(R.id.main_button_layout);
        btnAccessControl = (Button) findViewById(R.id.btn_access_control);
        tvAccessControl = (TextView) findViewById(R.id.tv_access_control);
        btnVideoSurveillance = (Button) findViewById(R.id.btn_video_surveillance);
        tvVideoSurveillance = (TextView) findViewById(R.id.tv_video_surveillance);
        btnInspection = (Button) findViewById(R.id.btn_inspection);
        tvInspection = (TextView) findViewById(R.id.tv_inspection);
        btnTempScreening = (Button) findViewById(R.id.btn_temp_screening);
        tvTempScreening = (TextView) findViewById(R.id.tv_temp_screening);
        btnParkTrack = (Button) findViewById(R.id.btn_park_track);
        tvParkTrack = (TextView) findViewById(R.id.tv_park_track);
        btnStatistics = (Button) findViewById(R.id.btn_statistics);
        tvStatistics = (TextView) findViewById(R.id.tv_statistics);
        btnAbnormalAlarm = (Button) findViewById(R.id.btn_abnormal_alarm);
        tvAbnormalAlarm = (TextView) findViewById(R.id.tv_abnormal_alarm);
        btnMore = (Button) findViewById(R.id.btn_more);
        tvMore = (TextView) findViewById(R.id.tv_more);
        mainNewsLayout = (ConstraintLayout) findViewById(R.id.main_news_layout);
        favoriteCamsLayout = (ConstraintLayout) findViewById(R.id.favorite_cams_layout);
        tvFavoriteCamsTitle = (TextView) findViewById(R.id.tv_favorite_cams_title);
        btnFavoriteCamsManage = (Button) findViewById(R.id.btn_favorite_cams_manage);
        bottomMenuLayout = (ConstraintLayout) findViewById(R.id.bottom_menu_layout);
        btnHome = (Button) findViewById(R.id.btn_home);
        tvHome = (TextView) findViewById(R.id.tv_home);
        btnAlarm = (Button) findViewById(R.id.btn_alarm);
        tvAlarm = (TextView) findViewById(R.id.tv_alarm);
        btnUser = (Button) findViewById(R.id.btn_user);
        tvUser = (TextView) findViewById(R.id.tv_user);
        rcDeviceList = (RecyclerView) findViewById(R.id.rc_device_list);
        btnReplay=(Button) findViewById(R.id.btn_video_replay);
        btnReplay.setOnClickListener(mOnClickListener);
        btnParkTrack.setOnClickListener(mOnClickListener);

        KeyMonitorActivity keyMonitorActivity=new KeyMonitorActivity();
        mDeviceHandler = new MainActivity.MainHandler(this);

        btnMore.setOnClickListener(mOnClickListener);
        btnVideoSurveillance.setOnClickListener(mOnClickListener);
        btnUser.setOnClickListener(mOnClickListener);
        btnAbnormalAlarm.setOnClickListener(mOnClickListener);
        btnFavoriteCamsManage.setOnClickListener(mOnClickListener);
        btnAccessControl.setOnClickListener(mOnClickListener);
        btnTempScreening.setOnClickListener(mOnClickListener);
        btnStatistics.setOnClickListener(mOnClickListener);
        btnInspection.setOnClickListener(mOnClickListener);
        btnAlarm.setOnClickListener(mOnClickListener);
        etSearchBox.setOnClickListener(mOnClickListener);
        btnPlusMenu.setOnClickListener(mOnClickListener);

        initLayoutSize();
        //智慧园区代码
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcDeviceList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.divide_gray_color)));
        rcDeviceList.setLayoutManager(linearLayoutManager);
        listKeyDevicesInfo();
    }

    public void listKeyDevicesInfo() {
        keyDeviceManage = KeyDeviceManage.getInstance();
        keyDeviceManage.setKeyDeviceListActivityWeakReference(this);  // Qiaoxp
        mDeviceInfoList = keyDeviceManage.mDeviceList;
        if (mDeviceInfoList.size() == 0) {
            Log.i("DeviceUtils","MainActivity 未找到设备");
            Toast.makeText(this, getString(R.string.tst_device_list_empty), Toast.LENGTH_SHORT).show();
        } else {
            adapter = new KeyDeviceListAdapter(this, mDeviceInfoList, mDeviceHandler); // Qiaoxp 智慧园区参考
            rcDeviceList.setAdapter(adapter);

        }
    }

    /**
     * 启动时自动登录
     */
    private void initLogin(){
        userSp = getSharedPreferences(getString(R.string.sp_user), MODE_PRIVATE);
        final String loginName = userSp.getString(getString(R.string.user_sp_name), "");
        final String loginPassword = userSp.getString(getString(R.string.user_sp_password), "");
        if(loginName.equals("")||loginPassword.equals("")){

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserUtils.loginForToken(mHandler, loginName, loginPassword, "password", "webApp", "webApp");
                    while(Common.ACCESS_TOKEN==null) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String userString = UserUtils.getUserId(Common.ACCESS_TOKEN);
                    if (!userString.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(userString);
                           JSONObject userIds = jsonObject.getJSONObject("datas");
                            Common.USER_ID=userIds.getString("id");
                            Log.w("MainActivity","User_ID:"+Common.USER_ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }).start();
        }
    }


    private void initBanner() {
        mMZBanner = (MZBannerView) findViewById(R.id.banner);
        mMZBanner.setIndicatorVisible(false);
        list = new ArrayList<>();
        for (int i = 0; i < RES.length; i++) {
            list.add(RES[i]);
        }
        // 设置数据
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }

    /**
     * 初始化视图尺寸，适配虚拟键和水滴屏
     */
    private void initLayoutSize(){
        Log.e("status", "status: "+statusBarHeight+"      nav: "+navigationBarHeight+"   ori: "+ getResources().getDimension(R.dimen.top_bar_origin_height));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) topMenuLayout.getLayoutParams();
        layoutParams.height = statusBarHeight+(int)getResources().getDimension(R.dimen.top_bar_origin_height);
        topMenuLayout.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) mainLayout.getLayoutParams();
        layoutParams1.bottomMargin = navigationBarHeight;
        mainLayout.setLayoutParams(layoutParams1);
    }


    private void initDirs(){
        String mainDirStr = FileUtils.FILE_DIR_PATH;
        File mainDir = new File(mainDirStr);
        if(!mainDir.exists()){
            mainDir.mkdir();
        }
        String snapDirStr = mainDirStr + File.separator + FileUtils.SNAP_SHOT_DIR;
        File snapDir = new File(snapDirStr);
        if(!snapDir.exists()){
            snapDir.mkdir();
        }
        String videoDirStr = mainDirStr + File.separator + FileUtils.VIDEO_DIR;
        File videoDir = new File(videoDirStr);
        if(!videoDir.exists()){
            videoDir.mkdir();
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //安防门禁
                case R.id.btn_access_control:
                    Intent intent0 = new Intent(MainActivity.this, AccessControlActivity.class);
                    startActivity(intent0);
 //                   Intent intent0 = new Intent(MainActivity.this, TestNotifyActivity.class);
  //                  startActivity(intent0);
                    break;
                    //系统通知
                case R.id.btn_more:
                    if(tagLogin) {
                        Intent intent1 = new Intent(MainActivity.this, NoticesActivity.class);
                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                    //视频监控
                case R.id.btn_video_surveillance:
                    if(tagLogin) {
                        Intent intent1 = new Intent(MainActivity.this, DeviceListActivity.class);
                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                    //首页
                case R.id.btn_home:
                    if(tagLogin) {
                        Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent2);

                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    //紧急报警
                case R.id.btn_alarm:
                    if(tagLogin) {
                        Intent intent2 = new Intent(MainActivity.this, UrgentAlarmUploadActivity.class);
                        startActivity(intent2);

                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }

                    break;
                    //我的
                case R.id.btn_user:
                    if(tagLogin) {
                        Intent intent2 = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent2);
                        finish();
                    }else {
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent2);
                    }
                    break;
                    //异常报警
                case R.id.btn_abnormal_alarm:
                    if(tagLogin) {
                        Intent intent4 = new Intent(MainActivity.this, AbnormalAlarmActivity.class);
                        startActivity(intent4);
                     }else {
                       Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                       startActivity(intent1);
                      }

                    break;
                    //重点监控设备
                case R.id.btn_favorite_cams_manage:
                    if(tagLogin) {
                        Intent intent5 = new Intent(MainActivity.this, KeyMonitorActivity.class);
                        startActivity(intent5);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                    //体温筛查
                case R.id.btn_temp_screening:
                    if(tagLogin) {
                        Intent intent6 = new Intent(MainActivity.this, TempCheckActivity.class);
                        startActivity(intent6);
                        Log.i("DeviceUtils","btn_temp_screening is clicked.");
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }

                    break;

                    //数据统计
                case R.id.btn_statistics:
                    if(tagLogin) {
                        Intent intent7 = new Intent(MainActivity.this, DataStatisActivity.class);
                        startActivity(intent7);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                    //巡检任务
                case R.id.btn_inspection:
                    if(tagLogin) {
                        Intent intent8 = new Intent(MainActivity.this, InspectionActivity.class);
                        startActivity(intent8);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                    //首页搜索框
                case R.id.et_search_box:
                    if(tagLogin) {
                        Intent intent9 = new Intent(MainActivity.this, DeviceListActivity.class);
                        startActivity(intent9);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }

                    break;
                    //隐患上报
                case R.id.btn_plus_menu:
                    if(tagLogin) {
                        Intent intent10 = new Intent(MainActivity.this, AlarmUploadActivity.class);
                        startActivity(intent10);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }

                    break;
                    //视频回放
                case R.id.btn_video_replay:
                    if(tagLogin) {
                        Intent intent11 = new Intent(MainActivity.this, ReplayListActivity.class);
                        startActivity(intent11);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                    //园区轨迹
                case R.id.btn_park_track:
                    if(tagLogin) {
                        Intent intent12 = new Intent(MainActivity.this, ParkTrackActivity.class);
                        startActivity(intent12);
                    }else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }

    public static class MainHandler extends Handler{
        private MainActivity activity;

        private MainHandler(MainActivity activity){
            WeakReference<MainActivity> reference = new WeakReference<MainActivity>(activity);
            this.activity = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case HANDLER_LIST_ALL_DEVICESB:
                    Log.i("DeviceUtils","mainActivity receieve DeviceHandlerb......");
                    activity.listKeyDevicesInfo();
                    break;
                case LOGIN_RESULT:
                    if (msg.obj.toString().equals("")) {  //登录失败
                        activity.tagLogin = false;
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            JSONObject datasJ = jsonObject.getJSONObject("datas");
                            String accessToken = datasJ.getString("access_token");
                            Common.ACCESS_TOKEN = "Bearer"+accessToken;
                            activity.tagLogin = true;
                            Log.e("login", accessToken);
                        } catch (JSONException e){
                            e.printStackTrace();
                            activity.tagLogin = false;
                        }

                    }
//                        Log.e("login", msg.obj.toString());
                    break;
            }
        }
    }


}
