/*    */ package org.videolan.libvlc.interfaces;
/*    */ 
/*    */ import androidx.annotation.Nullable;
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
/*    */ public abstract class AbstractVLCEvent
/*    */ {
/*    */   public final int type;
/*    */   protected final long arg1;
/*    */   protected final long arg2;
/*    */   protected final float argf1;
/*    */   protected final String args1;
/*    */   
/*    */   public AbstractVLCEvent(int type) {
/* 33 */     this.type = type;
/* 34 */     this.arg1 = this.arg2 = 0L;
/* 35 */     this.argf1 = 0.0F;
/* 36 */     this.args1 = null;
/*    */   }
/*    */   public AbstractVLCEvent(int type, long arg1) {
/* 39 */     this.type = type;
/* 40 */     this.arg1 = arg1;
/* 41 */     this.arg2 = 0L;
/* 42 */     this.argf1 = 0.0F;
/* 43 */     this.args1 = null;
/*    */   }
/*    */   public AbstractVLCEvent(int type, long arg1, long arg2) {
/* 46 */     this.type = type;
/* 47 */     this.arg1 = arg1;
/* 48 */     this.arg2 = arg2;
/* 49 */     this.argf1 = 0.0F;
/* 50 */     this.args1 = null;
/*    */   }
/*    */   public AbstractVLCEvent(int type, float argf) {
/* 53 */     this.type = type;
/* 54 */     this.arg1 = this.arg2 = 0L;
/* 55 */     this.argf1 = argf;
/* 56 */     this.args1 = null;
/*    */   }
/*    */   public AbstractVLCEvent(int type, long arg1, @Nullable String args1) {
/* 59 */     this.type = type;
/* 60 */     this.arg1 = arg1;
/* 61 */     this.arg2 = 0L;
/* 62 */     this.argf1 = 0.0F;
/* 63 */     this.args1 = args1;
/*    */   }
/*    */   
/*    */   public void release() {}
/*    */   
/*    */   public static interface Listener<T extends AbstractVLCEvent> {
/*    */     void onEvent(T param1T);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\AbstractVLCEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */