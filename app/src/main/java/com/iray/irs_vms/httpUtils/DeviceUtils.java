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
    private static String DEVICE_API_URL = "/api-user/device";
    private static String DEVICE_ALL_CURRENT_DEVICE = "/current";

    public static String listAllCurrentDevice(String accessToken){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
//            MediaType mediaType = MediaType.parse(Common.MEDIA_TYPE);
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+DEVICE_API_URL+DEVICE_ALL_CURRENT_DEVICE)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
