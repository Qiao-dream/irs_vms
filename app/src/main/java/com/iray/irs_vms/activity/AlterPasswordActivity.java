package com.iray.irs_vms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iray.irs_vms.R;
import com.shehuan.niv.NiceImageView;

public class AlterPasswordActivity extends BaseActivity {

    private ConstraintLayout alterPasswordMainLayout;
    private ConstraintLayout alterPasswordTopBar;
    private Button alterPasswordBtnClose;
    private TextView alterPasswordTvTitle;
    private TextView tvApOldPassword;
    private EditText etApOldPassword;
    private Button btnApHidePasswordOld;
    private TextView tvApNewPassword1;
    private EditText etApNewPassword1;
    private Button btnApHidePasswordNew1;
    private TextView tvApNewPassword2;
    private EditText etApNewPassword2;
    private Button btnApHidePasswordNew2;
    private Button btnAlterPasswordConfirm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_password);
        init();
    }

    private void init(){
        initView();
    }

    private void initView(){
        findView();
        initLayoutSize();

        etApOldPassword.setInputType(0x81);
        etApNewPassword1.setInputType(0x81);
        etApNewPassword2.setInputType(0x81);

    }

    private void findView(){
        alterPasswordMainLayout = (ConstraintLayout) findViewById(R.id.alter_password_main_layout);
        alterPasswordTopBar = (ConstraintLayout) findViewById(R.id.alter_password_top_bar);
        alterPasswordBtnClose = (Button) findViewById(R.id.alter_password_btn_close);
        alterPasswordTvTitle = (TextView) findViewById(R.id.alter_password_tv_title);
        tvApOldPassword = (TextView) findViewById(R.id.tv_ap_old_password);
        etApOldPassword = (EditText) findViewById(R.id.et_ap_old_password);
        btnApHidePasswordOld = (Button) findViewById(R.id.btn_ap_hide_password_old);
        tvApNewPassword1 = (TextView) findViewById(R.id.tv_ap_new_password1);
        etApNewPassword1 = (EditText) findViewById(R.id.et_ap_new_password1);
        btnApHidePasswordNew1 = (Button) findViewById(R.id.btn_ap_hide_password_new1);
        tvApNewPassword2 = (TextView) findViewById(R.id.tv_ap_new_password2);
        etApNewPassword2 = (EditText) findViewById(R.id.et_ap_new_password2);
        btnApHidePasswordNew2 = (Button) findViewById(R.id.btn_ap_hide_password_new2);
        btnAlterPasswordConfirm = (Button) findViewById(R.id.btn_alter_password_confirm);

        btnApHidePasswordOld.setOnClickListener(mOnClickListener);
        btnApHidePasswordNew1.setOnClickListener(mOnClickListener);
        btnApHidePasswordNew2.setOnClickListener(mOnClickListener);

    }

    private void initLayoutSize(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) alterPasswordTopBar.getLayoutParams();
        layoutParams.height = statusBarHeight+ (int)getResources().getDimension(R.dimen.top_bar_origin_height);
        alterPasswordTopBar.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) alterPasswordMainLayout.getLayoutParams();
        layoutParams1.bottomMargin = navigationBarHeight;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_ap_hide_password_old:
                    if (etApOldPassword.getInputType() == 0x81) {
                        etApOldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnApHidePasswordOld.setBackground(getDrawable(R.drawable.btn_show_password));
                    } else {
                        etApOldPassword.setInputType(0x81);
                        btnApHidePasswordOld.setBackground(getDrawable(R.drawable.btn_hide_password));
                    }
                    break;
                case R.id.btn_ap_hide_password_new1:
                    if (etApNewPassword1.getInputType() == 0x81) {
                        etApNewPassword1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnApHidePasswordNew1.setBackground(getDrawable(R.drawable.btn_show_password));
                    } else {
                        etApNewPassword1.setInputType(0x81);
                        btnApHidePasswordNew1.setBackground(getDrawable(R.drawable.btn_hide_password));
                    }
                    break;
                case R.id.btn_ap_hide_password_new2:
                    if (etApNewPassword2.getInputType() == 0x81) {
                        etApNewPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnApHidePasswordNew2.setBackground(getDrawable(R.drawable.btn_show_password));
                    } else {
                        etApNewPassword2.setInputType(0x81);
                        btnApHidePasswordNew2.setBackground(getDrawable(R.drawable.btn_hide_password));
                    }
                    break;
            }
        }
    };
}
