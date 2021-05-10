/*    */ package org.videolan.libvlc.stubs;
/*    */ 
/*    */ import android.content.Context;
/*    */ import java.util.List;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ import org.videolan.libvlc.interfaces.ILibVLCFactory;
/*    */ 
/*    */ 
/*    */ public class StubLibVLCFactory
/*    */   implements ILibVLCFactory
/*    */ {
/*    */   public ILibVLC getFromOptions(Context context, List<String> options) {
/* 13 */     return new StubLibVLC(context, options);
/*    */   }
/*    */ 
/*    */   
/*    */   public ILibVLC getFromContext(Context context) {
/* 18 */     return new StubLibVLC(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\stubs\StubLibVLCFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */