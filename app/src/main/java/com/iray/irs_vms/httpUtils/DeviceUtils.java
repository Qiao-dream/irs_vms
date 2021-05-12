package com.iray.irs_vms.httpUtils;

import android.os.Handler;
import android.os.Message;

import com.iray.irs_vms.activity.LoginActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class DeviceUtils {
    private static String VIDEO_API_URL = "/api-video/api";
    private static String SUB_VIDEO_DEVICE = "/device";
    private static String DEVICE_API_URL = "/api-user/device";
    private static String DEVICE_ALL_CURRENT_DEVICE = "/current";   //列出全部设备
    private static String VIDEO_DEVICE_QUERY_CHANNELS = "/query/channels";    //获取通道数据
    private static String VIDEO_GET_MEAN_STREAM = "/play";    //获取主码流地址

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
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
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
}
