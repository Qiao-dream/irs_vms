package com.iray.irs_vms.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;
import com.iray.irs_vms.httpUtils.RabbitMqUtils;
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

public class RabbitMqActivity extends AppCompatActivity {
    private static final String TAG ="RabbitMqActivity" ;
    List<String> routingKeys;
    public ConnectionFactory factory =  new ConnectionFactory();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbitmq);

        new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                String mqDeatil = RabbitMqUtils.getRoutingKey(Common.ACCESS_TOKEN);
                Log.w(TAG,"mqDeatil:"+mqDeatil);
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

        //连接设置
        setupConnectionFactory();
        //用于从线程中获取数据，更新ui
        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                TextView tv = (TextView) findViewById(R.id.txtrabbitmqt);
                tv.append(message + '\n');
                Log.i(TAG, "msg:" + message);
            }
        };
        //开启消费者线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                basicConsume(incomingMessageHandler);
            }
        }).start();
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
            for(int i=0;i<routingKeys.size();i++) {
                Log.e(TAG,"routingKeys.get(i):"+routingKeys.get(i));
                channel.queueBind("alarm_queue", "eventRecord", routingKeys.get(i));
               /* channel.queueBind("alarm_queue", "eventRecord", "event.*.1400367067399458818.#");
                channel.queueBind("alarm_queue", "eventRecord", "event.*.1412956161371541506.#");
                channel.queueBind("alarm_queue", "eventRecord", "event.*.1413026543860453378.#");
                channel.queueBind("alarm_queue", "eventRecord", "event.*.1407233667599699970.#");*/
            }
            //消费者监听消息
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                //重写监听方法
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String msg = new String(body,"UTF-8");
                    Log.w(TAG,"邮件消费者获取生产者消息:"+msg);
                }
            };

            channel.basicConsume("alarm_queue",true, defaultConsumer);   //绑定队列 事件监听

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
