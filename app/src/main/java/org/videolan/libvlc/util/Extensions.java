/*    */ package org.videolan.libvlc.util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Extensions
/*    */ {
/* 28 */   public static final HashSet<String> VIDEO = new HashSet<>();
/* 29 */   public static final HashSet<String> AUDIO = new HashSet<>();
/* 30 */   public static final HashSet<String> SUBTITLES = new HashSet<>();
/* 31 */   public static final HashSet<String> PLAYLIST = new HashSet<>();
/*    */ 
/*    */   
/*    */   static {
/* 35 */     String[] videoExtensions = { ".3g2", ".3gp", ".3gp2", ".3gpp", ".amv", ".asf", ".avi", ".divx", ".drc", ".dv", ".f4v", ".flv", ".gvi", ".gxf", ".h264", ".ismv", ".iso", ".m1v", ".m2v", ".m2t", ".m2ts", ".m4v", ".mkv", ".mov", ".mp2", ".mp2v", ".mp4", ".mp4v", ".mpe", ".mpeg", ".mpeg1", ".mpeg2", ".mpeg4", ".mpg", ".mpv2", ".mts", ".mtv", ".mxf", ".mxg", ".nsv", ".nut", ".nuv", ".ogm", ".ogv", ".ogx", ".ps", ".rec", ".rm", ".rmvb", ".tod", ".ts", ".tts", ".vob", ".vro", ".webm", ".wm", ".wmv", ".wtv", ".xesc" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     String[] audioExtensions = { ".3ga", ".a52", ".aac", ".ac3", ".adt", ".adts", ".aif", ".aifc", ".aiff", ".alac", ".amr", ".aob", ".ape", ".awb", ".caf", ".dts", ".flac", ".it", ".m4a", ".m4b", ".m4p", ".mid", ".mka", ".mlp", ".mod", ".mpa", ".mp1", ".mp2", ".mp3", ".mpc", ".mpga", ".oga", ".ogg", ".oma", ".opus", ".ra", ".ram", ".rmi", ".s3m", ".spx", ".tta", ".voc", ".vqf", ".w64", ".wav", ".wma", ".wv", ".xa", ".xm" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     String[] subtitlesExtensions = { ".idx", ".sub", ".srt", ".ssa", ".ass", ".smi", ".utf", ".utf8", ".utf-8", ".rt", ".aqt", ".txt", ".usf", ".jss", ".cdg", ".psb", ".mpsub", ".mpl2", ".pjs", ".dks", ".stl", ".vtt", ".ttml", ".mks" };
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     String[] playlistExtensions = { ".m3u", ".asx", ".b4s", ".pls", ".xspf" };
/*    */     
/* 57 */     VIDEO.addAll(Arrays.asList(videoExtensions));
/* 58 */     AUDIO.addAll(Arrays.asList(audioExtensions));
/* 59 */     SUBTITLES.addAll(Arrays.asList(subtitlesExtensions));
/* 60 */     PLAYLIST.addAll(Arrays.asList(playlistExtensions));
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\Extensions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */