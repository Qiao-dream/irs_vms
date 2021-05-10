/*    */ package org.videolan.libvlc;
/*    */ 
/*    */ import androidx.annotation.Nullable;
/*    */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*    */ import org.videolan.libvlc.interfaces.ILibVLC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RendererItem
/*    */   extends VLCObject<RendererItem.Event>
/*    */ {
/*    */   public static final int LIBVLC_RENDERER_CAN_AUDIO = 1;
/*    */   public static final int LIBVLC_RENDERER_CAN_VIDEO = 2;
/*    */   public final String name;
/*    */   public final String displayName;
/*    */   final String type;
/*    */   final String iconUrl;
/*    */   final int flags;
/*    */   private final long ref;
/*    */   
/*    */   RendererItem(String name, String type, String iconUrl, int flags, long ref) {
/* 23 */     int index = name.lastIndexOf('-');
/* 24 */     this.name = name;
/* 25 */     this.displayName = (index == -1) ? name : name.replace('-', ' ');
/* 26 */     this.type = type;
/* 27 */     this.iconUrl = iconUrl;
/* 28 */     this.flags = flags;
/* 29 */     this.ref = ref;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 34 */     return (obj instanceof RendererItem && this.ref == ((RendererItem)obj).ref);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/* 39 */     return new Event(eventType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onReleaseNative() {
/* 44 */     nativeReleaseItem();
/*    */   }
/*    */   private native void nativeReleaseItem();
/*    */   
/*    */   public static class Event extends AbstractVLCEvent { protected Event(int type) {
/* 49 */       super(type);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\RendererItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */