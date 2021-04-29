package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iray.irs_vms.R;

import com.iray.irs_vms.utils.DisplayUtil;
import com.iray.irs_vms.widget.MVlCVidoLayout;
import com.squareup.okhttp.internal.Util;


import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = "PreviewActivity";
//    private VideoPlayerFragment fragment;
//    private PreviewHandler mPreviewHandler;
    public static Context mContext;
    private VLCVideoLayout video_layout;
    LibVLC  mLibVLC;
    private RelativeLayout previewLayout;
    private MediaPlayer mMediaPlayer = null;
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    public static boolean flag_pic_ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);


//        fragment = new VideoPlayerFragment();
//        mPreviewHandler = new PreviewHandler(this);

        mContext = this;

        init();


//        try {
//            EventHandler em = EventHandler.getInstance();
//            em.addHandler(mPreviewHandler);
//            LibVLC mLibVLC = Util.getLibVlcInstance();
//            if (mLibVLC != null) {
//                mLibVLC.setSubtitlesEncoding("");
//                mLibVLC.setTimeStretching(false);
//                mLibVLC.setFrameSkip(true);
//                mLibVLC.setChroma("RV32");
//                mLibVLC.setVerboseMode(true);
//                mLibVLC.setAout(-1);
//                mLibVLC.setDeblocking(4);
//                mLibVLC.setNetworkCaching(1500);
//                //测试地址
////                String pathUri = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
//                String pathUri = "rtsp://10.10.25.42:554/stream0";
//                mLibVLC.playMyMRL(pathUri);
//            }
//        } catch (LibVlcException e) {
//            e.printStackTrace();
//        }
    }

    private void init() {
        initView();

    }


    private void initView() {
        findView();

//        ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) previewLayout.getLayoutParams();
//        layoutParams.width = DisplayUtil.getScreenWidth(mContext);
//        layoutParams.height = layoutParams.width *288 / 384;
//        previewLayout.setLayoutParams(layoutParams);

//        ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) video_layout.getLayoutParams();
//        layoutParams.width = DisplayUtil.getScreenWidth(mContext);
//        layoutParams.height = layoutParams.width *288 / 384;
//        video_layout.setLayoutParams(layoutParams);


    }


    private void findView() {
        video_layout = findViewById(R.id.video_layout);
//        previewLayout = findViewById(R.id.preview_layout);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initCamera_vlc();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
        finish();
    }

    private void initCamera_vlc() {
        try {

            Log.i("aaa", "************111111111******");
            final ArrayList<String> args = new ArrayList<>();//VLC参数
            args.add("--rtsp-tcp");//强制rtsp-tcp，加快加载视频速度
            args.add("--live-caching=0");
            args.add("--file-caching=0");
            args.add("--network-caching=0");//增加实时性，延时大概2-3秒
            mLibVLC = new LibVLC(mContext, args);
            mMediaPlayer = new MediaPlayer(mLibVLC);


//            RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams) video_layout.getLayoutParams();
//            layoutParams.width = width;
//            layoutParams.height = height;
//            video_layout.requestLayout();
//            video_layout.setLayoutParams(layoutParams);
//            mMediaPlayer.getVLCVout().setWindowSize(width,height);
//            mMediaPlayer.setAspectRatio( "384:288");//设置屏幕比例
//            mMediaPlayer.setScale(0);
//            mMediaPlayer.getVLCVout().setWindowSize(384,288);
//            mMediaPlayer.setAspectRatio( "384:288");//设置屏幕比例
//            mMediaPlayer.setScale(0);


            init_vlc_eventListener();

            String pathUri =  "rtsp://admin:admin@10.10.25.37:554/cam/realmonitor?channel=1&subtype=0";
            Uri uri = Uri.parse(pathUri);//rtsp流地址或其他流地址
            // Uri uri = Uri.parse("rtsp://10.10.25.42:554/stream0");//rtsp流地址或其他流地址
            //    Uri uri = Uri.parse("rtsp://10.10.25.35:554/cam/realmonitor?channel=1&subtype=0");
            //final Media media = new Media(mLibVLC, getAssets().openFd(ASSET_FILENAME));
            Log.i("aaa", "************3333333333333******");
            final Media media = new Media(mLibVLC, uri);
            media.setHWDecoderEnabled(false, false);//设置后才可以录制和截屏
            mMediaPlayer.setMedia(media);
            media.release();


            mMediaPlayer.setScale(0);
            mMediaPlayer.getVLCVout().setWindowSize(384, 288);

            ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) video_layout.getLayoutParams();
            int width = DisplayUtil.getScreenWidth(mContext);
            int sh = DisplayUtil.getScreenHeight(mContext);
            int height = width * 288 / 384;
            Log.e("vlc", "width: "+width+"    height: "+height);
            layoutParams.width = width;
            layoutParams.height = height;
            layoutParams.bottomMargin = (sh-height)/2;
            video_layout.requestLayout();
//            video_layout.setLayoutParams(layoutParams);
            mMediaPlayer.setAspectRatio("384:288");//设置屏幕比例
            mMediaPlayer.attachViews(video_layout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
            mMediaPlayer.setVideoScale(MediaPlayer.ScaleType.SURFACE_BEST_FIT);

            mMediaPlayer.play();



        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aaa", "************LibVlcException******");
        }

    }


    private void init_vlc_eventListener() {
        //监听播放状态
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                if (event.type == MediaPlayer.Event.Opening) {
                    Log.d(TAG, "VLC Opening");
                    //  progressBar.setVisibility(View.VISIBLE);
                } else if (event.type == MediaPlayer.Event.Buffering) {
                    Log.d(TAG, "VLC Buffering：" + event.getBuffering());
                    if (event.getBuffering() >= 100) {
                        // progressBar.setVisibility(View.GONE);
                    } //else
                    //  progressBar.setVisibility(View.VISIBLE);
                }else if(event.type == MediaPlayer.Event.EndReached){
                    AlertDialog dialog = new AlertDialog.Builder(PreviewActivity.this)
                            .setTitle("提示信息")
                            .setMessage("无法获取摄像机图像，请确保手机网络畅通！")
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).create();
                    dialog.setCanceledOnTouchOutside(false);
                    if (!PreviewActivity.this.isFinishing()) {
                        dialog.show();
                    }
                }else if (event.type == MediaPlayer.Event.Playing) {
                    Log.d(TAG, "VLC Playing");
                    //   menu.setVisibility(View.VISIBLE);
                } else if (event.type == MediaPlayer.Event.Stopped) {
                    Log.d(TAG, "VLC Stopped");
                    // progressBar.setVisibility(View.GONE);
                } else if (event.type == MediaPlayer.Event.EncounteredError) {
                    Log.d(TAG, "VLC EncounteredError");
                    //   progressBar.setVisibility(View.GONE);
                    AlertDialog dialog1 = new AlertDialog.Builder(PreviewActivity.this)
                            .setTitle("提示信息")
                            .setMessage("无法连接到网络摄像头，请确保手机已经连接到摄像头所在的wifi热点")
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    flag_pic_ok = false;
//                                    set_disenable_button();
                                }
                            }).create();
                    dialog1.setCanceledOnTouchOutside(false);
                    if (!PreviewActivity.this.isFinishing()) {
                        dialog1.show();
                    }
                } else if (event.type == MediaPlayer.Event.Vout) {
                    Log.d(TAG, "VLC Vout:" + event.getVoutCount());
                    //    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                        Log.i("aaa", "*********transaction:" + transaction + ";" + fragment);
//                        if (fragment != null) {
//                            transaction.add(R.id.frame_layout, fragment);
//                            transaction.commit();
                    flag_pic_ok = true;
//                    set_enable_button();
                    getVideoInfo(mContext,mMediaPlayer);
//                        }
                } else if (event.type == MediaPlayer.Event.RecordChanged) {
                    Log.d(TAG, "VLC RecordChanged");
                }
            }
        });
    }

    public  String getVideoInfo(Context context, MediaPlayer mMediaPlayer1)
    {
        Media media = (Media)mMediaPlayer1.getMedia();
        Log.i("aaa","*********media:" + media.getTrackCount());
        for (int i=0; i<media.getTrackCount();i++) {
            if (media.getTrack(i).type == 1) {
                Log.i("aaa","*********media:11" );
                Media.VideoTrack videoTrack = (Media.VideoTrack) media.getTrack(i);

                Log.i("aaa","*********media:11:"+String.valueOf(videoTrack.width) + ":" + String.valueOf(videoTrack.height) );
                return String.valueOf(videoTrack.width) + ":" + String.valueOf(videoTrack.height);
            }else{
                Log.i("aaa","*********media:12" );
                return null;
            }
        }
        return  null;
    }


//    private void initCC(String img_size) {
//        // Media media = (Media)mMediaPlayer.getMedia();
//
//        originalWidth = Common.DEFAULT_WIDTH;//Integer.valueOf(img_size.split(":")[0]);//
//        originalHeight = Common.DEFAULT_HEIGHT;// Integer.valueOf(img_size.split(":")[1]);
//        int video_w= (originalWidth* ScreenHeight /originalHeight);
//        int video_h =ScreenHeight;
//        ViewGroup.LayoutParams params1 = fl_camera.getLayoutParams();
//        params1.width =video_w;
//        params1.height = video_h;
//        Log.i("aaa", "**********aaaa**video width:" + (originalWidth* ScreenHeight /originalHeight));
//        fl_camera.requestLayout();
//
//        mMediaPlayer.getVLCVout().setWindowSize(video_w,video_h);
//        mMediaPlayer.setAspectRatio( video_w +":" + video_h);//设置屏幕比例
//        mMediaPlayer.setScale(0);
//
//        drawWidth = ScreenHeight * originalWidth / originalHeight;
//        surfaceWidth = drawSurfaceView.getWidth();
//        surfaceHeight = drawSurfaceView.getHeight();
//        drawHeight = ScreenHeight;
//        widthOffset = (surfaceWidth - drawWidth) / 2;  //设置surfaceview与成像区域的左侧偏移量
//        Log.i(TAG, "drawWidth:" + drawWidth + ";surfaceWidth:" + surfaceWidth + ";widthOffset" + widthOffset);
//
//
//    }
//
//
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            Log.i(TAG, "********action:" + action);
//            switch (action) {
//
//                case Common.IMAGE_SIZE:
//                    String im_size = intent.getStringExtra(Common.IMAGE_SIZE_VALUE);
//                    initCC(im_size);
//                    break;
//
//            }
//        }
//
//    };

