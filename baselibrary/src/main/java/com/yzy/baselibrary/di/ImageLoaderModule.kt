package com.yzy.baselibrary.di

import com.yzy.baselibrary.imageloader.BaseImageLoaderStrategy
import com.yzy.baselibrary.imageloader.ImageLoader
import com.yzy.baselibrary.imageloader.glide.GlideImageLoaderStrategy
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 *description: ImageLoaderModule.
 *@date 2019/7/15
 *@author: yzy.
 */
const val KODEIN_MODULE_IMAGELOADER_TAG = "imageLoaderModule"

val imageLoaderModule = Kodein.Module(KODEIN_MODULE_IMAGELOADER_TAG) {
    bind<BaseImageLoaderStrategy>() with singleton {
        GlideImageLoaderStrategy()
    }

    bind<ImageLoader>() with singleton { ImageLoader(instance()) }
}