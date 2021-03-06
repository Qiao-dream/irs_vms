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
            //????????????????????????
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
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
                    builder.setTitle("????????????");
                    String[] items = { "??????????????????", "??????" };
                    builder.setNegativeButton("??????", null);
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // ??????????????????
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //???startActivityForResult????????????????????????onActivityResult()????????????????????????????????????
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // ??????
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image.jpg"));
                                    // ?????????????????????????????????SD????????????
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
                    builder1.setTitle("????????????");
                    String[] items1 = { "??????????????????", "??????" };
                    builder1.setNegativeButton("??????", null);
                    builder1.setItems(items1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // ??????????????????
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //???startActivityForResult????????????????????????onActivityResult()????????????????????????????????????
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // ??????
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image1.jpg"));
                                    // ?????????????????????????????????SD????????????
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
                    builder2.setTitle("????????????");
                    String[] items2 = { "??????????????????", "??????" };
                    builder2.setNegativeButton("??????", null);
                    builder2.setItems(items2, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // ??????????????????
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //???startActivityForResult????????????????????????onActivityResult()????????????????????????????????????
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // ??????
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image2.jpg"));
                                    // ?????????????????????????????????SD????????????
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
                    //????????????????????????

                    final String location = etPosition.getText().toString().trim();
                    final String dangerDescrip = handleOpionion.getText().toString().trim();
                    final String suggestion = handleOpionion.getText().toString().trim();
                   //http://172.16.20.48/gateway/swagger-ui.html?urls.primaryName=file#/file-controller/uploadUsingPOST ???????????????
                    if(location.equals("")||dangerDescrip.equals("")||suggestion.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "??????????????????????????????", Toast.LENGTH_LONG);
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
                                //   json.put("pollingId", "31"); //??????????????????????????????????????????
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
                                //  json.put("tag", "1");   //??????????????????????????????????????????1.?????????????????????
                                String alarmResult = SaveDangers(Common.ACCESS_TOKEN, json.toString());
                                Log.i(TAG, "alarmResult:" + alarmResult);

                                JSONObject jsonObject3 = null;
                                jsonObject3 = new JSONObject( alarmResult);
                                String msg = jsonObject3.getString("msg");
                                if(msg.equals("????????????")){
                                    //???????????????????????????Toast?????????????????????
                                    Looper.prepare();
                                  //  Toast.makeText(getApplicationContext(),"??????????????????",Toast.LENGTH_LONG).show();
                                    Toast toast = Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    Looper.loop();
                                   //????????????????????????SharedPreference?????????????????????????????????
                                    SharedPreferences sp=getApplicationContext().getSharedPreferences("??????", Context.MODE_PRIVATE);
                                  //???SharedPreference???????????????????????????Editor
                                    SharedPreferences.Editor editor;
                                    editor = sp.edit();
                                    //???????????????
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
                    cutImage(tempUri); // ???????????????????????????
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // ???????????????????????????
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // ??????????????????????????????????????????????????????
                    }
                    break;
                case CROP_SMALL_PICTURE1:
                    if (data != null) {
                        setImageToView1(data); // ??????????????????????????????????????????????????????
                    }
                    break;
                case CROP_SMALL_PICTURE2:
                    if (data != null) {
                        setImageToView2(data); // ??????????????????????????????????????????????????????
                    }
                    break;
            }
        }
    }
    /**
     * ????????????????????????
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP??????action???????????????????????????
        intent.setDataAndType(uri, "image/*");
        // ????????????
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ?????????????????????
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
     * ?????????????????????????????????
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //??????????????????????????????????????????????????????????????????
            ivAlarmAvatar.setImageBitmap(mBitmap);//????????????
            saveBitmapToSD(mBitmap,Environment
                    .getExternalStorageDirectory()+"/temp_image.jpg");

        }
    }
    /**
     * ?????????????????????????????????
     */
    protected void setImageToView1(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //??????????????????????????????????????????????????????????????????
            ivAlarmAvatar1.setImageBitmap(mBitmap);//????????????
            saveBitmapToSD(mBitmap,Environment
                    .getExternalStorageDirectory()+"/temp_image1.jpg");

        }
    }
    /**
     * ?????????????????????????????????
     */
    protected void setImageToView2(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //??????????????????????????????????????????????????????????????????
            ivAlarmAvatar2.setImageBitmap(mBitmap);//????????????

            saveBitmapToSD(mBitmap,Environment
                    .getExternalStorageDirectory()+"/temp_image2.jpg");

        }
    }

    /**
     * ??????bitmap???sd???
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
