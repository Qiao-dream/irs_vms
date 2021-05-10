/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import androidx.annotation.Nullable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.interfaces.IVLCObject;
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
/*     */ abstract class VLCObject<T extends AbstractVLCEvent>
/*     */   implements IVLCObject<T>
/*     */ {
/*  36 */   private AbstractVLCEvent.Listener<T> mEventListener = null;
/*  37 */   private Handler mHandler = null;
/*     */   final ILibVLC mILibVLC;
/*  39 */   private int mNativeRefCount = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long mInstance;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isReleased() {
/*  57 */     return (this.mNativeRefCount == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized boolean retain() {
/*  65 */     if (this.mNativeRefCount > 0) {
/*  66 */       this.mNativeRefCount++;
/*  67 */       return true;
/*     */     } 
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void release() {
/*  80 */     int refCount = -1;
/*  81 */     synchronized (this) {
/*  82 */       if (this.mNativeRefCount == 0)
/*     */         return; 
/*  84 */       if (this.mNativeRefCount > 0) {
/*  85 */         refCount = --this.mNativeRefCount;
/*     */       }
/*     */       
/*  88 */       if (refCount == 0)
/*  89 */         setEventListener(null); 
/*     */     } 
/*  91 */     if (refCount == 0) {
/*     */       
/*  93 */       nativeDetachEvents();
/*  94 */       synchronized (this) {
/*  95 */         onReleaseNative();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void finalize() {
/* 102 */     if (!isReleased()) {
/* 103 */       throw new AssertionError("VLCObject (" + getClass().getName() + ") finalized but not natively released (" + this.mNativeRefCount + " refs)");
/*     */     }
/*     */   }
/*     */   
/*     */   public ILibVLC getLibVLC() {
/* 108 */     return this.mILibVLC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void setEventListener(AbstractVLCEvent.Listener<T> listener) {
/* 118 */     setEventListener(listener, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void setEventListener(AbstractVLCEvent.Listener<T> listener, Handler handler) {
/* 127 */     if (this.mHandler != null)
/* 128 */       this.mHandler.removeCallbacksAndMessages(null); 
/* 129 */     this.mEventListener = listener;
/* 130 */     if (this.mEventListener == null) {
/* 131 */       this.mHandler = null;
/* 132 */     } else if (this.mHandler == null) {
/* 133 */       this.mHandler = (handler != null) ? handler : new Handler(Looper.getMainLooper());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VLCObject(ILibVLC libvlc) {
/* 155 */     this.mInstance = 0L; this.mILibVLC = libvlc; } protected VLCObject(IVLCObject parent) { this.mInstance = 0L; this.mILibVLC = parent.getLibVLC(); } protected VLCObject() { this.mInstance = 0L;
/*     */     this.mILibVLC = null; }
/*     */    private synchronized void dispatchEventFromNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/* 158 */     if (isReleased())
/*     */       return; 
/* 160 */     T event = onEventNative(eventType, arg1, arg2, argf1, args1);
/*     */     class EventRunnable
/*     */       implements Runnable {
/*     */       private final AbstractVLCEvent.Listener<T> listener;
/*     */       private final T event;
/*     */       
/*     */       EventRunnable(AbstractVLCEvent.Listener<T> listener, T event) {
/* 167 */         this.listener = listener;
/* 168 */         this.event = event;
/*     */       }
/*     */       
/*     */       public void run() {
/* 172 */         this.listener.onEvent(this.event);
/* 173 */         this.event.release();
/*     */       }
/*     */     };
/*     */     
/* 177 */     if (event != null && this.mEventListener != null && this.mHandler != null) {
/* 178 */       this.mHandler.post(new EventRunnable(this.mEventListener, event));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object getWeakReference() {
/* 185 */     return new WeakReference<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dispatchEventFromWeakNative(Object weak, int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
/* 190 */     VLCObject obj = ((WeakReference<VLCObject>)weak).get();
/* 191 */     if (obj != null)
/* 192 */       obj.dispatchEventFromNative(eventType, arg1, arg2, argf1, args1); 
/*     */   }
/*     */   
/*     */   protected abstract T onEventNative(int paramInt, long paramLong1, long paramLong2, float paramFloat, String paramString);
/*     */   
/*     */   protected abstract void onReleaseNative();
/*     */   
/*     */   private native void nativeDetachEvents();
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\VLCObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */