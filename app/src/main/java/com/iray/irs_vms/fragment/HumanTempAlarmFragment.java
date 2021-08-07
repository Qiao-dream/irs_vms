package com.iray.irs_vms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.AbnormalAlarmActivity;
import com.iray.irs_vms.activity.AlarmUploadActivity;
import com.iray.irs_vms.adapter.AbnormalAlarmAdapter;
import com.iray.irs_vms.info.AbnormalAlarmInfo;
import com.iray.irs_vms.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

public class HumanTempAlarmFragment extends Fragment {
    public static String TAG="HumanTempAlarmFragment";
    private AbnormalAlarmAdapter adapter;
    public AbnormalAlarmActivity.AlarmListHandler mAlarmHandler;
    public List<AbnormalAlarmInfo> mAlarmList=new ArrayList<AbnormalAlarmInfo>();
    List<AbnormalAlarmInfo> result;
    private RecyclerView rcDeviceList;
    private Button btnUpload;
    public List<AbnormalAlarmInfo> mHumanTempAlarmList=new ArrayList<AbnormalAlarmInfo>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("HumanTempAlarmFragmemnt","onCreate...............");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag=true;
                while(flag) {
                    if (AllAlarmFragment.mAlarmList.size() != 0) {
                        flag=false;
                        for(int i=0;i<AllAlarmFragment.mAlarmList.size();i++){
                            if(AllAlarmFragment.mAlarmList.get(i).getEventType().equals("10006")){
                                mHumanTempAlarmList.add(AllAlarmFragment.mAlarmList.get(i));
                            }
                        }

                        Message msg = new Message();
                        msg.obj = mHumanTempAlarmList;
                        handler.sendMessage(msg);
                    }else{
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();

    }
    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG,"onStart------------------"+mHumanTempAlarmList.size());
        Message msg = new Message();
        msg.obj = mHumanTempAlarmList;
        handler.sendMessage(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG,"onResume------------------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG,"onDestroy------------------");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG,"onPause------------------");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG,"onStop------------------");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("HumanTempAlarmFragmemnt","onCreateView1...............");
        View view =  inflater.inflate(R.layout.activity_human_temp_alarm_list, container, false);
        rcDeviceList = (RecyclerView)view.findViewById(R.id.rc_human_temp_alarm_list);
        Log.w("HumanTempAlarmFragmemnt","onCreateView2...............");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcDeviceList.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.divide_gray_color)));
        rcDeviceList.setLayoutManager(linearLayoutManager);
        return view;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            result = (List) msg.obj;
            Log.i(TAG,"result:"+result.size());
            if (result.size() == 0) {
                Toast.makeText(getContext(), "目前没有体温报警信息", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new AbnormalAlarmAdapter(getContext(), result, mAlarmHandler);
                rcDeviceList.setAdapter(adapter);
            }


        }

    };

    public HumanTempAlarmFragment(){

    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //安防门禁
                case R.id.btn_access_control:
                    Intent intent0 = new Intent(getContext(), AlarmUploadActivity.class);
                    startActivity(intent0);
                    break;
                default:
                    break;
            }
        }
    };

}
