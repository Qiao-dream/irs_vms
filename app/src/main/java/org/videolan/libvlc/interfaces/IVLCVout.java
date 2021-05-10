package org.videolan.libvlc.interfaces;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.annotation.MainThread;

public interface IVLCVout {
  @MainThread
  void setVideoView(SurfaceView paramSurfaceView);
  
  @MainThread
  @TargetApi(14)
  void setVideoView(TextureView paramTextureView);
  
  @MainThread
  void setVideoSurface(Surface paramSurface, SurfaceHolder paramSurfaceHolder);
  
  @MainThread
  @TargetApi(14)
  void setVideoSurface(SurfaceTexture paramSurfaceTexture);
  
  @MainThread
  void setSubtitlesView(SurfaceView paramSurfaceView);
  
  @MainThread
  @TargetApi(14)
  void setSubtitlesView(TextureView paramTextureView);
  
  @MainThread
  void setSubtitlesSurface(Surface paramSurface, SurfaceHolder paramSurfaceHolder);
  
  @MainThread
  @TargetApi(14)
  void setSubtitlesSurface(SurfaceTexture paramSurfaceTexture);
  
  @MainThread
  void attachViews(OnNewVideoLayoutListener paramOnNewVideoLayoutListener);
  
  @MainThread
  void attachViews();
  
  @MainThread
  void detachViews();
  
  @MainThread
  boolean areViewsAttached();
  
  @MainThread
  void addCallback(Callback paramCallback);
  
  @MainThread
  void removeCallback(Callback paramCallback);
  
  @MainThread
  void sendMouseEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  @MainThread
  void setWindowSize(int paramInt1, int paramInt2);
  
  public static interface Callback {
    @MainThread
    void onSurfacesCreated(IVLCVout param1IVLCVout);
    
    @MainThread
    void onSurfacesDestroyed(IVLCVout param1IVLCVout);
  }
  
  public static interface OnNewVideoLayoutListener {
    @MainThread
    void onNewVideoLayout(IVLCVout param1IVLCVout, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
  }
}


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\IVLCVout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */