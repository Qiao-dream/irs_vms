/*    */ package org.videolan.libvlc.stubs;
/*    */ 
/*    */ import android.os.Handler;
/*    */ import org.videolan.libvlc.interfaces.IMedia;
/*    */ import org.videolan.libvlc.interfaces.IMediaList;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StubMediaList
/*    */   extends StubVLCObject<IMediaList.Event>
/*    */   implements IMediaList
/*    */ {
/*    */   public void setEventListener(IMediaList.EventListener listener, Handler handler) {}
/*    */   
/*    */   public int getCount() {
/* 16 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public IMedia getMediaAt(int index) {
/* 21 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLocked() {
/* 26 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\stubs\StubMediaList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */