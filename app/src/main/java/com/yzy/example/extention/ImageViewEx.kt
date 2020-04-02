package com.yzy.example.extention

import android.graphics.Bitmap
import android.widget.ImageView
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.example.R
import com.yzy.example.imageloader.ImageConfig
import com.yzy.example.imageloader.GlideImageLoaderStrategy
import com.yzy.example.imageloader.ImageLoadScaleType

/**
 *description: ImageView的扩展.
 *@date 2019/7/15
 *@author: yzy.
 *  加载url图片,默认CenterCrop和CrossFade效果
 */
fun ImageView.load(url: String?, placeholderId: Int = 0) {
    url?.let {
        val config = ImageConfig(
            useCrossFade = false,
            url = it,
            errorSrc = placeholderId,
            placeholder = placeholderId
        )
        load(config)
    }
}

fun ImageView.load(url: String?, placeholderId: Int = 0, errorRes: Int = 0) {
    url?.let {
        val config = ImageConfig(
            useCrossFade = false,
            url = it,
            errorSrc = placeholderId,
            placeholder = errorRes
        )
        load(config)
    }
}

fun ImageView.load(
    url: String?,
    placeholderId: Int = 0,
    success: (bitmap: Bitmap?) -> Unit,
    failed: () -> Unit
) {
    url?.let {
        val config = ImageConfig(
            useCrossFade = false,
            url = it,
            errorSrc = placeholderId,
            placeholder = placeholderId,
            success = success,
            failed = failed
        )
        load(config)
    }
}

