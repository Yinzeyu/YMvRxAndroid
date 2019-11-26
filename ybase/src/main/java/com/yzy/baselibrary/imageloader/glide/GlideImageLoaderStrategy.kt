package com.yzy.baselibrary.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache
import com.yzy.baselibrary.imageloader.BaseImageLoaderStrategy
import com.yzy.baselibrary.imageloader.DiskCacheStrategyType
import com.yzy.baselibrary.imageloader.ImageConfig
import com.yzy.baselibrary.imageloader.ImageLoadScaleType
import com.yzy.baselibrary.imageloader.glide.transformations.BlurTransformation
import com.yzy.baselibrary.imageloader.glide.transformations.RoundedCornersTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.signature.EmptySignature
import com.yzy.baselibrary.imageloader.cache.DataCacheKey
import java.io.File

/**
 *description: Glide实现的图片加载.
 *@date 2019/7/15
 *@author: yzy.
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy {
    override fun getCacheFile(context: Context, url: String): File? {
        try {
            val dataCacheKey = DataCacheKey(GlideUrl(url), EmptySignature.obtain())
            val safeKeyGenerator = SafeKeyGenerator()
            val safeKey = safeKeyGenerator.getSafeKey(dataCacheKey)
            val file = File(context.cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR)
            val diskLruCache =
                DiskLruCache.open(file, 1, 1, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE.toLong())
            val value = diskLruCache.get(safeKey)
            if (value != null) {
                return value.getFile(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun clearMemory(context: Context) {
        Glide.get(context.applicationContext).clearMemory()
    }

    override fun cleanDiskCache(context: Context) {
        Thread(Runnable { Glide.get(context.applicationContext).clearDiskCache() }).start()
    }

    override fun loadImage(config: ImageConfig) {
        if (config.context == null || (config.url.isNullOrBlank() && config.uri == null && config.resourceId == null)) {
            return
        }
        config.context?.let { context ->
            val glideRequest = GlideApp.with(context)
                .asBitmap()
                .apply {
                    if (config.url?.isNotBlank() == true) {
                        load(config.url)
                    }
                    if (config.url.isNullOrBlank() && config.uri != null) {
                        load(config.uri)
                    }
                    if (config.url.isNullOrBlank() && config.uri == null && config.resourceId != null) {
                        load(config.resourceId)
                    }
                    //缓存策略
                    when (config.cacheStrategy) {
                        DiskCacheStrategyType.ALL -> diskCacheStrategy(DiskCacheStrategy.ALL)
                        DiskCacheStrategyType.NONE -> diskCacheStrategy(DiskCacheStrategy.NONE)
                        DiskCacheStrategyType.RESOURCE -> diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        DiskCacheStrategyType.DATA -> diskCacheStrategy(DiskCacheStrategy.DATA)
                        DiskCacheStrategyType.AUTOMATIC -> diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    }
                    //ScaleType
                    when (config.imageLoadScaleType) {
                        ImageLoadScaleType.FitCenter -> fitCenter()
                        ImageLoadScaleType.CenterInside -> centerInside()
                        ImageLoadScaleType.CenterCrop -> centerCrop()
                        ImageLoadScaleType.CircleCrop -> circleCrop()
                        ImageLoadScaleType.NoScaleType -> {
                        }
                    }
                    //设置占位图片
                    if (config.placeholder != 0) {
                        placeholder(config.placeholder)
                    }
                    //设置错误的占位图片
                    if (config.errorSrc != 0) {
                        error(config.errorSrc)
                    }
                    //高斯模糊
                    if (config.blur) {
                        transform(BlurTransformation(config.blurRadius, config.sampling))
                    }
                    //圆角
                    if (config.corner) {
                        transform(
                            RoundedCornersTransformation(
                                config.cornerRadius,
                                config.margin,
                                config.cornerType
                            )
                        )
                    }
                    //自定义size
                    config.size?.let {
                        if (it.size > 1 && it[0] > 0 && it[1] > 0) {
                            override(it[0], it[1])
                        }
                    }
                    //暂时只写了渐入效果，如果要多种效果可以用枚举
                    if (config.useCrossFade) {
                        transition(BitmapTransitionOptions.withCrossFade())
                    } else {
                        dontAnimate()
                    }
                    //缩略图
                    thumbnail(config.thumbnail)
                }
            if (config.url?.isNotBlank() == true && config.url?.toLowerCase()?.startsWith("http") == true && config.progress != null) {
                //只有网络图有加载进度的回调
                config.url?.let {
                    GlideHttpClientManager.addListener(
                        url = it,
                        listener = object : OnImageProgressListener {
                            override fun onProgress(
                                url: String,
                                isComplete: Boolean,
                                percentage: Int,
                                bytesRead: Long,
                                totalBytes: Long
                            ) {
                                config.progress?.invoke(
                                    url,
                                    isComplete,
                                    percentage,
                                    bytesRead,
                                    totalBytes
                                )
                            }
                        })
                }
            }

            if (config.success != null || config.failed != null) {
                //需要监听成功或者失败的回调的
                glideRequest.into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        config.imageView?.setImageBitmap(resource)
                        config.success?.invoke(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        config.failed?.invoke()
                    }
                })
            } else {
                config.imageView?.let {
                    glideRequest.into(it)
                }
            }
        }
    }

}