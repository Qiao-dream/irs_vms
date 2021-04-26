package com.iray.irs_vms.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iray.irs_vms.utils.DisplayUtil;

public class BaseActivity extends AppCompatActivity {
    public int statusBarHeight;
    public int navigationBarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarHeight = DisplayUtil.getStatusHeight(this);
        navigationBarHeight = DisplayUtil.getNavigationBarHeight(this);
    }

}
