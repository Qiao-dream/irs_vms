package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.httpUtils.UserUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginName;
    private Button btnMoreName;
    private EditText etLoginPassword;
    private Button btnHidePassword;
    private CheckBox cbSavePassword;
    private Button btnForgetPassword;
    private TextView tvLoginFailedInfo;
    private Button btnLogin;
    private String accessToken;
    Handler mHandler;
    public final static int LOGIN_RESULT = 201;

    private SharedPreferences userSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        initView();
        setmHandler();

    }


    private void initView() {
        findView();
        userSp = getSharedPreferences(getString(R.string.sp_user), MODE_PRIVATE);
        boolean savePasswordTag = userSp.getBoolean(getString(R.string.user_sp_save_password), false);
        cbSavePassword.setChecked(savePasswordTag);
        String loginName = userSp.getString(getString(R.string.user_sp_name), "");
        if (!loginName.equals("")) {
            etLoginName.setText(loginName);
        }
        String loginPassword = userSp.getString(getString(R.string.user_sp_password), "");
        if (savePasswordTag && !loginPassword.equals("")) {
            etLoginPassword.setText(loginPassword);
        }
        etLoginPassword.setInputType(0x81);
        btnHidePassword.setBackground(getDrawable(R.drawable.btn_hide_password));
    }


    private void findView() {
        etLoginName = (EditText) findViewById(R.id.et_login_name);
        btnMoreName = (Button) findViewById(R.id.btn_more_name);
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        btnHidePassword = (Button) findViewById(R.id.btn_hide_password);
        cbSavePassword = (CheckBox) findViewById(R.id.cb_save_password);
        btnForgetPassword = (Button) findViewById(R.id.btn_forget_password);
        tvLoginFailedInfo = (TextView) findViewById(R.id.tv_login_failed_info);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnForgetPassword.setOnClickListener(mOnClickListener);
        btnHidePassword.setOnClickListener(mOnClickListener);
        btnLogin.setOnClickListener(mOnClickListener);
        btnMoreName.setOnClickListener(mOnClickListener);
        cbSavePassword.setOnClickListener(mOnClickListener);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    final String userName = etLoginName.getText().toString();
                    final String password = etLoginPassword.getText().toString();
                    if (userName.equals("") || password.equals("")) {
                        tvLoginFailedInfo.setText(getString(R.string.tv_login_info_empty));
                        tvLoginFailedInfo.setVisibility(View.VISIBLE);
                    } else {
                        tvLoginFailedInfo.setVisibility(View.INVISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UserUtils.loginForToken(mHandler, userName, password, "password", "webApp", "webApp");
                            }
                        }).start();
                    }
                    break;
                case R.id.btn_hide_password:
                    if (etLoginPassword.getInputType() == 0x81) {
                        etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnHidePassword.setBackground(getDrawable(R.drawable.btn_show_password));
                    } else {
                        etLoginPassword.setInputType(0x81);
                        btnHidePassword.setBackground(getDrawable(R.drawable.btn_hide_password));
                    }
                    break;
                case R.id.cb_save_password:
                    SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.sp_user), MODE_PRIVATE).edit();
                    editor.putBoolean(getString(R.string.user_sp_save_password), cbSavePassword.isChecked());
                    if(!cbSavePassword.isChecked()){
                        editor.putString(getString(R.string.user_sp_password), "");
                    }
                    editor.apply();
                    break;
                default:
                    break;
            }
        }
    };

    private void setmHandler() {
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case LOGIN_RESULT:
                        if (msg.obj.toString().equals("")) {  //登录失败
                            tvLoginFailedInfo.setText(getString(R.string.tv_login_service_failed));
                            tvLoginFailedInfo.setVisibility(View.VISIBLE);
                        } else {
                            tvLoginFailedInfo.setVisibility(View.INVISIBLE);
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.sp_user), MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.user_sp_name), etLoginName.getText().toString());
                            editor.putString(getString(R.string.user_sp_password), etLoginPassword.getText().toString());
                            editor.apply();
                            try {
                                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                JSONObject datasJ = jsonObject.getJSONObject("datas");
                                accessToken = datasJ.getString("access_token");
                                Common.ACCESS_TOKEN = "Bearer"+accessToken;
                                Log.e("login", accessToken);
                            } catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
//                        Log.e("login", msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
