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
val imageLoaderModule = Kodein.Module("imageLoaderModule") {
    bind<BaseImageLoaderStrategy>() with singleton {
        GlideImageLoaderStrategy()
    }

    bind<ImageLoader>() with singleton { ImageLoader(instance()) }
}