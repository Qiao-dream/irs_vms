package com.iray.irs_vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.httpUtils.NoticeUtils;

/**
 * 系统通知
 */
public class NoticeDetailActivity extends BaseActivity {
 //   private Button noticeCheckBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        Intent intent=getIntent();
        final String id = intent.getStringExtra("id");
        init();
        new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                String noticeDeatil = NoticeUtils.informationSearchById(Common.ACCESS_TOKEN, id);

            }
        }.start();
    }


    private void init() {
        initView();

    }
    private void initView() {
        findView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


    }

    private void findView() {
//        noticeCheckBtnClose=findViewById(R.id.notice_btn_close);
//        noticeCheckBtnClose.setOnClickListener(mOnClickListener);

    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //安防门禁
                case R.id.abnormal_persons:
                    Intent intent0 = new Intent(NoticeDetailActivity.this, AccessControlActivity.class);
                    startActivity(intent0);
                    break;
                case R.id.notice_btn_close:
                    NoticeDetailActivity.this.finish();


                default:
                    break;
            }
        }
    };

}
