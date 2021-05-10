/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.graphics.SurfaceTexture;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import android.view.Surface;
/*     */ import android.view.SurfaceHolder;
/*     */ import android.view.SurfaceView;
/*     */ import android.view.TextureView;
/*     */ import androidx.annotation.MainThread;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.videolan.libvlc.interfaces.IVLCVout;
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
/*     */ public class AWindow
/*     */   implements IVLCVout
/*     */ {
/*     */   private static final String TAG = "AWindow";
/*     */   private static final int ID_VIDEO = 0;
/*     */   private static final int ID_SUBTITLES = 1;
/*     */   private static final int ID_MAX = 2;
/*     */   private static final int SURFACE_STATE_INIT = 0;
/*     */   private static final int SURFACE_STATE_ATTACHED = 1;
/*     */   private static final int SURFACE_STATE_READY = 2;
/*     */   private final SurfaceHelper[] mSurfaceHelpers;
/*     */   private final SurfaceCallback mSurfaceCallback;
/*     */   
/*     */   private class SurfaceHelper
/*     */   {
/*     */     private final int mId;
/*     */     private final SurfaceView mSurfaceView;
/*     */     private final TextureView mTextureView;
/*     */     private final SurfaceHolder mSurfaceHolder;
/*     */     private Surface mSurface;
/*     */     private final SurfaceHolder.Callback mSurfaceHolderCallback;
/*     */     private final TextureView.SurfaceTextureListener mSurfaceTextureListener;
/*     */     
/*     */     private SurfaceHelper(int id, SurfaceView surfaceView) {
/* 144 */       this.mSurfaceHolderCallback = new SurfaceHolder.Callback()
/*     */         {
/*     */           public void surfaceCreated(SurfaceHolder holder) {
/* 147 */             if (holder != SurfaceHelper.this.mSurfaceHolder)
/* 148 */               throw new IllegalStateException("holders are different"); 
/* 149 */             SurfaceHelper.this.setSurface(holder.getSurface());
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
/*     */ 
/*     */           
/*     */           public void surfaceDestroyed(SurfaceHolder holder) {
/* 158 */             AWindow.this.onSurfaceDestroyed();
/*     */           }
/*     */         };
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
/* 187 */       this.mSurfaceTextureListener = createSurfaceTextureListener(); this.mId = id; this.mTextureView = null; this.mSurfaceView = surfaceView; this.mSurfaceHolder = this.mSurfaceView.getHolder(); } private SurfaceHelper(int id, TextureView textureView) { this.mSurfaceHolderCallback = new SurfaceHolder.Callback() { public void surfaceCreated(SurfaceHolder holder) { if (holder != SurfaceHelper.this.mSurfaceHolder) throw new IllegalStateException("holders are different");  SurfaceHelper.this.setSurface(holder.getSurface()); } public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {} public void surfaceDestroyed(SurfaceHolder holder) { AWindow.this.onSurfaceDestroyed(); } }; this.mSurfaceTextureListener = createSurfaceTextureListener(); this.mId = id; this.mSurfaceView = null; this.mSurfaceHolder = null; this.mTextureView = textureView; } private void setSurface(Surface surface) { if (surface.isValid() && AWindow.this.getNativeSurface(this.mId) == null) { this.mSurface = surface; AWindow.this.setNativeSurface(this.mId, this.mSurface); AWindow.this.onSurfaceCreated(); }  } private void attachSurfaceView() { this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback); setSurface(this.mSurfaceHolder.getSurface()); } @TargetApi(14) private void attachTextureView() { this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener); } private void attachSurface() { if (this.mSurfaceHolder != null) this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback);  setSurface(this.mSurface); } public void attach() { if (this.mSurfaceView != null) { attachSurfaceView(); } else if (this.mTextureView != null) { attachTextureView(); } else if (this.mSurface != null) { attachSurface(); } else { throw new IllegalStateException(); }  } @TargetApi(14) private void releaseTextureView() { if (this.mTextureView != null) this.mTextureView.setSurfaceTextureListener(null);  } private SurfaceHelper(int id, Surface surface, SurfaceHolder surfaceHolder) { this.mSurfaceHolderCallback = new SurfaceHolder.Callback() { public void surfaceCreated(SurfaceHolder holder) { if (holder != SurfaceHelper.this.mSurfaceHolder) throw new IllegalStateException("holders are different");  SurfaceHelper.this.setSurface(holder.getSurface()); } public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {} public void surfaceDestroyed(SurfaceHolder holder) { AWindow.this.onSurfaceDestroyed(); } }; this.mSurfaceTextureListener = createSurfaceTextureListener(); this.mId = id; this.mSurfaceView = null; this.mTextureView = null; this.mSurfaceHolder = surfaceHolder; this.mSurface = surface; }
/*     */     public void release() { this.mSurface = null; AWindow.this.setNativeSurface(this.mId, null); if (this.mSurfaceHolder != null)
/*     */         this.mSurfaceHolder.removeCallback(this.mSurfaceHolderCallback);  releaseTextureView(); }
/*     */     public boolean isReady() { return (this.mSurfaceView == null || this.mSurface != null); }
/*     */     public Surface getSurface() { return this.mSurface; }
/*     */     SurfaceHolder getSurfaceHolder() { return this.mSurfaceHolder; } @TargetApi(14) private TextureView.SurfaceTextureListener createSurfaceTextureListener() { return new TextureView.SurfaceTextureListener() {
/*     */           public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) { SurfaceHelper.this.setSurface(new Surface(surfaceTexture)); } public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {} public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) { AWindow.this.onSurfaceDestroyed();
/*     */             return true; } public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
/*     */         }; }
/* 196 */   } private final AtomicInteger mSurfacesState = new AtomicInteger(0);
/* 197 */   private OnNewVideoLayoutListener mOnNewVideoLayoutListener = null;
/* 198 */   private ArrayList<Callback> mIVLCVoutCallbacks = new ArrayList<>();
/* 199 */   private final Handler mHandler = new Handler(Looper.getMainLooper());
/*     */   
/*     */   private final Surface[] mSurfaces;
/* 202 */   private long mCallbackNativeHandle = 0L;
/* 203 */   private int mMouseAction = -1, mMouseButton = -1, mMouseX = -1, mMouseY = -1;
/* 204 */   private int mWindowWidth = -1; private int mWindowHeight = -1;
/*     */   
/* 206 */   private SurfaceTextureThread mSurfaceTextureThread = new SurfaceTextureThread();
/*     */ 
/*     */ 
/*     */   
/*     */   private final NativeLock mNativeLock;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int AWINDOW_REGISTER_ERROR = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int AWINDOW_REGISTER_FLAGS_SUCCESS = 1;
/*     */ 
/*     */   
/*     */   private static final int AWINDOW_REGISTER_FLAGS_HAS_VIDEO_LAYOUT_LISTENER = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureInitState() throws IllegalStateException {
/* 226 */     if (this.mSurfacesState.get() != 0) {
/* 227 */       throw new IllegalStateException("Can't set view when already attached. Current state: " + this.mSurfacesState
/* 228 */           .get() + ", mSurfaces[ID_VIDEO]: " + this.mSurfaceHelpers[0] + " / " + this.mSurfaces[0] + ", mSurfaces[ID_SUBTITLES]: " + this.mSurfaceHelpers[1] + " / " + this.mSurfaces[1]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void setView(int id, SurfaceView view) {
/* 234 */     ensureInitState();
/* 235 */     if (view == null)
/* 236 */       throw new NullPointerException("view is null"); 
/* 237 */     SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
/* 238 */     if (surfaceHelper != null) {
/* 239 */       surfaceHelper.release();
/*     */     }
/* 241 */     this.mSurfaceHelpers[id] = new SurfaceHelper(id, view);
/*     */   }
/*     */   
/*     */   private void setView(int id, TextureView view) {
/* 245 */     ensureInitState();
/* 246 */     if (view == null)
/* 247 */       throw new NullPointerException("view is null"); 
/* 248 */     SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
/* 249 */     if (surfaceHelper != null) {
/* 250 */       surfaceHelper.release();
/*     */     }
/* 252 */     this.mSurfaceHelpers[id] = new SurfaceHelper(id, view);
/*     */   }
/*     */   
/*     */   private void setSurface(int id, Surface surface, SurfaceHolder surfaceHolder) {
/* 256 */     ensureInitState();
/* 257 */     if (!surface.isValid() && surfaceHolder == null)
/* 258 */       throw new IllegalStateException("surface is not attached and holder is null"); 
/* 259 */     SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
/* 260 */     if (surfaceHelper != null) {
/* 261 */       surfaceHelper.release();
/*     */     }
/* 263 */     this.mSurfaceHelpers[id] = new SurfaceHelper(id, surface, surfaceHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void setVideoView(SurfaceView videoSurfaceView) {
/* 269 */     setView(0, videoSurfaceView);
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void setVideoView(TextureView videoTextureView) {
/* 275 */     setView(0, videoTextureView);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVideoSurface(Surface videoSurface, SurfaceHolder surfaceHolder) {
/* 280 */     setSurface(0, videoSurface, surfaceHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   @TargetApi(14)
/*     */   public void setVideoSurface(SurfaceTexture videoSurfaceTexture) {
/* 286 */     setSurface(0, new Surface(videoSurfaceTexture), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void setSubtitlesView(SurfaceView subtitlesSurfaceView) {
/* 292 */     setView(1, subtitlesSurfaceView);
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void setSubtitlesView(TextureView subtitlesTextureView) {
/* 298 */     setView(1, subtitlesTextureView);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSubtitlesSurface(Surface subtitlesSurface, SurfaceHolder surfaceHolder) {
/* 303 */     setSurface(1, subtitlesSurface, surfaceHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   @TargetApi(14)
/*     */   public void setSubtitlesSurface(SurfaceTexture subtitlesSurfaceTexture) {
/* 309 */     setSurface(1, new Surface(subtitlesSurfaceTexture), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void attachViews(OnNewVideoLayoutListener onNewVideoLayoutListener) {
/* 315 */     if (this.mSurfacesState.get() != 0 || this.mSurfaceHelpers[0] == null)
/* 316 */       throw new IllegalStateException("already attached or video view not configured"); 
/* 317 */     this.mSurfacesState.set(1);
/* 318 */     synchronized (this.mNativeLock) {
/* 319 */       this.mOnNewVideoLayoutListener = onNewVideoLayoutListener;
/* 320 */       this.mNativeLock.buffersGeometryConfigured = false;
/* 321 */       this.mNativeLock.buffersGeometryAbort = false;
/*     */     } 
/* 323 */     for (int id = 0; id < 2; id++) {
/* 324 */       SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
/* 325 */       if (surfaceHelper != null) {
/* 326 */         surfaceHelper.attach();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @MainThread
/*     */   public void attachViews() {
/* 333 */     attachViews(null);
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void detachViews() {
/* 339 */     if (this.mSurfacesState.get() == 0) {
/*     */       return;
/*     */     }
/* 342 */     this.mSurfacesState.set(0);
/* 343 */     this.mHandler.removeCallbacksAndMessages(null);
/* 344 */     synchronized (this.mNativeLock) {
/* 345 */       this.mOnNewVideoLayoutListener = null;
/* 346 */       this.mNativeLock.buffersGeometryAbort = true;
/* 347 */       this.mNativeLock.notifyAll();
/*     */     } 
/* 349 */     for (int id = 0; id < 2; id++) {
/* 350 */       SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
/* 351 */       if (surfaceHelper != null)
/* 352 */         surfaceHelper.release(); 
/* 353 */       this.mSurfaceHelpers[id] = null;
/*     */     } 
/* 355 */     for (Callback cb : this.mIVLCVoutCallbacks)
/* 356 */       cb.onSurfacesDestroyed(this); 
/* 357 */     if (this.mSurfaceCallback != null)
/* 358 */       this.mSurfaceCallback.onSurfacesDestroyed(this); 
/* 359 */     this.mSurfaceTextureThread.release();
/*     */   }
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public boolean areViewsAttached() {
/* 365 */     return (this.mSurfacesState.get() != 0);
/*     */   }
/*     */   
/*     */   @MainThread
/*     */   private void onSurfaceCreated() {
/* 370 */     if (this.mSurfacesState.get() != 1) {
/* 371 */       throw new IllegalArgumentException("invalid state");
/*     */     }
/* 373 */     SurfaceHelper videoHelper = this.mSurfaceHelpers[0];
/* 374 */     SurfaceHelper subtitlesHelper = this.mSurfaceHelpers[1];
/* 375 */     if (videoHelper == null) {
/* 376 */       throw new NullPointerException("videoHelper shouldn't be null here");
/*     */     }
/* 378 */     if (videoHelper.isReady() && (subtitlesHelper == null || subtitlesHelper.isReady())) {
/* 379 */       this.mSurfacesState.set(2);
/* 380 */       for (Callback cb : this.mIVLCVoutCallbacks)
/* 381 */         cb.onSurfacesCreated(this); 
/* 382 */       if (this.mSurfaceCallback != null)
/* 383 */         this.mSurfaceCallback.onSurfacesCreated(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   @MainThread
/*     */   private void onSurfaceDestroyed() {
/* 389 */     detachViews();
/*     */   }
/*     */   
/*     */   boolean areSurfacesWaiting() {
/* 393 */     return (this.mSurfacesState.get() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendMouseEvent(int action, int button, int x, int y) {
/* 398 */     synchronized (this.mNativeLock) {
/* 399 */       if (this.mCallbackNativeHandle != 0L && (this.mMouseAction != action || this.mMouseButton != button || this.mMouseX != x || this.mMouseY != y))
/*     */       {
/* 401 */         nativeOnMouseEvent(this.mCallbackNativeHandle, action, button, x, y); } 
/* 402 */       this.mMouseAction = action;
/* 403 */       this.mMouseButton = button;
/* 404 */       this.mMouseX = x;
/* 405 */       this.mMouseY = y;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWindowSize(int width, int height) {
/* 411 */     synchronized (this.mNativeLock) {
/* 412 */       if (this.mCallbackNativeHandle != 0L && (this.mWindowWidth != width || this.mWindowHeight != height))
/* 413 */         nativeOnWindowSize(this.mCallbackNativeHandle, width, height); 
/* 414 */       this.mWindowWidth = width;
/* 415 */       this.mWindowHeight = height;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setNativeSurface(int id, Surface surface) {
/* 420 */     synchronized (this.mNativeLock) {
/* 421 */       this.mSurfaces[id] = surface;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Surface getNativeSurface(int id) {
/* 426 */     synchronized (this.mNativeLock) {
/* 427 */       return this.mSurfaces[id];
/*     */     } 
/*     */   }
/*     */   private static class NativeLock {
/*     */     private NativeLock() {}
/*     */     private boolean buffersGeometryConfigured = false;
/*     */     private boolean buffersGeometryAbort = false; }
/*     */   
/* 435 */   public AWindow(SurfaceCallback surfaceCallback) { this.mNativeLock = new NativeLock(); this.mSurfaceCallback = surfaceCallback; this.mSurfaceHelpers = new SurfaceHelper[2]; this.mSurfaceHelpers[0] = null;
/*     */     this.mSurfaceHelpers[1] = null;
/*     */     this.mSurfaces = new Surface[2];
/*     */     this.mSurfaces[0] = null;
/* 439 */     this.mSurfaces[1] = null; } public void addCallback(Callback callback) { if (!this.mIVLCVoutCallbacks.contains(callback)) {
/* 440 */       this.mIVLCVoutCallbacks.add(callback);
/*     */     } }
/*     */ 
/*     */   
/*     */   public void removeCallback(Callback callback) {
/* 445 */     this.mIVLCVoutCallbacks.remove(callback);
/*     */   }
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
/*     */   private Surface getVideoSurface() {
/* 477 */     return getNativeSurface(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Surface getSubtitlesSurface() {
/* 487 */     return getNativeSurface(1);
/*     */   }
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
/*     */   private int registerNative(long nativeHandle) {
/* 503 */     if (nativeHandle == 0L)
/* 504 */       throw new IllegalArgumentException("nativeHandle is null"); 
/* 505 */     synchronized (this.mNativeLock) {
/* 506 */       if (this.mCallbackNativeHandle != 0L)
/* 507 */         return 0; 
/* 508 */       this.mCallbackNativeHandle = nativeHandle;
/* 509 */       if (this.mMouseAction != -1)
/* 510 */         nativeOnMouseEvent(this.mCallbackNativeHandle, this.mMouseAction, this.mMouseButton, this.mMouseX, this.mMouseY); 
/* 511 */       if (this.mWindowWidth != -1 && this.mWindowHeight != -1)
/* 512 */         nativeOnWindowSize(this.mCallbackNativeHandle, this.mWindowWidth, this.mWindowHeight); 
/* 513 */       int flags = 1;
/*     */       
/* 515 */       if (this.mOnNewVideoLayoutListener != null)
/* 516 */         flags |= 0x2; 
/* 517 */       return flags;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void unregisterNative() {
/* 523 */     synchronized (this.mNativeLock) {
/* 524 */       if (this.mCallbackNativeHandle == 0L)
/* 525 */         throw new IllegalArgumentException("unregister called when not registered"); 
/* 526 */       this.mCallbackNativeHandle = 0L;
/*     */     } 
/*     */   }
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
/*     */   private boolean setBuffersGeometry(Surface surface, int width, int height, int format) {
/* 542 */     return false;
/*     */   }
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
/*     */   private void setVideoLayout(final int width, final int height, final int visibleWidth, final int visibleHeight, final int sarNum, final int sarDen) {
/* 560 */     this.mHandler.post(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 564 */             if (AWindow.this.mOnNewVideoLayoutListener != null)
/* 565 */               AWindow.this.mOnNewVideoLayoutListener.onNewVideoLayout(AWindow.this, width, height, visibleWidth, visibleHeight, sarNum, sarDen); 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   @TargetApi(16)
/*     */   private static class SurfaceTextureThread
/*     */     implements Runnable, SurfaceTexture.OnFrameAvailableListener
/*     */   {
/* 574 */     private SurfaceTexture mSurfaceTexture = null;
/* 575 */     private Surface mSurface = null;
/*     */     
/*     */     private boolean mFrameAvailable = false;
/* 578 */     private Looper mLooper = null;
/* 579 */     private Thread mThread = null;
/*     */ 
/*     */     
/*     */     private boolean mIsAttached = false;
/*     */ 
/*     */     
/*     */     private boolean mDoRelease = false;
/*     */ 
/*     */ 
/*     */     
/*     */     private synchronized boolean createSurface() {
/* 590 */       if (this.mSurfaceTexture == null) {
/*     */         
/* 592 */         this.mThread = new Thread(this);
/* 593 */         this.mThread.start();
/* 594 */         while (this.mSurfaceTexture == null) {
/*     */           try {
/* 596 */             wait();
/* 597 */           } catch (InterruptedException ignored) {
/* 598 */             return false;
/*     */           } 
/*     */         } 
/* 601 */         this.mSurface = new Surface(this.mSurfaceTexture);
/*     */       } 
/* 603 */       return true;
/*     */     }
/*     */     
/*     */     private synchronized boolean attachToGLContext(int texName) {
/* 607 */       if (!createSurface())
/* 608 */         return false; 
/* 609 */       this.mSurfaceTexture.attachToGLContext(texName);
/* 610 */       this.mFrameAvailable = false;
/* 611 */       this.mIsAttached = true;
/* 612 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
/* 617 */       if (surfaceTexture == this.mSurfaceTexture) {
/* 618 */         if (this.mFrameAvailable)
/* 619 */           throw new IllegalStateException("An available frame was not updated"); 
/* 620 */         this.mFrameAvailable = true;
/* 621 */         notify();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 627 */       Looper.prepare();
/*     */       
/* 629 */       synchronized (this) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 636 */         this.mLooper = Looper.myLooper();
/* 637 */         this.mSurfaceTexture = new SurfaceTexture(0);
/*     */         
/* 639 */         this.mSurfaceTexture.detachFromGLContext();
/* 640 */         this.mSurfaceTexture.setOnFrameAvailableListener(this);
/* 641 */         notify();
/*     */       } 
/*     */       
/* 644 */       Looper.loop();
/*     */     }
/*     */     
/*     */     private synchronized void detachFromGLContext() {
/* 648 */       if (this.mDoRelease) {
/* 649 */         this.mLooper.quit();
/* 650 */         this.mLooper = null;
/*     */         
/*     */         try {
/* 653 */           this.mThread.join();
/* 654 */         } catch (InterruptedException interruptedException) {}
/*     */         
/* 656 */         this.mThread = null;
/*     */         
/* 658 */         this.mSurface.release();
/* 659 */         this.mSurface = null;
/* 660 */         this.mSurfaceTexture.release();
/* 661 */         this.mSurfaceTexture = null;
/* 662 */         this.mDoRelease = false;
/*     */       } else {
/* 664 */         this.mSurfaceTexture.detachFromGLContext();
/*     */       } 
/* 666 */       this.mIsAttached = false;
/*     */     }
/*     */     
/*     */     private boolean waitAndUpdateTexImage(float[] transformMatrix) {
/* 670 */       synchronized (this) {
/* 671 */         while (!this.mFrameAvailable) {
/*     */           try {
/* 673 */             wait(500L);
/* 674 */             if (!this.mFrameAvailable)
/* 675 */               return false; 
/* 676 */           } catch (InterruptedException interruptedException) {}
/*     */         } 
/*     */         
/* 679 */         this.mFrameAvailable = false;
/*     */       } 
/* 681 */       this.mSurfaceTexture.updateTexImage();
/* 682 */       this.mSurfaceTexture.getTransformMatrix(transformMatrix);
/* 683 */       return true;
/*     */     }
/*     */     
/*     */     private synchronized Surface getSurface() {
/* 687 */       if (!createSurface())
/* 688 */         return null; 
/* 689 */       return this.mSurface;
/*     */     }
/*     */     
/*     */     private synchronized void release() {
/* 693 */       if (this.mSurfaceTexture != null) {
/* 694 */         if (this.mIsAttached) {
/*     */           
/* 696 */           this.mDoRelease = true;
/*     */         } else {
/* 698 */           this.mSurface.release();
/* 699 */           this.mSurface = null;
/* 700 */           this.mSurfaceTexture.release();
/* 701 */           this.mSurfaceTexture = null;
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private SurfaceTextureThread() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean SurfaceTexture_attachToGLContext(int texName) {
/* 715 */     return this.mSurfaceTextureThread.attachToGLContext(texName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void SurfaceTexture_detachFromGLContext() {
/* 723 */     this.mSurfaceTextureThread.detachFromGLContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean SurfaceTexture_waitAndUpdateTexImage(float[] transformMatrix) {
/* 733 */     return this.mSurfaceTextureThread.waitAndUpdateTexImage(transformMatrix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Surface SurfaceTexture_getSurface() {
/* 741 */     return this.mSurfaceTextureThread.getSurface();
/*     */   }
/*     */   
/*     */   private static native void nativeOnMouseEvent(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native void nativeOnWindowSize(long paramLong, int paramInt1, int paramInt2);
/*     */   
/*     */   public static interface SurfaceCallback {
/*     */     @MainThread
/*     */     void onSurfacesCreated(AWindow param1AWindow);
/*     */     
/*     */     @MainThread
/*     */     void onSurfacesDestroyed(AWindow param1AWindow);
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\AWindow.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */