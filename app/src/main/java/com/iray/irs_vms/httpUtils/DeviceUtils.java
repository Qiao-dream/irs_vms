package com.iray.irs_vms.httpUtils;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class DeviceUtils {
    private static String TAG="DeviceUtils";
    private static String VIDEO_API_URL = "/api-video/api";
    private static String SUB_VIDEO_DEVICE = "/device";
    private static String DEVICE_API_URL = "/api-user/device";
    private static String DEVICE_ALL_CURRENT_DEVICE = "/current";   //列出全部设备
    private static String VIDEO_DEVICE_QUERY_CHANNELS = "/query/channels";    //获取通道数据
    private static String VIDEO_GET_MEAN_STREAM = "/play";    //获取主码流地址
    private static String VIDEO_GET_REPLAY_LIST = "/listVideos";    //获取回放列表
    private static String VIDEO_ADD_KEY_DEVICE = "/favorite";    //获取重点监控设备
    /**
     * 根据登录用户列出全部设备
     *
     * @param accessToken
     * @return
     */
    public static String listAllCurrentDevice(String accessToken){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+DEVICE_API_URL+DEVICE_ALL_CURRENT_DEVICE)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
           // Log.i(TAG,response.body().string());
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取重点监控视频列表（收藏列表）
     *
     * @param
     * @return
     */
    public static String listAllCurrentKeyDevice(String accessToken){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+DEVICE_API_URL+VIDEO_ADD_KEY_DEVICE)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
          //  Log.i(TAG,"debug1..."+response.body().string());
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 添加设备到重点监控（收藏功能）
     * @param accessToken
     * @param json
     * @return
     * Post请求
     */
    public static String AddFavorKeyDevice(String accessToken,String json){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            MediaType mediaType = MediaType.parse(Common.MEDIA_TYPE1);
            RequestBody body = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+DEVICE_API_URL+VIDEO_ADD_KEY_DEVICE+"/add")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            String resultStr = response.body().string();
            Log.w(TAG,"AddFavorKeyDevice resultStr:"+resultStr);
            return resultStr;
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 删除在重点监控中的设备
     * @param accessToken
     * @param id
     * @return
     */
    public static String DeleteFavorKeyDevice(String accessToken,String id){
        try {
            Log.w(TAG,"Delete key device ID:"+id);
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            Log.i(TAG,"debug...");
            String url = String.format("%s%s%s/id/%d", Common.HTTP_URL, DEVICE_API_URL,VIDEO_ADD_KEY_DEVICE, Long.parseLong(id));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .delete()
                    .build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            Log.i(TAG,"debug2..."+ e.toString());
            return "";
        }
    }

    /**
     * 根据设备主机ID查询所有通道（protocolChannelId、protocolDeviceId）
     * @param accessToken
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static String queryChannels(String accessToken, String id, int pageNum, int pageSize){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            String url = String.format("%s%s%s%s?id=%s&pageNum=%s&pageSize=%s", Common.HTTP_URL, VIDEO_API_URL, SUB_VIDEO_DEVICE, VIDEO_DEVICE_QUERY_CHANNELS, id, pageNum, pageSize);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String getMainStream(String accessToken, String protocolChannelId, String protocolDeviceId){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            String url = String.format("%s%s%s?protocol_channel_id=%s&protocol_device_id=%s", Common.HTTP_URL, VIDEO_API_URL, VIDEO_GET_MEAN_STREAM, protocolChannelId, protocolDeviceId);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取回放列表
     * @param accessToken
     * @param protocolChannelId
     * @param protocolDeviceId
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getReplayListByTime(String accessToken, String protocolChannelId, String protocolDeviceId, String startTime, String endTime){
        try {
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            JSONObject json = new JSONObject();
            try {
                json.put("channelId", protocolChannelId);
                json.put("deviceId", protocolDeviceId);
                json.put("endTime", endTime);
                json.put("map", null);
                json.put("startTime", startTime);
            } catch (JSONException e){
                e.printStackTrace();
            }
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
            String url = String.format("%s%s%s?protocol_channel_id=%s&protocol_device_id=%s", Common.HTTP_URL, VIDEO_API_URL, VIDEO_GET_REPLAY_LIST, protocolChannelId, protocolDeviceId);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
