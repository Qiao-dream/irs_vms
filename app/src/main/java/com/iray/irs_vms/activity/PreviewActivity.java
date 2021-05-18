package com.iray.irs_vms.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iray.irs_vms.R;

import com.iray.irs_vms.httpUtils.DeviceManage;
import com.warkiz.widget.IndicatorSeekBar;


import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class PreviewActivity extends BaseActivity {
    private static final String TAG = "PreviewActivity";
    //    private VideoPlayerFragment fragment;
//    private PreviewHandler mPreviewHandler;
    public static Context mContext;
    private VLCVideoLayout video_layout;
    LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer = null;
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    public static boolean flag_pic_ok = false;

    private ConstraintLayout previewMainLayout;
    private FrameLayout previewLayout;
    public ProgressBar pbPreview;
    private ConstraintLayout previewListTopBar;
    private TextView previewTvTitle;
    private Button previewBtnClose;
    private TextView previewTvLoading;
    private Button btnShowSettingSheet;


    DeviceManage deviceManage;
    private PreviewHandler previewHandler;
    private static final int HANDLER_GOT_PREVIEW_SIZE = 2001;
    public static final int HANDLER_GOT_RTSP = 2002;

    private int previewWidth, previewHeight;
    public String deviceId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mContext = this;
        init();

    }

    private void init() {
        previewHandler = new PreviewHandler(this);
        deviceManage = DeviceManage.getInstance();
        deviceManage.setPreviewActivityWeakReference(this);
        Intent intent = getIntent();
        deviceId = Objects.requireNonNull(intent.getExtras()).getString("id");
        initView();


    }


    private void initView() {
        findView();
        initLayoutSize();
        setLayoutLister();

    }


    private void findView() {
        video_layout = findViewById(R.id.video_layout);
        previewLayout = findViewById(R.id.preview_layout);
        pbPreview = findViewById(R.id.pb_preview);
        previewMainLayout = findViewById(R.id.preview_main_layout);
        previewListTopBar = (ConstraintLayout) findViewById(R.id.preview_top_bar);
        previewTvTitle = (TextView) findViewById(R.id.preview_tv_title);
        previewBtnClose = (Button) findViewById(R.id.preview_btn_close);
        previewTvLoading = findViewById(R.id.tv_image_loading);
        btnShowSettingSheet = findViewById(R.id.btn_show_ps_setting_sheet);
//        mVideoSurfaceFrame = (FrameLayout) video_layout.findViewById(R.id.player_surface_frame);
//        mVideoTexture = mVideoSurfaceFrame.findViewById(R.id.texture_stub);
//        subtitles_surface_stub = mVideoSurfaceFrame.findViewById(R.id.subtitles_surface_stub);
//        surface_stub = mVideoSurfaceFrame.findViewById(R.id.surface_stub);

        btnShowSettingSheet.setOnClickListener(mOnClickListener);
    }

    private void initLayoutSize() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) previewListTopBar.getLayoutParams();
        layoutParams.height = statusBarHeight + (int) getResources().getDimension(R.dimen.top_bar_origin_height);
        previewListTopBar.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) previewMainLayout.getLayoutParams();
        layoutParams1.bottomMargin = navigationBarHeight;
