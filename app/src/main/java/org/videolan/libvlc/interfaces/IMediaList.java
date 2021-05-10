/*    */ package org.videolan.libvlc.interfaces;
/*    */ 
/*    */ import android.os.Handler;
/*    */ 
/*    */ public interface IMediaList
/*    */   extends IVLCObject<IMediaList.Event> {
/*    */   void setEventListener(EventListener paramEventListener, Handler paramHandler);
/*    */   
/*    */   int getCount();
/*    */   
/*    */   IMedia getMediaAt(int paramInt);
/*    */   
/*    */   boolean isLocked();
/*    */   
/*    */   public static interface EventListener extends AbstractVLCEvent.Listener<Event> {}
/*    */   
/*    */   public static class Event extends AbstractVLCEvent {
/*    */     public static final int ItemAdded = 512;
/*    */     public static final int ItemDeleted = 514;
/*    */     public static final int EndReached = 516;
/*    */     
/*    */     public Event(int type, IMedia media, boolean retain, int index) {
/* 23 */       super(type);
/* 24 */       if (retain && (media == null || !media.retain()))
/* 25 */         throw new IllegalStateException("invalid media reference"); 
/* 26 */       this.media = media;
/* 27 */       this.retain = retain;
/* 28 */       this.index = index;
/*    */     }
/*    */     public final IMedia media; private final boolean retain; public final int index;
/*    */     
/*    */     public void release() {
/* 33 */       if (this.retain)
/* 34 */         this.media.release(); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\IMediaList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */