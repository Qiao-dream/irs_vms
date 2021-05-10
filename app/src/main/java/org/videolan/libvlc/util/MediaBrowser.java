/*     */ package org.videolan.libvlc.util;
/*     */ 
/*     */ import android.net.Uri;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import androidx.annotation.MainThread;
/*     */ import java.util.ArrayList;
/*     */ import org.videolan.libvlc.FactoryManager;
/*     */ import org.videolan.libvlc.MediaDiscoverer;
/*     */ import org.videolan.libvlc.MediaList;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.interfaces.IMedia;
/*     */ import org.videolan.libvlc.interfaces.IMediaFactory;
/*     */ import org.videolan.libvlc.interfaces.IMediaList;
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
/*     */ public class MediaBrowser
/*     */ {
/*     */   private static final String TAG = "MediaBrowser";
/*     */   private final ILibVLC mILibVlc;
/*  42 */   private final ArrayList<MediaDiscoverer> mMediaDiscoverers = new ArrayList<>();
/*  43 */   private final ArrayList<IMedia> mDiscovererMediaArray = new ArrayList<>();
/*     */   
/*     */   private IMediaList mBrowserMediaList;
/*     */   private IMedia mMedia;
/*     */   private EventListener mEventListener;
/*     */   private Handler mHandler;
/*     */   private boolean mAlive;
/*     */   private IMediaFactory mFactory;
/*     */   private static final String IGNORE_LIST_OPTION = ":ignore-filetypes=";
/*  52 */   private String mIgnoreList = "db,nfo,ini,jpg,jpeg,ljpg,gif,png,pgm,pgmyuv,pbm,pam,tga,bmp,pnm,xpm,xcf,pcx,tif,tiff,lbm,sfv,txt,sub,idx,srt,ssa,ass,smi,utf,utf-8,rt,aqt,txt,usf,jss,cdg,psb,mpsub,mpl2,pjs,dks,stl,vtt,ttml";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IMediaList.EventListener mBrowserMediaListEventListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IMediaList.EventListener mDiscovererMediaListEventListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Flag
/*     */   {
/*     */     public static final int Interact = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int NoSlavesAutodetect = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int ShowHiddenFiles = 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaBrowser(ILibVLC libvlc, EventListener listener, Handler handler) {
/* 109 */     this(libvlc, listener);
/* 110 */     this.mHandler = handler;
/*     */   }
/*     */   
/*     */   private void reset() {
/* 114 */     for (MediaDiscoverer md : this.mMediaDiscoverers)
/* 115 */       md.release(); 
/* 116 */     this.mMediaDiscoverers.clear();
/* 117 */     this.mDiscovererMediaArray.clear();
/* 118 */     if (this.mMedia != null) {
/* 119 */       this.mMedia.release();
/* 120 */       this.mMedia = null;
/*     */     } 
/*     */     
/* 123 */     if (this.mBrowserMediaList != null) {
/* 124 */       this.mBrowserMediaList.release();
/* 125 */       this.mBrowserMediaList = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void release() {
/* 134 */     reset();
/* 135 */     if (!this.mAlive)
/* 136 */       throw new IllegalStateException("MediaBrowser released more than one time"); 
/* 137 */     this.mILibVlc.release();
/* 138 */     this.mAlive = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void changeEventListener(EventListener eventListener) {
/* 147 */     reset();
/* 148 */     this.mEventListener = eventListener;
/*     */   }
/*     */   
/*     */   private void startMediaDiscoverer(String discovererName) {
/* 152 */     MediaDiscoverer md = new MediaDiscoverer(this.mILibVlc, discovererName);
/* 153 */     this.mMediaDiscoverers.add(md);
/* 154 */     MediaList ml = md.getMediaList();
/* 155 */     ml.setEventListener(this.mDiscovererMediaListEventListener, this.mHandler);
/* 156 */     ml.release();
/* 157 */     md.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void discoverNetworkShares() {
/* 165 */     reset();
/*     */ 
/*     */     
/* 168 */     MediaDiscoverer.Description[] descriptions = MediaDiscoverer.list(this.mILibVlc, 1);
/* 169 */     if (descriptions == null)
/*     */       return; 
/* 171 */     for (MediaDiscoverer.Description description : descriptions) {
/* 172 */       Log.i("MediaBrowser", "starting " + description.name + " discover (" + description.longName + ")");
/* 173 */       startMediaDiscoverer(description.name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void discoverNetworkShares(String serviceName) {
/* 183 */     reset();
/* 184 */     startMediaDiscoverer(serviceName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void browse(String path, int flags) {
/* 195 */     IMedia media = this.mFactory.getFromLocalPath(this.mILibVlc, path);
/* 196 */     browse(media, flags);
/* 197 */     media.release();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void browse(Uri uri, int flags) {
/* 208 */     IMedia media = this.mFactory.getFromUri(this.mILibVlc, uri);
/* 209 */     browse(media, flags);
/* 210 */     media.release();
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
/*     */   @MainThread
/*     */   public void browse(IMedia media, int flags) {
/* 224 */     media.retain();
/* 225 */     media.addOption(":ignore-filetypes=" + this.mIgnoreList);
/* 226 */     if ((flags & 0x2) != 0)
/* 227 */       media.addOption(":no-sub-autodetect-file"); 
/* 228 */     if ((flags & 0x4) != 0)
/* 229 */       media.addOption(":show-hiddenfiles"); 
/* 230 */     int mediaFlags = 1;
/* 231 */     if ((flags & 0x1) != 0)
/* 232 */       mediaFlags |= 0x8; 
/* 233 */     reset();
/* 234 */     this.mBrowserMediaList = media.subItems();
/* 235 */     this.mBrowserMediaList.setEventListener(this.mBrowserMediaListEventListener, this.mHandler);
/* 236 */     media.parseAsync(mediaFlags, 0);
/* 237 */     this.mMedia = media;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public int getMediaCount() {
/* 245 */     return (this.mBrowserMediaList != null) ? this.mBrowserMediaList.getCount() : this.mDiscovererMediaArray.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public IMedia getMediaAt(int index) {
/* 253 */     if (index < 0 || index >= getMediaCount()) {
/* 254 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 256 */     IMedia media = (this.mBrowserMediaList != null) ? this.mBrowserMediaList.getMediaAt(index) : this.mDiscovererMediaArray.get(index);
/* 257 */     media.retain();
/* 258 */     return media;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void setIgnoreFileTypes(String list) {
/* 269 */     this.mIgnoreList = list;
/*     */   }
/*     */   public MediaBrowser(ILibVLC libvlc, EventListener listener) {
/* 272 */     this.mBrowserMediaListEventListener = new IMediaList.EventListener()
/*     */       {
/*     */         public void onEvent(IMediaList.Event event) {
/* 275 */           if (MediaBrowser.this.mEventListener == null)
/*     */             return; 
/* 277 */           IMediaList.Event mlEvent = event;
/*     */           
/* 279 */           switch (mlEvent.type) {
/*     */             case 512:
/* 281 */               MediaBrowser.this.mEventListener.onMediaAdded(mlEvent.index, mlEvent.media);
/*     */               break;
/*     */             case 514:
/* 284 */               MediaBrowser.this.mEventListener.onMediaRemoved(mlEvent.index, mlEvent.media);
/*     */               break;
/*     */             case 516:
/* 287 */               MediaBrowser.this.mEventListener.onBrowseEnd();
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/* 292 */     this.mDiscovererMediaListEventListener = new IMediaList.EventListener()
/*     */       {
/*     */         public void onEvent(IMediaList.Event event) {
/* 295 */           if (MediaBrowser.this.mEventListener == null)
/*     */             return; 
/* 297 */           IMediaList.Event mlEvent = event;
/* 298 */           int index = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 303 */           switch (mlEvent.type) {
/*     */             case 512:
/* 305 */               MediaBrowser.this.mDiscovererMediaArray.add(mlEvent.media);
/* 306 */               MediaBrowser.this.mEventListener.onMediaAdded(index, mlEvent.media);
/*     */               break;
/*     */             case 514:
/* 309 */               index = MediaBrowser.this.mDiscovererMediaArray.indexOf(mlEvent.media);
/* 310 */               if (index != -1)
/* 311 */                 MediaBrowser.this.mDiscovererMediaArray.remove(index); 
/* 312 */               if (index != -1)
/* 313 */                 MediaBrowser.this.mEventListener.onMediaRemoved(index, mlEvent.media); 
/*     */               break;
/*     */             case 516:
/* 316 */               MediaBrowser.this.mEventListener.onBrowseEnd();
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/*     */     this.mFactory = (IMediaFactory)FactoryManager.getFactory(IMediaFactory.factoryId);
/*     */     this.mILibVlc = libvlc;
/*     */     this.mILibVlc.retain();
/*     */     this.mEventListener = listener;
/*     */     this.mAlive = true;
/*     */   }
/*     */   
/*     */   public static interface EventListener {
/*     */     void onMediaAdded(int param1Int, IMedia param1IMedia);
/*     */     
/*     */     void onMediaRemoved(int param1Int, IMedia param1IMedia);
/*     */     
/*     */     void onBrowseEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\MediaBrowser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */