package com.iray.irs_vms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iray.irs_vms.R;

import java.util.List;

public class DataDeviceActivity extends BaseActivity {
    private final String TAG = "DataAccessControlActivity";
    private ImageView accessControl;
    private ImageView tempScreen;
    private ImageView abnormalAlarm;
    private ImageView equipStatistics;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //写一个List集合，把每个页面，也就是Fragment,存进去
    private List<Fragment> list;
    private Button btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_data);
        init();
    }

    private void init() {

        initView();

    }

    private void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {


            }
        }
    };


}



