/*     */
package org.videolan.libvlc;
/*     */
/*     */

import android.annotation.TargetApi;
/*     */ import android.app.Activity;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import android.view.SurfaceView;
/*     */ import android.view.TextureView;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.view.ViewStub;
/*     */ import android.widget.FrameLayout;
///*     */ import org.videolan.R;
/*     */ import org.videolan.libvlc.interfaces.IMedia;
/*     */ import org.videolan.libvlc.interfaces.IVLCVout;
/*     */ import org.videolan.libvlc.util.AndroidUtil;
/*     */ import org.videolan.libvlc.util.DisplayManager;
/*     */ import org.videolan.libvlc.util.VLCVideoLayout;
/*     */
/*     */ import com.iray.irs_vms.R;

/*     */
/*     */
/*     */ class VideoHelper
        /*     */ implements IVLCVout.OnNewVideoLayoutListener
        /*     */ {
    /*     */   private static final String TAG = "LibVLC/VideoHelper";
    /*  27 */   private MediaPlayer.ScaleType mCurrentScaleType = MediaPlayer.ScaleType.SURFACE_BEST_FIT;
    /*     */
    /*  29 */   private int mVideoHeight = 0;
    /*  30 */   private int mVideoWidth = 0;
    /*  31 */   private int mVideoVisibleHeight = 0;
    /*  32 */   private int mVideoVisibleWidth = 0;
    /*  33 */   private int mVideoSarNum = 0;
    /*  34 */   private int mVideoSarDen = 0;
    /*     */
    /*     */   private FrameLayout mVideoSurfaceFrame;
    /*  37 */   private SurfaceView mVideoSurface = null;
    /*  38 */   private SurfaceView mSubtitlesSurface = null;
    /*  39 */   private TextureView mVideoTexture = null;
    /*     */
    /*  41 */   private final Handler mHandler = new Handler();
    /*  42 */   private View.OnLayoutChangeListener mOnLayoutChangeListener = null;
    /*     */
    /*     */   private DisplayManager mDisplayManager;
    /*     */   private MediaPlayer mMediaPlayer;

    /*     */
    /*     */   VideoHelper(MediaPlayer player, VLCVideoLayout surfaceFrame, DisplayManager dm, boolean subtitles, boolean textureView) {
        /*  48 */
        init(player, surfaceFrame, dm, subtitles, !textureView);
        /*     */
    }

    /*     */
    /*     */
    private void init(MediaPlayer player, VLCVideoLayout surfaceFrame, DisplayManager dm, boolean subtitles, boolean useSurfaceView) {
        /*  52 */
        this.mMediaPlayer = player;
        /*  53 */
        this.mDisplayManager = dm;
        /*  54 */
        boolean isPrimary = (this.mDisplayManager == null || this.mDisplayManager.isPrimary());
        /*  55 */
        if (isPrimary) {
            /*  56 */
            this.mVideoSurfaceFrame = (FrameLayout) surfaceFrame.findViewById(R.id.player_surface_frame);
            /*  57 */
            if (useSurfaceView) {
                /*  58 */
                ViewStub stub = (ViewStub) this.mVideoSurfaceFrame.findViewById(R.id.surface_stub);
                /*  59 */
                this.mVideoSurface = (stub != null) ? (SurfaceView) stub.inflate() : (SurfaceView) this.mVideoSurfaceFrame.findViewById(R.id.surface_video);
                /*  60 */
                if (subtitles) {
                    /*  61 */
                    stub = (ViewStub) this.mVideoSurfaceFrame.findViewById(R.id.subtitles_surface_stub);
                    /*  62 */
                    this.mSubtitlesSurface = (stub != null) ? (SurfaceView) stub.inflate() : (SurfaceView) this.mVideoSurfaceFrame.findViewById(R.id.surface_subtitles);
                    /*  63 */
                    this.mSubtitlesSurface.setZOrderMediaOverlay(true);
                    /*  64 */
                    this.mSubtitlesSurface.getHolder().setFormat(-3);
                    /*     */
                }
                /*     */
            } else {
                /*  67 */
                ViewStub stub = (ViewStub) this.mVideoSurfaceFrame.findViewById(R.id.texture_stub);
                /*  68 */
                this.mVideoTexture = (stub != null) ? (TextureView) stub.inflate() : (TextureView) this.mVideoSurfaceFrame.findViewById(R.id.texture_video);
                /*     */
            }
            /*  70 */
        } else if (this.mDisplayManager.getPresentation() != null) {
            /*  71 */
            this.mVideoSurfaceFrame = this.mDisplayManager.getPresentation().getSurfaceFrame();
            /*  72 */
            this.mVideoSurface = this.mDisplayManager.getPresentation().getSurfaceView();
            /*  73 */
            this.mSubtitlesSurface = this.mDisplayManager.getPresentation().getSubtitlesSurfaceView();
            /*     */
        }

//        changeMediaPlayerLayout();
        /*     */
    }

    /*     */
    /*     */   void release() {
        /*  78 */
        if (this.mMediaPlayer.getVLCVout().areViewsAttached()) detachViews();
        /*  79 */
        this.mMediaPlayer = null;
        /*  80 */
        this.mVideoSurfaceFrame = null;
        /*  81 */
        this.mHandler.removeCallbacks(null);
        /*  82 */
        this.mVideoSurface = null;
        /*  83 */
        this.mSubtitlesSurface = null;
        /*  84 */
        this.mVideoTexture = null;
        /*     */
    }

    /*     */
    /*     */   void attachViews() {
        /*  88 */
        if (this.mVideoSurface == null && this.mVideoTexture == null)
            /*  89 */ return;
        IVLCVout vlcVout = this.mMediaPlayer.getVLCVout();
        /*  90 */
        if (this.mVideoSurface != null)
            /*  91 */ {
            vlcVout.setVideoView(this.mVideoSurface);
            /*  92 */
            if (this.mSubtitlesSurface != null)
                /*  93 */ vlcVout.setSubtitlesView(this.mSubtitlesSurface);
        }
        /*  94 */
        else if (this.mVideoTexture != null)
            /*  95 */ {
            vlcVout.setVideoView(this.mVideoTexture);
        }
        /*     */
        else {
            return;
        }
        /*  97 */
        vlcVout.attachViews(this);
        /*     */
        /*  99 */
//        if (this.mOnLayoutChangeListener == null) {
//            /* 100 */
//            this.mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
//                /* 101 */           private final Runnable runnable = new Runnable()
//                        /*     */ {
//                    /*     */
//                    public void run() {
//                        /* 104 */
//                        if (VideoHelper.this.mVideoSurfaceFrame != null && VideoHelper.this.mOnLayoutChangeListener != null)
//                            VideoHelper.this.updateVideoSurfaces();
//                        /*     */
//                        /*     */
//                    }
//                    /*     */
//                };
//
//                /*     */
//                /*     */
//                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                    /* 110 */
//                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
//                        /* 111 */
//                        VideoHelper.this.mHandler.removeCallbacks(this.runnable);
//                        /* 112 */
//                        VideoHelper.this.mHandler.post(this.runnable);
//                        /*     */
//                    }
//                    /*     */
//                }
//                /*     */
//            };
//            /*     */
//        }
        /* 117 */
//        this.mVideoSurfaceFrame.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        /* 118 */
        this.mMediaPlayer.setVideoTrackEnabled(true);
        /*     */
    }

    /*     */
    /*     */   void detachViews() {
        /* 122 */
        if (this.mOnLayoutChangeListener != null && this.mVideoSurfaceFrame != null) {
            /* 123 */
            this.mVideoSurfaceFrame.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
            /* 124 */
            this.mOnLayoutChangeListener = null;
            /*     */
        }
        /* 126 */
        this.mMediaPlayer.setVideoTrackEnabled(false);
        /* 127 */
        this.mMediaPlayer.getVLCVout().detachViews();
        /*     */
    }

    /*     */
    private void changeMediaPlayerLayout(int displayW, int displayH) {
        IMedia.VideoTrack vtrack;
        /*     */
        boolean videoSwapped;
        /* 131 */
        if (this.mMediaPlayer.isReleased())
            /*     */ return;
        /* 133 */
        switch (this.mCurrentScaleType) {
            /*     */
            case SURFACE_BEST_FIT:
                /* 135 */
                this.mMediaPlayer.setAspectRatio(null);
                /* 136 */
                this.mMediaPlayer.setScale(0.0F);
                /*     */
                break;
            /*     */
            case SURFACE_FIT_SCREEN:
                /*     */
            case SURFACE_FILL:
                /* 140 */
                vtrack = this.mMediaPlayer.getCurrentVideoTrack();
                /* 141 */
                if (vtrack == null)
                    /*     */ return;
                /* 143 */
                videoSwapped = (vtrack.orientation == 5 || vtrack.orientation == 6);
                /*     */
                /* 145 */
                if (this.mCurrentScaleType == MediaPlayer.ScaleType.SURFACE_FIT_SCREEN) {
                    /* 146 */
                    float scale;
                    int videoW = vtrack.width;
                    /* 147 */
                    int videoH = vtrack.height;
                    /*     */
                    /* 149 */
                    if (videoSwapped) {
                        /* 150 */
                        int swap = videoW;
                        /* 151 */
                        videoW = videoH;
                        /* 152 */
                        videoH = swap;
                        /*     */
                    }
                    /* 154 */
                    if (vtrack.sarNum != vtrack.sarDen) {
                        /* 155 */
                        videoW = videoW * vtrack.sarNum / vtrack.sarDen;
                        /*     */
                    }
                    /* 157 */
                    float ar = videoW / videoH;
                    /* 158 */
                    float dar = displayW / displayH;
                    /*     */
                    /*     */
                    /* 161 */
                    if (dar >= ar) {
                        /* 162 */
                        scale = displayW / videoW;
                        /*     */
                    } else {
                        /* 164 */
                        scale = displayH / videoH;
                        /* 165 */
                    }
                    this.mMediaPlayer.setScale(scale);
                    /* 166 */
                    this.mMediaPlayer.setAspectRatio(null);
                    break;
                    /*     */
                }
                /* 168 */
                this.mMediaPlayer.setScale(0.0F);
                /* 169 */
                this.mMediaPlayer.setAspectRatio(!videoSwapped ? ("" + displayW + ":" + displayH) : ("" + displayH + ":" + displayW));
                /*     */
                break;
            /*     */
            /*     */
            /*     */
            /*     */
            case SURFACE_16_9:
                /* 175 */
                this.mMediaPlayer.setAspectRatio("16:9");
                /* 176 */
                this.mMediaPlayer.setScale(0.0F);
                /*     */
                break;
            /*     */
            case SURFACE_4_3:
                /* 179 */
                this.mMediaPlayer.setAspectRatio("4:3");
                /* 180 */
                this.mMediaPlayer.setScale(0.0F);
                /*     */
                break;
            /*     */
            case SURFACE_ORIGINAL:
                /* 183 */
                this.mMediaPlayer.setAspectRatio(null);
                /* 184 */
                this.mMediaPlayer.setScale(1.0F);
                /*     */
                break;
            /*     */
        }
    }

//    @TargetApi(24)
//        /*     */   void updateVideoSurfaces() {
//        /*     */
//        int sw, sh;
//        /*     */
//        TextureView textureView;
//        /*     */
//        double ar, vw;
//        /* 191 */
//        if (this.mMediaPlayer == null || this.mMediaPlayer.isReleased() || !this.mMediaPlayer.getVLCVout().areViewsAttached())
//            /* 192 */ return;
//        boolean isPrimary = (this.mDisplayManager == null || this.mDisplayManager.isPrimary());
//        /* 193 */
//        Activity activity = (isPrimary && this.mVideoSurfaceFrame.getContext() instanceof Activity) ? (Activity) this.mVideoSurfaceFrame.getContext() : null;
//        /*     */
//        /*     */
//        /*     */
//        /*     */
//        /*     */
//        /* 199 */
//        if (activity != null) {
//            /*     */
//            /*     */
//            /* 202 */
//            sw = activity.getWindow().getDecorView().getWidth();
//            /* 203 */
//            sh = activity.getWindow().getDecorView().getHeight();
//            /* 204 */
//        } else if (this.mDisplayManager != null && this.mDisplayManager.getPresentation() != null && this.mDisplayManager.getPresentation().getWindow() != null) {
//            /* 205 */
//            sw = this.mDisplayManager.getPresentation().getWindow().getDecorView().getWidth();
//            /* 206 */
//            sh = this.mDisplayManager.getPresentation().getWindow().getDecorView().getHeight();
//            /*     */
//        } else {
//            /*     */
//            return;
//            /*     */
//        }
//        /* 210 */
//        if (sw * sh == 0) {
//            /* 211 */
//            Log.e("LibVLC/VideoHelper", "Invalid surface size");
//            /*     */
//            /*     */
//            return;
//            /*     */
//        }
//        /* 215 */
//        this.mMediaPlayer.getVLCVout().setWindowSize(sw, sh);
//        /*     */
//        /*     */
//        /* 218 */
//        SurfaceView surfaceView = this.mVideoSurface;
//        /* 219 */
//        if (surfaceView == null) {
//            /* 220 */
//            textureView = this.mVideoTexture;
//            /*     */
//        }
//        /* 222 */
//        ViewGroup.LayoutParams lp = textureView.getLayoutParams();
//        /* 223 */
//        if (this.mVideoWidth * this.mVideoHeight == 0 || (AndroidUtil.isNougatOrLater && activity != null && activity.isInPictureInPictureMode())) {
//            /*     */
//            /* 225 */
//            lp.width = -1;
//            /* 226 */
//            lp.height = -1;
//            /* 227 */
//            textureView.setLayoutParams(lp);
//            /* 228 */
//            if (this.mSubtitlesSurface != null)
//                /* 229 */ this.mSubtitlesSurface.setLayoutParams(lp);
//            /* 230 */
//            lp = this.mVideoSurfaceFrame.getLayoutParams();
//            /* 231 */
//            lp.width = -1;
//            /* 232 */
//            lp.height = -1;
//            /* 233 */
//            this.mVideoSurfaceFrame.setLayoutParams(lp);
//            /* 234 */
//            if (this.mVideoWidth * this.mVideoHeight == 0) changeMediaPlayerLayout(sw, sh);
//            /*     */
//            /*     */
//            return;
//            /*     */
//        }
//        /* 238 */
//        if (lp.width == lp.height && lp.width == -1) {
//            /*     */
//            /* 240 */
//            this.mMediaPlayer.setAspectRatio(null);
//            /* 241 */
//            this.mMediaPlayer.setScale(0.0F);
//            /*     */
//        }
//        /*     */
//        /* 244 */
//        double dw = sw, dh = sh;
//        /* 245 */
//        boolean isPortrait = (isPrimary && (this.mVideoSurfaceFrame.getResources().getConfiguration()).orientation == 1);
//        /*     */
//        /* 247 */
//        if ((sw > sh && isPortrait) || (sw < sh && !isPortrait)) {
//            /* 248 */
//            dw = sh;
//            /* 249 */
//            dh = sw;
//            /*     */
//        }
//        /*     */
//        /*     */
//        /*     */
//        /* 254 */
//        if (this.mVideoSarDen == this.mVideoSarNum) {
//            /*     */
//            /* 256 */
//            vw = this.mVideoVisibleWidth;
//            /* 257 */
//            ar = this.mVideoVisibleWidth / this.mVideoVisibleHeight;
//            /*     */
//        } else {
//            /*     */
//            /* 260 */
//            vw = this.mVideoVisibleWidth * this.mVideoSarNum / this.mVideoSarDen;
//            /* 261 */
//            ar = vw / this.mVideoVisibleHeight;
//            /*     */
//        }
//        /*     */
//        /*     */
//        /* 265 */
//        double dar = dw / dh;
//        /*     */
//        /* 267 */
//        switch (this.mCurrentScaleType) {
//            /*     */
//            case SURFACE_BEST_FIT:
//                /* 269 */
//                if (dar < ar) {
//                    /* 270 */
//                    dh = dw / ar;
//                    break;
//                    /*     */
//                }
//                /* 272 */
//                dw = dh * ar;
//                /*     */
//                break;
//            /*     */
//            case SURFACE_FIT_SCREEN:
//                /* 275 */
//                if (dar >= ar) {
//                    /* 276 */
//                    dh = dw / ar;
//                    break;
//                    /*     */
//                }
//                /* 278 */
//                dw = dh * ar;
//                /*     */
//                break;
//            /*     */
//            /*     */
//            /*     */
//            case SURFACE_16_9:
//                /* 283 */
//                ar = 1.7777777777777777D;
//                /* 284 */
//                if (dar < ar) {
//                    /* 285 */
//                    dh = dw / ar;
//                    break;
//                    /*     */
//                }
//                /* 287 */
//                dw = dh * ar;
//                /*     */
//                break;
//            /*     */
//            case SURFACE_4_3:
//                /* 290 */
//                ar = 1.3333333333333333D;
//                /* 291 */
//                if (dar < ar) {
//                    /* 292 */
//                    dh = dw / ar;
//                    break;
//                    /*     */
//                }
//                /* 294 */
//                dw = dh * ar;
//                /*     */
//                break;
//            /*     */
//            case SURFACE_ORIGINAL:
//                /* 297 */
//                dh = this.mVideoVisibleHeight;
//                /* 298 */
//                dw = vw;
//                /*     */
//                break;
//            /*     */
//        }
//        /*     */
//        /*     */
//        /* 303 */
//        lp.width = (int) Math.ceil(dw * this.mVideoWidth / this.mVideoVisibleWidth);
//        /* 304 */
//        lp.height = (int) Math.ceil(dh * this.mVideoHeight / this.mVideoVisibleHeight);
//        /* 305 */
//        textureView.setLayoutParams(lp);
//        /* 306 */
//        if (this.mSubtitlesSurface != null) this.mSubtitlesSurface.setLayoutParams(lp);
//        /*     */
//        /* 308 */
//        textureView.invalidate();
//        /* 309 */
//        if (this.mSubtitlesSurface != null) this.mSubtitlesSurface.invalidate();
//        /*     */
//        /*     */
//    }

    /*     */
    /*     */
    /*     */
    @TargetApi(17)
    /*     */ public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        /* 316 */
        this.mVideoWidth = width;
        /* 317 */
        this.mVideoHeight = height;
        /* 318 */
        this.mVideoVisibleWidth = visibleWidth;
        /* 319 */
        this.mVideoVisibleHeight = visibleHeight;
        /* 320 */
        this.mVideoSarNum = sarNum;
        /* 321 */
        this.mVideoSarDen = sarDen;
        /* 322 */
//        updateVideoSurfaces();
        /*     */
    }

    /*     */
    /*     */   void setVideoScale(MediaPlayer.ScaleType type) {
        /* 326 */
        this.mCurrentScaleType = type;
        /* 327 */
//        updateVideoSurfaces();
        /*     */
    }

    /*     */
    /*     */   MediaPlayer.ScaleType getVideoScale() {
        /* 331 */
        return this.mCurrentScaleType;
        /*     */
    }
    /*     */
}


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\VideoHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */