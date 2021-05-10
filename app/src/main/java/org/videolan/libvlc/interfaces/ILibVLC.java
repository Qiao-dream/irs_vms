/*   */ package org.videolan.libvlc.interfaces;

import android.content.Context;

/*   */
/*   */ public interface ILibVLC extends IVLCObject<ILibVLC.Event> {
/*   */   Context getAppContext();
/*   */   
/*   */   public static class Event extends AbstractVLCEvent {
/*   */     protected Event(int type) {
/* 8 */       super(type);
/*   */     }
/*   */   }
/*   */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\ILibVLC.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */