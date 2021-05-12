package com.iray.irs_vms.httpUtils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.iray.irs_vms.activity.DeviceListActivity;
import com.iray.irs_vms.activity.MainActivity;
import com.iray.irs_vms.activity.PreviewActivity;
import com.iray.irs_vms.info.DeviceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DeviceManage {
    String TAG = "DeviceManage";
    private static DeviceManage instance = null;
    //    private DeviceListActivity deviceListActivity;
    private WeakReference<DeviceListActivity> deviceListActivityWeakReference;
    public List<DeviceInfo> mDeviceList;
    private WeakReference<PreviewActivity> previewActivityWeakReference;
    String testDeviceResultStr = "1";

    private static final String DEVICE_MANAGE_LIST_DEVICES = "list_devices";
    private static final String DEVICE_MANAGE_GET_CHANNELS = "get_channels";

    private DeviceManage() {
        mDeviceList = new ArrayList<DeviceInfo>();
    }

    public static synchronized DeviceManage getInstance() {
        if (null == instance) {
            instance = new DeviceManage();
        }
        return instance;
    }

    public void setDeviceListActivityWeakReference(DeviceListActivity deviceListActivity) {
        this.deviceListActivityWeakReference = new WeakReference<DeviceListActivity>(deviceListActivity);
    }

    public void setPreviewActivityWeakReference(PreviewActivity previewActivity) {
        this.previewActivityWeakReference = new WeakReference<PreviewActivity>(previewActivity);
    }

    public void listAllDevice() {
        DeviceManageTask task = new DeviceManageTask(deviceListActivityWeakReference.get().pbDeviceList);
        task.execute(new String[]{DEVICE_MANAGE_LIST_DEVICES});
    }

    public void getDeviceChannel() {
        DeviceManageTask task = new DeviceManageTask(previewActivityWeakReference.get().pbPreview);
        task.execute(new String[]{DEVICE_MANAGE_GET_CHANNELS});
    }


    private class DeviceManageTask extends AsyncTask<String, String, String> {
        private ProgressBar mProgressBar;
        private WeakReference<DeviceListActivity> deviceListActivity = null;
        private WeakReference<PreviewActivity> previewActivity = null;
        private String rtsp = "";

        public DeviceManageTask(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
            this.deviceListActivity = deviceListActivityWeakReference;
            this.previewActivity = previewActivityWeakReference;
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
                case DEVICE_MANAGE_LIST_DEVICES:
                    String deviceListStr = DeviceUtils.listAllCurrentDevice(Common.ACCESS_TOKEN);
                    if (!deviceListStr.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(deviceListStr);
                            JSONArray deviceDatas = jsonObject.getJSONArray("datas");
                            JSONObject aDeviceData;
                            for (int i = 0; i < deviceDatas.length(); i++) {
                                aDeviceData = deviceDatas.getJSONObject(i);
                                DeviceInfo deviceInfoBuff = new DeviceInfo();
                                deviceInfoBuff.setDeviceId(aDeviceData.getString("id"));
                                deviceInfoBuff.setDeviceOrg(aDeviceData.getString("organizationName"));
                                deviceInfoBuff.setDeviceName(aDeviceData.getString("name"));
                                deviceInfoBuff.setDeviceTransport(aDeviceData.isNull("transport") ? 0 : aDeviceData.getInt("transport"));
                                deviceInfoBuff.setDeviceType(aDeviceData.isNull("deviceType") ? 0 : aDeviceData.getInt("deviceType"));
                                deviceInfoBuff.setDeviceOnline(!aDeviceData.isNull("online") && aDeviceData.getBoolean("online"));
                                mDeviceList.add(deviceInfoBuff);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case DEVICE_MANAGE_GET_CHANNELS:
                    String protocolChannelId = "";
                    String protocolDeviceId = "";
                    String channelResultStr = DeviceUtils.queryChannels(Common.ACCESS_TOKEN, previewActivityWeakReference.get().deviceId, 1, 10);
                    Log.e(TAG, "channelResultStr: " + channelResultStr);
                    if (!channelResultStr.equals("")) {
                        try {
                            JSONObject channelObject = new JSONObject(channelResultStr);
                            JSONArray channelData = channelObject.getJSONArray("data");
                            JSONObject aChannelData = channelData.getJSONObject(0);
                            protocolChannelId = aChannelData.getString("protocolChannelId");
                            protocolDeviceId = aChannelData.getString("protocolDeviceId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!protocolChannelId.equals("") && !protocolDeviceId.equals("")) {
                        String getMainStreamResultStr = DeviceUtils.getMainStream(Common.ACCESS_TOKEN, protocolChannelId, protocolDeviceId);
                        Log.e(TAG, "getMainStreamResultStr: " + getMainStreamResultStr);
                        if (!getMainStreamResultStr.equals("")) {
                            rtsp = "";
                            try {
                                JSONObject mainStreamObject = new JSONObject(getMainStreamResultStr);
                                if (!mainStreamObject.isNull("datas")) {
                                    JSONObject mainStreamObjectData = mainStreamObject.getJSONObject("datas");
                                    rtsp = mainStreamObjectData.getString("rtsp");
                                    Log.e(TAG, "rtsp: " + rtsp);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                case DEVICE_MANAGE_LIST_DEVICES:
                    deviceListActivity.get().sendDeviceHandler(DeviceListActivity.HANDLER_LIST_ALL_DEVICES);
                    break;
                case DEVICE_MANAGE_GET_CHANNELS:
                    previewActivity.get().sendPreviewHandler(PreviewActivity.HANDLER_GOT_RTSP, rtsp);
                    break;
                default:
                    break;
            }

            mProgressBar.setVisibility(View.GONE);
        }


    }
}
