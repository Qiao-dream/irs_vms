/*     */ package org.videolan.libvlc.stubs;
/*     */ 
/*     */ import android.content.res.AssetFileDescriptor;
/*     */ import android.net.Uri;
/*     */ import java.io.FileDescriptor;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.interfaces.IMedia;
/*     */ import org.videolan.libvlc.interfaces.IMediaList;
/*     */ 
/*     */ public class StubMedia
/*     */   extends StubVLCObject<IMedia.Event>
/*     */   implements IMedia
/*     */ {
/*     */   private Uri mUri;
/*     */   private ILibVLC mILibVLC;
/*  16 */   private int mType = 0;
/*     */   
/*     */   public StubMedia(ILibVLC iLibVLC, String path) {
/*  19 */     this(iLibVLC, Uri.parse(path));
/*     */   }
/*     */   
/*     */   public StubMedia(ILibVLC iLibVLC, Uri uri) {
/*  23 */     this.mUri = uri;
/*  24 */     this.mILibVLC = iLibVLC;
/*     */   }
/*     */   
/*     */   public StubMedia(ILibVLC iLibVLC, FileDescriptor fd) {
/*  28 */     this.mILibVLC = iLibVLC;
/*     */   }
/*     */   
/*     */   public StubMedia(ILibVLC iLibVLC, AssetFileDescriptor assetFileDescriptor) {
/*  32 */     this.mILibVLC = iLibVLC;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDuration() {
/*  37 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getState() {
/*  42 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public IMediaList subItems() {
/*  47 */     return new StubMediaList();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parse(int flags) {
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parse() {
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parseAsync(int flags, int timeout) {
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parseAsync(int flags) {
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parseAsync() {
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/*  77 */     return this.mType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrackCount() {
/*  82 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public IMedia.Track getTrack(int idx) {
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMeta(int id) {
/*  92 */     if (this.mUri == null)
/*  93 */       return null; 
/*  94 */     switch (id) {
/*     */       case 0:
/*  96 */         return getTitle();
/*     */       case 10:
/*  98 */         return this.mUri.getPath();
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   private String getTitle() {
/* 104 */     if ("file".equals(this.mUri.getScheme())) {
/* 105 */       return this.mUri.getLastPathSegment();
/*     */     }
/* 107 */     return this.mUri.getPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHWDecoderEnabled(boolean enabled, boolean force) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEventListener(IMedia.EventListener listener) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOption(String option) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSlave(IMedia.Slave slave) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSlaves() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public IMedia.Slave[] getSlaves() {
/* 137 */     return new IMedia.Slave[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public Uri getUri() {
/* 142 */     return this.mUri;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParsed() {
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IMedia.Stats getStats() {
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultMediaPlayerOptions() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retain() {
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReleased() {
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ILibVLC getLibVLC() {
/* 177 */     return this.mILibVLC;
/*     */   }
/*     */   
/*     */   public void setType(int type) {
/* 181 */     this.mType = type;
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\stubs\StubMedia.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */