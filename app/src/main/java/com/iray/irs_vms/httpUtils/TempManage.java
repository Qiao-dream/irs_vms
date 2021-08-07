package com.iray.irs_vms.httpUtils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.iray.irs_vms.activity.PreviewActivity;
import com.iray.irs_vms.activity.ReplayListActivity;
import com.iray.irs_vms.activity.TempCheckActivity;
import com.iray.irs_vms.info.TempCheckInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TempManage {
    String TAG = "TempManage";
    private static TempManage instance = null;
    //    private TempCheckActivity TempCheckActivity;
    private WeakReference<TempCheckActivity> TempCheckActivityWeakReference;
    public  List<TempCheckInfo> mTempList;
    public static String personTotal;   //通行人数
    public static int tempAbnormals;   //体温异常
    public static int unMasks;   //未戴口罩
    public static List<String> mTempNameList=new ArrayList<String>();
    private WeakReference<PreviewActivity> previewActivityWeakReference;
    private WeakReference<ReplayListActivity> replayListActivityWeakReference;
    String testTempResultStr = "1";
    private String currenActivity; //当前调用manage的Context;

    private static final String Temp_MANAGE_LIST_TempS = "list_Temps";
    private static final String Temp_MANAGE_GET_CHANNELS = "get_channels";
    private static final String Temp_MANAGE_GET_REPLAY_LIST = "get_replay_list";

    private TempManage() {
        mTempList = new ArrayList<TempCheckInfo>();
    }

    public static synchronized TempManage getInstance() {
        if (null == instance) {
            instance = new TempManage();
        }
        return instance;
    }

    public void setTempCheckActivityWeakReference(TempCheckActivity TempCheckActivity) {
        this.TempCheckActivityWeakReference = new WeakReference<>(TempCheckActivity);
    }

    public void setPreviewActivityWeakReference(PreviewActivity previewActivity) {
        this.previewActivityWeakReference = new WeakReference<PreviewActivity>(previewActivity);
    }

    public void setReplayListActivityWeakReference(ReplayListActivity replayListActivity) {
        this.replayListActivityWeakReference = new WeakReference<>(replayListActivity);
    }

    /**
     * 获取体温筛查信息列表
     */
    public void listAllTempDl() {
        currenActivity = "TempCheckActivity";
        TempManageTask task = new TempManageTask(TempCheckActivityWeakReference.get().pbTempList);
        task.execute(new String[]{Temp_MANAGE_LIST_TempS});
    }

    /**
     * 获取设备作为sp成员
     */
    public void listAllTempRl() {
        currenActivity = "ReplayListActivity";
        TempManageTask task = new TempManageTask(replayListActivityWeakReference.get().pbReplayList);
        task.execute(new String[]{Temp_MANAGE_LIST_TempS});
    }

    /**
     * 获取回放列表
     */
    public void getReplayList() {
        currenActivity = "ReplayListActivity";
        TempManageTask task = new TempManageTask(replayListActivityWeakReference.get().pbReplayList);
        task.execute(new String[]{Temp_MANAGE_GET_REPLAY_LIST});
    }

    /**
     * 获取rtsp地址
     */
    public void getRtsp() {
        currenActivity = "PreviewActivity";
        TempManageTask task = new TempManageTask(previewActivityWeakReference.get().pbPreview);
        task.execute(new String[]{Temp_MANAGE_GET_CHANNELS});
    }


    private class TempManageTask extends AsyncTask<String, String, String> {
        private ProgressBar mProgressBar;
        private WeakReference<TempCheckActivity> tempCheckActivity = null;
        private WeakReference<PreviewActivity> previewActivity = null;
        private WeakReference<ReplayListActivity> replayListActivity = null;
        private String rtsp = "";
        private String channelCount = "1";

        public TempManageTask(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
            this.tempCheckActivity = TempCheckActivityWeakReference;
            this.previewActivity = previewActivityWeakReference;
            this.replayListActivity = replayListActivityWeakReference;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = strings[0];
            switch (strings[0]) {
                case Temp_MANAGE_LIST_TempS:
                    mTempList.clear();
                    JSONObject json = new JSONObject();
                    try {
                        json.put("currentPage","1");
                        json.put("size","2");
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String totalPerson = TempCheckUtils.GetTempStatics(Common.ACCESS_TOKEN,json.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(totalPerson);
                        personTotal = jsonObject.getString("count");
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            TempCheckInfo tempInfoBuff = new TempCheckInfo();
                            tempInfoBuff.setId(jsonObject1.getString("id"));
                            tempInfoBuff.setDevId(jsonObject1.getString("devId"));
                            tempInfoBuff.setDeviceName(jsonObject1.getString("devName"));
                            tempInfoBuff.setDevChannel(jsonObject1.getString("devChannel"));
                            tempInfoBuff.setImgUrl(jsonObject1.getString("imgUrl"));
                            tempInfoBuff.setName(jsonObject1.getString("name"));
                            tempInfoBuff.setTemp(jsonObject1.getString("temp"));
                            tempInfoBuff.setMask(jsonObject1.getString("mask"));
                            tempInfoBuff.setAbnormal(jsonObject1.getString("abnormal"));
                            mTempList.add(tempInfoBuff);

                            for(int j=0;j<mTempList.size();j++){
                                if(mTempList.get(i).getMask().equals("none")){
                                    unMasks++;
                                }

                            }
                            for(int k=0;k<mTempList.size();k++){
                                if(Float.valueOf(mTempList.get(i).getTemp())>37.5){
                                    tempAbnormals++;
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case Temp_MANAGE_GET_CHANNELS:

                    break;
                case Temp_MANAGE_GET_REPLAY_LIST:


            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case Temp_MANAGE_LIST_TempS:
                    if (currenActivity.equals("TempCheckActivity")) {
                        tempCheckActivity.get().sendTempHandler(TempCheckActivity.HANDLER_LIST_ALL_TEMPS);
                    }
                    break;
                case Temp_MANAGE_GET_CHANNELS:
                    previewActivity.get().sendPreviewHandler(PreviewActivity.HANDLER_GOT_RTSP, rtsp, channelCount);
                    break;
                default:
                    break;
            }

            mProgressBar.setVisibility(View.GONE);
        }


    }
}
