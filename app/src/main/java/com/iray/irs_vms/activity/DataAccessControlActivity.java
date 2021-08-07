package com.iray.irs_vms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iray.irs_vms.R;
import com.iray.irs_vms.fragment.TodayAccessFragment;

import java.util.ArrayList;
import java.util.List;

public class DataAccessControlActivity extends BaseActivity {
    private final String TAG = "DataAccessControlActivity";
    private ImageView accessControl;
    private ImageView tempScreen;
    private ImageView abnormalAlarm;
    private ImageView equipStatistics;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //写一个List集合，把每个页面，也就是Fragment,存进去
    private List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"今天", "近7天", "近30天", "自定义"};
    private Button btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_data);
        //实例化
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //页面，数据源，里面是创建的三个页面（Fragment）
        list = new ArrayList<>();
        list.add(new TodayAccessFragment());
        list.add(new TodayAccessFragment());

        //ViewPager的适配器，获得Fragment管理器
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //将TabLayout和ViewPager绑定在一起，一个动另一个也会跟着动
        tabLayout.setupWithViewPager(viewPager);


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

    //创建Fragment的适配器
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        //获得每个页面的下标
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        //获得List的大小
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}



