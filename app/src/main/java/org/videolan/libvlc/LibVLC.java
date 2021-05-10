/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.util.Log;
/*     */ import androidx.annotation.Nullable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.util.HWDecoderUtil;
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
/*     */ public class LibVLC
/*     */   extends VLCObject<ILibVLC.Event>
/*     */   implements ILibVLC
/*     */ {
/*     */   private static final String TAG = "VLC/LibVLC";
/*     */   final Context mAppContext;
/*     */   
/*     */   public static class Event
/*     */     extends AbstractVLCEvent
/*     */   {
/*     */     protected Event(int type) {
/*  43 */       super(type);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LibVLC(Context context, List<String> options) {
/*  53 */     this.mAppContext = context.getApplicationContext();
/*  54 */     loadLibraries();
/*     */     
/*  56 */     if (options == null)
/*  57 */       options = new ArrayList<>(); 
/*  58 */     boolean setAout = true, setChroma = true;
/*     */     
/*  60 */     for (String option : options) {
/*  61 */       if (option.startsWith("--aout="))
/*  62 */         setAout = false; 
/*  63 */       if (option.startsWith("--android-display-chroma"))
/*  64 */         setChroma = false; 
/*  65 */       if (!setAout && !setChroma) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  70 */     if (setAout || setChroma) {
/*  71 */       if (setAout) {
/*  72 */         HWDecoderUtil.AudioOutput hwAout = HWDecoderUtil.getAudioOutputFromDevice();
/*  73 */         if (hwAout == HWDecoderUtil.AudioOutput.OPENSLES) {
/*  74 */           options.add("--aout=opensles");
/*     */         } else {
/*  76 */           options.add("--aout=android_audiotrack");
/*     */         } 
/*  78 */       }  if (setChroma) {
/*  79 */         options.add("--android-display-chroma");
/*  80 */         options.add("RV16");
/*     */       } 
/*     */     } 
/*  83 */     nativeNew(options.<String>toArray(new String[options.size()]), context.getDir("vlc", 0).getAbsolutePath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LibVLC(Context context) {
/*  90 */     this(context, null);
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
/*     */   
/*     */   protected ILibVLC.Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Context getAppContext() {
/* 128 */     return this.mAppContext;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onReleaseNative() {
/* 133 */     nativeRelease();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserAgent(String name, String http) {
/* 144 */     nativeSetUserAgent(name, http);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean sLoaded = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void loadLibraries() {
/* 157 */     if (sLoaded)
/*     */       return; 
/* 159 */     sLoaded = true;
/*     */     
/*     */     try {
/* 162 */       System.loadLibrary("c++_shared");
/* 163 */       System.loadLibrary("vlc");
/* 164 */       System.loadLibrary("vlcjni");
/* 165 */     } catch (UnsatisfiedLinkError ule) {
/* 166 */       Log.e("VLC/LibVLC", "Can't load vlcjni library: " + ule);
/*     */       
/* 168 */       System.exit(1);
/* 169 */     } catch (SecurityException se) {
/* 170 */       Log.e("VLC/LibVLC", "Encountered a security issue when loading vlcjni library: " + se);
/*     */       
/* 172 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static native String version();
/*     */   
/*     */   public static native int majorVersion();
/*     */   
/*     */   public static native String compiler();
/*     */   
/*     */   public static native String changeset();
/*     */   
/*     */   private native void nativeNew(String[] paramArrayOfString, String paramString);
/*     */   
/*     */   private native void nativeRelease();
/*     */   
/*     */   private native void nativeSetUserAgent(String paramString1, String paramString2);
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\LibVLC.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */