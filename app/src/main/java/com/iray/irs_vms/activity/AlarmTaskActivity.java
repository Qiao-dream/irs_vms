package com.iray.irs_vms.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;
import com.shehuan.niv.NiceImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.iray.irs_vms.httpUtils.AlarmUploadUtils.SaveDangers;
import static com.iray.irs_vms.httpUtils.AlarmUploadUtils1.FileConventer;

public class AlarmTaskActivity extends BaseActivity {
    public static String TAG="AlarmTaskActivity";
    private NiceImageView ivAlarmAvatar;
    private NiceImageView ivAlarmAvatar1;
    private NiceImageView ivAlarmAvatar2;
    private Button alarm_upload_btn_close;
    private Button alarm_button;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int CROP_SMALL_PICTURE1 = 3;
    private static final int CROP_SMALL_PICTURE2 = 4;
    private Bitmap mBitmap;
    private int choice=0;
    private EditText etPosition,dangerDescrip,handleOpionion;
    File file;
    File file1;
    File file2;
    String id="";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_task);
        init();
        Intent intent1 = getIntent();
        id=intent1.getStringExtra("id");
        Log.w("InspectListOfflineAdapter","id1:_______________________________"+id);
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
        ivAlarmAvatar = (NiceImageView) findViewById(R.id.iv_alarm_avatar);
        ivAlarmAvatar.setOnClickListener(mOnClickListener);
        ivAlarmAvatar1 = (NiceImageView) findViewById(R.id.iv_alarm_avatar1);
        ivAlarmAvatar1.setOnClickListener(mOnClickListener);
        ivAlarmAvatar2 = (NiceImageView) findViewById(R.id.iv_alarm_avatar2);
        ivAlarmAvatar2.setOnClickListener(mOnClickListener);
        alarm_upload_btn_close=(Button)findViewById(R.id.alarm_upload_btn_close);
        alarm_upload_btn_close.setOnClickListener(mOnClickListener);
        alarm_button=(Button)findViewById(R.id.alarm_button);
        alarm_button.setOnClickListener(mOnClickListener);
        etPosition=findViewById(R.id.et_position);
        dangerDescrip=findViewById(R.id.et_position);
        handleOpionion=findViewById(R.id.et_position);
    }
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_alarm_avatar:
                    choice=0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlarmTaskActivity.this);
                    builder.setTitle("添加图片");
                    String[] items = { "选择本地照片", "拍照" };
                    builder.setNegativeButton("取消", null);
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // 选择本地照片
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // 拍照
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image.jpg"));
                                    // 将拍照所得的相片保存到SD卡根目录
                                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                    break;
                            }
                        }
                    });
                    builder.show();
                    break;
                case R.id.iv_alarm_avatar1:
                    choice=1;
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AlarmTaskActivity.this);
                    builder1.setTitle("添加图片");
                    String[] items1 = { "选择本地照片", "拍照" };
                    builder1.setNegativeButton("取消", null);
                    builder1.setItems(items1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // 选择本地照片
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // 拍照
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image1.jpg"));
                                    // 将拍照所得的相片保存到SD卡根目录
                                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                    break;
                            }
                        }
                    });
                    builder1.show();
                    break;
                case R.id.iv_alarm_avatar2:
                    choice=2;
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AlarmTaskActivity.this);
                    builder2.setTitle("添加图片");
                    String[] items2 = { "选择本地照片", "拍照" };
                    builder2.setNegativeButton("取消", null);
                    builder2.setItems(items2, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // 选择本地照片
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // 拍照
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image2.jpg"));
                                    // 将拍照所得的相片保存到SD卡根目录
                                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                    break;
                            }
                        }
                    });
                    builder2.show();
                    break;
                case R.id.alarm_upload_btn_close:
                    finish();
                    break;
                case R.id.alarm_button:
                    //通过接口上传数据

                    final String location = etPosition.getText().toString().trim();
                    final String dangerDescrip = handleOpionion.getText().toString().trim();
                    final String suggestion = handleOpionion.getText().toString().trim();
                   //http://172.16.20.48/gateway/swagger-ui.html?urls.primaryName=file#/file-controller/uploadUsingPOST 转图片地址
                    if(location.equals("")||dangerDescrip.equals("")||suggestion.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "请填写详细的描述信息", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                            file= new File(Environment
                                    .getExternalStorageDirectory(), "temp_image.jpg");
                            file1= new File(Environment
                                    .getExternalStorageDirectory(), "temp_image1.jpg");
                            file2= new File(Environment
                                    .getExternalStorageDirectory(), "temp_image2.jpg");
                            Log.w(TAG,"File:"+file);
                            String fileConventerStr = FileConventer(Common.ACCESS_TOKEN, file);
                            String fileConventerStr1 = FileConventer(Common.ACCESS_TOKEN, file1);
                            String fileConventerStr2 = FileConventer(Common.ACCESS_TOKEN, file2);
                            try {
                                JSONObject jsonObject = null;
                                jsonObject = new JSONObject(fileConventerStr);
                                JSONObject json = new JSONObject();
                                String url = jsonObject.getString("url");

                                JSONObject jsonObject1 = null;
                                jsonObject1 = new JSONObject(fileConventerStr1);
                                JSONObject json1 = new JSONObject();
                                String url1 = jsonObject1.getString("url");

                                JSONObject jsonObject2 = null;
                                jsonObject2 = new JSONObject(fileConventerStr2);
                                JSONObject json2 = new JSONObject();
                                String url2 = jsonObject2.getString("url");
                                //   json.put("pollingId", "31"); //如果不关联巡检任务去掉该字段
                                Log.w(TAG,"url:"+url+","+url1+","+url2);
                                Log.w(TAG,"location:"+location);
                                Log.w(TAG,"dangerDescription:"+dangerDescrip);
                                Log.w(TAG,"suggestion:"+suggestion);
                                Log.w(TAG,"pollingId:"+id);

                                json.put("url", url+","+url1+","+url2);
                                json.put("pollingId", id);
                                json.put("location", location);
                                json.put("dangerDescription", dangerDescrip);
                                json.put("suggestion", suggestion);
                                json.put("hiddenDanger", "123");
                                //  json.put("tag", "1");   //如果不关联巡检任务该字段值为1.关联去掉该字段
                                String alarmResult = SaveDangers(Common.ACCESS_TOKEN, json.toString());
                                Log.i(TAG, "alarmResult:" + alarmResult);

                                JSONObject jsonObject3 = null;
                                jsonObject3 = new JSONObject( alarmResult);
                                String msg = jsonObject3.getString("msg");
                                if(msg.equals("操作成功")){
                                    //解决在子线程中调用Toast的异常情况处理
                                    Looper.prepare();
                                  //  Toast.makeText(getApplicationContext(),"任务上报成功",Toast.LENGTH_LONG).show();
                                    Toast toast = Toast.makeText(getApplicationContext(), "任务已经上报成功", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    Looper.loop();
                                   //可以创建一个新的SharedPreference来对储存的文件进行操作
                                    SharedPreferences sp=getApplicationContext().getSharedPreferences("名称", Context.MODE_PRIVATE);
                                  //像SharedPreference中写入数据需要使用Editor
                                    SharedPreferences.Editor editor;
                                    editor = sp.edit();
                                    //类似键值对
                                  /*  editor.putString("name", "string");
                                    editor.putInt("age", 0);
                                    editor.putBoolean("read", true);*/
                                   //editor.apply();
                                    editor.commit();
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();

                default:
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                case CROP_SMALL_PICTURE1:
                    if (data != null) {
                        setImageToView1(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                case CROP_SMALL_PICTURE2:
                    if (data != null) {
                        setImageToView2(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        if(choice==0) {
            startActivityForResult(intent, CROP_SMALL_PICTURE);
        }else if(choice==1){
            startActivityForResult(intent, CROP_SMALL_PICTURE1);
        }else if(choice==2){
            startActivityForResult(intent, CROP_SMALL_PICTURE2);
        }
    }


    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形
            ivAlarmAvatar.setImageBitmap(mBitmap);//显示图片
            saveBitmapToSD(mBitmap,Environment
                    .getExternalStorageDirectory()+"/temp_image.jpg");

        }
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView1(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形
            ivAlarmAvatar1.setImageBitmap(mBitmap);//显示图片
            saveBitmapToSD(mBitmap,Environment
                    .getExternalStorageDirectory()+"/temp_image1.jpg");

        }
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView2(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形
            ivAlarmAvatar2.setImageBitmap(mBitmap);//显示图片

            saveBitmapToSD(mBitmap,Environment
                    .getExternalStorageDirectory()+"/temp_image2.jpg");

        }
    }

    /**
     * 保存bitmap到sd卡
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean saveBitmapToSD(Bitmap bitmap, String path)
    {
        boolean flag = false;
        if(bitmap == null || path == null || path.length() == 0)
            return flag;
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            Log.w(TAG,"Picture path:"+path);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fos != null)
            {
                flag = true;
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

}
