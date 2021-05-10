/*    */ package org.videolan.libvlc.util;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.AttributeSet;
/*    */ import android.widget.FrameLayout;
/*    */ import androidx.annotation.NonNull;
/*    */ import androidx.annotation.Nullable;
/*    */ import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.iray.irs_vms.R;

/*    */
public class VLCVideoLayout extends FrameLayout {
    public VLCVideoLayout(@NonNull Context context) {
        super(context);
        this.setupLayout(context);
    }

    public VLCVideoLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setupLayout(context);
    }

    public VLCVideoLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setupLayout(context);
    }

    @RequiresApi(
            api = 21
    )
    public VLCVideoLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setupLayout(context);
    }

    private void setupLayout(@NonNull Context context) {
        inflate(context, R.layout.vlc_video_layout, this);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        this.setBackgroundResource(color.black);
//        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)this.getLayoutParams();
//        lp.height = -1;
//        lp.width = -1;
//        this.setLayoutParams(lp);
    }
}