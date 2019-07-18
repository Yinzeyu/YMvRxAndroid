package com.yzy.baselibrary.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

/**
 *description: ImageConfig.
 *@date 2019/7/15
 *@author: yzy.
 */
class ImageConfig constructor(builder: Builder? = null) {

  var context: Context? = null
  var url: String? = null //图片的url
  var uri: Uri? = null //图片的uri
  var resourceId: Int? = null //图片的资源ID
  var imageView: ImageView? = null //ImageView
  var placeholder: Int = 0  //占位图
  var errorSrc: Int = 0 //错误图
  var cacheStrategy: DiskCacheStrategyType = DiskCacheStrategyType.ALL //磁盘缓存方式默认为全部缓存
  var size: IntArray? = null //自定义大小
  var useCrossFade: Boolean = true//是否使用渐入效果
  var blur: Boolean = false//是否使用高斯模糊
  var blurRadius: Int = 0 //使用高斯模糊的模糊度 0-25
  var sampling: Int = 0 //使用高斯模糊的采样，即缩放
  var corner: Boolean = false//是否使用圆角
  var cornerRadius: Int = 0 //圆角的角度px
  var cornerType: CornerType = CornerType.ALL //圆角类型，默认是四个角都有
  var margin: Int = 0 // 圆角加载的边距
  var imageLoadScaleType: ImageLoadScaleType = ImageLoadScaleType.CenterCrop //图片显示的类型，默认为CenterCrop
  var thumbnail: Float = 1.0F // 缩略图大小
  var success: ((bitmap: Bitmap?) -> Unit)? = null //图片加载成功的回调
  var failed: (() -> Unit)? = null //图片加载失败的回调
  var progress: ((url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)? =
    null //图片加载进度的回调

  init {
    builder?.let { build ->
      context = build.context
      url = build.url
      uri = build.uri
      resourceId = build.resourceId
      imageView = build.imageView
      placeholder = build.placeholder
      errorSrc = build.errorSrc
      cacheStrategy = build.cacheStrategy
      size = build.size
      useCrossFade = build.useCrossFade
      blur = build.blur//是否使用高斯模糊
      blurRadius = build.blurRadius
      sampling = build.sampling
      corner = build.corner//是否使用圆角
      cornerRadius = build.cornerRadius
      cornerType = build.cornerType
      margin = build.margin
      imageLoadScaleType = build.imageLoadScaleType//ScaleType
      success = build.success
      failed = build.failed
      progress = build.progress
      thumbnail = build.thumbnail
    }
  }


  class Builder {
    var context: Context? = null
    var url: String? = null
    var uri: Uri? = null
    var resourceId: Int? = null
    var imageView: ImageView? = null
    var placeholder: Int = 0
    var errorSrc: Int = 0
    var cacheStrategy: DiskCacheStrategyType = DiskCacheStrategyType.ALL
    var size: IntArray? = null
    var useCrossFade: Boolean = true//使用渐入效果
    var blur: Boolean = false//是否使用高斯模糊
    var blurRadius: Int = 0
    var sampling: Int = 0
    var corner: Boolean = false//是否使用圆角
    var cornerRadius: Int = 0
    var cornerType: CornerType = CornerType.ALL
    var margin: Int = 0
    var imageLoadScaleType: ImageLoadScaleType = ImageLoadScaleType.CenterCrop//ScaleType
    var thumbnail: Float = 1.0F // 缩略图大小
    var success: ((bitmap: Bitmap?) -> Unit)? = null
    var failed: (() -> Unit)? = null
    var progress: ((url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)? =
      null //图片加载进度的回调

    /**
     * context
     */
    fun context(context: Context): Builder {
      this.context = context
      return this
    }

    /**
     * 图片地址
     */
    fun url(url: String): Builder {
      this.url = url
      return this
    }

    /**
     * 图片地址
     */
    fun uri(uri: Uri): Builder {
      this.uri = uri
      return this
    }

    /**
     * 图片资源ID
     */
    fun resourceId(resourceId: Int): Builder {
      this.resourceId = resourceId
      return this
    }

    /**
     * 缓存策略:
     * DiskCacheStrategyType.ALL
     * DiskCacheStrategyType.NONE
     * DiskCacheStrategyType.RESOURCE
     * DiskCacheStrategyType.DATA
     * DiskCacheStrategyType.AUTOMATIC
     */
    fun cacheStrategy(cacheStrategy: DiskCacheStrategyType): Builder {
      this.cacheStrategy = cacheStrategy
      return this
    }

    /**
     * 占位图
     */
    fun placeholder(placeholder: Int): Builder {
      this.placeholder = placeholder
      return this
    }

    /**
     * 错误占位图
     */
    fun errorSrc(errorSrc: Int): Builder {
      this.errorSrc = errorSrc
      return this
    }

    fun imageView(imageView: ImageView): Builder {
      this.imageView = imageView
      return this
    }

    /**
     * 是否使用渐入效果
     */
    fun useCrossFade(useCrossFade: Boolean): Builder {
      this.useCrossFade = useCrossFade
      return this
    }

    /**
     * 指定大小
     */
    fun override(width: Int, height: Int): Builder {
      this.size = intArrayOf(width, height)
      return this
    }

    /**
     * 高斯模糊
     * @param radius 模糊度1-25
     * @param sampling 缩放
     */
    fun blur(radius: Int, sampling: Int): Builder {
      if (radius != 0)
        this.blur = true
      this.blurRadius = radius
      this.sampling = sampling
      return this
    }

    /**
     * 圆角
     * @param radius 圆角px
     * @param cornerType 圆角类型默认四个角都有
     * @param margin
     */
    fun corner(radius: Int, cornerType: CornerType = CornerType.ALL, margin: Int = 0): Builder {
      if (radius != 0)
        corner = true
      this.cornerRadius = radius
      this.cornerType = cornerType
      this.margin = margin
      return this
    }

    //默认
    fun centerCrop(): Builder {
      this.imageLoadScaleType = ImageLoadScaleType.CenterCrop
      return this
    }

    fun centerInside(): Builder {
      this.imageLoadScaleType = ImageLoadScaleType.CenterInside
      return this
    }

    fun fitCenter(): Builder {
      this.imageLoadScaleType = ImageLoadScaleType.FitCenter
      return this
    }

    fun circleCrop(): Builder {
      this.imageLoadScaleType = ImageLoadScaleType.CircleCrop
      return this
    }

    /**
     * 不需要ScaleType
     */
    fun noScaleType(): Builder {
      this.imageLoadScaleType = ImageLoadScaleType.NoScaleType
      return this
    }

    /**
     * 加载成功的回调
     */
    fun onSuccess(success: (bitmap: Bitmap?) -> Unit): Builder {
      this.success = success
      return this
    }

    /**
     * 加载失败的回调
     */
    fun onFailed(failed: () -> Unit): Builder {
      this.failed = failed
      return this
    }

    /**
     * 加载进度的回调
     */
    fun onProgress(progress: (url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit): Builder {
      this.progress = progress
      return this
    }

    /**
     * 缩略图
     */
    fun thumbnail(thumbnail: Float): Builder {
      this.thumbnail = thumbnail
      return this
    }


    fun build(): ImageConfig {
      return ImageConfig(this)
    }
  }

  companion object {
    fun builder(): Builder {
      return Builder()
    }
  }

}