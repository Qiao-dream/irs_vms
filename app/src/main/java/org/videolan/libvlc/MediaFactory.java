/*    */ package org.videolan.libvlc;
/*    */ 
/*    */ import android.content.res.AssetFileDescriptor;
/*    */ import android.net.Uri;
/*    */ import java.io.FileDescriptor;
/*    */ import org.videolan.libvlc.interfaces.IComponentFactory;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ import org.videolan.libvlc.interfaces.IMedia;
/*    */ import org.videolan.libvlc.interfaces.IMediaFactory;
/*    */ 
/*    */ public class MediaFactory
/*    */   implements IMediaFactory {
/*    */   static {
/* 14 */     FactoryManager.registerFactory(IMediaFactory.factoryId, (IComponentFactory)new MediaFactory());
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromLocalPath(ILibVLC iLibVLC, String path) {
/* 19 */     return new Media(iLibVLC, path);
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromUri(ILibVLC iLibVLC, Uri uri) {
/* 24 */     return new Media(iLibVLC, uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromFileDescriptor(ILibVLC iLibVLC, FileDescriptor fd) {
/* 29 */     return new Media(iLibVLC, fd);
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getFromAssetFileDescriptor(ILibVLC iLibVLC, AssetFileDescriptor assetFileDescriptor) {
/* 34 */     return new Media(iLibVLC, assetFileDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\MediaFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */