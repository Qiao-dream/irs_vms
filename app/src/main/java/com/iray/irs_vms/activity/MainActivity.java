package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.httpUtils.UserUtils;
import com.iray.irs_vms.utils.DisplayUtil;
import com.iray.irs_vms.utils.FileUtils;
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
    private TextView tvUser;

    private SharedPreferences userSp;
    public static boolean tagLogin = false;    //登录标记
    //Handler
    MainHandler mHandler;
    public final static int LOGIN_RESULT = 201;


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMZBanner.start();
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

        btnMore.setOnClickListener(mOnClickListener);
        btnVideoSurveillance.setOnClickListener(mOnClickListener);
        btnUser.setOnClickListener(mOnClickListener);
        btnAbnormalAlarm.setOnClickListener(mOnClickListener);

        initLayoutSize();

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
                case R.id.btn_more:
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_video_surveillance:
                    if(tagLogin) {
                        Intent intent1 = new Intent(MainActivity.this, DeviceListActivity.class);
                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                    break;
                case R.id.btn_user:
                    if(tagLogin) {
                        Intent intent2 = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent2);
                    }else {
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent2);
                    }
                    break;
                case R.id.btn_abnormal_alarm:
                    Intent intent4 = new Intent(MainActivity.this, ReplayListActivity.class);
                    startActivity(intent4);
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



    private static class MainHandler extends Handler{
        private MainActivity activity;

        private MainHandler(MainActivity activity){
            WeakReference<MainActivity> reference = new WeakReference<MainActivity>(activity);
            this.activity = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
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
