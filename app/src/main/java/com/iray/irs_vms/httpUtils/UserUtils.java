package com.iray.irs_vms.httpUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class UserUtils {
    private static String USER_API_URL = "/api-user";
    private static String LOGIN_API_URL = "/api-uaa/oauth/token";
    private static String LOGIN_USER_ID = "/users/current";


    public final static int LOGIN_RESULT = 201;


    public static void loginForToken(Handler mHandler, String userName, String password, String grantType, String clientId, String clientSecret){
        Message message = new Message();
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            MediaType mediaType = MediaType.parse(Common.MEDIA_TYPE);
            RequestBody body = RequestBody.create(mediaType, String.format("username=%s&password=%s&grant_type=%s&client_id=%s&client_secret=%s", userName, password, grantType, clientId, clientSecret));
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+LOGIN_API_URL)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            String resultStr = response.body().string();
            Log.w("UserUtils","UserUtils resultStr:"+resultStr);
            if(resultStr.equals("")){
                message = mHandler.obtainMessage(LOGIN_RESULT, "");
            } else {
                message = mHandler.obtainMessage(LOGIN_RESULT, resultStr);
            }
            mHandler.sendMessage(message);
        } catch (Exception e){
            e.printStackTrace();
            message = mHandler.obtainMessage(LOGIN_RESULT, "");
            mHandler.sendMessage(message);
        }
    }

    /**
     * 获取UserID
     * GET请求
     * @param
     * @return
     */
    public static String getUserId(String accessToken){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+USER_API_URL+LOGIN_USER_ID)
                    .addHeader("Content-Type", Common.MEDIA_TYPE)
                    .addHeader("Authorization", accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            String userIds=response.body().string();
              Log.i("UserUtils","User ID..."+userIds);
            return userIds;
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }



}