fun ImageView.load(
    url: String?,
    placeholderId: Int = 0,
    success: (bitmap: Bitmap?) -> Unit,
    failed: () -> Unit,
    progress: ((url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)? = null
) {
    url?.let {
        val config = ImageConfig(
            useCrossFade = false,
            url = it,
            errorSrc = placeholderId,
            placeholder = placeholderId,
            success = success,
            failed = failed,
            progress = progress
        )
        load(config)
    }
}

fun ImageView.load(url: String?, placeholderId: Int = 0, config: ImageConfig) {
    url?.let {
        config.context = BaseApplication.getApp()
        config.imageView = this
        config.url = url
        config.errorSrc = placeholderId
        config.placeholder = placeholderId
        config.useCrossFade = false
        loader.loadImage(config)
    }
}

/**
 * 根据配置加载图片
 */
fun ImageView.load(config: ImageConfig) {
    config.url?.let {
        config.context = BaseApplication.getApp()
        config.imageView = this
        loader.loadImage(config)
    }
}

val loader: GlideImageLoaderStrategy by lazy {
    GlideImageLoaderStrategy()
}

/**
 * 加载圆形
 */
fun ImageView.loadCircle(
    url: String?
) {
    url?.let {
        val config = ImageConfig(
            useCrossFade = true,
            url = it,
            imageLoadScaleType = ImageLoadScaleType.CircleCrop
        )
        load(config)
    }
}
///**
// * 加载uri图片,默认CenterCrop和CrossFade效果
// */
//fun ImageView.load(uri: Uri?, placeholderId: Int) {
//    uri?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(false)
//            .uri(it)
//            .errorSrc(placeholderId)//设置默认的占位图
//            .placeholder(placeholderId)//设置默认的加载错误图
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载本地资源,默认CenterCrop和CrossFade效果
// */
//fun ImageView.load(@DrawableRes resourceId: Int?) {
//    resourceId?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(false)
//            .resourceId(it)
//            .build()
//        load(config)
//    }
//}
//
//
///**
// * 加载文件图片,默认CenterCrop和CrossFade效果
// */
//fun ImageView.load(file: File?) {
//    GlideApp.with(BaseApplication.getApp())
//        .asBitmap()
//        .load(file)
//        .centerCrop()
//        .transition(BitmapTransitionOptions.withCrossFade())
//        .into(this)
//}
//
//
///**
// * 加载图片有占位图和加载错误图,默认CenterCrop和CrossFade效果
// */
//fun ImageView.load(url: String?, loadingResId: Int, errorResId: Int) {
//    url?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .imageView(this)
//            .url(it)
//            .placeholder(loadingResId)
//            .errorSrc(errorResId)
//            .build()
//        load(config)
//    }
//}


///**
// * 加载高斯模糊图
// * @param radius 模糊度1-25
// * @param sampling 缩放，越大缩放比例越大
// */
//fun ImageView.loadBlur(url: String?, radius: Int = 12, sampling: Int = 7) {
//    url?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .blur(radius, sampling)
//            .url(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载高斯模糊图
// * @param radius 模糊度1-25
// * @param sampling 缩放，越大缩放比例越大
// */
//fun ImageView.loadBlur(uri: Uri?, radius: Int = 12, sampling: Int = 7) {
//    uri?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .blur(radius, sampling)
//            .uri(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载高斯模糊图
// * @param radius 模糊度1-25
// * @param sampling 缩放，越大缩放比例越大
// */
//fun ImageView.loadBlur(@DrawableRes resourceId: Int?, radius: Int = 12, sampling: Int = 7) {
//    resourceId?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .blur(radius, sampling)
//            .resourceId(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载圆角图
// * @param radius 圆角dp
// * @param cornerType 圆角类型默认四个角都有
// * @param margin
// */
//fun ImageView.loadCorner(
//    url: String?,
//    radius: Int,
//    cornerType: CornerType = CornerType.ALL,
//    margin: Int = 0
//) {
//    url?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .corner(context.dp2px(radius), cornerType, margin)
//            .url(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载圆角图
// * @param radius 圆角dp
// * @param cornerType 圆角类型默认四个角都有
// * @param margin
// */
//fun ImageView.loadCorner(
//    uri: Uri?,
//    radius: Int,
//    cornerType: CornerType = CornerType.ALL,
//    margin: Int = 0
//) {
//    uri?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .corner(context.dp2px(radius), cornerType, margin)
//            .uri(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载圆角图
// * @param radius 圆角dp
// * @param cornerType 圆角类型默认四个角都有
// * @param margin
// */
//fun ImageView.loadCorner(
//    @DrawableRes resourceId: Int?,
//    radius: Int,
//    cornerType: CornerType = CornerType.ALL,
//    margin: Int = 0
//) {
//    resourceId?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .corner(context.dp2px(radius), cornerType, margin)
//            .resourceId(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载圆形
// */
//fun ImageView.loadCircle(
//    url: String?
//) {
//    url?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .circleCrop()
//            .url(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载圆形
// */
//fun ImageView.loadCircle(
//    uri: Uri?
//) {
//    uri?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .circleCrop()
//            .uri(it)
//            .build()
//        load(config)
//    }
//}
//
///**
// * 加载圆形
// */
//fun ImageView.loadCircle(
//    @DrawableRes resourceId: Int?
//) {
//    resourceId?.let {
//        val config: ImageConfig = ImageConfig.builder()
//            .useCrossFade(true)
//            .circleCrop()
//            .resourceId(it)
//            .build()
//        load(config)
//    }
//}

//默认的图片加载SDL,可以配置默认的占位图等
//fun imageLoadDefult(config: ImageConfig.Builder.() -> Unit) {
//    val builder = ImageConfig.builder()
//    builder.apply(config)
//    builder.useCrossFade(false)
////    builder.errorSrc()//错误占位图
////    builder.placeholder()//加载中占位图
//    val imageConfig = builder.build()
//    imageConfig.imageView?.load(imageConfig)
//}
//
//fun ImageView.leftMargin(dp: Int) {
//    val params = layoutParams
//    if (params is ViewGroup.MarginLayoutParams) {
//        params.marginStart = context.dp2px(dp)
//    }
//    layoutParams = params
//}
//
//fun ImageView.bottomMargin(px: Int) {
//    val params = layoutParams
//    if (params is ViewGroup.MarginLayoutParams) {
//        params.setMargins(
//            params.leftMargin,
//            params.topMargin,
//            params.rightMargin,
//            px
//        )
//    }
//    layoutParams = params
//}
//
//fun ImageView.topMargin(px: Int) {
//    val params = layoutParams
//    if (params is ViewGroup.MarginLayoutParams) {
//        params.setMargins(
//            params.leftMargin,
//            px,
//            params.rightMargin,
//            params.bottomMargin
//        )
//    }
//    layoutParams = params
//}
//加载正方形图片
