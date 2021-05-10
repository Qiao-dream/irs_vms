/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import androidx.annotation.Nullable;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
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
/*     */ public class MediaDiscoverer
/*     */   extends VLCObject<MediaDiscoverer.Event>
/*     */ {
/*     */   private static final String TAG = "LibVLC/MediaDiscoverer";
/*     */   
/*     */   public static class Event
/*     */     extends AbstractVLCEvent
/*     */   {
/*     */     public static final int Started = 1280;
/*     */     public static final int Ended = 1281;
/*     */     
/*     */     protected Event(int type) {
/*  38 */       super(type);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Description
/*     */   {
/*     */     public final String name;
/*     */     
/*     */     public final String longName;
/*     */     public final int category;
/*     */     
/*     */     public static class Category
/*     */     {
/*     */       public static final int Devices = 0;
/*     */       public static final int Lan = 1;
/*     */       public static final int Podcasts = 2;
/*     */       public static final int LocalDirs = 3;
/*     */     }
/*     */     
/*     */     private Description(String name, String longName, int category) {
/*  59 */       this.name = name;
/*  60 */       this.longName = longName;
/*  61 */       this.category = category;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Description createDescriptionFromNative(String name, String longName, int category) {
/*  68 */     return new Description(name, longName, category);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   private MediaList mMediaList = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaDiscoverer(ILibVLC iLibVLC, String name) {
/*  82 */     super(iLibVLC);
/*  83 */     nativeNew(iLibVLC, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean start() {
/*  92 */     if (isReleased())
/*  93 */       throw new IllegalStateException("MediaDiscoverer is released"); 
/*  94 */     return nativeStart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 102 */     if (isReleased())
/* 103 */       throw new IllegalStateException("MediaDiscoverer is released"); 
/* 104 */     nativeStop();
/*     */   }
/*     */   
/*     */   public void setEventListener(EventListener listener) {
/* 108 */     setEventListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/* 113 */     switch (eventType) {
/*     */       case 1280:
/*     */       case 1281:
/* 116 */         return new Event(eventType);
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaList getMediaList() {
/* 128 */     synchronized (this) {
/* 129 */       if (this.mMediaList != null) {
/* 130 */         this.mMediaList.retain();
/* 131 */         return this.mMediaList;
/*     */       } 
/*     */     } 
/* 134 */     MediaList mediaList = new MediaList(this);
/* 135 */     synchronized (this) {
/* 136 */       this.mMediaList = mediaList;
/* 137 */       this.mMediaList.retain();
/* 138 */       return this.mMediaList;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onReleaseNative() {
/* 144 */     if (this.mMediaList != null)
/* 145 */       this.mMediaList.release(); 
/* 146 */     nativeRelease();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Description[] list(ILibVLC iLibVLC, int category) {
/* 155 */     return nativeList(iLibVLC, category);
/*     */   }
/*     */   
/*     */   private native void nativeNew(ILibVLC paramILibVLC, String paramString);
/*     */   
/*     */   private native void nativeRelease();
/*     */   
/*     */   private native boolean nativeStart();
/*     */   
/*     */   private native void nativeStop();
/*     */   
/*     */   private static native Description[] nativeList(ILibVLC paramILibVLC, int paramInt);
/*     */   
/*     */   public static interface EventListener extends AbstractVLCEvent.Listener<Event> {}
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\MediaDiscoverer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */