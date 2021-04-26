package com.iray.irs_vms.widget;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.iray.irs_vms.R;
import com.iray.irs_vms.httpUtils.Common;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Util;
import org.videolan.libvlc.WeakHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VideoPlayerFragment extends Fragment implements IVideoPlayer {
    public final static String TAG = "VideoPlayerFragment";

    private SurfaceHolder surfaceHolder = null;
    private LibVLC mLibVLC = null;

    private int mVideoHeight;
    private int mVideoWidth;
    private int mSarDen;
    private int mSarNum;
    private int mUiVisibility = -1;
    private static final int SURFACE_SIZE = 3;

    private SurfaceView surfaceView = null;
    //截图后的图片的宽度
    private static  int PIC_WIDTH = 640;
    //截图后的图片的高度
    private static  int PIC_HEIGHT = 360;
    private String mPicCachePath;
    private Context  ctx;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //存放VLC的截屏图片的文件夹路径
        View view = inflater.inflate(R.layout.video_player, null);
        Log.i(TAG,"************onCreateView*");
        init(view);
        if (Util.isICSOrLater())
            getActivity().getWindow().getDecorView().findViewById(android.R.id.content)
                    .setOnSystemUiVisibilityChangeListener(
                            new OnSystemUiVisibilityChangeListener() {

                                @Override
                                public void onSystemUiVisibilityChange(
                                        int visibility) {
                                    if (visibility == mUiVisibility)
                                        return;
                                    setSurfaceSize(mVideoWidth, mVideoHeight,
                                            mSarNum, mSarDen);
                                    if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                                        Log.d(TAG, "onSystemUiVisibilityChange");
                                    }
                                    mUiVisibility = visibility;
                                }
                            });

        try {
            mLibVLC = LibVLC.getInstance();
            if (mLibVLC != null) {
                EventHandler em = EventHandler.getInstance();
                em.addHandler(eventHandler);
            }
        } catch (LibVlcException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreateView: " + e.getMessage());
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mLibVLC.isPlaying()) {
            mLibVLC.play();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mLibVLC.stop();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 初始化组件
     */
    private void init(View view) {
        surfaceView = (SurfaceView) view.findViewById(R.id.main_surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.RGBX_8888);
        surfaceHolder.addCallback(mSurfaceCallback);
        mPicCachePath = Common.FILE_PATH;//
        PIC_WIDTH = Common.getScreenHeight(this.getActivity())*Common.DEFAULT_WIDTH/Common.DEFAULT_HEIGHT;
        PIC_HEIGHT = Common.getScreenHeight(this.getActivity());

        File file = new File(mPicCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public String getVideoSize(){
        return String.valueOf(mVideoWidth)+":" +String.valueOf(mVideoHeight);
    }
    public void videoRecord() {
        try {

            Log.w("aaa", "********:"+mLibVLC.videoIsRecordable() + "int:" +  mLibVLC.getVideoTracksCount());
            if (mLibVLC.videoIsRecording()) {
                Log.w("aaa", "record stop begin");
                if (mLibVLC.videoRecordStop()) {
                    Toast.makeText(getActivity(), "停止录像", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "停止录像失败",  Toast.LENGTH_LONG).show();
                }
                Log.w("aaa", "record stop end");
            } else {
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//                String name = df.format(new Date());
                String name =String.valueOf(System.currentTimeMillis());;
                Log.w("aaa", "record start begin");
                mLibVLC.videoRecordStart(mPicCachePath  + name);
                Common.Delay(100);
                Log.w("aaa", "record start end:" +mLibVLC.videoIsRecording()  );
                if(mLibVLC.videoIsRecording()) {
                    Log.w("aaa", "************1111111111111111111");
                    Toast.makeText(getActivity(), "开始录像", Toast.LENGTH_LONG).show();
                }else{
                    mLibVLC.videoRecordStart(mPicCachePath  + name);
                    Common.Delay(100);
                    if(mLibVLC.videoIsRecording()) {
                        Log.w("aaa", "************2222222222222");
                        Toast.makeText(getActivity(), "开始录像", Toast.LENGTH_LONG).show();
                    }else{
                        Log.w("aaa", "************3333333333333");
                        Toast.makeText(getActivity(), "开始录像失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean videoRecordStop() {
        Log.i("aaa", "*****record stop begin:" +mLibVLC.videoIsRecording());

            if(mLibVLC.videoIsRecording()) {
                Log.i("aaa", "record stop begin");
                if (mLibVLC.videoRecordStop()) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }

    }
        public boolean videoRecordStart () {
            try {

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    String name = df.format(new Date());
                    Log.w("aaa", "record start begin:" +mPicCachePath + System.currentTimeMillis() );
                    if (mLibVLC.videoRecordStart(mPicCachePath + System.currentTimeMillis())) {
                        Log.i("aaa", "record start end:"+mLibVLC.videoIsRecording());
                        return true;
                    } else {
                        Log.i("aaa", "record start end false");
                        return false;
                    }
            } catch (Exception e) {
                e.printStackTrace();
                return  false;
            }
        }
        /**
         * 截图
         */
        public String snapShot () {
            try {
                String name = mPicCachePath + System.currentTimeMillis() + ".jpg";
                //调用LibVlc的截屏功能，传入一个路径，及图片的宽高
                if (mLibVLC.takeSnapShot(name, PIC_WIDTH, PIC_HEIGHT)) {
                    Log.i(TAG, "snapShot: 保存成功--" + System.currentTimeMillis()+"w：" +surfaceView.getWidth() + ";h:" + surfaceView.getHeight());
                    return name;
                }
                Log.i(TAG, "snapShot: 保存失败");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    /**
     * 截图
     */
    public String snapShot_real_size () {
        try {
            String name = mPicCachePath + System.currentTimeMillis() + ".jpg";
            //调用LibVlc的截屏功能，传入一个路径，及图片的宽高
            if (mLibVLC.takeSnapShot(name, mVideoWidth, mVideoHeight)) {
                Log.i(TAG, "snapShot: 保存成功--" + System.currentTimeMillis()+"w：" +surfaceView.getWidth() + ";h:" + surfaceView.getHeight());
                return name;
            }
            Log.i(TAG, "snapShot: 保存失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

        /**
         * 传入文件路径，获取bitmap
         *
         * @param path 路径
         */
        public Bitmap getFramePicture (String path){
            if (TextUtils.isEmpty(path)) {
                Log.i(TAG, "faceDetect: 文件路径为空|| mFaceUtil == null");
                return null;
            }
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            return file2Bitmap(file);
        }

        private RtspCallBack callBack;

        public void setRtspCallBack (RtspCallBack callBack){
            this.callBack = callBack;
        }

        public interface RtspCallBack {
            void pushData(List<Bitmap> faces);
        }

        @Override
        public void onConfigurationChanged (Configuration newConfig){
            setSurfaceSize(mVideoWidth, mVideoHeight, mSarNum, mSarDen);
            super.onConfigurationChanged(newConfig);
        }

        /**
         * attach and disattach surface to the lib
         */
        private final Callback mSurfaceCallback = new Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                if (format == PixelFormat.RGBX_8888)
                    Log.d(TAG, "Pixel format is RGBX_8888");
                else if (format == PixelFormat.RGB_565)
                    Log.d(TAG, "Pixel format is RGB_565");
                else if (format == ImageFormat.YV12)
                    Log.d(TAG, "Pixel format is YV12");
                else
                    Log.d(TAG, "Pixel format is other/unknown");
                mLibVLC.attachSurface(holder.getSurface(),
                        VideoPlayerFragment.this);
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG,"**********surfaceCreated");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG,"**********surfaceDestroyed");
                mLibVLC.detachSurface();
            }
        };

        public final Handler mHandler = new VideoPlayerHandler(this);

        private static class VideoPlayerHandler extends
                WeakHandler<VideoPlayerFragment> {
            public VideoPlayerHandler(VideoPlayerFragment owner) {
                super(owner);
            }

            @Override
            public void handleMessage(Message msg) {
                VideoPlayerFragment activity = getOwner();
                if (activity == null) // WeakReference could be GC'ed early
                    return;

                switch (msg.what) {
                    case SURFACE_SIZE:
                        activity.changeSurfaceSize();
                        break;
                }
            }
        }

        private void changeSurfaceSize () {
            // get screen size
            int dw = getActivity().getWindow().getDecorView().getWidth();
            int dh = getActivity().getWindow().getDecorView().getHeight()-Common.getStatusBarHeight(this.getActivity());
            Log.i(TAG,"******11111****dw:" + dw+";dh:" + dh);

            // getWindow().getDecorView() doesn't always take orientation into
            // account, we have to correct the values
            boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            if (dw > dh && isPortrait || dw < dh && !isPortrait) {
                int d = dw;
                dw = dh;
                dh = d;
            }
            if (dw * dh == 0)
                return;
            // compute the aspect ratio
            double ar, vw;
            double density = (double) mSarNum / (double) mSarDen;
            Log.i(TAG,"******11111****mSarNum:" + mSarNum+";mSarDen:" + mSarDen);
            if (density == 1.0) {
                /* No indication about the density, assuming 1:1 */
                ar = (double) mVideoWidth / (double) mVideoHeight;
            } else {
                /* Use the specified aspect ratio */
                vw = mVideoWidth * density;
                ar = vw / mVideoHeight;
            }

            // compute the display aspect ratio
            double dar = (double) dw / (double) dh;
            if (dar < ar)
                dh = (int) (dw / ar);
            else
                dw = (int) (dh * ar);
            Log.i(TAG,"**********ar:" + ar+";dar:" + dar);
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            LayoutParams lp = surfaceView.getLayoutParams();
            lp.width = dw;
            lp.height = dh;
            Log.i(TAG,"**********dw:" + dw+";dh:" + dh);
            surfaceView.setLayoutParams(lp);
            surfaceView.invalidate();
            sendBroadcastImagesize(getVideoSize());
        }

        private final Handler eventHandler = new VideoPlayerEventHandler(this);

        private static class VideoPlayerEventHandler extends
                WeakHandler<VideoPlayerFragment> {
            public VideoPlayerEventHandler(VideoPlayerFragment owner) {
                super(owner);
            }

            @Override
            public void handleMessage(Message msg) {
                VideoPlayerFragment activity = getOwner();
                if (activity == null)
                    return;
               // Log.d(TAG, "Event = " + msg.getData().getInt("event"));
                switch (msg.getData().getInt("event")) {
                    case EventHandler.MediaPlayerPlaying:
                        Log.i(TAG, "MediaPlayerPlaying");
                        break;
                    case EventHandler.MediaPlayerPaused:
                        Log.i(TAG, "MediaPlayerPaused");
                        break;
                    case EventHandler.MediaPlayerStopped:
                        Log.i(TAG, "MediaPlayerStopped");
                        break;
                    case EventHandler.MediaPlayerEndReached:
                        Log.i(TAG, "MediaPlayerEndReached");
                       // activity.getActivity().finish();


                        break;
                    case EventHandler.MediaPlayerVout:
                        Log.i(TAG, "MediaPlayerVout");
                     //   activity.getActivity().finish();
                        break;
                    default:
                     //   Log.d(TAG, "Event not handled");
                        break;
                }
            }
        }

        @Override
        public void onDestroy () {
            if (mLibVLC != null) {
                mLibVLC.stop();
            }
//            MainActivity.flag_pic_ok =false;
            EventHandler em = EventHandler.getInstance();
            Log.i(TAG,"**************em:" +em );
            em.removeHandler(eventHandler);
            super.onDestroy();
        }
//    public  void qingP() {
//        Canvas canvas = surfaceHolder.lockCanvas(null);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        Paint p = new Paint();
//        canvas.drawBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.infiray), 0, 0, p);
//        /*
//         * 解锁画布并提交内容
//         */
//        surfaceHolder.unlockCanvasAndPost(canvas);
//    }
        public void setSurfaceSize ( int width, int height, int sar_num, int sar_den){
            if (width * height == 0)
                return;

            mVideoHeight = height;
            mVideoWidth = width;
            mSarNum = sar_num;
            mSarDen = sar_den;
            Message msg = mHandler.obtainMessage(SURFACE_SIZE);
            mHandler.sendMessage(msg);
        }

        @Override
        public void setSurfaceSize ( int width, int height, int visible_width,
        int visible_height, int sar_num, int sar_den){
            mVideoHeight = height;
            mVideoWidth = width;
            mSarNum = sar_num;
            mSarDen = sar_den;
            Log.i(TAG,"*************mVideoWidth:" + mVideoWidth +";mVideoHeight:" + mVideoHeight);
            Message msg = mHandler.obtainMessage(SURFACE_SIZE);
            mHandler.sendMessage(msg);
        }
    private void sendBroadcastImagesize(String value) {
        Intent intent = new Intent();
        intent.putExtra(Common.IMAGE_SIZE_VALUE, value);
        intent.setAction(Common.IMAGE_SIZE);

        getActivity().sendBroadcast(intent);
        Log.i(TAG, "read>>>>>  sendBroadcastImagesize:" + value);
    }


        private Bitmap file2Bitmap (File file){
            if (file == null) {
                return null;
            }
            try {
                FileInputStream fis = new FileInputStream(file);
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
