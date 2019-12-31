package com.yzy.example.imModel.emoji

import android.content.Context
import android.widget.ImageView

interface IImageLoader {
    /**
     * 图片加载
     */
    fun displayImage(context: Context, path: String, imageView: ImageView)
}