package com.iray.irs_vms.httpUtils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.iray.irs_vms.activity.DeviceListActivity;
import com.iray.irs_vms.activity.MainActivity;
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
    String testDeviceResultStr = "1";

    private static final String DEVICE_MANAGE_LIST_DEVICES = "list_devices";

    private DeviceManage(){
        mDeviceList = new ArrayList<DeviceInfo>();
    }

    public static synchronized DeviceManage getInstance(){
        if(null == instance){
            instance = new DeviceManage();
        }
        return instance;
    }

    public void setDeviceListActivityWeakReference(DeviceListActivity deviceListActivity) {
        this.deviceListActivityWeakReference = new WeakReference<DeviceListActivity>(deviceListActivity);
    }


    public void listAllDevice(){
        DeviceManageTask task = new DeviceManageTask(deviceListActivityWeakReference.get().pbDeviceList);
        task.execute(new String[]{DEVICE_MANAGE_LIST_DEVICES});
    }


    private class DeviceManageTask extends AsyncTask<String, String, String> {
        private ProgressBar mProgressBar;
        private WeakReference<DeviceListActivity> deviceListActivity = null;

        public DeviceManageTask(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
            this.deviceListActivity = deviceListActivityWeakReference;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = strings[0];
            if(strings[0].equals(DEVICE_MANAGE_LIST_DEVICES)){
                String deviceListStr = DeviceUtils.listAllCurrentDevice(Common.ACCESS_TOKEN);
                if(!deviceListStr.equals("")){
                    try {
                        JSONObject jsonObject = new JSONObject(deviceListStr);
                        JSONArray deviceDatas = jsonObject.getJSONArray("datas");
                        JSONObject aDeviceData;
                        for(int i = 0; i<deviceDatas.length(); i++){
                            aDeviceData = deviceDatas.getJSONObject(i);
                            DeviceInfo deviceInfoBuff = new DeviceInfo();
                            deviceInfoBuff.setDeviceOrg(aDeviceData.getString("organizationName"));
                            deviceInfoBuff.setDeviceName(aDeviceData.getString("name"));
                            deviceInfoBuff.setDeviceTransport(aDeviceData.isNull("transport")?0:aDeviceData.getInt("transport"));
                            deviceInfoBuff.setDeviceType(aDeviceData.isNull("deviceType")?0:aDeviceData.getInt("deviceType"));
                            deviceInfoBuff.setDeviceOnline(!aDeviceData.isNull("online")&&aDeviceData.getBoolean("online"));
                            mDeviceList.add(deviceInfoBuff);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals(DEVICE_MANAGE_LIST_DEVICES)){
                deviceListActivity.get().sendDeviceHandler(DeviceListActivity.HANDLER_LIST_ALL_DEVICES);
            }
            mProgressBar.setVisibility(View.GONE);
        }


    }
}
