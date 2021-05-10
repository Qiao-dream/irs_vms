package org.videolan.libvlc.interfaces;

public interface IVLCObject<T extends AbstractVLCEvent> {
  boolean retain();
  
  void release();
  
  boolean isReleased();
  
  ILibVLC getLibVLC();
}


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\interfaces\IVLCObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */