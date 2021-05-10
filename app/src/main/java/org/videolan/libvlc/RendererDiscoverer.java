/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import androidx.annotation.Nullable;
/*     */ import androidx.collection.LongSparseArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RendererDiscoverer
/*     */   extends VLCObject<RendererDiscoverer.Event>
/*     */ {
/*     */   private static final String TAG = "LibVLC/RendererDiscoverer";
/*  35 */   final List<RendererItem> mRenderers = new ArrayList<>();
/*     */   private final LongSparseArray<RendererItem> index;
/*     */   
/*     */   public static class Event
/*     */     extends AbstractVLCEvent {
/*     */     public static final int ItemAdded = 1282;
/*     */     public static final int ItemDeleted = 1283;
/*     */     private final RendererItem item;
/*     */     
/*     */     protected Event(int type, long nativeHolder, RendererItem item) {
/*  45 */       super(type, nativeHolder);
/*  46 */       this.item = item;
/*  47 */       item.retain();
/*     */     }
/*     */     
/*     */     public RendererItem getItem() {
/*  51 */       return this.item;
/*     */     }
/*     */ 
/*     */     
/*     */     public void release() {
/*  56 */       this.item.release();
/*  57 */       super.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static RendererItem createItemFromNative(String name, String type, String iconUrl, int flags, long ref) {
/*  63 */     return new RendererItem(name, type, iconUrl, flags, ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RendererDiscoverer(ILibVLC iLibVLC, String name) {
/*  75 */     super(iLibVLC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     this.index = new LongSparseArray(); nativeNew(iLibVLC, name);
/*     */   }
/* 131 */   public boolean start() { if (isReleased()) throw new IllegalStateException("MediaDiscoverer is released");  return nativeStart(); } public void stop() { if (isReleased()) throw new IllegalStateException("MediaDiscoverer is released");  setEventListener(null); nativeStop(); release(); } public void setEventListener(EventListener listener) { setEventListener(listener); } private synchronized RendererItem insertItemFromEvent(long arg1) { RendererItem item = nativeNewItem(arg1);
/* 132 */     this.index.put(arg1, item);
/* 133 */     this.mRenderers.add(item);
/* 134 */     return item; }
/*     */   public static Description[] list(ILibVLC ILibVlc) { return nativeList(ILibVlc); }
/*     */   public static interface EventListener extends AbstractVLCEvent.Listener<Event> {}
/*     */   public static class Description {
/* 138 */     public final String name; final String longName; private Description(String name, String longName) { this.name = name; this.longName = longName; } } protected Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) { switch (eventType) { case 1282: return new Event(eventType, arg1, insertItemFromEvent(arg1));case 1283: return new Event(eventType, arg1, removeItemFromEvent(arg1)); }  return null; } private synchronized RendererItem removeItemFromEvent(long arg1) { RendererItem item = (RendererItem)this.index.get(arg1);
/* 139 */     if (item != null) {
/* 140 */       this.index.remove(arg1);
/* 141 */       this.mRenderers.remove(item);
/* 142 */       item.release();
/*     */     } 
/* 144 */     return item; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onReleaseNative() {
/* 149 */     for (RendererItem item : this.mRenderers) item.release(); 
/* 150 */     this.mRenderers.clear();
/* 151 */     nativeRelease();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Description createDescriptionFromNative(String name, String longName) {
/* 156 */     return new Description(name, longName);
/*     */   }
/*     */   
/*     */   private native void nativeNew(ILibVLC paramILibVLC, String paramString);
/*     */   
/*     */   private native void nativeRelease();
/*     */   
/*     */   private native boolean nativeStart();
/*     */   
/*     */   private native void nativeStop();
/*     */   
/*     */   private static native Description[] nativeList(ILibVLC paramILibVLC);
/*     */   
/*     */   private native RendererItem nativeNewItem(long paramLong);
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\RendererDiscoverer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */