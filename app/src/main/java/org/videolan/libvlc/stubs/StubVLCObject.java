/*    */ package org.videolan.libvlc.stubs;
/*    */ 
/*    */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ import org.videolan.libvlc.interfaces.IVLCObject;
/*    */ 
/*    */ public class StubVLCObject<T extends AbstractVLCEvent>
/*    */   implements IVLCObject<T> {
/*    */   public boolean retain() {
/* 10 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void release() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isReleased() {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ILibVLC getLibVLC() {
/* 25 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\stubs\StubVLCObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */