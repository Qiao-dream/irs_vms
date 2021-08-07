package com.iray.irs_vms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.AlarmUploadActivity;
import com.iray.irs_vms.activity.InspectionActivity;
import com.iray.irs_vms.adapter.InspectListRemoteAdapter;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.info.InspectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.iray.irs_vms.httpUtils.InspectUtils.listInspectTasks;

public class TodayAccessFragment extends Fragment {
    public static String TAG="TodayAccessFragment";
    private InspectListRemoteAdapter adapter;
    public InspectionActivity.OfflineInspectListHandler mInspectHandler;
    public List<InspectInfo> mInspectList=new ArrayList<InspectInfo>();
    List<InspectInfo> result;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.activity_data_access_statis, container, false);
        return view;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             result = (List) msg.obj;
             Log.i(TAG,"result:"+result.size());
            if (result.size() == 0) {
                Toast.makeText(getContext(), getString(R.string.tst_inspect_list_empty), Toast.LENGTH_SHORT).show();
            } else {
                adapter = new InspectListRemoteAdapter(getContext(), result, mInspectHandler);

            }


        }

    };

    public TodayAccessFragment(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String inspectResult = listInspectTasks(Common.ACCESS_TOKEN, 1,"1", "10","");
                Log.i(TAG, "inspectResult:" + inspectResult);
                if (!inspectResult.equals("")) {
                    try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(inspectResult);
                    JSONArray inspectDatas = null;
                        inspectDatas = jsonObject.getJSONArray("data");
                    for (int i = 0; i < inspectDatas.length(); i++) {
                            JSONObject aDeviceData;
                            aDeviceData = inspectDatas.getJSONObject(i);
                            InspectInfo inspectInfoBuff = new InspectInfo();
                            inspectInfoBuff.setId(aDeviceData.getString("id"));
                            inspectInfoBuff.setName(aDeviceData.getString("name"));
                            inspectInfoBuff.setUserId(aDeviceData.getString("userId"));
                            inspectInfoBuff.setUserName(aDeviceData.getString("userName"));
                            inspectInfoBuff.setPollingType(aDeviceData.getString("pollingType"));
                            inspectInfoBuff.setPollingDev(aDeviceData.getString("pollingDev"));
                            inspectInfoBuff.setTaskDescription(aDeviceData.getString("taskDescription"));
                            inspectInfoBuff.setDangerDescription(aDeviceData.getString("dangerDescription"));
                            inspectInfoBuff.setSuggestion(aDeviceData.getString("suggestion"));
                            inspectInfoBuff.setTime(aDeviceData.getString("time"));
                            inspectInfoBuff.setHiddenDanger(aDeviceData.getString("hiddenDanger"));
                            inspectInfoBuff.setPollingTypeV(aDeviceData.getString("pollingTypeV"));
                            inspectInfoBuff.setPollingStateV(aDeviceData.getString("pollingStateV"));
                            inspectInfoBuff.setTimeV(aDeviceData.getString("timeV"));
                            inspectInfoBuff.setPollingId(aDeviceData.getString("pollingId"));
                            inspectInfoBuff.setUrl(aDeviceData.getString("url"));
                            inspectInfoBuff.setLocation(aDeviceData.getString("location"));
                            inspectInfoBuff.setTag(aDeviceData.getString("tag"));
                            mInspectList.add(inspectInfoBuff);

                            Message msg = new Message();
                            msg.obj = mInspectList;
                            handler.sendMessage(msg);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
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
