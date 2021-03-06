package com.iray.irs_vms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.iray.irs_vms.R;
import com.shehuan.niv.NiceImageView;

import java.io.File;

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
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private Bitmap mBitmap;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBitmap!=null) {
            ivUserAvatar.setImageBitmap(mBitmap);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mBitmap!=null) {
            ivUserAvatar.setImageBitmap(mBitmap);
        }
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
        userBtnHome.setOnClickListener(mOnClickListener);
        userBtnUser.setOnClickListener(mOnClickListener);
        userBtnAlarm.setOnClickListener(mOnClickListener);
        userMenuFeedbackText.setOnClickListener(mOnClickListener);
        ivUserAvatar.setOnClickListener(mOnClickListener);
        userBtnClose=(Button)findViewById(R.id.user_btn_close);
        userBtnClose.setOnClickListener(mOnClickListener);
        userTvHome.setOnClickListener(mOnClickListener);
        SharedPreferences pre = getSharedPreferences("data",MODE_PRIVATE);
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

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_btn_user:
                    Intent intent0 = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(intent0);
                    break;
                case R.id.user_btn_alarm:
                    Intent intent = new Intent(UserActivity.this, UrgentAlarmUploadActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_btn_home:
                   // Toast.makeText(UserActivity.this, "frnfshs", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.iv_user_avatar:
                    //??????????????????
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                    builder.setTitle("????????????");
                    String[] items = { "??????????????????", "??????" };
                    builder.setNegativeButton("??????", null);
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // ??????????????????
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //???startActivityForResult????????????????????????onActivityResult()????????????????????????????????????
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // ??????
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image.jpg"));
                                    // ?????????????????????????????????SD????????????
                                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                    break;
                            }
                        }
                    });
                    builder.show();

                    break;
                case R.id.user_menu_feedback_text:
                    //????????????
                    Intent intent3 = new Intent(UserActivity.this, FeedBackActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.user_btn_close:
                    finish();
                    Intent intent4 = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent4);
                default:
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // ???????????????????????????
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // ???????????????????????????
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // ??????????????????????????????????????????????????????
                    }
                    break;
            }
        }
    }
    /**
     * ????????????????????????
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP??????action???????????????????????????
        intent.setDataAndType(uri, "image/*");
        // ????????????
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ?????????????????????
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * ?????????????????????????????????
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            ivUserAvatar.setImageBitmap(mBitmap);//????????????
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????...
            SharedPreferences pref = UserActivity.this.getSharedPreferences("data",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Content",mBitmap.toString());
            editor.commit();
            Toast.makeText(UserActivity.this, "??????????????????" , Toast.LENGTH_LONG).show();
        }
    }
}