package com.iray.irs_vms.httpUtils;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.iray.irs_vms.activity.AbnormalAlarmActivity;
import com.iray.irs_vms.activity.PreviewActivity;
import com.iray.irs_vms.activity.ReplayListActivity;
import com.iray.irs_vms.info.AbnormalAlarmInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AbnormalAlarmManage {
    String TAG = "AbnormalAlarmManage";
    private static AbnormalAlarmManage instance = null;
    //    private AbnormalAlarmActivity AbnormalAlarmActivity;
    private WeakReference<AbnormalAlarmActivity> abnormalAlarmActivityWeakReference;
    public  List<AbnormalAlarmInfo> mAlarmList;
    public static String personTotal;   //通行人数
    public static int tempAbnormals;   //体温异常
    public static int unMasks;   //未戴口罩
    public static List<String> mTempNameList=new ArrayList<String>();
    private WeakReference<PreviewActivity> previewActivityWeakReference;
    private WeakReference<ReplayListActivity> replayListActivityWeakReference;
    String testTempResultStr = "1";
    private String currenActivity; //当前调用manage的Context;

    private static final String ALARM_GET_EVENT = "alarm_get_event";
    private static final String ALARM_GET_ABNORMAL = "alarm_get_abnormal";


    private AbnormalAlarmManage() {
        mAlarmList = new ArrayList<AbnormalAlarmInfo>();
    }

    public static synchronized AbnormalAlarmManage getInstance() {
        if (null == instance) {
            instance = new AbnormalAlarmManage();
        }
        return instance;
    }

    public void setAbnormalAlarmActivityWeakReference(AbnormalAlarmActivity AbnormalAlarmActivity) {
        this.abnormalAlarmActivityWeakReference = new WeakReference<>(AbnormalAlarmActivity);
    }



    /**
     * 查询异常报警的信息
     */
    public void listAllAlarmDl() {
        currenActivity = "AbnormalAlarmActivity";
        AlarmManageTask task = new AlarmManageTask(abnormalAlarmActivityWeakReference.get().pbAlarmList);
        task.execute(new String[]{ALARM_GET_ABNORMAL});
    }

    private class AlarmManageTask extends AsyncTask<String, String, String> {
        private ProgressBar mProgressBar;
        private WeakReference<AbnormalAlarmActivity> abnormalAlarmActivity = null;

        public AlarmManageTask(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
            this.abnormalAlarmActivity = abnormalAlarmActivityWeakReference;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
     //       mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = strings[0];
            switch (strings[0]) {
                case ALARM_GET_ABNORMAL:
                    mAlarmList.clear();
                    JSONObject json = new JSONObject();
                    try {
                        json.put("currentPage",1);
                        json.put("size",10);
                        json.put("eventType",10001);
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
                            mAlarmList.add(alarmInfoBuff);

                         /*
                            resultStr:{"count":107290,"data":[{"id":"1417011653358129153",
                            "eventName":"区域温度","eventType":"10001",
                            "eventTimeStamp":"2021-07-19 14:41:19",
                            "devId":"1413436090466963457","devName":"IraytekPT45haha",
                            "devChannel":"1","detail":"区域1报警",
                            "rtspUrl":"rtsp://172.16.20.48:8554/rtp/1413436090466963457_1-0",
                            "detailUrl":null,"state":1,"process":null,"stateV":"未处理"}],
                            "code":0,"msg":"success"}
                           */

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ALARM_GET_EVENT:

                    break;


            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case ALARM_GET_ABNORMAL:
                    if (currenActivity.equals("AbnormalAlarmActivity")) {
                        abnormalAlarmActivity.get().sendAlarmHandler(AbnormalAlarmActivity.HANDLER_LIST_ALL_ALARMS);
                    }
                    break;
                case ALARM_GET_EVENT:

                    break;
                default:
                    break;
            }

           // mProgressBar.setVisibility(View.GONE);
        }
    }
}
