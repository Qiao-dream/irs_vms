package com.iray.irs_vms.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.iray.irs_vms.R;

public class TestNotifyActivity extends BaseActivity {
    Notification notification = null;
    //BaseNotification
    private Button bt01;

    //UpdateBaseNotification
    private Button bt02;

    //ClearBaseNotification
    private Button bt03;

    //MediaNotification
    private Button bt04;

    //ClearMediaNotification
    private Button bt05;

    //ClearALL
    private Button bt06;

    //CustomNotification
    private Button bt07;

    //通知管理器
    private NotificationManager nm;

    //通知显示内容
    private PendingIntent pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*加载页面*/
        setContentView(R.layout.lesson10);


        init();
    }

    private void init() {
        bt01 = (Button)findViewById(R.id.le10bt01);
        bt02 = (Button)findViewById(R.id.le10bt02);
        bt03 = (Button)findViewById(R.id.le10bt03);
        bt04 = (Button)findViewById(R.id.le10bt04);
        bt05 = (Button)findViewById(R.id.le10bt05);
        bt06 = (Button)findViewById(R.id.le10bt06);
        bt07 = (Button)findViewById(R.id.le10bt07);

        bt01.setOnClickListener(onclick);
        bt02.setOnClickListener(onclick);
        bt03.setOnClickListener(onclick);
        bt04.setOnClickListener(onclick);
        bt05.setOnClickListener(onclick);
        bt06.setOnClickListener(onclick);
        bt07.setOnClickListener(onclick);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this,TestNotifyActivity.class);

        pd = PendingIntent.getActivity(TestNotifyActivity.this, 0, intent, 0);
    }

    View.OnClickListener onclick = new View.OnClickListener() {

        //BASE Notification ID
        private int Notification_ID_BASE = 110;

        private Notification baseNF;

        //Notification ID
        private int Notification_ID_MEDIA = 119;

        private Notification mediaNF;

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.le10bt01:

                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        String id = "channelId";
                        String name = "channelName";
                        NotificationChannel channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW);
                        manager.createNotificationChannel(channel);
                        notification = new Notification.Builder(TestNotifyActivity.this)
                                .setChannelId(id)
                                .setContentTitle("通知栏消息")
                                .setContentText("这只是一个测试，请忽略")
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)

                                .build();
                    }else{
                        notification = new NotificationCompat.Builder(TestNotifyActivity.this)
                                .setContentTitle("通知栏消息")
                                .setContentText("这只是一个测试，请忽略")
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)

                                .build();
                    }
                    manager.notify(1,notification);
                    break;

                case R.id.le10bt02:

                    mediaNF = new Notification.Builder(getApplicationContext())
                            .setContentTitle("标题")
                            .setContentText("内容")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();

                    nm.notify(1,mediaNF);

                    break;

                case R.id.le10bt03:

                    //清除 baseNF
                    nm.cancel(Notification_ID_BASE);
                    break;

                case R.id.le10bt04:

                    break;

                case R.id.le10bt05:
                    //清除 mediaNF
                    nm.cancel(Notification_ID_MEDIA);
                    break;

                case R.id.le10bt06:
                    nm.cancelAll();
                    break;

                case R.id.le10bt07:
                    //自定义下拉视图，比如下载软件时，显示的进度条。
                    Notification notification = new Notification();

                    notification.icon = R.drawable.icon_key;
                    notification.tickerText = "Custom!";

                    RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom);
                    contentView.setImageViewResource(R.id.image, R.drawable.icon_key);
                    contentView.setTextViewText(R.id.text, "Hello, this message is in a custom expanded view");
                    notification.contentView = contentView;

                    //使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法
                    //但是必须定义 contentIntent
                    notification.contentIntent = pd;

                    nm.notify(3, notification);
                    break;
            }
        }
    };


}