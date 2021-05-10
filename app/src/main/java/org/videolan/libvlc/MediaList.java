/*     */
package org.videolan.libvlc;
/*     */
/*     */

import android.os.Handler;
/*     */ import android.util.SparseArray;
/*     */ import androidx.annotation.Nullable;
/*     */ import org.videolan.libvlc.interfaces.AbstractVLCEvent;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
/*     */ import org.videolan.libvlc.interfaces.IMedia;
/*     */ import org.videolan.libvlc.interfaces.IMediaList;
/*     */ import org.videolan.libvlc.interfaces.IVLCObject;
import org.videolan.libvlc.VLCObject;

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
/*     */ public class MediaList
        /*     */ extends VLCObject<IMediaList.Event>
        /*     */ implements IMediaList
        /*     */ {
    /*     */   private static final String TAG = "LibVLC/MediaList";
    /*  36 */   private int mCount = 0;
    /*  37 */   private final SparseArray<IMedia> mMediaArray = new SparseArray();
    /*     */   private boolean mLocked = false;

    /*     */
    /*     */
    private void init() {
        /*  41 */
        lock();
        /*  42 */
        this.mCount = nativeGetCount();
        /*  43 */
        for (int i = 0; i < this.mCount; i++)
            /*  44 */
            this.mMediaArray.put(i, new Media(this, i));
        /*  45 */
        unlock();
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    public MediaList(ILibVLC iLibVLC) {
        /*  54 */
        super(iLibVLC);
        /*  55 */
        nativeNewFromLibVlc(iLibVLC);
        /*  56 */
        init();
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    protected MediaList(MediaDiscoverer md) {
        /*  63 */
        super(md);
        /*  64 */
        nativeNewFromMediaDiscoverer(md);
        /*  65 */
        init();
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    protected MediaList(IMedia m) {
        /*  72 */
        super((IVLCObject) m);
        /*  73 */
        nativeNewFromMedia(m);
        /*  74 */
        init();
        /*     */
    }

    /*     */
    /*     */
    private synchronized IMedia insertMediaFromEvent(int index) {
        /*  78 */
        for (int i = this.mCount - 1; i >= index; i--)
            /*  79 */
            this.mMediaArray.put(i + 1, this.mMediaArray.valueAt(i));
        /*  80 */
        this.mCount++;
        /*  81 */
        IMedia media = new Media(this, index);
        /*  82 */
        this.mMediaArray.put(index, media);
        /*  83 */
        return media;
        /*     */
    }

    /*     */
    /*     */
    private synchronized IMedia removeMediaFromEvent(int index) {
        /*  87 */
        this.mCount--;
        /*  88 */
        IMedia media = (IMedia) this.mMediaArray.get(index);
        /*  89 */
        if (media != null)
            /*  90 */ media.release();
        /*  91 */
        for (int i = index; i < this.mCount; i++) {
            /*  92 */
            this.mMediaArray.put(i, this.mMediaArray.valueAt(i + 1));
            /*     */
        }
        /*  94 */
        return media;
        /*     */
    }

    /*     */
    /*     */
    public void setEventListener(EventListener listener, Handler handler) {
        /*  98 */
        setEventListener(listener, handler);
        /*     */
    }

    /*     */
    /*     */
    protected synchronized Event onEventNative(int eventType, long arg1, long arg2, float argf1, @Nullable String args1) {
        /*     */
        int index;
        /* 103 */
        if (this.mLocked)
            /* 104 */ throw new IllegalStateException("already locked from event callback");
        /* 105 */
        this.mLocked = true;
        /* 106 */
        Event event = null;
        /*     */
        /*     */
        /* 109 */
        switch (eventType) {
            /*     */
            case 512:
                /* 111 */
                index = (int) arg1;
                /* 112 */
                if (index != -1) {
                    /* 113 */
                    IMedia media = insertMediaFromEvent(index);
                    /* 114 */
                    event = new Event(eventType, media, true, index);
                    /*     */
                }
                /*     */
                break;
            /*     */
            case 514:
                /* 118 */
                index = (int) arg1;
                /* 119 */
                if (index != -1) {
                    /* 120 */
                    IMedia media = removeMediaFromEvent(index);
                    /* 121 */
                    event = new Event(eventType, media, false, index);
                    /*     */
                }
                /*     */
                break;
            /*     */
            case 516:
                /* 125 */
                event = new Event(eventType, null, false, -1);
                /*     */
                break;
            /*     */
        }
        /* 128 */
        this.mLocked = false;
        /* 129 */
        return event;
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    public synchronized int getCount() {
        /* 136 */
        return this.mCount;
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    public synchronized IMedia getMediaAt(int index) {
        /* 146 */
        if (index < 0 || index >= getCount())
            /* 147 */ throw new IndexOutOfBoundsException();
        /* 148 */
        IMedia media = (IMedia) this.mMediaArray.get(index);
        /* 149 */
        media.retain();
        /* 150 */
        return media;
        /*     */
    }

    /*     */
    /*     */
    /*     */
    public void onReleaseNative() {
        /* 155 */
        for (int i = 0; i < this.mMediaArray.size(); i++) {
            /* 156 */
            IMedia media = (IMedia) this.mMediaArray.get(i);
            /* 157 */
            if (media != null) {
                /* 158 */
                media.release();
                /*     */
            }
            /*     */
        }
        /* 161 */
        nativeRelease();
        /*     */
    }

    /*     */
    /*     */
    private synchronized void lock() {
        /* 165 */
        if (this.mLocked)
            /* 166 */ throw new IllegalStateException("already locked");
        /* 167 */
        this.mLocked = true;
        /* 168 */
        nativeLock();
        /*     */
    }

    /*     */
    /*     */
    private synchronized void unlock() {
        /* 172 */
        if (!this.mLocked)
            /* 173 */ throw new IllegalStateException("not locked");
        /* 174 */
        this.mLocked = false;
        /* 175 */
        nativeUnlock();
        /*     */
    }

    /*     */
    /*     */
    public synchronized boolean isLocked() {
        /* 179 */
        return this.mLocked;
        /*     */
    }

    /*     */
    /*     */
    private native void nativeNewFromLibVlc(ILibVLC paramILibVLC);

    /*     */
    /*     */
    private native void nativeNewFromMediaDiscoverer(MediaDiscoverer paramMediaDiscoverer);

    /*     */
    /*     */
    private native void nativeNewFromMedia(IMedia paramIMedia);

    /*     */
    /*     */
    private native void nativeRelease();

    /*     */
    /*     */
    private native int nativeGetCount();

    /*     */
    /*     */
    private native void nativeLock();

    /*     */
    /*     */
    private native void nativeUnlock();
    /*     */
}





/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\MediaList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */