/*     */ package org.videolan.libvlc.util;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.Presentation;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.media.MediaRouter;
/*     */ import android.os.Bundle;
/*     */ import android.os.Handler;
/*     */ import android.view.Display;
/*     */ import android.view.SurfaceView;
/*     */ import android.view.WindowManager;
/*     */ import android.widget.FrameLayout;
/*     */ import androidx.annotation.NonNull;
/*     */ import androidx.annotation.Nullable;
/*     */ import androidx.core.content.ContextCompat;
/*     */ import androidx.lifecycle.LiveData;
/*     */ import androidx.lifecycle.Observer;
/*     */ import com.iray.irs_vms.R;
/*     */ import org.videolan.libvlc.RendererItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DisplayManager
/*     */ {
/*     */   private static final String TAG = "VLC/DisplayManager";
/*     */   private Activity mActivity;
/*     */   private LiveData<RendererItem> mSelectedRenderer;
/*     */   private RendererItem mRendererItem;
/*     */   private boolean mTextureView;
/*     */   private MediaRouter mMediaRouter;
/*     */   private MediaRouter.SimpleCallback mMediaRouterCallback;
/*     */   private SecondaryDisplay mPresentation;
/*     */   private DisplayType mDisplayType;
/*  40 */   private int mPresentationId = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Observer<RendererItem> mRendererObs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DialogInterface.OnDismissListener mOnDismissListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimary() {
/*  56 */     return (this.mDisplayType == DisplayType.PRIMARY);
/*     */   }
/*     */   
/*     */   public boolean isSecondary() {
/*  60 */     return (this.mDisplayType == DisplayType.PRESENTATION);
/*     */   }
/*     */   
/*     */   public boolean isOnRenderer() {
/*  64 */     return (this.mDisplayType == DisplayType.RENDERER);
/*     */   }
/*     */   
/*  67 */   public DisplayManager(@NonNull Activity activity, @Nullable LiveData<RendererItem> selectedRender, boolean textureView, boolean cloneMode, boolean benchmark) { this.mRendererObs = new Observer<RendererItem>()
/*     */       {
/*     */         public void onChanged(RendererItem rendererItem) {
/*  70 */           if (DisplayManager.this.mRendererItem != rendererItem) {
/*  71 */             DisplayManager.this.mRendererItem = rendererItem;
/*  72 */             DisplayManager.this.updateDisplayType();
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/*  77 */     this.mOnDismissListener = new DialogInterface.OnDismissListener()
/*     */       {
/*     */         public void onDismiss(DialogInterface dialog) {
/*  80 */           if (dialog == DisplayManager.this.mPresentation) {
/*     */             
/*  82 */             DisplayManager.this.mPresentation = null;
/*  83 */             DisplayManager.this.mPresentationId = -1;
/*     */           }  } }; this.mActivity = activity; this.mSelectedRenderer = selectedRender; this.mMediaRouter = (MediaRouter)ContextCompat.getSystemService(activity.getApplicationContext(), MediaRouter.class); this.mTextureView = textureView; this.mPresentation = (!cloneMode && !benchmark && selectedRender != null && selectedRender.getValue() == null) ? createPresentation() : null;
/*     */     if (this.mSelectedRenderer != null) {
/*     */       this.mRendererItem = (RendererItem)this.mSelectedRenderer.getValue();
/*     */       this.mSelectedRenderer.observeForever(this.mRendererObs);
/*     */     } 
/*  89 */     this.mDisplayType = benchmark ? DisplayType.PRIMARY : getCurrentType(); } public void release() { if (this.mPresentation != null) {
/*  90 */       this.mPresentation.dismiss();
/*  91 */       this.mPresentation = null;
/*     */     } 
/*  93 */     if (this.mSelectedRenderer != null) this.mSelectedRenderer.removeObserver(this.mRendererObs);  }
/*     */ 
/*     */   
/*     */   private void updateDisplayType() {
/*  97 */     if (this.mDisplayType != getCurrentType()) (new Handler()).postDelayed(new Runnable()
/*     */           {
/*     */             public void run() {
/* 100 */               DisplayManager.this.mActivity.recreate();
/*     */             }
/*     */           },  100L); 
/*     */   }
/*     */   
/*     */   private DisplayType getCurrentType() {
/* 106 */     if (this.mPresentationId != -1) return DisplayType.PRESENTATION; 
/* 107 */     if (this.mRendererItem != null) return DisplayType.RENDERER; 
/* 108 */     return DisplayType.PRIMARY;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SecondaryDisplay getPresentation() {
/* 113 */     return this.mPresentation;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public DisplayType getDisplayType() {
/* 118 */     return this.mDisplayType;
/*     */   }
/*     */   
/*     */   private SecondaryDisplay createPresentation() {
/* 122 */     if (this.mMediaRouter == null) return null; 
/* 123 */     MediaRouter.RouteInfo route = this.mMediaRouter.getSelectedRoute(2);
/* 124 */     Display presentationDisplay = (route != null) ? route.getPresentationDisplay() : null;
/* 125 */     if (presentationDisplay != null) {
/*     */       
/* 127 */       SecondaryDisplay presentation = new SecondaryDisplay((Context)this.mActivity, presentationDisplay);
/* 128 */       presentation.setOnDismissListener(this.mOnDismissListener);
/*     */       try {
/* 130 */         presentation.show();
/* 131 */         this.mPresentationId = presentationDisplay.getDisplayId();
/* 132 */         return presentation;
/* 133 */       } catch (WindowManager.InvalidDisplayException ex) {
/*     */         
/* 135 */         this.mPresentationId = -1;
/*     */       } 
/*     */     } 
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   public boolean setMediaRouterCallback() {
/* 142 */     if (this.mMediaRouter == null || this.mMediaRouterCallback != null) return false; 
/* 143 */     this.mMediaRouterCallback = new MediaRouter.SimpleCallback()
/*     */       {
/*     */         public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo info)
/*     */         {
/* 147 */           int newDisplayId = (info.getPresentationDisplay() != null) ? info.getPresentationDisplay().getDisplayId() : -1;
/* 148 */           if (newDisplayId == DisplayManager.this.mPresentationId)
/* 149 */             return;  DisplayManager.this.mPresentationId = newDisplayId;
/* 150 */           if (newDisplayId == -1) { DisplayManager.this.removePresentation(); }
/* 151 */           else { DisplayManager.this.updateDisplayType(); }
/*     */            }
/*     */       };
/* 154 */     this.mMediaRouter.addCallback(2, (MediaRouter.Callback)this.mMediaRouterCallback);
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   public void removeMediaRouterCallback() {
/* 159 */     if (this.mMediaRouter != null) this.mMediaRouter.removeCallback((MediaRouter.Callback)this.mMediaRouterCallback); 
/* 160 */     this.mMediaRouterCallback = null;
/*     */   }
/*     */   
/*     */   private void removePresentation() {
/* 164 */     if (this.mMediaRouter == null) {
/*     */       return;
/*     */     }
/* 167 */     if (this.mPresentation != null) {
/* 168 */       this.mPresentation.dismiss();
/* 169 */       this.mPresentation = null;
/*     */     } 
/* 171 */     updateDisplayType();
/*     */   }
/*     */   
/*     */   public class SecondaryDisplay
/*     */     extends Presentation {
/*     */     public static final String TAG = "VLC/SecondaryDisplay";
/*     */     private FrameLayout mSurfaceFrame;
/*     */     private SurfaceView mSurfaceView;
/*     */     private SurfaceView mSubtitlesSurfaceView;
/*     */     
/*     */     public SecondaryDisplay(Context outerContext, Display display) {
/* 182 */       super(outerContext, display);
/*     */     }
/*     */     
/*     */     public SecondaryDisplay(Context outerContext, Display display, int theme) {
/* 186 */       super(outerContext, display, theme);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onCreate(Bundle savedInstanceState) {
/* 191 */       super.onCreate(savedInstanceState);
/* 192 */       setContentView(R.layout.player_remote);
/* 193 */       this.mSurfaceFrame = (FrameLayout)findViewById(R.id.remote_player_surface_frame);
/* 194 */       this.mSurfaceView = (SurfaceView)this.mSurfaceFrame.findViewById(R.id.remote_player_surface);
/* 195 */       this.mSubtitlesSurfaceView = (SurfaceView)this.mSurfaceFrame.findViewById(R.id.remote_subtitles_surface);
/* 196 */       this.mSubtitlesSurfaceView.setZOrderMediaOverlay(true);
/* 197 */       this.mSubtitlesSurfaceView.getHolder().setFormat(-3);
/*     */     }
/*     */ 
/*     */     
/*     */     public FrameLayout getSurfaceFrame() {
/* 202 */       return this.mSurfaceFrame;
/*     */     }
/*     */     
/*     */     public SurfaceView getSurfaceView() {
/* 206 */       return this.mSurfaceView;
/*     */     }
/*     */     
/*     */     public SurfaceView getSubtitlesSurfaceView() {
/* 210 */       return this.mSubtitlesSurfaceView;
/*     */     } }
/*     */   
/*     */   public enum DisplayType {
/* 214 */     PRIMARY, PRESENTATION, RENDERER;
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\DisplayManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */