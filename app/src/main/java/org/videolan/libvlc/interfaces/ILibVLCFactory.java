/*   */ package org.videolan.libvlc.interfaces;
/*   */ 
/*   */ import android.content.Context;
/*   */ import java.util.List;
/*   */ 
/*   */ public interface ILibVLCFactory
/*   */   extends IComponentFactory {
/* 8 */   public static final String factoryId = ILibVLCFactory.class.getName();
/*   */   
/*   */   ILibVLC getFromOptions(Context paramContext, List<String> paramList);
/*   */   
/*   */   ILibVLC getFromContext(Context paramContext);
/*   */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\ILibVLCFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */