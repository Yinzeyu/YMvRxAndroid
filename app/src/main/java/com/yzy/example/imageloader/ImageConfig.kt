//package com.yzy.example.imageloader
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.net.Uri
//import android.widget.ImageView
//import java.io.File
//
///**
// *description: ImageConfig.
// *@date 2019/7/15
// *@author: yzy.
// */
//class ImageConfig(
//    var context: Context? = null,
//    var url: String? = null,//图片的url
//    var uri: Uri? = null,//图片的uri
//    var resourceId: Int? = null, //图片的资源ID
//    var imageView: ImageView? = null, //ImageView
//    var placeholder: Int = 0, //占位图
//    var errorSrc: Int = 0, //错误图
//    var cacheStrategy: DiskCacheStrategyType = DiskCacheStrategyType.ALL, //磁盘缓存方式默认为全部缓存
//    var size: IntArray? = null, //自定义大小
//    var useCrossFade: Boolean = true,//是否使用渐入效果
//    var blur: Boolean = false,//是否使用高斯模糊
//    var blurRadius: Int = 0, //使用高斯模糊的模糊度 0-25
//    var sampling: Int = 0, //使用高斯模糊的采样，即缩放
//    var corner: Boolean = false,//是否使用圆角
//    var cornerRadius: Int = 0, //圆角的角度px
//    var cornerType: CornerType = CornerType.ALL, //圆角类型，默认是四个角都有
//    var margin: Int = 0, // 圆角加载的边距
//    var imageLoadScaleType: ImageLoadScaleType = ImageLoadScaleType.CenterCrop, //图片显示的类型，默认为CenterCrop
//    var thumbnail: Float = 1.0F, // 缩略图大小
//    var success: ((bitmap: Bitmap?) -> Unit)? = null, //图片加载成功的回调
//    var failed: (() -> Unit)? = null, //图片加载失败的回调
//    var progress: ((url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)? = null //图片加载进度的回调) constructor(builder: Builder? = null
//)
//
///**
// *description: ScaleType.
// *@date 2019/7/15
// *@author: yzy.
// */
//enum class ImageLoadScaleType {
//    CenterCrop,
//    FitCenter,
//    CenterInside,
//    CircleCrop,
//    NoScaleType
//}
//
///**
// *description: 圆角类型.
// *@date 2019/7/15
// *@author: yzy.
// */
//
//enum class CornerType {
//    ALL,
//    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
//    TOP, BOTTOM, LEFT, RIGHT,
//    OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
//    DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
//}
//
///**
// *description: 磁盘缓存类型.
// *@date 2019/7/15
// *@author: yzy.
// */
//
//enum class DiskCacheStrategyType {
//    ALL,
//    NONE,
//    RESOURCE,
//    DATA,
//    AUTOMATIC
//}
//
//interface BaseImageLoaderStrategy {
//    /**
//     * 加载图片
//     */
//    fun loadImage(config: ImageConfig)
////    /**
////     * 获取缓存文件
////     */
////    fun getCacheFile(context: Context, url: String): File?
//
//    /**
//     * 清除内存缓存
//     */
//    fun clearMemory(context: Context)
//
//    /**
//     * 清除磁盘缓存
//     */
//    fun cleanDiskCache(context: Context)
//}