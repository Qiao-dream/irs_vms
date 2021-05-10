/*     */ package org.videolan.libvlc.util;
/*     */ 
/*     */ import android.net.Uri;
/*     */ import androidx.annotation.MainThread;
/*     */ import java.util.ArrayList;
/*     */ import org.videolan.libvlc.LibVLC;
/*     */ import org.videolan.libvlc.Media;
/*     */ import org.videolan.libvlc.MediaPlayer;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.interfaces.IMedia;
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
/*     */ public class Dumper
/*     */ {
/*     */   private final ILibVLC mILibVLC;
/*     */   private final MediaPlayer mMediaPlayer;
/*     */   private final Listener mListener;
/*     */   
/*     */   @MainThread
/*     */   public Dumper(Uri uri, String filepath, Listener listener) {
/*  52 */     if (uri == null || filepath == null || listener == null)
/*  53 */       throw new IllegalArgumentException("arguments shouldn't be null"); 
/*  54 */     this.mListener = listener;
/*     */     
/*  56 */     ArrayList<String> options = new ArrayList<>(8);
/*  57 */     options.add("--demux");
/*  58 */     options.add("dump2,none");
/*  59 */     options.add("--demuxdump-file");
/*  60 */     options.add(filepath);
/*  61 */     options.add("--no-video");
/*  62 */     options.add("--no-audio");
/*  63 */     options.add("--no-spu");
/*  64 */     options.add("-vv");
/*  65 */     this.mILibVLC = (ILibVLC)new LibVLC(null, options);
/*     */     
/*  67 */     Media media = new Media(this.mILibVLC, uri);
/*  68 */     this.mMediaPlayer = new MediaPlayer((IMedia)media);
/*  69 */     this.mMediaPlayer.setEventListener(new MediaPlayer.EventListener()
/*     */         {
/*     */           public void onEvent(MediaPlayer.Event event) {
/*  72 */             switch (event.type) {
/*     */               case 259:
/*  74 */                 Dumper.this.mListener.onProgress(event.getBuffering());
/*     */                 break;
/*     */               case 265:
/*     */               case 266:
/*  78 */                 Dumper.this.mListener.onFinish((event.type == 265));
/*  79 */                 Dumper.this.cancel();
/*     */                 break;
/*     */             } 
/*     */           
/*     */           }
/*     */         });
/*  85 */     media.release();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void start() {
/*  93 */     this.mMediaPlayer.play();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void cancel() {
/* 102 */     this.mMediaPlayer.stop();
/* 103 */     this.mMediaPlayer.release();
/* 104 */     this.mILibVLC.release();
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void onFinish(boolean param1Boolean);
/*     */     
/*     */     void onProgress(float param1Float);
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\Dumper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */