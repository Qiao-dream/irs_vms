package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.iray.irs_vms.httpUtils.UserUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginName;
    private Button btnMoreName;
    private EditText etLoginPassword;
    private Button btnHidePassword;
    private CheckBox cbSavePassword;
    private Button btnForgetPassword;
    private TextView tvLoginFailedInfo;
    private Button btnLogin;

    Handler mHandler;
    public final static int LOGIN_RESULT = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){
        initView();
        setmHandler();
    }


    private void initView() {
        findView();

    }


    private void findView(){
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
                    if(etLoginPassword.getInputType()==InputType.TYPE_TEXT_VARIATION_PASSWORD){
                        etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
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
                        Log.e("login", msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
