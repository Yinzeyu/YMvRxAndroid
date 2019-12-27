package com.yzy.example.imageloader

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import java.io.File
import java.io.InputStream

/**
 *description: GlideConfiguration.
 *@date 2019/7/15
 *@author: yzy.
 */

@GlideModule
class GlideConfiguration : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //配置磁盘缓存
        val cacheDir = context.cacheDir.path + File.separator + ".image"
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, cacheDir,
            IMAGE_DISK_CACHE_MAX_SIZE
        ))

        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize

        val customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()

        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //Glide默认使用HttpURLConnection做网络请求,在这切换成okhttp请求
        registry.replace(
                GlideUrl::class.java, InputStream::class.java,
                OkHttpUrlLoader.Factory(GlideHttpClientManager.okHttpClient)
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    companion object {
        const val IMAGE_DISK_CACHE_MAX_SIZE = 200 * 1024 * 1024L//图片缓存文件最大值为200Mb
    }
}