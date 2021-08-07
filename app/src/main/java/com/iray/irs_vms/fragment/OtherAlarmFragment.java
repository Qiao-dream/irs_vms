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

public class OtherAlarmFragment extends Fragment {
    public static String TAG="OtherAlarmFragment";
    private AbnormalAlarmAdapter adapter;
    public AbnormalAlarmActivity.AlarmListHandler mAlarmHandler;
    public List<AbnormalAlarmInfo> mOtherAlarmList=new ArrayList<AbnormalAlarmInfo>();
    List<AbnormalAlarmInfo> result;
    private RecyclerView rcDeviceList;
    private Button btnUpload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("OtherAlarmFragmemnt","onCreate...............");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag=true;
                while(flag) {
                    if (AllAlarmFragment.mAlarmList.size() != 0) {
                        flag=false;
                        for(int i=0;i<AllAlarmFragment.mAlarmList.size();i++){
                            if(AllAlarmFragment.mAlarmList.get(i).getEventType().equals("10008")){
                                mOtherAlarmList.add(AllAlarmFragment.mAlarmList.get(i));
                            }
                        }
                        Message msg = new Message();
                        msg.obj = mOtherAlarmList;
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
    public void onStart() {
        super.onStart();
        Log.w(TAG,"onStart------------------"+mOtherAlarmList.size());
        Message msg = new Message();
        msg.obj = mOtherAlarmList;
        handler.sendMessage(msg);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("OtherAlarmFragmemnt","onCreateView1...............");
        View view =  inflater.inflate(R.layout.activity_other_alarm_list, container, false);
        rcDeviceList = (RecyclerView)view.findViewById(R.id.rc_other_alarm_list);
        Log.w("OtherAlarmFragmemnt","onCreateView2...............");
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
                Toast.makeText(getContext(), "目前没有其他报警信息", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new AbnormalAlarmAdapter(getContext(), result, mAlarmHandler);
                rcDeviceList.setAdapter(adapter);
            }


        }

    };

    public OtherAlarmFragment(){

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
