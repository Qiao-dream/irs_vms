/*     */ package org.videolan.libvlc.media;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.media.AudioAttributes;
/*     */ import android.media.MediaFormat;
/*     */ import android.media.TimedText;
/*     */ import android.net.Uri;
/*     */ import android.os.Parcel;
/*     */ import android.os.Parcelable;
/*     */ import android.view.Surface;
/*     */ import android.view.SurfaceHolder;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.videolan.libvlc.LibVLC;
/*     */ import org.videolan.libvlc.Media;
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
/*     */ public class MediaPlayer
/*     */ {
/*     */   public static final int MEDIA_ERROR_UNKNOWN = 1;
/*     */   public static final int MEDIA_ERROR_SERVER_DIED = 100;
/*     */   public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
/*     */   public static final int MEDIA_ERROR_IO = -1004;
/*     */   public static final int MEDIA_ERROR_MALFORMED = -1007;
/*     */   public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
/*     */   public static final int MEDIA_ERROR_TIMED_OUT = -110;
/*     */   public static final int MEDIA_INFO_UNKNOWN = 1;
/*     */   public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
/*     */   public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
/*     */   public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
/*     */   public static final int MEDIA_INFO_BUFFERING_START = 701;
/*     */   public static final int MEDIA_INFO_BUFFERING_END = 702;
/*     */   public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
/*     */   public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
/*     */   public static final int MEDIA_INFO_METADATA_UPDATE = 802;
/*     */   public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
/*     */   public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
/*     */   public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
/*     */   public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
/*     */   public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
/*     */   public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
/*  71 */   private IMedia mCurrentMedia = null; private final ILibVLC mILibVLC;
/*     */   private org.videolan.libvlc.MediaPlayer mMediaPlayer;
/*     */   public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
/*     */   
/*     */   public MediaPlayer() {
/*  76 */     this.mILibVLC = (ILibVLC)new LibVLC(null);
/*  77 */     this.mMediaPlayer = new org.videolan.libvlc.MediaPlayer(this.mILibVLC);
/*     */   }
/*     */   
/*     */   public static MediaPlayer create(Context context, Uri uri) {
/*  81 */     return create(context, uri, null);
/*     */   }
/*     */   
/*     */   public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder) {
/*  85 */     return create(context, uri, holder, null, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder, AudioAttributes audioAttributes, int audioSessionId) {
/*  90 */     MediaPlayer player = new MediaPlayer();
/*     */     
/*  92 */     return player;
/*     */   }
/*     */   
/*     */   public static MediaPlayer create(Context context, int resid) {
/*  96 */     return create(context, resid, null, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MediaPlayer create(Context context, int resid, AudioAttributes audioAttributes, int audioSessionId) {
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
/* 106 */     setDataSource(context, uri, (Map<String, String>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
/* 112 */     this.mCurrentMedia = (IMedia)new Media(this.mILibVLC, uri);
/* 113 */     this.mMediaPlayer.setMedia(this.mCurrentMedia);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
/* 118 */     this.mCurrentMedia = (IMedia)new Media(this.mILibVLC, path);
/* 119 */     this.mMediaPlayer.setMedia(this.mCurrentMedia);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
/* 124 */     this.mCurrentMedia = (IMedia)new Media(this.mILibVLC, fd);
/* 125 */     this.mMediaPlayer.setMedia(this.mCurrentMedia);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
/* 131 */     setDataSource(fd);
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() throws IOException, IllegalStateException {}
/*     */   
/*     */   public void prepareAsync() {
/* 138 */     this.mCurrentMedia.addOption(":video-paused");
/* 139 */     this.mMediaPlayer.play();
/*     */   }
/*     */   
/*     */   public void setDisplay(SurfaceHolder sh) {
/* 143 */     this.mMediaPlayer.getVLCVout().setVideoSurface(sh.getSurface(), sh);
/*     */   }
/*     */   
/*     */   public void setSurface(Surface surface) {
/* 147 */     this.mMediaPlayer.getVLCVout().setVideoSurface(surface, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVideoScalingMode(int mode) {}
/*     */   
/*     */   public void start() throws IllegalStateException {
/* 154 */     this.mMediaPlayer.play();
/*     */   }
/*     */   
/*     */   public void stop() throws IllegalStateException {
/* 158 */     this.mMediaPlayer.stop();
/*     */   }
/*     */   
/*     */   public void pause() throws IllegalStateException {
/* 162 */     this.mMediaPlayer.pause();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWakeMode(Context context, int mode) {}
/*     */ 
/*     */   
/*     */   public void setScreenOnWhilePlaying(boolean screenOn) {}
/*     */   
/*     */   public int getVideoWidth() {
/* 172 */     return -1;
/*     */   }
/*     */   
/*     */   public int getVideoHeight() {
/* 176 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean isPlaying() {
/* 180 */     return this.mMediaPlayer.isPlaying();
/*     */   }
/*     */ 
/*     */   
/*     */   public void seekTo(int msec) throws IllegalStateException {}
/*     */ 
/*     */   
/*     */   public int getCurrentPosition() {
/* 188 */     return (int)this.mMediaPlayer.getTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDuration() {
/* 193 */     return (int)this.mMediaPlayer.getLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNextMediaPlayer(MediaPlayer next) {}
/*     */   
/*     */   public void release() {
/* 200 */     this.mMediaPlayer.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {}
/*     */ 
/*     */   
/*     */   public void setAudioStreamType(int streamtype) {}
/*     */ 
/*     */   
/*     */   public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {}
/*     */ 
/*     */   
/*     */   public void setLooping(boolean looping) {}
/*     */   
/*     */   public boolean isLooping() {
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   public void setVolume(float leftVolume, float rightVolume) {
/* 220 */     this.mMediaPlayer.setVolume((int)((leftVolume + rightVolume) * 100.0F / 2.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAudioSessionId(int sessionId) throws IllegalArgumentException, IllegalStateException {}
/*     */   
/*     */   public int getAudioSessionId() {
/* 227 */     return 0;
/*     */   } public void attachAuxEffect(int effectId) {}
/*     */   public void setAuxEffectSendLevel(float level) {}
/*     */   public static interface OnInfoListener {
/*     */     boolean onInfo(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2); }
/*     */   public static interface OnErrorListener {
/*     */     boolean onError(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2); }
/*     */   public static interface OnTimedTextListener {
/*     */     void onTimedText(MediaPlayer param1MediaPlayer, TimedText param1TimedText); }
/*     */   public static interface OnVideoSizeChangedListener {
/*     */     void onVideoSizeChanged(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2); }
/*     */   public static interface OnSeekCompleteListener {
/*     */     void onSeekComplete(MediaPlayer param1MediaPlayer); }
/*     */   public static interface OnBufferingUpdateListener {
/*     */     void onBufferingUpdate(MediaPlayer param1MediaPlayer, int param1Int); }
/*     */   public static interface OnCompletionListener {
/*     */     void onCompletion(MediaPlayer param1MediaPlayer); }
/*     */   public static interface OnPreparedListener {
/*     */     void onPrepared(MediaPlayer param1MediaPlayer); }
/*     */   public static class TrackInfo implements Parcelable { public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0; public static final int MEDIA_TRACK_TYPE_VIDEO = 1; public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
/*     */     public int getTrackType() {
/* 248 */       return 0;
/*     */     } public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3; public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
/*     */     TrackInfo(Parcel in) {}
/*     */     public String getLanguage() {
/* 252 */       return "und";
/*     */     }
/*     */     
/*     */     public MediaFormat getFormat() {
/* 256 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int describeContents() {
/* 261 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeToParcel(Parcel dest, int flags) {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 270 */       return "";
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public TrackInfo[] getTrackInfo() throws IllegalStateException {
/* 276 */     TrackInfo[] trackInfo = new TrackInfo[1];
/* 277 */     return trackInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTimedTextSource(String path, String mimeType) {
/* 283 */     this.mMediaPlayer.addSlave(0, path, false);
/*     */   }
/*     */   
/*     */   public void addTimedTextSource(Context context, Uri uri, String mimeType) {
/* 287 */     this.mMediaPlayer.addSlave(0, uri, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTimedTextSource(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {}
/*     */ 
/*     */   
/*     */   public void addTimedTextSource(FileDescriptor fd, long offset, long length, String mime) throws IllegalArgumentException, IllegalStateException {}
/*     */ 
/*     */   
/*     */   public int getSelectedTrack(int trackType) throws IllegalStateException {
/* 299 */     return 0;
/*     */   }
/*     */   
/*     */   public void selectTrack(int index) throws IllegalStateException {}
/*     */   
/*     */   public void deselectTrack(int index) throws IllegalStateException {}
/*     */   
/*     */   protected void finalize() {}
/*     */   
/*     */   public void setOnPreparedListener(OnPreparedListener listener) {}
/*     */   
/*     */   public void setOnCompletionListener(OnCompletionListener listener) {}
/*     */   
/*     */   public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {}
/*     */   
/*     */   public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {}
/*     */   
/*     */   public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {}
/*     */   
/*     */   public void setOnTimedTextListener(OnTimedTextListener listener) {}
/*     */   
/*     */   public void setOnErrorListener(OnErrorListener listener) {}
/*     */   
/*     */   public void setOnInfoListener(OnInfoListener listener) {}
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\media\MediaPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */