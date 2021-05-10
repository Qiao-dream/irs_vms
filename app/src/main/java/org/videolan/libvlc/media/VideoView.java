/*     */ package org.videolan.libvlc.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.content.Context;
/*     */ import android.graphics.Canvas;
/*     */ import android.media.MediaFormat;
/*     */ import android.media.MediaPlayer;
/*     */ import android.net.Uri;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.KeyEvent;
/*     */ import android.view.MotionEvent;
/*     */ import android.view.SurfaceView;
/*     */ import android.view.accessibility.AccessibilityEvent;
/*     */ import android.view.accessibility.AccessibilityNodeInfo;
/*     */ import android.widget.MediaController;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import org.videolan.libvlc.LibVLC;
/*     */ import org.videolan.libvlc.Media;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VideoView
/*     */   extends SurfaceView
/*     */   implements MediaController.MediaPlayerControl
/*     */ {
/*     */   private static ILibVLC sILibVLC;
/*     */   
/*     */   public VideoView(Context context) {
/*  59 */     super(context);
/*  60 */     sILibVLC = (ILibVLC)new LibVLC(context, null);
/*     */   }
/*     */   
/*     */   public VideoView(Context context, AttributeSet attrs) {
/*  64 */     this(context, attrs, 0);
/*     */   }
/*     */   
/*     */   public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
/*  68 */     this(context, attrs, defStyleAttr, 0);
/*     */   }
/*     */   
/*     */   @TargetApi(21)
/*     */   public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
/*  73 */     super(context, attrs, defStyleAttr, defStyleRes);
/*     */   }
/*     */ 
/*     */   
/*     */   @TargetApi(14)
/*     */   public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
/*  79 */     super.onInitializeAccessibilityEvent(event);
/*     */   }
/*     */ 
/*     */   
/*     */   @TargetApi(14)
/*     */   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
/*  85 */     super.onInitializeAccessibilityNodeInfo(info);
/*     */   }
/*     */   
/*     */   public int resolveAdjustedSize(int desiredSize, int measureSpec) {
/*  89 */     return getDefaultSize(desiredSize, measureSpec);
/*     */   }
/*     */   
/*     */   public void setVideoPath(String path) {
/*  93 */     Media media = new Media(sILibVLC, path);
/*     */   }
/*     */   
/*     */   public void setVideoURI(Uri uri) {
/*  97 */     Media media = new Media(sILibVLC, uri);
/*     */   }
/*     */   
/*     */   @TargetApi(21)
/*     */   public void setVideoURI(Uri uri, Map<String, String> headers) {
/* 102 */     setVideoURI(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubtitleSource(InputStream is, MediaFormat format) {}
/*     */ 
/*     */   
/*     */   public void setMediaController(MediaController controller) {}
/*     */ 
/*     */   
/*     */   public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {}
/*     */ 
/*     */   
/*     */   public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {}
/*     */ 
/*     */   
/*     */   public void setOnErrorListener(MediaPlayer.OnErrorListener l) {}
/*     */ 
/*     */   
/*     */   @TargetApi(17)
/*     */   public void setOnInfoListener(MediaPlayer.OnInfoListener l) {}
/*     */ 
/*     */   
/*     */   public boolean onTouchEvent(MotionEvent ev) {
/* 127 */     return super.onTouchEvent(ev);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onTrackballEvent(MotionEvent ev) {
/* 132 */     return super.onTrackballEvent(ev);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onKeyDown(int keyCode, KeyEvent event) {
/* 137 */     return super.onKeyDown(keyCode, event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void pause() {}
/*     */ 
/*     */   
/*     */   public void stopPlayback() {}
/*     */ 
/*     */   
/*     */   public void suspend() {}
/*     */ 
/*     */   
/*     */   public void resume() {}
/*     */ 
/*     */   
/*     */   public int getDuration() {
/* 159 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentPosition() {
/* 164 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void seekTo(int msec) {}
/*     */ 
/*     */   
/*     */   public boolean isPlaying() {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBufferPercentage() {
/* 178 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPause() {
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSeekBackward() {
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSeekForward() {
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @TargetApi(18)
/*     */   public int getAudioSessionId() {
/* 199 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onAttachedToWindow() {
/* 204 */     super.onAttachedToWindow();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDetachedFromWindow() {
/* 209 */     super.onDetachedFromWindow();
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Canvas canvas) {
/* 214 */     super.draw(canvas);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
/* 219 */     super.onLayout(changed, left, top, right, bottom);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
/* 224 */     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\media\VideoView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */