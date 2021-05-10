/*      */ package org.videolan.libvlc;
/*      */ 
/*      */ import android.annotation.TargetApi;
/*      */ import android.content.BroadcastReceiver;
/*      */ import android.content.Context;
/*      */ import android.content.Intent;
/*      */ import android.content.IntentFilter;
/*      */ import android.content.res.AssetFileDescriptor;
/*      */ import android.media.AudioDeviceCallback;
/*      */ import android.media.AudioDeviceInfo;
/*      */ import android.media.AudioManager;
/*      */ import android.net.Uri;
/*      */ import android.os.Handler;
/*      */ import android.os.Looper;
/*      */ import android.util.SparseArray;
/*      */ import androidx.annotation.NonNull;
/*      */ import androidx.annotation.Nullable;
/*      */ import androidx.annotation.RequiresApi;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*      */ import org.videolan.libvlc.interfaces.ILibVLC;
/*      */ import org.videolan.libvlc.interfaces.IMedia;
/*      */ import org.videolan.libvlc.interfaces.IVLCObject;
/*      */ import org.videolan.libvlc.interfaces.IVLCVout;
/*      */ import org.videolan.libvlc.util.AndroidUtil;
/*      */ import org.videolan.libvlc.util.DisplayManager;
/*      */ import org.videolan.libvlc.util.VLCUtil;
/*      */ import org.videolan.libvlc.util.VLCVideoLayout;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MediaPlayer
/*      */   extends VLCObject<MediaPlayer.Event>
/*      */ {
/*      */   public static class Event
/*      */     extends AbstractVLCEvent
/*      */   {
/*      */     public static final int MediaChanged = 256;
/*      */     public static final int Opening = 258;
/*      */     public static final int Buffering = 259;
/*      */     public static final int Playing = 260;
/*      */     public static final int Paused = 261;
/*      */     public static final int Stopped = 262;
/*      */     public static final int EndReached = 265;
/*      */     public static final int EncounteredError = 266;
/*      */     public static final int TimeChanged = 267;
/*      */     public static final int PositionChanged = 268;
/*      */     public static final int SeekableChanged = 269;
/*      */     public static final int PausableChanged = 270;
/*      */     public static final int LengthChanged = 273;
/*      */     public static final int Vout = 274;
/*      */     public static final int ESAdded = 276;
/*      */     public static final int ESDeleted = 277;
/*      */     public static final int ESSelected = 278;
/*      */     public static final int RecordChanged = 286;
/*      */     
/*      */     protected Event(int type) {
/*   95 */       super(type);
/*      */     }
/*      */     protected Event(int type, long arg1) {
/*   98 */       super(type, arg1);
/*      */     }
/*      */     
/*      */     protected Event(int type, long arg1, long arg2) {
/*  102 */       super(type, arg1, arg2);
/*      */     }
/*      */     
/*      */     protected Event(int type, float argf) {
/*  106 */       super(type, argf);
/*      */     }
/*      */     
/*      */     protected Event(int type, long arg1, @Nullable String args1) {
/*  110 */       super(type, arg1, args1);
/*      */     }
/*      */     
/*      */     public long getTimeChanged() {
/*  114 */       return this.arg1;
/*      */     }
/*      */     
/*      */     public long getLengthChanged() {
/*  118 */       return this.arg1;
/*      */     }
/*      */     
/*      */     public float getPositionChanged() {
/*  122 */       return this.argf1;
/*      */     }
/*      */     public int getVoutCount() {
/*  125 */       return (int)this.arg1;
/*      */     }
/*      */     public int getEsChangedType() {
/*  128 */       return (int)this.arg1;
/*      */     }
/*      */     public int getEsChangedID() {
/*  131 */       return (int)this.arg2;
/*      */     }
/*      */     public boolean getPausable() {
/*  134 */       return (this.arg1 != 0L);
/*      */     }
/*      */     public boolean getSeekable() {
/*  137 */       return (this.arg1 != 0L);
/*      */     }
/*      */     public float getBuffering() {
/*  140 */       return this.argf1;
/*      */     }
/*      */     public boolean getRecording() {
/*  143 */       return (this.arg1 != 0L);
/*      */     }
/*      */     @Nullable
/*      */     public String getRecordPath() {
/*  147 */       return this.args1;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Position
/*      */   {
/*      */     public static final int Disable = -1;
/*      */     
/*      */     public static final int Center = 0;
/*      */     
/*      */     public static final int Left = 1;
/*      */     
/*      */     public static final int Right = 2;
/*      */     
/*      */     public static final int Top = 3;
/*      */     
/*      */     public static final int TopLeft = 4;
/*      */     
/*      */     public static final int TopRight = 5;
/*      */     
/*      */     public static final int Bottom = 6;
/*      */     public static final int BottomLeft = 7;
/*      */     public static final int BottomRight = 8;
/*      */   }
/*      */   
/*      */   public static class Navigate
/*      */   {
/*      */     public static final int Activate = 0;
/*      */     public static final int Up = 1;
/*      */     public static final int Down = 2;
/*      */     public static final int Left = 3;
/*      */     public static final int Right = 4;
/*      */   }
/*      */   
/*      */   public static class Title
/*      */   {
/*      */     public final long duration;
/*      */     public final String name;
/*      */     private final int flags;
/*      */     
/*      */     private static class Flags
/*      */     {
/*      */       public static final int MENU = 1;
/*      */       public static final int INTERACTIVE = 2;
/*      */     }
/*      */     
/*      */     public Title(long duration, String name, int flags) {
/*  195 */       this.duration = duration;
/*  196 */       this.name = name;
/*  197 */       this.flags = flags;
/*      */     }
/*      */     
/*      */     public boolean isMenu() {
/*  201 */       return ((this.flags & 0x1) != 0);
/*      */     }
/*      */     
/*      */     public boolean isInteractive() {
/*  205 */       return ((this.flags & 0x2) != 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static Title createTitleFromNative(long duration, String name, int flags) {
/*  211 */     return new Title(duration, name, flags);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Chapter
/*      */   {
/*      */     public final long timeOffset;
/*      */ 
/*      */ 
/*      */     
/*      */     public final long duration;
/*      */ 
/*      */     
/*      */     public final String name;
/*      */ 
/*      */ 
/*      */     
/*      */     private Chapter(long timeOffset, long duration, String name) {
/*  231 */       this.timeOffset = timeOffset;
/*  232 */       this.duration = duration;
/*  233 */       this.name = name;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static Chapter createChapterFromNative(long timeOffset, long duration, String name) {
/*  239 */     return new Chapter(timeOffset, duration, name);
/*      */   }
/*      */   
/*      */   public static class TrackDescription {
/*      */     public final int id;
/*      */     public final String name;
/*      */     
/*      */     private TrackDescription(int id, String name) {
/*  247 */       this.id = id;
/*  248 */       this.name = name;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static TrackDescription createTrackDescriptionFromNative(int id, String name) {
/*  254 */     return new TrackDescription(id, name);
/*      */   }
/*      */   
/*      */   public static class Equalizer
/*      */   {
/*      */     private long mInstance;
/*      */     
/*      */     private Equalizer() {
/*  262 */       nativeNew();
/*      */     }
/*      */     
/*      */     private Equalizer(int index) {
/*  266 */       nativeNewFromPreset(index);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void finalize() throws Throwable {
/*      */       try {
/*  272 */         nativeRelease();
/*      */       } finally {
/*  274 */         super.finalize();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Equalizer create() {
/*  284 */       return new Equalizer();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Equalizer createFromPreset(int index) {
/*  294 */       return new Equalizer(index);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static int getPresetCount() {
/*  301 */       return nativeGetPresetCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static String getPresetName(int index) {
/*  314 */       return nativeGetPresetName(index);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static int getBandCount() {
/*  321 */       return nativeGetBandCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static float getBandFrequency(int index) {
/*  333 */       return nativeGetBandFrequency(index);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getPreAmp() {
/*  342 */       return nativeGetPreAmp();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean setPreAmp(float preamp) {
/*  355 */       return nativeSetPreAmp(preamp);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getAmp(int index) {
/*  365 */       return nativeGetAmp(index);
/*      */     }
/*      */ 
/*      */     
/*      */     private static native int nativeGetPresetCount();
/*      */ 
/*      */     
/*      */     private static native String nativeGetPresetName(int param1Int);
/*      */ 
/*      */     
/*      */     private static native int nativeGetBandCount();
/*      */ 
/*      */     
/*      */     public boolean setAmp(int index, float amp) {
/*  379 */       return nativeSetAmp(index, amp);
/*      */     }
/*      */     private static native float nativeGetBandFrequency(int param1Int);
/*      */     
/*      */     private native void nativeNew();
/*      */     
/*      */     private native void nativeNewFromPreset(int param1Int);
/*      */     
/*      */     private native void nativeRelease();
/*      */     
/*      */     private native float nativeGetPreAmp();
/*      */     
/*      */     private native boolean nativeSetPreAmp(float param1Float);
/*      */     
/*      */     private native float nativeGetAmp(int param1Int);
/*      */     
/*      */     private native boolean nativeSetAmp(int param1Int, float param1Float); }
/*      */   
/*  397 */   public enum ScaleType { SURFACE_BEST_FIT,
/*  398 */     SURFACE_FIT_SCREEN,
/*  399 */     SURFACE_FILL,
/*  400 */     SURFACE_16_9,
/*  401 */     SURFACE_4_3,
/*  402 */     SURFACE_ORIGINAL; }
/*      */   
/*  404 */   public static final int SURFACE_SCALES_COUNT = (ScaleType.values()).length;
/*      */   
/*  406 */   private IMedia mMedia = null;
/*  407 */   private RendererItem mRenderer = null;
/*  408 */   private AssetFileDescriptor mAfd = null;
/*      */   private boolean mPlaying = false;
/*      */   private boolean mPlayRequested = false;
/*      */   private boolean mListenAudioPlug = true;
/*  412 */   private int mVoutCount = 0;
/*      */   private boolean mAudioReset = false;
/*  414 */   private String mAudioOutput = "android_audiotrack";
/*  415 */   private String mAudioOutputDevice = null;
/*      */   
/*      */   private boolean mAudioPlugRegistered = false;
/*      */   private boolean mAudioDigitalOutputEnabled = false;
/*  419 */   private String mAudioPlugOutputDevice = "stereo";
/*      */ 
/*      */   
/*      */   private boolean mCanDoPassthrough;
/*      */   
/*  424 */   private VideoHelper mVideoHelper = null;
/*      */   
/*  426 */   private final AWindow mWindow = new AWindow(new AWindow.SurfaceCallback()
/*      */       {
/*      */         public void onSurfacesCreated(AWindow vout) {
/*  429 */           boolean play = false;
/*  430 */           boolean enableVideo = false;
/*  431 */           synchronized (MediaPlayer.this) {
/*  432 */             if (!MediaPlayer.this.mPlaying && MediaPlayer.this.mPlayRequested) {
/*  433 */               play = true;
/*  434 */             } else if (MediaPlayer.this.mVoutCount == 0) {
/*  435 */               enableVideo = true;
/*      */             } 
/*  437 */           }  if (play) {
/*  438 */             MediaPlayer.this.play();
/*  439 */           } else if (enableVideo) {
/*  440 */             MediaPlayer.this.setVideoTrackEnabled(true);
/*      */           } 
/*      */         }
/*      */         
/*      */         public void onSurfacesDestroyed(AWindow vout) {
/*  445 */           boolean disableVideo = false;
/*  446 */           synchronized (MediaPlayer.this) {
/*  447 */             if (MediaPlayer.this.mVoutCount > 0)
/*  448 */               disableVideo = true; 
/*      */           } 
/*  450 */           if (disableVideo)
/*  451 */             MediaPlayer.this.setVideoTrackEnabled(false); 
/*      */         }
/*      */       });
/*      */   
/*      */   private synchronized void updateAudioOutputDevice(long encodingFlags, String defaultDevice) {
/*  456 */     this.mCanDoPassthrough = (encodingFlags != 0L);
/*  457 */     String newDeviceId = (this.mAudioDigitalOutputEnabled && this.mCanDoPassthrough) ? ("encoded:" + encodingFlags) : defaultDevice;
/*  458 */     if (!newDeviceId.equals(this.mAudioPlugOutputDevice)) {
/*  459 */       this.mAudioPlugOutputDevice = newDeviceId;
/*  460 */       setAudioOutputDeviceInternal(this.mAudioPlugOutputDevice, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isEncoded(int encoding) {
/*  465 */     switch (encoding) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 14:
/*  471 */         return true;
/*      */     } 
/*  473 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private long getEncodingFlags(int[] encodings) {
/*  478 */     if (encodings == null)
/*  479 */       return 0L; 
/*  480 */     long encodingFlags = 0L;
/*  481 */     for (int encoding : encodings) {
/*  482 */       if (isEncoded(encoding))
/*  483 */         encodingFlags |= (1 << encoding); 
/*      */     } 
/*  485 */     return encodingFlags;
/*      */   }
/*      */   
/*      */   @TargetApi(21)
/*      */   private BroadcastReceiver createAudioPlugReceiver() {
/*  490 */     return new BroadcastReceiver()
/*      */       {
/*      */         public void onReceive(Context context, Intent intent) {
/*  493 */           String action = intent.getAction();
/*  494 */           if (action == null)
/*      */             return; 
/*  496 */           if (action.equalsIgnoreCase("android.media.action.HDMI_AUDIO_PLUG")) {
/*  497 */             boolean hasHdmi = (intent.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) == 1);
/*      */             
/*  499 */             long encodingFlags = !hasHdmi ? 0L : MediaPlayer.this.getEncodingFlags(intent.getIntArrayExtra("android.media.extra.ENCODINGS"));
/*  500 */             MediaPlayer.this.updateAudioOutputDevice(encodingFlags, "stereo");
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*  506 */   private final BroadcastReceiver mAudioPlugReceiver = (AndroidUtil.isLolliPopOrLater && !AndroidUtil.isMarshMallowOrLater) ? 
/*  507 */     createAudioPlugReceiver() : null;
/*      */   
/*      */   @TargetApi(21)
/*      */   private void registerAudioPlugV21(boolean register) {
/*  511 */     if (register) {
/*  512 */       IntentFilter intentFilter = new IntentFilter("android.media.action.HDMI_AUDIO_PLUG");
/*  513 */       Intent stickyIntent = this.mILibVLC.getAppContext().registerReceiver(this.mAudioPlugReceiver, intentFilter);
/*  514 */       if (stickyIntent != null)
/*  515 */         this.mAudioPlugReceiver.onReceive(this.mILibVLC.getAppContext(), stickyIntent); 
/*      */     } else {
/*  517 */       this.mILibVLC.getAppContext().unregisterReceiver(this.mAudioPlugReceiver);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @TargetApi(23)
/*      */   private AudioDeviceCallback createAudioDeviceCallback() {
/*  524 */     return new AudioDeviceCallback()
/*      */       {
/*  526 */         private SparseArray<Long> mEncodedDevices = new SparseArray();
/*      */         
/*      */         private void onAudioDevicesChanged() {
/*  529 */           long encodingFlags = 0L;
/*  530 */           for (int i = 0; i < this.mEncodedDevices.size(); i++) {
/*  531 */             encodingFlags |= ((Long)this.mEncodedDevices.valueAt(i)).longValue();
/*      */           }
/*      */ 
/*      */           
/*  535 */           String defaultDevice = (encodingFlags == 0L) ? "stereo" : "pcm";
/*  536 */           MediaPlayer.this.updateAudioOutputDevice(encodingFlags, defaultDevice);
/*      */         }
/*      */ 
/*      */         
/*      */         @RequiresApi(23)
/*      */         public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
/*  542 */           for (AudioDeviceInfo info : addedDevices) {
/*  543 */             if (info.isSink()) {
/*      */               
/*  545 */               long encodingFlags = MediaPlayer.this.getEncodingFlags(info.getEncodings());
/*  546 */               if (encodingFlags != 0L)
/*  547 */                 this.mEncodedDevices.put(info.getId(), Long.valueOf(encodingFlags)); 
/*      */             } 
/*  549 */           }  onAudioDevicesChanged();
/*      */         }
/*      */ 
/*      */         
/*      */         @RequiresApi(23)
/*      */         public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
/*  555 */           for (AudioDeviceInfo info : removedDevices) {
/*  556 */             if (info.isSink())
/*      */             {
/*  558 */               this.mEncodedDevices.remove(info.getId()); } 
/*      */           } 
/*  560 */           onAudioDevicesChanged();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*  565 */   private final AudioDeviceCallback mAudioDeviceCallback = AndroidUtil.isMarshMallowOrLater ? 
/*  566 */     createAudioDeviceCallback() : null;
/*      */   
/*      */   @TargetApi(23)
/*      */   private void registerAudioPlugV23(boolean register) {
/*  570 */     AudioManager am = (AudioManager)this.mILibVLC.getAppContext().getSystemService(AudioManager.class);
/*  571 */     if (register) {
/*  572 */       this.mAudioDeviceCallback.onAudioDevicesAdded(am.getDevices(2));
/*  573 */       am.registerAudioDeviceCallback(this.mAudioDeviceCallback, null);
/*      */     } else {
/*  575 */       am.unregisterAudioDeviceCallback(this.mAudioDeviceCallback);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void registerAudioPlug(boolean register) {
/*  580 */     if (register == this.mAudioPlugRegistered)
/*      */       return; 
/*  582 */     if (this.mAudioDeviceCallback != null) {
/*  583 */       registerAudioPlugV23(register);
/*  584 */     } else if (this.mAudioPlugReceiver != null) {
/*  585 */       registerAudioPlugV21(register);
/*  586 */     }  this.mAudioPlugRegistered = register;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  593 */   Handler mHandlerMainThread = new Handler(Looper.getMainLooper());
/*      */   
/*      */   private ByteBuffer mBuffer;
/*      */   
/*      */   private MediaPlayCallback mCallback;
/*      */ 
/*      */   
/*      */   public MediaPlayer(ILibVLC iLibVLC) {
/*  601 */     super(iLibVLC);
/*  602 */     nativeNewFromLibVlc(iLibVLC, this.mWindow);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaPlayer(@NonNull IMedia media) {
/*  611 */     super((IVLCObject)media);
/*  612 */     if (media == null || media.isReleased())
/*  613 */       throw new IllegalArgumentException("Media is null or released"); 
/*  614 */     this.mMedia = media;
/*  615 */     this.mMedia.retain();
/*  616 */     nativeNewFromMedia(this.mMedia, this.mWindow);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public IVLCVout getVLCVout() {
/*  624 */     return this.mWindow;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void attachViews(@NonNull VLCVideoLayout surfaceFrame, @Nullable DisplayManager dm, boolean subtitles, boolean textureView) {
/*  636 */     this.mVideoHelper = new VideoHelper(this, surfaceFrame, dm, subtitles, textureView);
/*  637 */     this.mVideoHelper.attachViews();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void detachViews() {
/*  644 */     if (this.mVideoHelper != null) {
/*  645 */       this.mVideoHelper.release();
/*  646 */       this.mVideoHelper = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
///*      */   public void updateVideoSurfaces() {
///*  654 */     if (this.mVideoHelper != null) this.mVideoHelper.updateVideoSurfaces();
///*      */
///*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVideoScale(@NonNull ScaleType type) {
/*  662 */     if (this.mVideoHelper != null) this.mVideoHelper.setVideoScale(type);
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public ScaleType getVideoScale() {
/*  671 */     return (this.mVideoHelper != null) ? this.mVideoHelper.getVideoScale() : ScaleType.SURFACE_BEST_FIT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMedia(@Nullable IMedia media) {
/*  680 */     if (media != null) {
/*  681 */       if (media.isReleased())
/*  682 */         throw new IllegalArgumentException("Media is released"); 
/*  683 */       media.setDefaultMediaPlayerOptions();
/*      */     } 
/*  685 */     nativeSetMedia(media);
/*  686 */     synchronized (this) {
/*  687 */       if (this.mMedia != null) {
/*  688 */         this.mMedia.release();
/*      */       }
/*  690 */       if (media != null)
/*  691 */         media.retain(); 
/*  692 */       this.mMedia = media;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int setRenderer(@Nullable RendererItem item) {
/*  701 */     if (this.mRenderer != null) this.mRenderer.release(); 
/*  702 */     if (item != null) item.retain(); 
/*  703 */     this.mRenderer = item;
/*  704 */     return nativeSetRenderer(item);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean hasMedia() {
/*  712 */     return (this.mMedia != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public synchronized IMedia getMedia() {
/*  720 */     if (this.mMedia != null)
/*  721 */       this.mMedia.retain(); 
/*  722 */     return this.mMedia;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void play() {
/*  730 */     synchronized (this) {
/*  731 */       if (!this.mPlaying) {
/*      */         
/*  733 */         if (this.mAudioReset) {
/*  734 */           if (this.mAudioOutput != null)
/*  735 */             nativeSetAudioOutput(this.mAudioOutput); 
/*  736 */           if (this.mAudioOutputDevice != null)
/*  737 */             nativeSetAudioOutputDevice(this.mAudioOutputDevice); 
/*  738 */           this.mAudioReset = false;
/*      */         } 
/*  740 */         if (this.mListenAudioPlug)
/*  741 */           registerAudioPlug(true); 
/*  742 */         this.mPlayRequested = true;
/*  743 */         if (this.mWindow.areSurfacesWaiting())
/*      */           return; 
/*      */       } 
/*  746 */       this.mPlaying = true;
/*      */     } 
/*  748 */     nativePlay();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void playAsset(@NonNull Context context, @NonNull String assetFilename) throws IOException {
/*  758 */     this.mAfd = context.getAssets().openFd(assetFilename);
/*  759 */     play(this.mAfd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void play(@NonNull AssetFileDescriptor afd) {
/*  767 */     IMedia media = new Media(this.mILibVLC, afd);
/*  768 */     play(media);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void play(@NonNull String path) {
/*  776 */     IMedia media = new Media(this.mILibVLC, path);
/*  777 */     play(media);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void play(@NonNull Uri uri) {
/*  785 */     IMedia media = new Media(this.mILibVLC, uri);
/*  786 */     play(media);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void play(@NonNull IMedia media) {
/*  794 */     setMedia(media);
/*  795 */     media.release();
/*  796 */     play();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void stop() {
/*  804 */     synchronized (this) {
/*  805 */       this.mPlayRequested = false;
/*  806 */       this.mPlaying = false;
/*  807 */       this.mAudioReset = true;
/*      */     } 
/*  809 */     nativeStop();
/*  810 */     if (this.mAfd != null) {
/*  811 */       try { this.mAfd.close(); }
/*  812 */       catch (IOException iOException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVideoTitleDisplay(int position, int timeout) {
/*  822 */     nativeSetVideoTitleDisplay(position, timeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getScale() {
/*  832 */     return nativeGetScale();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScale(float scale) {
/*  845 */     nativeSetScale(scale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAspectRatio() {
/*  854 */     return nativeGetAspectRatio();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAspectRatio(String aspect) {
/*  863 */     nativeSetAspectRatio(aspect);
/*      */   }
/*      */   
/*      */   private boolean isAudioTrack() {
/*  867 */     return (this.mAudioOutput != null && this.mAudioOutput.equals("android_audiotrack"));
/*      */   }
/*      */   
/*      */   public boolean takeSnapShot(int number, String path, int width, int height) {
/*  871 */     return nativeTakeSnapShot(number, path, width, height);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateViewpoint(float yaw, float pitch, float roll, float fov, boolean absolute) {
/*  886 */     return nativeUpdateViewpoint(yaw, pitch, roll, fov, absolute);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean setAudioOutput(String aout) {
/*  902 */     this.mAudioOutput = aout;
/*      */ 
/*      */     
/*  905 */     this.mListenAudioPlug = isAudioTrack();
/*  906 */     if (!this.mListenAudioPlug) {
/*  907 */       registerAudioPlug(false);
/*      */     }
/*  909 */     boolean ret = nativeSetAudioOutput(aout);
/*      */     
/*  911 */     if (!ret) {
/*  912 */       this.mAudioOutput = null;
/*  913 */       this.mListenAudioPlug = false;
/*      */     } 
/*      */     
/*  916 */     if (this.mListenAudioPlug) {
/*  917 */       registerAudioPlug(true);
/*      */     }
/*  919 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean setAudioDigitalOutputEnabled(boolean enabled) {
/*  932 */     if (enabled == this.mAudioDigitalOutputEnabled)
/*  933 */       return true; 
/*  934 */     if (!this.mListenAudioPlug || !isAudioTrack()) {
/*  935 */       return false;
/*      */     }
/*  937 */     registerAudioPlug(false);
/*  938 */     this.mAudioDigitalOutputEnabled = enabled;
/*  939 */     registerAudioPlug(true);
/*  940 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean forceAudioDigitalEncodings(int[] encodings) {
/*  950 */     if (!isAudioTrack()) {
/*  951 */       return false;
/*      */     }
/*  953 */     if (encodings.length == 0) {
/*  954 */       setAudioOutputDeviceInternal(null, true);
/*      */     } else {
/*  956 */       String newDeviceId = "encoded:" + getEncodingFlags(encodings);
/*  957 */       if (!newDeviceId.equals(this.mAudioPlugOutputDevice)) {
/*  958 */         this.mAudioPlugOutputDevice = newDeviceId;
/*  959 */         setAudioOutputDeviceInternal(this.mAudioPlugOutputDevice, true);
/*      */       } 
/*      */     } 
/*  962 */     return true;
/*      */   }
/*      */   
/*      */   private synchronized boolean setAudioOutputDeviceInternal(String id, boolean fromUser) {
/*  966 */     this.mAudioOutputDevice = id;
/*  967 */     if (fromUser) {
/*      */       
/*  969 */       this.mListenAudioPlug = (this.mAudioOutputDevice == null && isAudioTrack());
/*  970 */       if (!this.mListenAudioPlug) {
/*  971 */         registerAudioPlug(false);
/*      */       }
/*      */     } 
/*  974 */     boolean ret = nativeSetAudioOutputDevice(id);
/*      */     
/*  976 */     if (!ret) {
/*  977 */       this.mAudioOutputDevice = null;
/*  978 */       this.mListenAudioPlug = false;
/*      */     } 
/*      */     
/*  981 */     if (this.mListenAudioPlug) {
/*  982 */       registerAudioPlug(true);
/*      */     }
/*  984 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setAudioOutputDevice(String id) {
/* 1004 */     return setAudioOutputDeviceInternal(id, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Title[] getTitles() {
/* 1013 */     return nativeGetTitles();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chapter[] getChapters(int title) {
/* 1023 */     return nativeGetChapters(title);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getVideoTracksCount() {
/* 1030 */     return nativeGetVideoTracksCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TrackDescription[] getVideoTracks() {
/* 1037 */     return nativeGetVideoTracks();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getVideoTrack() {
/* 1046 */     return nativeGetVideoTrack();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setVideoTrack(int index) {
/* 1056 */     if (index == -1 || (this.mWindow.areViewsAttached() && !this.mWindow.areSurfacesWaiting())) {
/* 1057 */       return nativeSetVideoTrack(index);
/*      */     }
/* 1059 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVideoTrackEnabled(boolean enabled) {
/* 1068 */     if (!enabled) {
/* 1069 */       setVideoTrack(-1);
/* 1070 */     } else if (!isReleased() && hasMedia() && getVideoTrack() == -1) {
/* 1071 */       TrackDescription[] tracks = getVideoTracks();
/* 1072 */       if (tracks != null) {
/* 1073 */         for (TrackDescription track : tracks) {
/* 1074 */           if (track.id != -1) {
/* 1075 */             setVideoTrack(track.id);
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IMedia.VideoTrack getCurrentVideoTrack() {
/* 1087 */     if (getVideoTrack() == -1)
/* 1088 */       return null; 
/* 1089 */     int trackCount = this.mMedia.getTrackCount();
/* 1090 */     for (int i = 0; i < trackCount; i++) {
/* 1091 */       IMedia.Track track = this.mMedia.getTrack(i);
/* 1092 */       if (track.type == 1)
/* 1093 */         return (IMedia.VideoTrack)track; 
/*      */     } 
/* 1095 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAudioTracksCount() {
/* 1102 */     return nativeGetAudioTracksCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TrackDescription[] getAudioTracks() {
/* 1109 */     return nativeGetAudioTracks();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAudioTrack() {
/* 1118 */     return nativeGetAudioTrack();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setAudioTrack(int index) {
/* 1127 */     return nativeSetAudioTrack(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getAudioDelay() {
/* 1136 */     return nativeGetAudioDelay();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setAudioDelay(long delay) {
/* 1146 */     return nativeSetAudioDelay(delay);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSpuTracksCount() {
/* 1153 */     return nativeGetSpuTracksCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TrackDescription[] getSpuTracks() {
/* 1160 */     return nativeGetSpuTracks();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSpuTrack() {
/* 1169 */     return nativeGetSpuTrack();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSpuTrack(int index) {
/* 1178 */     return nativeSetSpuTrack(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSpuDelay() {
/* 1187 */     return nativeGetSpuDelay();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSpuDelay(long delay) {
/* 1197 */     return nativeSetSpuDelay(delay);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setEqualizer(Equalizer equalizer) {
/* 1222 */     return nativeSetEqualizer(equalizer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addSlave(int type, Uri uri, boolean select) {
/* 1233 */     return nativeAddSlave(type, VLCUtil.encodeVLCUri(uri), select);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean record(String directory) {
/* 1244 */     return nativeRecord(directory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addSlave(int type, String path, boolean select) {
/* 1255 */     return addSlave(type, Uri.fromFile(new File(path)), select);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setEventListener(EventListener listener) {
/* 1341 */     super.setEventListener(listener);
/*      */   }
/*      */ 
/*      */   
/*      */   protected synchronized Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/* 1346 */     switch (eventType) {
/*      */       case 256:
/*      */       case 262:
/*      */       case 265:
/*      */       case 266:
/* 1351 */         this.mVoutCount = 0;
/* 1352 */         notify();
/*      */       case 258:
/*      */       case 259:
/* 1355 */         return new Event(eventType, argf1);
/*      */       case 260:
/*      */       case 261:
/* 1358 */         return new Event(eventType);
/*      */       case 267:
/* 1360 */         return new Event(eventType, arg1);
/*      */       case 273:
/* 1362 */         return new Event(eventType, arg1);
/*      */       case 268:
/* 1364 */         return new Event(eventType, argf1);
/*      */       case 274:
/* 1366 */         this.mVoutCount = (int)arg1;
/* 1367 */         notify();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
///* 1374 */         this.mHandlerMainThread.post(new Runnable() {
///*      */               public void run() {
///* 1376 */                 MediaPlayer.this.updateVideoSurfaces();
///*      */               }
///*      */             });
/* 1379 */         return new Event(eventType, arg1);
/*      */       case 276:
/*      */       case 277:
/*      */       case 278:
/* 1383 */         return new Event(eventType, arg1, arg2);
/*      */       case 269:
/*      */       case 270:
/* 1386 */         return new Event(eventType, arg1);
/*      */       case 286:
/* 1388 */         return new Event(eventType, arg1, args1);
/*      */     } 
/* 1390 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onReleaseNative() {
/* 1395 */     detachViews();
/* 1396 */     this.mWindow.detachViews();
/* 1397 */     registerAudioPlug(false);
/*      */     
/* 1399 */     if (this.mMedia != null)
/* 1400 */       this.mMedia.release(); 
/* 1401 */     if (this.mRenderer != null)
/* 1402 */       this.mRenderer.release(); 
/* 1403 */     this.mVoutCount = 0;
/* 1404 */     nativeRelease();
/*      */   }
/*      */   
/*      */   public boolean canDoPassthrough() {
/* 1408 */     return this.mCanDoPassthrough;
/*      */   }
/*      */   
/*      */   public void setVideoFormat(String format, int width, int height, int pitch) {
/* 1412 */     nativeSetVideoFormat(format, width, height, pitch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVideoCallback(ByteBuffer buffer, MediaPlayCallback callback) {
/* 1421 */     this.mBuffer = buffer;
/* 1422 */     this.mCallback = callback;
/* 1423 */     nativeSetVideoBuffer(buffer);
/*      */   }
/*      */   
/*      */   public native void setRate(float paramFloat);
/*      */   
/*      */   public native float getRate();
/*      */   
/*      */   public native boolean isPlaying();
/*      */   
/*      */   public native boolean isSeekable();
/*      */   
/*      */   public native void pause();
/*      */   
/*      */   public native int getPlayerState();
/*      */   
/*      */   public native int getVolume();
/*      */   
/*      */   public native int setVolume(int paramInt);
/*      */   
/*      */   public native long getTime();
/*      */   
/*      */   public native long setTime(long paramLong);
/*      */   
/*      */   public native float getPosition();
/*      */   
/*      */   public native void setPosition(float paramFloat);
/*      */   
/*      */   public native long getLength();
/*      */   
/*      */   public native int getTitle();
/*      */   
/*      */   public native void setTitle(int paramInt);
/*      */   
/*      */   public native int getChapter();
/*      */   
/*      */   public native int previousChapter();
/*      */   
/*      */   public native int nextChapter();
/*      */   
/*      */   public native void setChapter(int paramInt);
/*      */   
/*      */   public native void navigate(int paramInt);
/*      */   
/*      */   private native void nativeSetVideoFormat(String paramString, int paramInt1, int paramInt2, int paramInt3);
/*      */   
/*      */   private native void nativeSetVideoBuffer(ByteBuffer paramByteBuffer);
/*      */   
/*      */   private native void nativeNewFromLibVlc(ILibVLC paramILibVLC, AWindow paramAWindow);
/*      */   
/*      */   private native void nativeNewFromMedia(IMedia paramIMedia, AWindow paramAWindow);
/*      */   
/*      */   private native void nativeRelease();
/*      */   
/*      */   private native void nativeSetMedia(IMedia paramIMedia);
/*      */   
/*      */   private native void nativePlay();
/*      */   
/*      */   private native void nativeStop();
/*      */   
/*      */   private native int nativeSetRenderer(RendererItem paramRendererItem);
/*      */   
/*      */   private native void nativeSetVideoTitleDisplay(int paramInt1, int paramInt2);
/*      */   
/*      */   private native float nativeGetScale();
/*      */   
/*      */   private native void nativeSetScale(float paramFloat);
/*      */   
/*      */   private native String nativeGetAspectRatio();
/*      */   
/*      */   private native void nativeSetAspectRatio(String paramString);
/*      */   
/*      */   private native boolean nativeUpdateViewpoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean);
/*      */   
/*      */   private native boolean nativeSetAudioOutput(String paramString);
/*      */   
/*      */   private native boolean nativeSetAudioOutputDevice(String paramString);
/*      */   
/*      */   private native Title[] nativeGetTitles();
/*      */   
/*      */   private native Chapter[] nativeGetChapters(int paramInt);
/*      */   
/*      */   private native int nativeGetVideoTracksCount();
/*      */   
/*      */   private native TrackDescription[] nativeGetVideoTracks();
/*      */   
/*      */   private native int nativeGetVideoTrack();
/*      */   
/*      */   private native boolean nativeSetVideoTrack(int paramInt);
/*      */   
/*      */   private native int nativeGetAudioTracksCount();
/*      */   
/*      */   private native TrackDescription[] nativeGetAudioTracks();
/*      */   
/*      */   private native int nativeGetAudioTrack();
/*      */   
/*      */   private native boolean nativeSetAudioTrack(int paramInt);
/*      */   
/*      */   private native long nativeGetAudioDelay();
/*      */   
/*      */   private native boolean nativeSetAudioDelay(long paramLong);
/*      */   
/*      */   private native int nativeGetSpuTracksCount();
/*      */   
/*      */   private native TrackDescription[] nativeGetSpuTracks();
/*      */   
/*      */   private native int nativeGetSpuTrack();
/*      */   
/*      */   private native boolean nativeSetSpuTrack(int paramInt);
/*      */   
/*      */   private native long nativeGetSpuDelay();
/*      */   
/*      */   private native boolean nativeSetSpuDelay(long paramLong);
/*      */   
/*      */   private native boolean nativeAddSlave(int paramInt, String paramString, boolean paramBoolean);
/*      */   
/*      */   private native boolean nativeRecord(String paramString);
/*      */   
/*      */   private native boolean nativeSetEqualizer(Equalizer paramEqualizer);
/*      */   
/*      */   private native boolean nativeTakeSnapShot(int paramInt1, String paramString, int paramInt2, int paramInt3);
/*      */   
/*      */   public static interface EventListener extends AbstractVLCEvent.Listener<Event> {}
/*      */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\MediaPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */