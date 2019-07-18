package com.yzy.baselibrary.imageloader

import android.content.Context
import java.io.File

/**
 *description: BaseImageLoaderStrategy.
 *@date 2019/7/15
 *@author: yzy.
 */
interface BaseImageLoaderStrategy {

    /**
     * 加载图片
     */
    fun loadImage(config: ImageConfig)

    /**
     * 获取缓存文件
     */
    fun getCacheFile(context: Context, url: String): File?

    /**
     * 清除内存缓存
     */
    fun clearMemory(context: Context)

    /**
     * 清除磁盘缓存
     */
    fun cleanDiskCache(context: Context)
}
