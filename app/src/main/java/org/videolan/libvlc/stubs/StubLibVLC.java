/*    */ package org.videolan.libvlc.stubs;
/*    */ 
/*    */ import android.content.Context;
/*    */ import java.util.List;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ 
/*    */ public class StubLibVLC
/*    */   extends StubVLCObject<ILibVLC.Event>
/*    */   implements ILibVLC {
/*    */   private final Context mContext;
/*    */   
/*    */   public StubLibVLC(Context context, List<String> options) {
/* 13 */     this.mContext = context;
/*    */   }
/*    */   
/*    */   public StubLibVLC(Context context) {
/* 17 */     this(context, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Context getAppContext() {
/* 22 */     return this.mContext;
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\stubs\StubLibVLC.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */