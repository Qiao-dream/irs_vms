package com.iray.irs_vms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.utils.DisplayUtil;
import com.shehuan.niv.NiceImageView;

public class UserActivity extends BaseActivity {

    private ConstraintLayout userMainLayout;
    private ConstraintLayout userTopBar;
    private ConstraintLayout userInfoLayout;
    private Button userBtnClose;
    private Button userBtnSetting;
    private NiceImageView ivUserAvatar;
    private TextView txUserName;
    private TextView txUserInfoPosition;
    private TextView txUserInfoJobNumber;
    private TextView userMenuSettingIcon;
    private TextView userMenuSettingText;
    private TextView userMenuDividingLine1;
    private TextView userMenuAlarmPushingIcon;
    private TextView userMenuAlarmPushingText;
    private TextView userMenuDividingLine2;
    private TextView userMenuFeedbackIcon;
    private TextView userMenuFeedbackText;
    private Button userBtnHome;
    private TextView userTvHome;
    private Button userBtnAlarm;
    private TextView userTvAlarm;
    private Button userBtnUser;
    private TextView userTvUser;
    private TextView tvUserMenuCsText;
    private TextView tvUserMenuCsNumber;
    private TextView tvUserMenuCsIcon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    private void init(){
        initView();
    }

    private void initView(){
        findView();
        initLayoutSize();
    }

    private void findView(){
        userMainLayout = (ConstraintLayout) findViewById(R.id.user_main_layout);
        userTopBar = (ConstraintLayout) findViewById(R.id.user_top_bar);
        userInfoLayout = findViewById(R.id.user_info_layout);
        userBtnClose = (Button) findViewById(R.id.user_btn_close);
        userBtnSetting = (Button) findViewById(R.id.user_btn_setting);
        ivUserAvatar = (NiceImageView) findViewById(R.id.iv_user_avatar);
        txUserName = (TextView) findViewById(R.id.tx_user_name);
        txUserInfoPosition = (TextView) findViewById(R.id.tx_user_info_position);
        txUserInfoJobNumber = (TextView) findViewById(R.id.tx_user_info_job_number);
        userMenuSettingIcon = (TextView) findViewById(R.id.user_menu_setting_icon);
        userMenuSettingText = (TextView) findViewById(R.id.user_menu_setting_text);
        userMenuDividingLine1 = (TextView) findViewById(R.id.user_menu_dividing_line1);
        userMenuAlarmPushingIcon = (TextView) findViewById(R.id.user_menu_alarm_pushing_icon);
        userMenuAlarmPushingText = (TextView) findViewById(R.id.user_menu_alarm_pushing_text);
        userMenuDividingLine2 = (TextView) findViewById(R.id.user_menu_dividing_line2);
        userMenuFeedbackIcon = (TextView) findViewById(R.id.user_menu_feedback_icon);
        userMenuFeedbackText = (TextView) findViewById(R.id.user_menu_feedback_text);
        userBtnHome = (Button) findViewById(R.id.user_btn_home);
        userTvHome = (TextView) findViewById(R.id.user_tv_home);
        userBtnAlarm = (Button) findViewById(R.id.user_btn_alarm);
        userTvAlarm = (TextView) findViewById(R.id.user_tv_alarm);
        userBtnUser = (Button) findViewById(R.id.user_btn_user);
        userTvUser = (TextView) findViewById(R.id.user_tv_user);
        tvUserMenuCsText = (TextView) findViewById(R.id.tv_user_menu_cs_text);
        tvUserMenuCsNumber = (TextView) findViewById(R.id.tv_user_menu_cs_number);
        tvUserMenuCsIcon = (TextView) findViewById(R.id.tv_user_menu_cs_icon);
    }

    private void initLayoutSize(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) userTopBar.getLayoutParams();
        layoutParams.height = statusBarHeight+ (int)getResources().getDimension(R.dimen.user_top_bar_origin_height);
        userTopBar.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) userMainLayout.getLayoutParams();
        layoutParams1.bottomMargin = navigationBarHeight;
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
        layoutParams2.topMargin = statusBarHeight+(int)getResources().getDimension(R.dimen.user_info_layout_margin_top);
    }
}
