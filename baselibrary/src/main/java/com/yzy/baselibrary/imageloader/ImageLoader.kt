package com.yzy.baselibrary.imageloader

import com.yzy.baselibrary.app.BaseApplication
import org.kodein.di.generic.instance

/**
 *description: ImageLoader.
 *@date 2019/7/15
 *@author: yzy.
 */
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
  val loader: ImageLoader by BaseApplication.INSTANCE.kodein.instance()
  val builder = ImageConfig.builder()
  builder.apply(config)
  loader.loadImage(builder.build())
}