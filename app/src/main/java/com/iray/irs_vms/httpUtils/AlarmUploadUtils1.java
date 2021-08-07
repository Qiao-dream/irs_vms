package com.iray.irs_vms.httpUtils;

import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AlarmUploadUtils1 {
    private static String TAG = "AlarmUploadUtils";
    private static String ALARM_API_URL = "172.16.20.5:9900/api-garden/system/task/saveDangers";   //账号：admin admin
    private static String FILE_CONVERTER_URL = "/api-file";     //账号：java 123456
    private static String FILE_CONVERTER_METHOD = "/files-anon";

    /**
     * 请求方式：post
     * {
     * "pollingId":31,                 //如果不关联巡检任务去掉该字段
     * "url":"http://172.16.20.5:8888/group1/M00/00/34/rBAUBWCiHLqAespNAACuAMi-fg406.jpeg,http://172.16.20.5:8888/group1/M00/00/34/rBAUBWCiHLqAespNAACuAMi-fg406.jpeg",
     * "location": "3楼",
     * "dangerDescription":"131",
     * "suggestion":"w234",
     * "hiddenDanger":"123",
     * "tag":1                          //如果不关联巡检任务该字段值为1.关联去掉该字段
     * }
     * 返回数据：
     * {
     * "msg": "success",
     * "code": 0     //失败code码为 1
     * }
     */
     //转换图片地址
    public static String FileConventer(String accessToken, File file) {
        //用jsonOject方式转string传递其他参数
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file))//文件
                    .build();
            Request request = new Request.Builder()
                    .url(Common.HTTP_URL+FILE_CONVERTER_URL + FILE_CONVERTER_METHOD).post(requestBody)
                    .addHeader("Authorization", accessToken)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String resultStr = response.body().string();
            Log.w("AlarmUploadUtils1", "AlarmUploadUtils1 resultStr:" + resultStr);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
