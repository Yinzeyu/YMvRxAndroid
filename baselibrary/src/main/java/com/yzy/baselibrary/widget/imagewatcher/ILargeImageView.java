package com.yzy.baselibrary.widget.imagewatcher;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import com.yzy.baselibrary.widget.imagewatcher.factory.BitmapDecoderFactory;

/**
 * Created by LuckyJayce on 2016/11/24.
 */
public interface ILargeImageView {

    int getImageWidth();

    int getImageHeight();

    boolean hasLoad();

    void setOnImageLoadListener(BlockImageLoader.OnImageLoadListener onImageLoadListener);

    void setImage(BitmapDecoderFactory factory);

    void setImage(BitmapDecoderFactory factory, Drawable defaultDrawable);

    void setImage(Bitmap bm);

    void setImage(Drawable drawable);

    void setImage(@DrawableRes int resId);

    void setImageDrawable(Drawable drawable);

    void setScale(float scale);

    float getScale();

    BlockImageLoader.OnImageLoadListener getOnImageLoadListener();
}