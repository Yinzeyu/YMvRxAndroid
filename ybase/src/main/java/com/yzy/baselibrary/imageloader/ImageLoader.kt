package com.yzy.baselibrary.imageloader

import com.yzy.baselibrary.app.BaseApplication
import org.kodein.di.generic.instance

/**
 *description: ScaleType.
 *@date 2019/7/15
 *@author: yzy.
 */
enum class ImageLoadScaleType {
    CenterCrop,
    FitCenter,
    CenterInside,
    CircleCrop,
    NoScaleType
}

/**
 *description: 圆角类型.
 *@date 2019/7/15
 *@author: yzy.
 */

enum class CornerType {
    ALL,
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
    TOP, BOTTOM, LEFT, RIGHT,
    OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
    DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
}

/**
 *description: 磁盘缓存类型.
 *@date 2019/7/15
 *@author: yzy.
 */

enum class DiskCacheStrategyType {
    ALL,
    NONE,
    RESOURCE,
    DATA,
    AUTOMATIC
}

class ImageLoader constructor(private var strategy: BaseImageLoaderStrategy) {

    fun loadImage(config: ImageConfig) {
        strategy.loadImage(config)
    }

    fun setLoadImgStrategy(strategy: BaseImageLoaderStrategy) {
        this.strategy = strategy
    }

}

//图片加载的dsl
fun imageLoad(config: ImageConfig.Builder.() -> Unit) {
    val loader: ImageLoader by BaseApplication.getApp().kodein.instance()
    val builder = ImageConfig.builder()
    builder.apply(config)
    loader.loadImage(builder.build())
}