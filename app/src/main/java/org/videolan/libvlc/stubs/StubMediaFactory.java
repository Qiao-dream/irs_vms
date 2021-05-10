/*    */ package org.videolan.libvlc.stubs;
/*    */ 
/*    */ import android.content.res.AssetFileDescriptor;
/*    */ import android.net.Uri;
/*    */ import java.io.FileDescriptor;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ import org.videolan.libvlc.interfaces.IMedia;
/*    */ import org.videolan.libvlc.interfaces.IMediaFactory;
/*    */ 
/*    */ 
/*    */ public class StubMediaFactory
/*    */   implements IMediaFactory
/*    */ {
/*    */   public IMedia getFromLocalPath(ILibVLC iLibVLC, String path) {
/* 15 */     return new StubMedia(iLibVLC, path);
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromUri(ILibVLC iLibVLC, Uri uri) {
/* 20 */     return new StubMedia(iLibVLC, uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromFileDescriptor(ILibVLC iLibVLC, FileDescriptor fd) {
/* 25 */     return new StubMedia(iLibVLC, fd);
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromAssetFileDescriptor(ILibVLC iLibVLC, AssetFileDescriptor assetFileDescriptor) {
/* 30 */     return new StubMedia(iLibVLC, assetFileDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\stubs\StubMediaFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */