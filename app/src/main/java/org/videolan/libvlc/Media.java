/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import android.content.res.AssetFileDescriptor;
/*     */ import android.net.Uri;
/*     */ import androidx.annotation.Nullable;
/*     */ import java.io.FileDescriptor;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.interfaces.IMedia;
/*     */ import org.videolan.libvlc.interfaces.IMediaList;
/*     */ import org.videolan.libvlc.interfaces.IVLCObject;
/*     */ import org.videolan.libvlc.util.AndroidUtil;
/*     */ import org.videolan.libvlc.util.HWDecoderUtil;
/*     */ import org.videolan.libvlc.util.VLCUtil;
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
/*     */ public class Media
/*     */   extends VLCObject<IMedia.Event>
/*     */   implements IMedia
/*     */ {
/*     */   private static final String TAG = "LibVLC/Media";
/*     */   private static final int PARSE_STATUS_INIT = 0;
/*     */   private static final int PARSE_STATUS_PARSING = 1;
/*     */   private static final int PARSE_STATUS_PARSED = 2;
/*     */   
/*     */   private static Track createAudioTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int channels, int rate) {
/*  45 */     return (Track)new AudioTrack(codec, originalCodec, id, profile, level, bitrate, language, description, channels, rate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Track createVideoTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen, int orientation, int projection) {
/*  55 */     return (Track)new VideoTrack(codec, originalCodec, id, profile, level, bitrate, language, description, height, width, sarNum, sarDen, frameRateNum, frameRateDen, orientation, projection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Track createSubtitleTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, String encoding) {
/*  64 */     return (Track)new SubtitleTrack(codec, originalCodec, id, profile, level, bitrate, language, description, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Track createUnknownTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
/*  72 */     return (Track)new UnknownTrack(codec, originalCodec, id, profile, level, bitrate, language, description);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Slave createSlaveFromNative(int type, int priority, String uri) {
/*  78 */     return new Slave(type, priority, uri);
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
/*     */   private static Stats createStatsFromNative(int readBytes, float inputBitrate, int demuxReadBytes, float demuxBitrates, int demuxCorrupted, int demuxDiscontinuity, int decodedVideo, int decodedAudio, int displayedPictures, int lostPictures, int playedAbuffers, int lostAbuffers, int sentPackets, int sentBytes, float sendBitrate) {
/*  97 */     return new Stats(readBytes, inputBitrate, demuxReadBytes, demuxBitrates, demuxCorrupted, demuxDiscontinuity, decodedVideo, decodedAudio, displayedPictures, lostPictures, playedAbuffers, lostAbuffers, sentPackets, sentBytes, sendBitrate);
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
/* 108 */   private Uri mUri = null;
/* 109 */   private MediaList mSubItems = null;
/* 110 */   private int mParseStatus = 0;
/* 111 */   private final String[] mNativeMetas = new String[25];
/* 112 */   private Track[] mNativeTracks = null;
/* 113 */   private long mDuration = -1L;
/* 114 */   private int mState = -1;
/* 115 */   private int mType = -1;
/*     */ 
/*     */   
/*     */   private boolean mCodecOptionSet = false;
/*     */ 
/*     */   
/*     */   private boolean mFileCachingSet = false;
/*     */ 
/*     */   
/*     */   private boolean mNetworkCachingSet = false;
/*     */ 
/*     */   
/*     */   public Media(ILibVLC iLibVLC, String path) {
/* 128 */     super(iLibVLC);
/* 129 */     nativeNewFromPath(iLibVLC, path);
/* 130 */     this.mUri = VLCUtil.UriFromMrl(nativeGetMrl());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Media(ILibVLC iLibVLC, Uri uri) {
/* 140 */     super(iLibVLC);
/* 141 */     nativeNewFromLocation(iLibVLC, VLCUtil.encodeVLCUri(uri));
/* 142 */     this.mUri = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Media(ILibVLC iLibVLC, FileDescriptor fd) {
/* 152 */     super(iLibVLC);
/* 153 */     nativeNewFromFd(iLibVLC, fd);
/* 154 */     this.mUri = VLCUtil.UriFromMrl(nativeGetMrl());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Media(ILibVLC iLibVLC, AssetFileDescriptor afd) {
/* 164 */     super(iLibVLC);
/* 165 */     long offset = afd.getStartOffset();
/* 166 */     long length = afd.getLength();
/* 167 */     nativeNewFromFdWithOffsetLength(iLibVLC, afd.getFileDescriptor(), offset, length);
/* 168 */     this.mUri = VLCUtil.UriFromMrl(nativeGetMrl());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Media(IMediaList ml, int index) {
/* 177 */     super((IVLCObject)ml);
/* 178 */     if (ml == null || ml.isReleased())
/* 179 */       throw new IllegalArgumentException("MediaList is null or released"); 
/* 180 */     if (!ml.isLocked())
/* 181 */       throw new IllegalStateException("MediaList should be locked"); 
/* 182 */     nativeNewFromMediaList(ml, index);
/* 183 */     this.mUri = VLCUtil.UriFromMrl(nativeGetMrl());
/*     */   }
/*     */   
/*     */   public void setEventListener(EventListener listener) {
/* 187 */     setEventListener(listener);
/*     */   }
/*     */   
/*     */   protected synchronized Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/*     */     int id;
/* 192 */     switch (eventType) {
/*     */       
/*     */       case 0:
/* 195 */         id = (int)arg1;
/* 196 */         if (id >= 0 && id < 25)
/* 197 */           this.mNativeMetas[id] = null; 
/* 198 */         return new Event(eventType, arg1);
/*     */       case 2:
/* 200 */         this.mDuration = -1L;
/*     */         break;
/*     */       case 3:
/* 203 */         postParse();
/* 204 */         return new Event(eventType, arg1);
/*     */       case 5:
/* 206 */         this.mState = -1;
/*     */         break;
/*     */     } 
/* 209 */     return new Event(eventType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Uri getUri() {
/* 216 */     return this.mUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDuration() {
/* 223 */     synchronized (this) {
/* 224 */       if (this.mDuration != -1L)
/* 225 */         return this.mDuration; 
/* 226 */       if (isReleased())
/* 227 */         return 0L; 
/*     */     } 
/* 229 */     long duration = nativeGetDuration();
/* 230 */     synchronized (this) {
/* 231 */       this.mDuration = duration;
/* 232 */       return this.mDuration;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getState() {
/* 242 */     synchronized (this) {
/* 243 */       if (this.mState != -1)
/* 244 */         return this.mState; 
/* 245 */       if (isReleased())
/* 246 */         return 7; 
/*     */     } 
/* 248 */     int state = nativeGetState();
/* 249 */     synchronized (this) {
/* 250 */       this.mState = state;
/* 251 */       return this.mState;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaList subItems() {
/* 261 */     synchronized (this) {
/* 262 */       if (this.mSubItems != null) {
/* 263 */         this.mSubItems.retain();
/* 264 */         return this.mSubItems;
/*     */       } 
/*     */     } 
/* 267 */     MediaList subItems = new MediaList(this);
/* 268 */     synchronized (this) {
/* 269 */       this.mSubItems = subItems;
/* 270 */       this.mSubItems.retain();
/* 271 */       return this.mSubItems;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void postParse() {
/* 277 */     if ((this.mParseStatus & 0x2) != 0)
/*     */       return; 
/* 279 */     this.mParseStatus &= 0xFFFFFFFE;
/* 280 */     this.mParseStatus |= 0x2;
/* 281 */     this.mNativeTracks = null;
/* 282 */     this.mDuration = -1L;
/* 283 */     this.mState = -1;
/* 284 */     this.mType = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parse(int flags) {
/* 294 */     boolean parse = false;
/* 295 */     synchronized (this) {
/* 296 */       if ((this.mParseStatus & 0x3) == 0) {
/* 297 */         this.mParseStatus |= 0x1;
/* 298 */         parse = true;
/*     */       } 
/*     */     } 
/* 301 */     if (parse && nativeParse(flags)) {
/* 302 */       postParse();
/* 303 */       return true;
/*     */     } 
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parse() {
/* 314 */     return parse(2);
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
/*     */   public boolean parseAsync(int flags, int timeout) {
/* 330 */     boolean parse = false;
/* 331 */     synchronized (this) {
/* 332 */       if ((this.mParseStatus & 0x3) == 0) {
/* 333 */         this.mParseStatus |= 0x1;
/* 334 */         parse = true;
/*     */       } 
/*     */     } 
/* 337 */     return (parse && nativeParseAsync(flags, timeout));
/*     */   }
/*     */   
/*     */   public boolean parseAsync(int flags) {
/* 341 */     return parseAsync(flags, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseAsync() {
/* 350 */     return parseAsync(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isParsed() {
/* 357 */     return ((this.mParseStatus & 0x2) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 366 */     synchronized (this) {
/* 367 */       if (this.mType != -1)
/* 368 */         return this.mType; 
/* 369 */       if (isReleased())
/* 370 */         return 0; 
/*     */     } 
/* 372 */     int type = nativeGetType();
/* 373 */     synchronized (this) {
/* 374 */       this.mType = type;
/* 375 */       return this.mType;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Track[] getTracks() {
/* 380 */     synchronized (this) {
/* 381 */       if (this.mNativeTracks != null)
/* 382 */         return this.mNativeTracks; 
/* 383 */       if (isReleased())
/* 384 */         return null; 
/*     */     } 
/* 386 */     Track[] tracks = nativeGetTracks();
/* 387 */     synchronized (this) {
/* 388 */       this.mNativeTracks = tracks;
/* 389 */       return this.mNativeTracks;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTrackCount() {
/* 397 */     Track[] tracks = getTracks();
/* 398 */     return (tracks != null) ? tracks.length : 0;
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
/*     */   public Track getTrack(int idx) {
/* 410 */     Track[] tracks = getTracks();
/* 411 */     if (tracks == null || idx < 0 || idx >= tracks.length)
/* 412 */       return null; 
/* 413 */     return tracks[idx];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMeta(int id) {
/* 423 */     if (id < 0 || id >= 25) {
/* 424 */       return null;
/*     */     }
/* 426 */     synchronized (this) {
/* 427 */       if (this.mNativeMetas[id] != null)
/* 428 */         return this.mNativeMetas[id]; 
/* 429 */       if (isReleased()) {
/* 430 */         return null;
/*     */       }
/*     */     } 
/* 433 */     String meta = nativeGetMeta(id);
/* 434 */     synchronized (this) {
/* 435 */       this.mNativeMetas[id] = meta;
/* 436 */       return meta;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getMediaCodecModule() {
/* 442 */     return AndroidUtil.isLolliPopOrLater ? "mediacodec_ndk" : "mediacodec_jni";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHWDecoderEnabled(boolean enabled, boolean force) {
/* 453 */     if (LibVLC.majorVersion() == 3) {
/*     */       
/* 455 */       HWDecoderUtil.Decoder decoder = enabled ? HWDecoderUtil.getDecoderFromDevice() : HWDecoderUtil.Decoder.NONE;
/*     */ 
/*     */ 
/*     */       
/* 459 */       if (decoder == HWDecoderUtil.Decoder.UNKNOWN && force) {
/* 460 */         decoder = HWDecoderUtil.Decoder.ALL;
/*     */       }
/* 462 */       if (decoder == HWDecoderUtil.Decoder.NONE || decoder == HWDecoderUtil.Decoder.UNKNOWN) {
/* 463 */         addOption(":codec=all");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 476 */       if (!this.mFileCachingSet)
/* 477 */         addOption(":file-caching=1500"); 
/* 478 */       if (!this.mNetworkCachingSet) {
/* 479 */         addOption(":network-caching=1500");
/*     */       }
/* 481 */       StringBuilder sb = new StringBuilder(":codec=");
/* 482 */       if (decoder == HWDecoderUtil.Decoder.MEDIACODEC || decoder == HWDecoderUtil.Decoder.ALL)
/* 483 */         sb.append(getMediaCodecModule()).append(","); 
/* 484 */       if (force && (decoder == HWDecoderUtil.Decoder.OMX || decoder == HWDecoderUtil.Decoder.ALL))
/* 485 */         sb.append("iomx,"); 
/* 486 */       sb.append("all");
/*     */       
/* 488 */       addOption(sb.toString());
/*     */     }
/* 490 */     else if (!enabled) {
/* 491 */       addOption(":no-hw-dec");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultMediaPlayerOptions() {
/* 498 */     if (LibVLC.majorVersion() == 3) {
/*     */       boolean codecOptionSet;
/* 500 */       synchronized (this) {
/* 501 */         codecOptionSet = this.mCodecOptionSet;
/* 502 */         this.mCodecOptionSet = true;
/*     */       } 
/* 504 */       if (!codecOptionSet) {
/* 505 */         setHWDecoderEnabled(true, false);
/*     */       }
/*     */     } 
/*     */     
/* 509 */     if (this.mUri != null && this.mUri.getScheme() != null && !this.mUri.getScheme().equalsIgnoreCase("file") && this.mUri
/* 510 */       .getLastPathSegment() != null && this.mUri.getLastPathSegment().toLowerCase().endsWith(".iso")) {
/* 511 */       addOption(":demux=dvdnav,any");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOption(String option) {
/* 520 */     synchronized (this) {
/* 521 */       if (!this.mCodecOptionSet && option.startsWith(":codec="))
/* 522 */         this.mCodecOptionSet = true; 
/* 523 */       if (!this.mNetworkCachingSet && option.startsWith(":network-caching="))
/* 524 */         this.mNetworkCachingSet = true; 
/* 525 */       if (!this.mFileCachingSet && option.startsWith(":file-caching="))
/* 526 */         this.mFileCachingSet = true; 
/*     */     } 
/* 528 */     nativeAddOption(option);
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
/*     */   public void addSlave(Slave slave) {
/* 542 */     nativeAddSlave(slave.type, slave.priority, slave.uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSlaves() {
/* 549 */     nativeClearSlaves();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Slave[] getSlaves() {
/* 561 */     return nativeGetSlaves();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Stats getStats() {
/* 569 */     return nativeGetStats();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onReleaseNative() {
/* 574 */     if (this.mSubItems != null)
/* 575 */       this.mSubItems.release(); 
/* 576 */     nativeRelease();
/*     */   }
/*     */   
/*     */   private native void nativeNewFromPath(ILibVLC paramILibVLC, String paramString);
/*     */   
/*     */   private native void nativeNewFromLocation(ILibVLC paramILibVLC, String paramString);
/*     */   
/*     */   private native void nativeNewFromFd(ILibVLC paramILibVLC, FileDescriptor paramFileDescriptor);
/*     */   
/*     */   private native void nativeNewFromFdWithOffsetLength(ILibVLC paramILibVLC, FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2);
/*     */   
/*     */   private native void nativeNewFromMediaList(IMediaList paramIMediaList, int paramInt);
/*     */   
/*     */   private native void nativeRelease();
/*     */   
/*     */   private native boolean nativeParseAsync(int paramInt1, int paramInt2);
/*     */   
/*     */   private native boolean nativeParse(int paramInt);
/*     */   
/*     */   private native String nativeGetMrl();
/*     */   
/*     */   private native int nativeGetState();
/*     */   
/*     */   private native String nativeGetMeta(int paramInt);
/*     */   
/*     */   private native Track[] nativeGetTracks();
/*     */   
/*     */   private native long nativeGetDuration();
/*     */   
/*     */   private native int nativeGetType();
/*     */   
/*     */   private native void nativeAddOption(String paramString);
/*     */   
/*     */   private native void nativeAddSlave(int paramInt1, int paramInt2, String paramString);
/*     */   
/*     */   private native void nativeClearSlaves();
/*     */   
/*     */   private native Slave[] nativeGetSlaves();
/*     */   
/*     */   private native Stats nativeGetStats();
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\Media.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */