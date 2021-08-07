package com.iray.irs_vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.adapter.NoticeListAdapter;
import com.iray.irs_vms.httpUtils.NoticesManage;
import com.iray.irs_vms.info.NoticeInfo;
import com.iray.irs_vms.widget.RecycleViewDivider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统通知
 */
public class NoticesActivity extends BaseActivity {
    public static String TAG="NoticesActivity";
    private Handler handler = new Handler();
    List<NoticeInfo> mNoticeInfoList=new ArrayList<NoticeInfo>();
    private RecyclerView rcNoticeList;
    private NoticeListAdapter adapter;
    public static final int HANDLER_LIST_ALL_NOTICES = 1001;
    public static final int HANDLER_GET_NOTICE_ID = 1002;
    public NoticesActivity.NoticeListHandler mNoticeHandler;
    public ProgressBar pbnoticeList;
    NoticesManage noticeManage;
    private ConstraintLayout noticeCheckTopBar;
    private ConstraintLayout noticeCheckMainLayout;
    private TextView accessPersonsValue;  //通行人数
    private TextView noticeAbnormal;  //体温异常
    private TextView unMask;  //未戴口罩
    private TextView abnormal_persons;
    private Button noticeCheckBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        init();
    }

    private void listNoticesInfo() {
        Log.w(TAG,"listNoticesInfo。。。。。。");
        mNoticeInfoList = noticeManage.mNoticesList;
        if (mNoticeInfoList.size() == 0) {
            Toast.makeText(getApplicationContext(),"未发现通知数据",Toast.LENGTH_LONG);
        } else {
            adapter = new NoticeListAdapter(getApplicationContext(), mNoticeInfoList, mNoticeHandler);
            rcNoticeList.setAdapter(adapter);

        }
    }


    private void init() {
        initView();
        mNoticeHandler = new NoticesActivity.NoticeListHandler(this);
        noticeManage = noticeManage.getInstance();
        noticeManage.setNoticesActivityWeakReference(this);

    }
    private void initView() {
        findView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcNoticeList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.divide_gray_color)));
        rcNoticeList.setLayoutManager(linearLayoutManager);


    }

    private void findView() {
        pbnoticeList = findViewById(R.id.pb_notice_list);
        rcNoticeList = (RecyclerView) findViewById(R.id.rc_notice_list);
        accessPersonsValue=findViewById(R.id.access_persons_value);
        noticeCheckBtnClose=findViewById(R.id.notice_btn_close);
        noticeCheckBtnClose.setOnClickListener(mOnClickListener);

    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //安防门禁
                case R.id.abnormal_persons:
                    Intent intent0 = new Intent(NoticesActivity.this, AccessControlActivity.class);
                    startActivity(intent0);
                    break;
                case R.id.notice_btn_close:
                    NoticesActivity.this.finish();


                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        noticeManage.listAllNoticesDl();
    }

    public void sendNoticeHandler(int msgWhat) {
        mNoticeHandler.sendEmptyMessage(msgWhat);
    }

    public static class NoticeListHandler extends Handler {
        private WeakReference<NoticesActivity> reference;
        private NoticesActivity activity;

        public NoticeListHandler(NoticesActivity noticeListActivity) {
            super();
            this.reference = new WeakReference<NoticesActivity>(noticeListActivity);
            this.activity = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LIST_ALL_NOTICES:
                    activity.listNoticesInfo();
                    break;
                case HANDLER_GET_NOTICE_ID:
                    if(!msg.getData().getString("id").equals("")){
                        Intent intent = new Intent(activity, PreviewActivity.class);
                        intent.putExtra("id", msg.getData().getString("id"));
                        intent.putExtra("org", msg.getData().getString("org"));
                        intent.putExtra("name", msg.getData().getString("name"));
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.tst_get_device_id_error), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
