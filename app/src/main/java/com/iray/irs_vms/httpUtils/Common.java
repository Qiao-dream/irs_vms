package com.iray.irs_vms.httpUtils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.iray.irs_vms.utils.DisplayUtil;

import java.io.File;

public class Common {
    // public static String HTTP_URL = "http://172.16.20.5:9900";
     public static String HTTP_URL = "http://172.16.20.48/gateway";  //用户名：Java  密码：123456
    // public static String HTTP_URL = "http://223.78.120.21:7799/gateway";   //用户名：admin 密码：admin123 /smartpark995
    public static String ACCESS_TOKEN=null;
    public static String USER_ID=null;
    public static String FILE_PATH = getSDPath() + File.separator + "iRayMedia/";
    public static final int DEFAULT_WIDTH = 384;
    public static final int DEFAULT_HEIGHT = 288;
    public  static final String  IMAGE_SIZE="IMAGE_SIZE";
    public  static final String  IMAGE_SIZE_VALUE="IMAGE_SIZE_VALUE";

    public static String MEDIA_TYPE = "application/x-www-form-urlencoded";
    public static String MEDIA_TYPE1 = ("application/json; charset=utf-8");


    private static String getSDPath() {
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            return Environment.getExternalStorageDirectory().toString();
        } else
            return Environment.getDownloadCacheDirectory().toString();
    }

    public static int getScreenHeight(Context ctx) {
        int height = DisplayUtil.getScreenHeight(ctx) - getStatusBarHeight(ctx);
        return height;
    }
    public static int getScreenWidth(Context ctx) {
        int width = DisplayUtil.getScreenWidth(ctx);
        return width;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context ctx) {
        Resources resources = ctx.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static void Delay(int time) {
        try {
            Thread.sleep(time);
        } catch (java.lang.InterruptedException e) {
            Log.i("aaaCommon", "***********e:" + e.toString());
        }
    }
}
