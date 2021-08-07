package com.iray.irs_vms.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.DialogActivity;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.httpUtils.RabbitMqUtils;
import com.iray.irs_vms.info.NewAlarmInfo;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.iray.irs_vms.activity.MainActivity.tagLogin;

public class AlarmService extends Service {
    private  boolean Flag_10001=true;
    private  boolean Flag_10002=true;
    private  boolean Flag_10003=true;
    private  boolean Flag_10004=true;
    private  boolean Flag_10005=true;


    public ConnectionFactory factory =  new ConnectionFactory();;
    private boolean serviceRunning = false;
    List<String> routingKeys;
    // 通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;

    // 必须实现的方法，用户返回Binder对象
    private static final String TAG ="AlarmService" ;
    public static List<NewAlarmInfo> mAlarmList=new ArrayList<NewAlarmInfo>();
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Alarm Service --onBind()--");
        return null;
    }

    // 创建Service时调用该方法，只调用一次
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Alarm Service --onCreate()--");
        final WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams   params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置window type
        //params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0+
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            params.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                while(!tagLogin) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Alarm Service --not login--");
                }
                String mqDeatil = RabbitMqUtils.getRoutingKey(Common.ACCESS_TOKEN);
                Log.w(TAG,"Alarm Service mqDeatil:"+mqDeatil);
                try {
                    JSONObject jsonObject = new JSONObject(mqDeatil);
                    JSONArray deviceDatas = jsonObject.getJSONArray("datas");
                    JSONObject aDeviceData;
                    routingKeys =new ArrayList();
                    for (int i = 0; i < deviceDatas.length(); i++) {
                        aDeviceData = deviceDatas.getJSONObject(i);
                        routingKeys.add(aDeviceData.getString("routingKey"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        setupConnectionFactory();
        //用于从线程中获取数据，更新ui
        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                String diplayDetail="";
                if (!message.equals("")) {
                    try {
                        JSONObject aAlarmData = new JSONObject(message);
                        NewAlarmInfo alarmInfoBuff = new NewAlarmInfo();
                        alarmInfoBuff.setEventId(aAlarmData.getString("eventId"));
                        String eventType=aAlarmData.getString("eventType");
                        alarmInfoBuff.setEventType(eventType);
                        alarmInfoBuff.setEventTime(aAlarmData.getString("eventTime"));
                        alarmInfoBuff.setEventLevel(aAlarmData.getString("eventLevel"));
                        alarmInfoBuff.setDevId(aAlarmData.getString("devId"));
                        alarmInfoBuff.setDevType(aAlarmData.getString("devType"));
                        alarmInfoBuff.setDevName(aAlarmData.getString("devName"));
                        alarmInfoBuff.setDevChannel(aAlarmData.getString("devChannel"));
                        String data=aAlarmData.getString("data");
                        alarmInfoBuff.setData(data);
                        mAlarmList.add(alarmInfoBuff);
                        String dataType=null;
                        if (!data.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                if(eventType.equals("10001")){
                                     dataType="regions";
                                    JSONArray alarmDatas = jsonObject.getJSONArray(dataType);
                                    JSONObject aData = null;

                                    for (int i = 0; i < alarmDatas.length(); i++) {
                                        aData = alarmDatas.getJSONObject(i);

                                    }
                                    String alarmType="";
                                    if(aData.getString("type ").equals("0")) {
                                        alarmType="点";
                                    }
                                    if(aData.getString("type ").equals("1")) {
                                        alarmType="线";
                                    }
                                    if(aData.getString("type ").equals("2")) {
                                        alarmType="区域";
                                    }

                                    if(Flag_10001) {
                                        diplayDetail = "设备编号为:" + aData.getString("num") + ",类型为:" + alarmType + "的设备出现火点报警,";
                                        Log.e(TAG, "Alarm diplayDetail:" + diplayDetail);
                                        Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                                        intent.putExtra("displayDetail", diplayDetail);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Flag_10001=false;
                                    }
                                }
                                if(eventType.equals("10002")){
                                     dataType="fireRegions";
                                    JSONArray alarmDatas = jsonObject.getJSONArray(dataType);
                                    JSONObject aData = null;

                                    for (int i = 0; i < alarmDatas.length(); i++) {
                                        aData = alarmDatas.getJSONObject(i);
                                    }
                                    if(aData.getString("x")!=null) {
                                        diplayDetail = "坐标X:" + aData.getString("x") + ",坐标Y:" + aData.getString("y") + "width:" + aData.getString("width") + ",height:" + aData.getString("height")
                                                + "的设备出现火点报警,";
                                        Log.e(TAG, "Alarm diplayDetail:" + diplayDetail);
                                        if (Flag_10002) {
                                            Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                                            intent.putExtra("displayDetail", diplayDetail);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            Flag_10002 = false;
                                        }
                                    }
                                }
                                if(eventType.equals("10003")){
                                     dataType="invadeRegions";
                                    JSONArray alarmDatas = jsonObject.getJSONArray(dataType);
                                    JSONObject aData = null;
                                    for (int i = 0; i < alarmDatas.length(); i++) {
                                        aData = alarmDatas.getJSONObject(i);
                                        aData.getString("num");
                                    }
                                    diplayDetail="编号为"+aData.getString("num")+"的设备出现区域入侵";
                                    Log.e(TAG,"Alarm diplayDetail:"+diplayDetail);

                                    if(Flag_10003) {
                                        Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                                        intent.putExtra("displayDetail", diplayDetail);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Flag_10003=false;
                                    }
                                }
                                if(eventType.equals("10004")){
                                     dataType="lines";
                                    JSONArray alarmDatas = jsonObject.getJSONArray(dataType);
                                    JSONObject aData = null;
                                    for (int i = 0; i < alarmDatas.length(); i++) {
                                        aData = alarmDatas.getJSONObject(i);
                                        aData.getString("num");
                                        aData.getString("direction");
                                    }
                                    String direction="";
                                    if(aData.getString("direction").equals("0")){
                                        direction="双向";
                                    }
                                    if(aData.getString("direction").equals("1")){
                                        direction="A->B";
                                    }
                                    if(aData.getString("direction").equals("2")){
                                        direction="B->A";

                                    }

                                    diplayDetail="编号为"+aData.getString("num")+"的设备出现拌线报警," +
                                            "方向为："+direction;
                                    Log.e(TAG,"Alarm diplayDetail:"+diplayDetail);

                                    if(Flag_10004) {
                                        Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                                        intent.putExtra("displayDetail", diplayDetail);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Flag_10004=false;
                                    }
                                }
                                if(eventType.equals("10005")){
                                     dataType="faces";
                                    JSONArray alarmDatas = jsonObject.getJSONArray(dataType);
                                    JSONObject aData = null;

                                    for (int i = 0; i < alarmDatas.length(); i++) {
                                        aData = alarmDatas.getJSONObject(i);
                                        aData.getString("name");
                                        aData.getString("age");
                                    }
                                    diplayDetail="体温为："+aData.getString("temp")+"的用户";
                                    Log.e(TAG,"Alarm diplayDetail:"+diplayDetail);

                                    if(Flag_10005) {
                                        Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                                        intent.putExtra("displayDetail", diplayDetail);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Flag_10005=false;
                                    }
                                }

                                showNotifictionIcon(getApplicationContext(),diplayDetail);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        //开启消费者线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    /*Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    intent.putExtra("displayDetail", "人脸识别报警测试中");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                basicConsume(incomingMessageHandler);
            }
        }).start();
    }
    // 每次启动Servcie时都会调用该方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("--Alarm Service onStartCommand()--");
        return super.onStartCommand(intent, flags, startId);
    }

    // 解绑Servcie调用该方法
    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("--Alarm Service onUnbind()--");
        return super.onUnbind(intent);
    }

    // 退出或者销毁时调用该方法
    @Override
    public void onDestroy() {
        serviceRunning = false;
        System.out.println("--Alarm Service onDestroy()--");
        super.onDestroy();
    }
    /**
     * 连接设置
     */
    private void setupConnectionFactory() {
        //172.16.20.5的参数信息
        /*factory.setHost("172.16.20.5");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");*/

        //172.16.20.48的参数信息
        factory.setHost("172.16.20.48");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("IRaytek567");

        factory.setVirtualHost("eventCenter");
    }

    /**
     * 收消息（从发布者那边订阅消息）
     */
    private void basicConsume(final Handler handler){

        try {
            //连接
            Connection connection = factory.newConnection() ;
            //通道
            final Channel channel = connection.createChannel() ;

            //消费者声明队列
            channel.queueDeclare("alarm_queue", false, false, false, null);
            //消费者队列绑定 路由
            if(routingKeys!=null){
            for(int i=0;i<routingKeys.size();i++) {
                Log.e(TAG,"routingKeys.get(i):"+routingKeys.get(i));
                channel.queueBind("alarm_queue", "eventRecord", routingKeys.get(i));

            }
            }else{
                Log.w(TAG,"AlarmService  error for network.");
            }

            //消费者监听消息
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                //重写监听方法
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String msg = new String(body,"UTF-8");
                    Log.w(TAG,"生产者消息:"+msg);
                    //从message池中获取msg对象更高效
                    Message uimsg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", msg);
                    uimsg.setData(bundle);
                    handler.sendMessage(uimsg);

                }
            };

            channel.basicConsume("alarm_queue",true, defaultConsumer);   //绑定队列 事件监听

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void showNotifictionIcon(Context context,String displayDetail) {
        Notification notification = null;
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String id = "channelId";
            String name = "channelName";
            NotificationChannel channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setContentTitle("报警提示")
                    .setContentText(displayDetail)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
        }else{
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("报警提示")
                    .setContentText(displayDetail)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .build();
        }
        manager.notify(1,notification);

    }

}

