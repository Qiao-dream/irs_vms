/*    */ package org.videolan.libvlc.util;
/*    */ 
/*    */ import android.net.Uri;
/*    */ import android.os.Build;
/*    */ import java.io.File;
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
/*    */ 
/*    */ public class AndroidUtil
/*    */ {
/* 30 */   public static final boolean isPOrLater = (Build.VERSION.SDK_INT >= 28);
/* 31 */   public static final boolean isOOrLater = (isPOrLater || Build.VERSION.SDK_INT >= 26);
/* 32 */   public static final boolean isNougatMR1OrLater = (isOOrLater || Build.VERSION.SDK_INT >= 25);
/* 33 */   public static final boolean isNougatOrLater = (isNougatMR1OrLater || Build.VERSION.SDK_INT >= 24);
/* 34 */   public static final boolean isMarshMallowOrLater = (isNougatOrLater || Build.VERSION.SDK_INT >= 23);
/* 35 */   public static final boolean isLolliPopOrLater = (isMarshMallowOrLater || Build.VERSION.SDK_INT >= 21);
/* 36 */   public static final boolean isKitKatOrLater = (isLolliPopOrLater || Build.VERSION.SDK_INT >= 19);
/* 37 */   public static final boolean isJellyBeanMR2OrLater = (isKitKatOrLater || Build.VERSION.SDK_INT >= 18);
/*    */   
/*    */   public static File UriToFile(Uri uri) {
/* 40 */     return new File(uri.getPath().replaceFirst("file://", ""));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Uri PathToUri(String path) {
/* 50 */     return Uri.fromFile(new File(path));
/*    */   }
/*    */   
/*    */   public static Uri LocationToUri(String location) {
/* 54 */     Uri uri = Uri.parse(location);
/* 55 */     if (uri.getScheme() == null)
/* 56 */       throw new IllegalArgumentException("location has no scheme"); 
/* 57 */     return uri;
/*    */   }
/*    */   
/*    */   public static Uri FileToUri(File file) {
/* 61 */     return Uri.fromFile(file);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\AndroidUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */