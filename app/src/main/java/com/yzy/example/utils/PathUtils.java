package com.yzy.example.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * description :
 *
 * @author : yzy
 * @date : 2019/4/10 10:02
 */
public class PathUtils {
    private static class SingleTonHolder {
        private static final PathUtils INSTANCE = new PathUtils();
    }

    public static PathUtils getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    private PathUtils() {
    }

    /**
     * 获取APP卸载自动删除的Cache目录，最后带/
     */
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File dir = context.getExternalCacheDir();
            if (dir != null) {
                cachePath = dir.getPath();
            }
        }
        if (TextUtils.isEmpty(cachePath)) {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator;
    }

    /**
     * 获取APP卸载自动删除的File目录，最后带/
     */
    public String getDiskFileDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File dir = context.getExternalFilesDir(null);
            if (dir != null) {
                cachePath = dir.getPath();
            }
        }
        if (TextUtils.isEmpty(cachePath)) {
            cachePath = context.getFilesDir().getPath();
        }
        //放到专有目录，方便清理
        File aimyBigImgDir = new File(cachePath + File.separator + "aimyBigImg");
        if (!aimyBigImgDir.exists()) {
            aimyBigImgDir.mkdirs();
        }
        return aimyBigImgDir.getPath() + File.separator;
    }
}
