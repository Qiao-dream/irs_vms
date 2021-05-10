/*    */ package org.videolan.libvlc;
/*    */ 
/*    */ import android.content.Context;
/*    */ import java.util.List;
/*    */ import org.videolan.libvlc.interfaces.IComponentFactory;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ import org.videolan.libvlc.interfaces.ILibVLCFactory;
/*    */ 
/*    */ public class LibVLCFactory
/*    */   implements ILibVLCFactory {
/*    */   static {
/* 12 */     FactoryManager.registerFactory(ILibVLCFactory.factoryId, (IComponentFactory)new LibVLCFactory());
/*    */   }
/*    */ 
/*    */   
/*    */   public ILibVLC getFromOptions(Context context, List<String> options) {
/* 17 */     return new LibVLC(context, options);
/*    */   }
/*    */ 
/*    */   
/*    */   public ILibVLC getFromContext(Context context) {
/* 22 */     return new LibVLC(context, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\LibVLCFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */