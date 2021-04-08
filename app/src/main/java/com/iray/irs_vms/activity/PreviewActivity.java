package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iray.irs_vms.R;
import com.iray.irs_vms.widget.VideoPlayerFragment;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Util;

import java.lang.ref.WeakReference;

public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = "PreviewActivity";
    private VideoPlayerFragment fragment;
    private PreviewHandler mPreviewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        fragment = new VideoPlayerFragment();
        mPreviewHandler = new PreviewHandler(this);


        try {
            EventHandler em = EventHandler.getInstance();
            em.addHandler(mPreviewHandler);
            LibVLC mLibVLC = Util.getLibVlcInstance();
            if (mLibVLC != null) {
                mLibVLC.setSubtitlesEncoding("");
                mLibVLC.setTimeStretching(false);
                mLibVLC.setFrameSkip(true);
                mLibVLC.setChroma("RV32");
                mLibVLC.setVerboseMode(true);
                mLibVLC.setAout(-1);
                mLibVLC.setDeblocking(4);
                mLibVLC.setNetworkCaching(1500);
                //测试地址
//                String pathUri = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
                String pathUri = "rtsp://10.10.25.42:554/stream0";
                mLibVLC.playMyMRL(pathUri);
            }
        } catch (LibVlcException e) {
            e.printStackTrace();
        }
    }


    private static class PreviewHandler extends Handler{
        private WeakReference<PreviewActivity> reference;
        private PreviewActivity previewActivity;
//        private PreviewHandler(){
//            super();
//        }
        private PreviewHandler(PreviewActivity activity){
            reference = new WeakReference<PreviewActivity>(activity);
            previewActivity = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "Event = " + msg.getData().getInt("event"));
            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaPlayerPlaying:
                case EventHandler.MediaPlayerPaused:
                    break;
                case EventHandler.MediaPlayerStopped:
                    break;
                case EventHandler.MediaPlayerEndReached:
                    AlertDialog dialog = new AlertDialog.Builder(previewActivity)
                            .setTitle("提示信息")
                            .setMessage("无法获取摄像机图像，请确保手机网络畅通！")
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    previewActivity.finish();
                                }
                            }).create();
                    dialog.setCanceledOnTouchOutside(false);
                    break;
                case EventHandler.MediaPlayerVout:
                    if (msg.getData().getInt("data") > 0) {
                        FragmentTransaction transaction = previewActivity.getFragmentManager().beginTransaction();
                        Log.i("aaa", "*********transaction:" + transaction + ";" + previewActivity.fragment);
                        if (previewActivity.fragment != null) {
                            transaction.add(R.id.preview_layout, previewActivity.fragment);
                            transaction.commit();
//                           initCC();
//                            flag_pic_ok = true;
//                            set_enable_button();
                        }
                    }
                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    break;
                case EventHandler.MediaPlayerEncounteredError:
                    AlertDialog dialog1 = new AlertDialog.Builder(reference.get())
                            .setTitle("提示信息")
                            .setMessage("无法连接到网络摄像头，请确保手机已经连接到摄像头所在的wifi热点")
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    previewActivity.finish();
                                }
                            }).create();
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.show();
                    break;
                default:
                    Log.d(TAG, "Event not handled ");
                    break;
            }
        }

    }


}
