package com.iray.irs_vms.httpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.iray.irs_vms.activity.LoginActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class UserUtils {
    private static String USER_API_URL = "/api-user";
    private static String LOGIN_API_URL = "/api-uaa/oauth/token";
    private static String MEDIA_TYPE = "application/x-www-form-urlencoded";


    public static void loginForToken(Handler mHandler, String userName, String password, String grantType, String clientId, String clientSecret){
        Message message = new Message();
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            MediaType mediaType = MediaType.parse(MEDIA_TYPE);
            RequestBody body = RequestBody.create(mediaType, String.format("username=%s&password=%s&grant_type=%s&client_id=%s&client_secret=%s", userName, password, grantType, clientId, clientSecret));
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+LOGIN_API_URL)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            String resultStr = response.body().string();
            if(resultStr.equals("")){
                message = mHandler.obtainMessage(LoginActivity.LOGIN_RESULT, -1);
            } else {
                message = mHandler.obtainMessage(LoginActivity.LOGIN_RESULT, resultStr);
            }
            mHandler.sendMessage(message);
        } catch (Exception e){
            e.printStackTrace();
            message = mHandler.obtainMessage(LoginActivity.LOGIN_RESULT, -1);
            mHandler.sendMessage(message);
        }
    }
}
