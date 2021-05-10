package com.iray.irs_vms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.PreviewActivity;
import com.iray.irs_vms.utils.DisplayUtil;

import org.videolan.libvlc.util.VLCVideoLayout;
import com.iray.irs_vms.R;

public class MVlCVidoLayout extends VLCVideoLayout {
    public MVlCVidoLayout(@NonNull Context context) {
        super(context);
//        this.setupLayout(context);
    }

    public MVlCVidoLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        this.setupLayout(context);
    }

    public MVlCVidoLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.setupLayout(context);
    }

    public MVlCVidoLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        this.setupLayout(context);
    }

//    private void setupLayout(@NonNull Context context) {
//        inflate(context, layout.vlc_video_layout, this);
//
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        this.setBackgroundResource(R.color.red);
//        int width = DisplayUtil.getScreenWidth(PreviewActivity.mContext);
//        int height = width * 288 / 384;
//        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)this.getLayoutParams();
//        lp.height = height;
//        lp.width = width;
//        this.setLayoutParams(lp);
    }
}
