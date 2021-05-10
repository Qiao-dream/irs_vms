/*   */ package org.videolan.libvlc.interfaces;
/*   */ 
/*   */ import android.content.res.AssetFileDescriptor;
/*   */ import android.net.Uri;
/*   */ import java.io.FileDescriptor;
/*   */ 
/*   */ public interface IMediaFactory
/*   */   extends IComponentFactory {
/* 9 */   public static final String factoryId = IMediaFactory.class.getName();
/*   */   
/*   */   IMedia getFromLocalPath(ILibVLC paramILibVLC, String paramString);
/*   */   
/*   */   IMedia getFromUri(ILibVLC paramILibVLC, Uri paramUri);
/*   */   
/*   */   IMedia getFromFileDescriptor(ILibVLC paramILibVLC, FileDescriptor paramFileDescriptor);
/*   */   
/*   */   IMedia getFromAssetFileDescriptor(ILibVLC paramILibVLC, AssetFileDescriptor paramAssetFileDescriptor);
/*   */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\IMediaFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */