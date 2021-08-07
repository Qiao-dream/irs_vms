package com.iray.irs_vms.httpUtils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.iray.irs_vms.activity.NoticesActivity;
import com.iray.irs_vms.activity.PreviewActivity;
import com.iray.irs_vms.activity.ReplayListActivity;
import com.iray.irs_vms.info.NoticeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.iray.irs_vms.httpUtils.NoticeUtils.listNoticeTasks;

public class NoticesManage {
    String TAG = "NoticesManage";
    private static NoticesManage instance = null;
    private WeakReference<NoticesActivity> NoticesActivityWeakReference;
    public  List<NoticeInfo> mNoticesList;
    private WeakReference<PreviewActivity> previewActivityWeakReference;
    private WeakReference<ReplayListActivity> replayListActivityWeakReference;
    String testNoticesResultStr = "1";
    private String currenActivity; //当前调用manage的Context;

    private static final String NOTICES_MANAGE_LIST_NOTICES = "list_Notices";
    private static final String NOTICES_MANAGE_GET_CHANNELS = "get_channels";
    private static final String NOTICES_MANAGE_GET_REPLAY_LIST = "get_replay_list";
    private NoticesManage() {
        mNoticesList = new ArrayList<NoticeInfo>();
    }

    public static synchronized NoticesManage getInstance() {
        if (null == instance) {
            instance = new NoticesManage();
        }
        return instance;
    }

    public void setNoticesActivityWeakReference(NoticesActivity NoticesActivity) {
        this.NoticesActivityWeakReference = new WeakReference<>(NoticesActivity);
    }

    public void setPreviewActivityWeakReference(PreviewActivity previewActivity) {
        this.previewActivityWeakReference = new WeakReference<PreviewActivity>(previewActivity);
    }

    public void setReplayListActivityWeakReference(ReplayListActivity replayListActivity) {
        this.replayListActivityWeakReference = new WeakReference<>(replayListActivity);
    }

    /**
     * 获取系统通知信息列表
     */
    public void listAllNoticesDl() {
        currenActivity = "NoticesActivity";
        NoticesManage.NoticesManageTask task = new NoticesManage.NoticesManageTask(NoticesActivityWeakReference.get().pbnoticeList);
        task.execute(new String[]{NOTICES_MANAGE_LIST_NOTICES});
    }

    private class NoticesManageTask extends AsyncTask<String, String, String> {
        private ProgressBar mProgressBar;
        private WeakReference<NoticesActivity>noticesActivity = null;
        private WeakReference<PreviewActivity> previewActivity = null;
        private WeakReference<ReplayListActivity> replayListActivity = null;
        private String rtsp = "";
        private String channelCount = "1";

        public NoticesManageTask(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
            this.noticesActivity = NoticesActivityWeakReference;
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
                case NOTICES_MANAGE_LIST_NOTICES:
                    String noticeList = listNoticeTasks(Common.ACCESS_TOKEN, "1", "10");
                    Log.i(TAG,"noticeList:"+noticeList);
                    if (!noticeList.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(noticeList);
                            JSONArray noticeDatas = jsonObject.getJSONArray("data");
                            JSONObject aNoticeData;
                            for (int i = 0; i < noticeDatas.length(); i++) {
                                aNoticeData = noticeDatas.getJSONObject(i);
                                NoticeInfo noticeInfoBuff = new NoticeInfo();
                                noticeInfoBuff.setId(aNoticeData.getString("id"));
                                noticeInfoBuff.setHeadline(aNoticeData.getString("headline"));
                                noticeInfoBuff.setSubhead(aNoticeData.getString("subhead"));
                                noticeInfoBuff.setUserId(aNoticeData.getString("userId"));
                                noticeInfoBuff.setUserName(aNoticeData.getString("userName"));
                                noticeInfoBuff.setTime(aNoticeData.getString("time"));
                                noticeInfoBuff.setDetail(aNoticeData.getString("detail"));
                                mNoticesList.add(noticeInfoBuff);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case NOTICES_MANAGE_LIST_NOTICES:
                    if (currenActivity.equals("NoticesActivity")) {
                        noticesActivity.get().sendNoticeHandler(NoticesActivity.HANDLER_LIST_ALL_NOTICES);
                    }
                    break;

                default:
                    break;
            }

            mProgressBar.setVisibility(View.GONE);
        }

    }
}