//        previewMainLayout.setLayoutParams(layoutParams1);
    }

    private void setLayoutLister() {
        ViewTreeObserver vto = previewLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                previewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                previewWidth = previewLayout.getWidth();
                previewHeight = previewLayout.getHeight();
                Log.e("test", "previewWidth: " + previewWidth + "      previewHeight: " + previewHeight);
                previewHandler.sendEmptyMessage(HANDLER_GOT_PREVIEW_SIZE);
            }
        });
    }

    public void sendPreviewHandler(int msgWhat, String... str) {

        if (str.length == 0) {
            previewHandler.sendEmptyMessage(msgWhat);
        } else {
            Message msg = new Message();
            msg.what = msgWhat;
            Bundle b = new Bundle();
            for (int i = 0; i < str.length; i++) {
                b.putString(String.valueOf(i), str[i]);
            }
            msg.setData(b);
            previewHandler.sendMessage(msg);
        }
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_show_ps_setting_sheet:
                    showSettingSheet();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.detachViews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mLibVLC.release();
        }
        finish();
    }


    private void showSettingSheet(){
        final BottomSheetDialog dialog = new BottomSheetDialog(PreviewActivity.this);
        View dialogView = LayoutInflater.from(PreviewActivity.this).inflate(R.layout.preview_setting_layout, null);
        TextView psTvZoom = (TextView) dialogView.findViewById(R.id.ps_tv_zoom);
        Button psBtnZoomLess = (Button) dialogView.findViewById(R.id.ps_btn_zoom_less);
        IndicatorSeekBar psSbZoom = (IndicatorSeekBar) dialogView.findViewById(R.id.ps_sb_zoom);
        Button psBtnZoomPlus = (Button) dialogView.findViewById(R.id.ps_btn_zoom_plus);
        TextView psTvFocus = (TextView) dialogView.findViewById(R.id.ps_tv_focus);
        Button psBtnFocusLess = (Button) dialogView.findViewById(R.id.ps_btn_focus_less);
        IndicatorSeekBar psSbFocus = (IndicatorSeekBar) dialogView.findViewById(R.id.ps_sb_focus);
        Button psBtnFocusPlus = (Button) dialogView.findViewById(R.id.ps_btn_focus_plus);
        TextView psTvAperture = (TextView) dialogView.findViewById(R.id.ps_tv_aperture);
        Button psBtnApertureLess = (Button) dialogView.findViewById(R.id.ps_btn_aperture_less);
        IndicatorSeekBar psSbAperture = (IndicatorSeekBar) dialogView.findViewById(R.id.ps_sb_aperture);
        Button psBtnAperturePlus = (Button) dialogView.findViewById(R.id.ps_btn_aperture_plus);


        dialog.setContentView(dialogView);
//        dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundResource(R.color.menu_color);
        dialog.show();
    }

    public void initCamera_vlc(String rtsp, int previewWidth, int previewHeight) {
        try {

            Log.i("aaa", "************111111111******");
            final ArrayList<String> args = new ArrayList<>();//VLC参数
            args.add("--rtsp-tcp");//强制rtsp-tcp，加快加载视频速度
            args.add("--live-caching=0");
            args.add("--file-caching=0");
            args.add("--network-caching=0");//增加实时性，延时大概2-3秒
            mLibVLC = new LibVLC(mContext, args);
            mMediaPlayer = new MediaPlayer(mLibVLC);
            init_vlc_eventListener();

//            String pathUri = "rtsp://admin:admin@10.10.25.34:554/cam/realmonitor?channel=1&subtype=0";
            String pathUri = rtsp;
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
            mMediaPlayer.getVLCVout().setWindowSize(previewWidth, previewHeight);

            mMediaPlayer.attachViews(video_layout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
//            mMediaPlayer.setVideoScale(MediaPlayer.ScaleType.SURFACE_FILL);

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
                } else if (event.type == MediaPlayer.Event.EndReached) {
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
                } else if (event.type == MediaPlayer.Event.Playing) {
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
                    getVideoInfo(mContext, mMediaPlayer);

                    previewTvLoading.setVisibility(View.INVISIBLE);

//                        }
                } else if (event.type == MediaPlayer.Event.RecordChanged) {
                    Log.d(TAG, "VLC RecordChanged");
                }
            }
        });
    }

    public String getVideoInfo(Context context, MediaPlayer mMediaPlayer1) {
        Media media = (Media) mMediaPlayer1.getMedia();
        Log.i("aaa", "*********media:" + media.getTrackCount());
        for (int i = 0; i < media.getTrackCount(); i++) {
            if (media.getTrack(i).type == 1) {
                Log.i("aaa", "*********media:11");
                Media.VideoTrack videoTrack = (Media.VideoTrack) media.getTrack(i);

                Log.i("aaa", "*********media:11:" + String.valueOf(videoTrack.width) + ":" + String.valueOf(videoTrack.height));
                return String.valueOf(videoTrack.width) + ":" + String.valueOf(videoTrack.height);
            } else {
                Log.i("aaa", "*********media:12");
                return null;
            }
        }
        return null;
    }


    private static class PreviewHandler extends Handler {
        private PreviewActivity previewActiviy;

        private PreviewHandler(PreviewActivity previewActiviy) {
            WeakReference<PreviewActivity> reference = new WeakReference<>(previewActiviy);
            this.previewActiviy = reference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HANDLER_GOT_PREVIEW_SIZE:
//                    this.previewActiviy.initCamera_vlc();
                    this.previewActiviy.deviceManage.getDeviceChannel();
                    break;
                case HANDLER_GOT_RTSP:
                    String rtsp = msg.getData().getString("0");
                    if (!rtsp.equals("")) {
                        this.previewActiviy.initCamera_vlc(rtsp, this.previewActiviy.previewWidth, this.previewActiviy.previewHeight);
                    } else {
                        Toast.makeText(this.previewActiviy, this.previewActiviy.getString(R.string.tst_get_rtsp_error), Toast.LENGTH_SHORT).show();
                    }
                default:
                    break;
            }
        }
    }


}
