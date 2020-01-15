package com.yzy.example.widget.imagewatcher.loader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.UiThread;


/**
 * 图片加载器接口，实现 ImageLoader 可扩展自己的图片加载器
 * Created by hitomi on 2017/1/20.
 * <p>
 * email: 196425254@qq.com
 */
public interface ImageLoader {
    /**
     * 状态码，取消加载原图
     */
    int STATUS_DISPLAY_CANCEL = -1;
    /**
     * 状态码，加载原图失败
     */
    int STATUS_DISPLAY_FAILED = 0;
    /**
     * 状态码，加载原图成功
     */
    int STATUS_DISPLAY_SUCCESS = 1;

    /**
     * 加载并显示原图
     *
     * @param imageUrl       图片地址
     * @param imageView      用于图片加载成功后显示的 ImageView
     * @param placeholder    加载完成之前显示的占位图
     * @param sourceCallback 图片加载过程的回调
     */
    void showImage(String imageUrl, ImageView imageView, Drawable placeholder, final SourceCallback sourceCallback);

    /**
     * 异步加载图片
     *
     * @param imageUrl 图片地址
     * @param callback 片加载完成的回调
     */
    void loadImageAsync(String imageUrl, ImageView imageView, final ThumbnailCallback callback);

    interface SourceCallback {
        @UiThread
        void onProgress(int progress);


        @UiThread
        void onDelivered(int status);
    }

    interface ThumbnailCallback {
        @UiThread
        void onFinish(Drawable drawable);
    }
}
