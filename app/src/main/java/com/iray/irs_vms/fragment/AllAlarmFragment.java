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
import com.iray.irs_vms.httpUtils.AbnormalAlarmUtils;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.info.AbnormalAlarmInfo;
import com.iray.irs_vms.widget.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllAlarmFragment extends Fragment {
    public static String TAG="AllAlarmFragment";
    private AbnormalAlarmAdapter adapter;
    public AbnormalAlarmActivity.AlarmListHandler mAlarmHandler;
    public static List<AbnormalAlarmInfo> mAlarmList=new ArrayList<AbnormalAlarmInfo>();
    List<AbnormalAlarmInfo> result;
    private RecyclerView rcDeviceList;
    private Button btnUpload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG,"onStart------------------"+mAlarmList.size());
        Message msg = new Message();
        msg.obj = mAlarmList;
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
        View view =  inflater.inflate(R.layout.activity_all_alarm_list, container, false);
        rcDeviceList = (RecyclerView)view.findViewById(R.id.rc_all_alarm_list);
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
            if (result.size() == 0) {
                Toast.makeText(getContext(), "目前没有报警信息", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new AbnormalAlarmAdapter(getContext(), result, mAlarmHandler);
                rcDeviceList.setAdapter(adapter);
            }
        }

    };

    public AllAlarmFragment(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("currentPage",1);
                        json.put("size",10000);
                       // json.put("eventType",10001);
                        json.put("startTime","");
                        json.put("endTime","");
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  final String alarmEventType = AbnormalAlarmUtils.GetAlarmEventType(Common.ACCESS_TOKEN);
                    final String alarmEventType = AbnormalAlarmUtils.GetAlarmAbnormal(Common.ACCESS_TOKEN,json.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(alarmEventType);

                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            AbnormalAlarmInfo alarmInfoBuff = new AbnormalAlarmInfo();
                            alarmInfoBuff.setId(jsonObject1.getString("id"));
                            alarmInfoBuff.setEventTimeStamp(jsonObject1.getString("eventTimeStamp"));
                            alarmInfoBuff.setDevName(jsonObject1.getString("devName"));
                            alarmInfoBuff.setDevChannel(jsonObject1.getString("devChannel"));
                            alarmInfoBuff.setDetail(jsonObject1.getString("detail"));
                            alarmInfoBuff.setDetailUrl(jsonObject1.getString("detailUrl"));
                            alarmInfoBuff.setRtspUrl(jsonObject1.getString("rtspUrl"));
                            alarmInfoBuff.setState(jsonObject1.getString("state"));
                            alarmInfoBuff.setProcess(jsonObject1.getString("process"));
                            alarmInfoBuff.setStateV(jsonObject1.getString("stateV"));
                            alarmInfoBuff.setEventType(jsonObject1.getString("eventType"));
                            mAlarmList.add(alarmInfoBuff);


                            Message msg = new Message();
                            msg.obj = mAlarmList;
                            handler.sendMessage(msg);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }).start();
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
