package com.iray.irs_vms.httpUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class RabbitMqUtils {
    private static String TAG="RabbitMqUtils";
    private static String RABBIT_API_URL = "/api-event/routing-key/all";

    /**
     * 消息队列路由键api
     * Routing Key Controller
     */
    public static String getRoutingKey(String accessToken){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            String url = String.format("%s", Common.HTTP_URL+RABBIT_API_URL
                  );
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
