package com.iray.irs_vms.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    private static String TAG = "fileUtils";
    public static final String FILE_DIR_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "iRay";
    public static final String SNAP_SHOT_DIR = "snapShot";
    public static final String VIDEO_DIR = "video";
}
