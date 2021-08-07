package com.iray.irs_vms.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.iray.irs_vms.httpUtils.AlarmUploadUtils.batchUpdate;

public class AlarmInfoActivity extends BaseActivity {
    private ImageView ivVideo,ivFire;
    private Spinner spHandle;
    private ArrayAdapter<String> handleMethodAdapter;
    String rtsp;
    String id;
    String eventType="";
    private TextView tvFire;
    private EditText etHandleprocess;
    private Button btnAlarmConfirm;
    private static String TAG="AlarmUploadUtils";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_info);

        //通过Activity.getIntent()获取当前页面接收到的Intent。
        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        rtsp = intent.getStringExtra("rtsp");
        id= intent.getStringExtra("id");
        eventType= intent.getStringExtra("eventType");
        Log.w(TAG,"eventType----------:"+eventType);
        final Spinner handleSpinner = (Spinner) findViewById(R.id.sp_handle);
        String[] cates=new String[]{
                "处理","报警","上报",  "转交","误报","其他"
        };
        handleMethodAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cates);
        handleMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        handleSpinner.setAdapter(handleMethodAdapter);
        init();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (Build.VERSION.SDK_INT >= 23){
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

    }

    private void init(){
        initView();
    }
    private void initView(){
        ivVideo=findViewById(R.id.iv_video);
        btnAlarmConfirm=findViewById(R.id.btn_alarm_confirm);
        btnAlarmConfirm.setOnClickListener(mOnClickListener);
        ivVideo.setOnClickListener(mOnClickListener);
        etHandleprocess=findViewById(R.id.et_handleprocess);
        spHandle=findViewById(R.id.sp_handle);
        ivFire=findViewById(R.id.iv_fire);
        tvFire=findViewById(R.id.tv_fire);
        if(eventType.equals("10001")){
            ivFire.setImageResource(R.drawable.btn_alarm_fire);
            tvFire.setText("火点报警");
        }
        if(eventType.equals("10002")){
            ivFire.setImageResource(R.drawable.btn_alarm_banxian);
            tvFire.setText("拌线报警");
        }
        if(eventType.equals("10003")){
            ivFire.setImageResource(R.drawable.btn_alarm_ruqin);
            tvFire.setText("入侵报警");
        } if(eventType.equals("10004")){
            ivFire.setImageResource(R.drawable.btn_alarm_temp);
            tvFire.setText("温度报警");
        }
        if(eventType.equals("10005")){
            ivFire.setImageResource(R.drawable.btn_alarm_stranger);
            tvFire.setText("陌生人报警");
        }
        if(eventType.equals("10006")){
            ivFire.setImageResource(R.drawable.btn_alarm_humantemp);
            tvFire.setText("体温报警");
        }
        if(eventType.equals("10007")){
            ivFire.setImageResource(R.drawable.btn_alarm_mask);
            tvFire.setText("紧急报警");
        }
        if(eventType.equals("10008")){
            ivFire.setImageResource(R.drawable.btn_alarm_other);
            tvFire.setText("其他报警");
        }



    }
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_video:
                    Intent intent = new Intent(AlarmInfoActivity.this, PreviewActivity.class);
                    Message msg = new Message();
                    intent.putExtra("id", "1");
                    intent.putExtra("org", "报警");
                    intent.putExtra("name", "预览");
                    intent.putExtra("rtsp", rtsp);
                    AlarmInfoActivity.this.startActivity(intent);
                    break;

                case R.id.btn_alarm_confirm:
                    //处理报警信息
                    String comment = etHandleprocess.getText().toString();
                    int status=spHandle.getSelectedItemPosition();
                    if(comment.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "请填写详细的处理信息", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONArray array =new JSONArray();
                    JSONObject json =new JSONObject();
                    try {
                        json.put("comment", comment);
                        json.put("id", id);
                        json.put("status", status);
                        array.put(json);
                        System.out.println(array.toString());
                    }catch (Exception e){
                    }
                    String alarmResult = batchUpdate(Common.ACCESS_TOKEN, array.toString());
                    Log.i(TAG, "alarmResult:" + alarmResult);
                    JSONObject jsonObject3 = null;
                    try {
                        jsonObject3 = new JSONObject( alarmResult);
                        String msg = jsonObject3.getString("resp_msg");
                        if(msg.equals("修改成功")) {
                            //解决在子线程中调用Toast的异常情况处理
                            Looper.prepare();
                            //  Toast.makeText(getApplicationContext(),"任务上报成功",Toast.LENGTH_LONG).show();
                            Toast toast = Toast.makeText(getApplicationContext(), "报警信息处理成功", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            Looper.loop();

                        }else{
                            //解决在子线程中调用Toast的异常情况处理
                            Looper.prepare();
                            //  Toast.makeText(getApplicationContext(),"任务上报成功",Toast.LENGTH_LONG).show();
                            Toast toast = Toast.makeText(getApplicationContext(), "报警信息处理失败，请重新处理", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            Looper.loop();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                }).start();
            break;
                default:
                    break;
            }
        }
    };
}