//    private static class PreviewHandler extends Handler {
//        private WeakReference<PreviewActivity> reference;
//        private PreviewActivity previewActivity;
//
//        //        private PreviewHandler(){
////            super();
////        }
//        private PreviewHandler(PreviewActivity activity) {
//            reference = new WeakReference<PreviewActivity>(activity);
//            previewActivity = reference.get();
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            Log.d(TAG, "Event = " + msg.getData().getInt("event"));
//            switch (msg.getData().getInt("event")) {
//                case EventHandler.MediaPlayerPlaying:
//                case EventHandler.MediaPlayerPaused:
//                    break;
//                case EventHandler.MediaPlayerStopped:
//                    break;
//                case EventHandler.MediaPlayerEndReached:
//                    AlertDialog dialog = new AlertDialog.Builder(previewActivity)
//                            .setTitle("提示信息")
//                            .setMessage("无法获取摄像机图像，请确保手机网络畅通！")
//                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    previewActivity.finish();
//                                }
//                            }).create();
//                    dialog.setCanceledOnTouchOutside(false);
//                    break;
//                case EventHandler.MediaPlayerVout:
//                    if (msg.getData().getInt("data") > 0) {
//                        FragmentTransaction transaction = previewActivity.getFragmentManager().beginTransaction();
//                        Log.i("aaa", "*********transaction:" + transaction + ";" + previewActivity.fragment);
//                        if (previewActivity.fragment != null) {
//                            transaction.add(R.id.preview_layout, previewActivity.fragment);
//                            transaction.commit();
////                           initCC();
////                            flag_pic_ok = true;
////                            set_enable_button();
//                        }
//                    }
//                    break;
//                case EventHandler.MediaPlayerPositionChanged:
//                    break;
//                case EventHandler.MediaPlayerEncounteredError:
//                    AlertDialog dialog1 = new AlertDialog.Builder(reference.get())
//                            .setTitle("提示信息")
//                            .setMessage("无法连接到网络摄像头，请确保手机已经连接到摄像头所在的wifi热点")
//                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    previewActivity.finish();
//                                }
//                            }).create();
//                    dialog1.setCanceledOnTouchOutside(false);
//                    dialog1.show();
//                    break;
//                default:
//                    Log.d(TAG, "Event not handled ");
//                    break;
//            }
//        }
//
//    }


}
