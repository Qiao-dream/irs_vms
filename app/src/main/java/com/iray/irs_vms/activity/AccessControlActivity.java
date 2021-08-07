package com.iray.irs_vms.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.AccessControlUtils;
import com.iray.irs_vms.httpUtils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.iray.irs_vms.httpUtils.DeviceManage.mDeviceNameList;

public class AccessControlActivity extends BaseActivity {

    private static final String TAG ="AccessControlActivity";
    private ArrayAdapter<String> deviceNameSpAdapter;
    private Button btnClose;
    private TextView accessPersons, abnormal_persons_value,temp_abnormal_value,without_mask_value;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_control);
        final Spinner deviceNameSpinner = (Spinner) findViewById(R.id.device_name_value);
        btnClose=findViewById(R.id.access_control_btn_close);
        btnClose.setOnClickListener(mOnClickListener);
        accessPersons=findViewById(R.id.access_persons_value);
        abnormal_persons_value=findViewById(R.id.abnormal_persons_value);
        temp_abnormal_value=findViewById(R.id.temp_abnormal_value);
        without_mask_value=findViewById(R.id.without_mask_value);

        //找到Spinner控件
        deviceNameSpinner.setPrompt("请选择设备名称:");
        final List<String> deviceList = new ArrayList<String>();
        deviceNameSpAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mDeviceNameList);
        deviceNameSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceNameSpinner.setAdapter(deviceNameSpAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String totalPerson = AccessControlUtils.QueryGetTotal(Common.ACCESS_TOKEN);
                String datas = null;
                try {
                    JSONObject jsonObject = new JSONObject(totalPerson);
                    datas = jsonObject.getString("datas");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI操作；
                final String finalPersons = datas;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        accessPersons.setText(finalPersons);
                    }
                });

            }
        }).start();


        deviceNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
               // Log.w(TAG,"选中的设备名称是："+deviceNameSpAdapter.get((int)id));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      //  Log.w(TAG,"ACCESS_TOKEN:"+Common.ACCESS_TOKEN);
                        JSONObject json = new JSONObject();
                        try {
                            json.put("startTime","");
                            json.put("endTime","");
                            json.put("deviceId","");

                        }catch (JSONException e) {
                         e.printStackTrace();
                        }
                        String userStatistics = AccessControlUtils.getUserStatistics(Common.ACCESS_TOKEN, json.toString());
                        if (!userStatistics.equals("")) {
                            try {
                                JSONObject userObject = new JSONObject(userStatistics);
                                JSONObject datas = userObject.getJSONObject("datas");
                                String total =  datas.getString("total");  //通行总人次
                                String staffCount = datas.getString("staffCount");  //员工人数
                                final String strangerCount=datas.getString("strangerCount"); //异常人次
                                final String abnormalCount=datas.getString("abnormalCount");  //体温异常
                                final String withoutMaskCount=datas.getString("withoutMaskCount");  //未带口罩

                                //更新UI操作；
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        abnormal_persons_value.setText(strangerCount);
                                        temp_abnormal_value.setText(abnormalCount);
                                        without_mask_value.setText(withoutMaskCount);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject json1 = new JSONObject();
                        try {
                            json1.put("startTime","");
                            json1.put("endTime","");
                            json1.put("deviceId","");

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.w(TAG,"Common.ACCESS_TOKEN:"+Common.ACCESS_TOKEN);
                         AccessControlUtils.getAccessAll(Common.ACCESS_TOKEN,json1.toString());
                    }
                }).start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //安防门禁
                case R.id.access_control_btn_close:
                    AccessControlActivity.this.finish();
                    break;

                default:
                    break;
            }
        }
    };
}
