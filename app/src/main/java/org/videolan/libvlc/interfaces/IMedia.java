/*     */ package org.videolan.libvlc.interfaces;

import android.net.Uri;

public interface IMedia extends IVLCObject<IMedia.Event> { long getDuration(); int getState(); IMediaList subItems(); boolean parse(int paramInt); boolean parse(); boolean parseAsync(int paramInt1, int paramInt2); boolean parseAsync(int paramInt); boolean parseAsync(); int getType(); int getTrackCount();
/*     */   Track getTrack(int paramInt);
/*     */   String getMeta(int paramInt);
/*     */   void setHWDecoderEnabled(boolean paramBoolean1, boolean paramBoolean2);
/*     */   void setEventListener(EventListener paramEventListener);
/*     */   void addOption(String paramString);
/*     */   void addSlave(Slave paramSlave);
/*     */   void clearSlaves();
/*     */   Slave[] getSlaves();
/*     */   Uri getUri();
/*     */   boolean isParsed();
/*     */   Stats getStats();
/*     */   void setDefaultMediaPlayerOptions();
/*     */   public static class Event extends AbstractVLCEvent { public static final int MetaChanged = 0; public static final int SubItemAdded = 1; public static final int DurationChanged = 2;
/*     */     public Event(int type) {
/*  16 */       super(type);
/*     */     }
/*     */     public static final int ParsedChanged = 3; public static final int StateChanged = 5; public static final int SubItemTreeAdded = 6;
/*     */     public Event(int type, long arg1) {
/*  20 */       super(type, arg1);
/*     */     }
/*     */     
/*     */     public int getMetaId() {
/*  24 */       return (int)this.arg1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getParsedStatus() {
/*  33 */       return (int)this.arg1;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface EventListener
/*     */     extends AbstractVLCEvent.Listener<Event> {}
/*     */ 
/*     */   
/*     */   public static class Type
/*     */   {
/*     */     public static final int Unknown = 0;
/*     */     
/*     */     public static final int File = 1;
/*     */     
/*     */     public static final int Directory = 2;
/*     */     
/*     */     public static final int Disc = 3;
/*     */     
/*     */     public static final int Stream = 4;
/*     */     
/*     */     public static final int Playlist = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Meta
/*     */   {
/*     */     public static final int Title = 0;
/*     */     
/*     */     public static final int Artist = 1;
/*     */     
/*     */     public static final int Genre = 2;
/*     */     
/*     */     public static final int Copyright = 3;
/*     */     
/*     */     public static final int Album = 4;
/*     */     
/*     */     public static final int TrackNumber = 5;
/*     */     public static final int Description = 6;
/*     */     public static final int Rating = 7;
/*     */     public static final int Date = 8;
/*     */     public static final int Setting = 9;
/*     */     public static final int URL = 10;
/*     */     public static final int Language = 11;
/*     */     public static final int NowPlaying = 12;
/*     */     public static final int Publisher = 13;
/*     */     public static final int EncodedBy = 14;
/*     */     public static final int ArtworkURL = 15;
/*     */     public static final int TrackID = 16;
/*     */     public static final int TrackTotal = 17;
/*     */     public static final int Director = 18;
/*     */     public static final int Season = 19;
/*     */     public static final int Episode = 20;
/*     */     public static final int ShowName = 21;
/*     */     public static final int Actors = 22;
/*     */     public static final int AlbumArtist = 23;
/*     */     public static final int DiscNumber = 24;
/*     */     public static final int MAX = 25;
/*     */   }
/*     */   
/*     */   public static class State
/*     */   {
/*     */     public static final int NothingSpecial = 0;
/*     */     public static final int Opening = 1;
/*     */     public static final int Playing = 3;
/*     */     public static final int Paused = 4;
/*     */     public static final int Stopped = 5;
/*     */     public static final int Ended = 6;
/*     */     public static final int Error = 7;
/*     */     public static final int MAX = 8;
/*     */   }
/*     */   
/*     */   public static class Parse
/*     */   {
/*     */     public static final int ParseLocal = 0;
/*     */     public static final int ParseNetwork = 1;
/*     */     public static final int FetchLocal = 2;
/*     */     public static final int FetchNetwork = 4;
/*     */     public static final int DoInteract = 8;
/*     */   }
/*     */   
/*     */   public static class ParsedStatus
/*     */   {
/*     */     public static final int Skipped = 1;
/*     */     public static final int Failed = 2;
/*     */     public static final int Timeout = 3;
/*     */     public static final int Done = 4;
/*     */   }
/*     */   
/*     */   public static abstract class Track
/*     */   {
/*     */     public final int type;
/*     */     public final String codec;
/*     */     public final String originalCodec;
/*     */     public final int id;
/*     */     public final int profile;
/*     */     public final int level;
/*     */     public final int bitrate;
/*     */     public final String language;
/*     */     public final String description;
/*     */     
/*     */     public static class Type
/*     */     {
/*     */       public static final int Unknown = -1;
/*     */       public static final int Audio = 0;
/*     */       public static final int Video = 1;
/*     */       public static final int Text = 2;
/*     */     }
/*     */     
/*     */     protected Track(int type, String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
/* 143 */       this.type = type;
/* 144 */       this.codec = codec;
/* 145 */       this.originalCodec = originalCodec;
/* 146 */       this.id = id;
/* 147 */       this.profile = profile;
/* 148 */       this.level = level;
/* 149 */       this.bitrate = bitrate;
/* 150 */       this.language = language;
/* 151 */       this.description = description;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class AudioTrack
/*     */     extends Track
/*     */   {
/*     */     public final int channels;
/*     */     
/*     */     public final int rate;
/*     */ 
/*     */     
/*     */     public AudioTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int channels, int rate) {
/* 165 */       super(0, codec, originalCodec, id, profile, level, bitrate, language, description);
/* 166 */       this.channels = channels;
/* 167 */       this.rate = rate;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VideoTrack
/*     */     extends Track
/*     */   {
/*     */     public final int height;
/*     */ 
/*     */     
/*     */     public final int width;
/*     */ 
/*     */     
/*     */     public final int sarNum;
/*     */ 
/*     */     
/*     */     public final int sarDen;
/*     */ 
/*     */     
/*     */     public final int frameRateNum;
/*     */ 
/*     */     
/*     */     public final int frameRateDen;
/*     */ 
/*     */     
/*     */     public final int orientation;
/*     */ 
/*     */     
/*     */     public final int projection;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final class Orientation
/*     */     {
/*     */       public static final int TopLeft = 0;
/*     */       
/*     */       public static final int TopRight = 1;
/*     */       
/*     */       public static final int BottomLeft = 2;
/*     */       
/*     */       public static final int BottomRight = 3;
/*     */       
/*     */       public static final int LeftTop = 4;
/*     */       
/*     */       public static final int LeftBottom = 5;
/*     */       
/*     */       public static final int RightTop = 6;
/*     */       
/*     */       public static final int RightBottom = 7;
/*     */     }
/*     */ 
/*     */     
/*     */     public static final class Projection
/*     */     {
/*     */       public static final int Rectangular = 0;
/*     */       
/*     */       public static final int EquiRectangular = 1;
/*     */       
/*     */       public static final int CubemapLayoutStandard = 256;
/*     */     }
/*     */ 
/*     */     
/*     */     public VideoTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen, int orientation, int projection) {
/* 232 */       super(1, codec, originalCodec, id, profile, level, bitrate, language, description);
/* 233 */       this.height = height;
/* 234 */       this.width = width;
/* 235 */       this.sarNum = sarNum;
/* 236 */       this.sarDen = sarDen;
/* 237 */       this.frameRateNum = frameRateNum;
/* 238 */       this.frameRateDen = frameRateDen;
/* 239 */       this.orientation = orientation;
/* 240 */       this.projection = projection;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SubtitleTrack
/*     */     extends Track
/*     */   {
/*     */     public final String encoding;
/*     */ 
/*     */     
/*     */     public SubtitleTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, String encoding) {
/* 253 */       super(2, codec, originalCodec, id, profile, level, bitrate, language, description);
/* 254 */       this.encoding = encoding;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class UnknownTrack
/*     */     extends Track
/*     */   {
/*     */     public UnknownTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
/* 264 */       super(-1, codec, originalCodec, id, profile, level, bitrate, language, description);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Slave
/*     */   {
/*     */     public final int type;
/*     */     
/*     */     public final int priority;
/*     */     
/*     */     public final String uri;
/*     */ 
/*     */     
/*     */     public static class Type
/*     */     {
/*     */       public static final int Subtitle = 0;
/*     */       
/*     */       public static final int Audio = 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Slave(int type, int priority, String uri) {
/* 288 */       this.type = type;
/* 289 */       this.priority = priority;
/* 290 */       this.uri = uri;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Stats
/*     */   {
/*     */     public final int readBytes;
/*     */     
/*     */     public final float inputBitrate;
/*     */     
/*     */     public final int demuxReadBytes;
/*     */     
/*     */     public final float demuxBitrate;
/*     */     
/*     */     public final int demuxCorrupted;
/*     */     
/*     */     public final int demuxDiscontinuity;
/*     */     
/*     */     public final int decodedVideo;
/*     */     
/*     */     public final int decodedAudio;
/*     */     public final int displayedPictures;
/*     */     public final int lostPictures;
/*     */     public final int playedAbuffers;
/*     */     public final int lostAbuffers;
/*     */     public final int sentPackets;
/*     */     public final int sentBytes;
/*     */     public final float sendBitrate;
/*     */     
/*     */     public Stats(int readBytes, float inputBitrate, int demuxReadBytes, float demuxBitrate, int demuxCorrupted, int demuxDiscontinuity, int decodedVideo, int decodedAudio, int displayedPictures, int lostPictures, int playedAbuffers, int lostAbuffers, int sentPackets, int sentBytes, float sendBitrate) {
/* 321 */       this.readBytes = readBytes;
/* 322 */       this.inputBitrate = inputBitrate;
/* 323 */       this.demuxReadBytes = demuxReadBytes;
/* 324 */       this.demuxBitrate = demuxBitrate;
/* 325 */       this.demuxCorrupted = demuxCorrupted;
/* 326 */       this.demuxDiscontinuity = demuxDiscontinuity;
/* 327 */       this.decodedVideo = decodedVideo;
/* 328 */       this.decodedAudio = decodedAudio;
/* 329 */       this.displayedPictures = displayedPictures;
/* 330 */       this.lostPictures = lostPictures;
/* 331 */       this.playedAbuffers = playedAbuffers;
/* 332 */       this.lostAbuffers = lostAbuffers;
/* 333 */       this.sentPackets = sentPackets;
/* 334 */       this.sentBytes = sentBytes;
/* 335 */       this.sendBitrate = sendBitrate;
/*     */     }
/*     */   } }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\IMedia.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */