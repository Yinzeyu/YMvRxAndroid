package com.yzy.baselibrary.imageloader.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import java.security.MessageDigest

/**
 *description: Glide图片Bitmap转换的基类.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BitmapTransformation : Transformation<Bitmap> {

  abstract fun key(): String

  override fun transform(context: Context, resource: Resource<Bitmap>, outWidth: Int,
                         outHeight: Int): Resource<Bitmap> {
    if (!Util.isValidDimensions(outWidth, outHeight)) {
      throw IllegalArgumentException(
          "Cannot apply transformation on width: " + outWidth + " or height: " + outHeight
              + " less than or equal to zero and not Target.SIZE_ORIGINAL")
    }
    val bitmapPool = Glide.get(context).bitmapPool
    val toTransform = resource.get()
    val targetWidth = if (outWidth == Target.SIZE_ORIGINAL) toTransform.width else outWidth
    val targetHeight = if (outHeight == Target.SIZE_ORIGINAL) toTransform.height else outHeight
    val transformed = transform(context.applicationContext, bitmapPool, toTransform,
        targetWidth, targetHeight)

    val result: Resource<Bitmap>?
    result = if (toTransform == transformed) {
      resource
    } else {
      BitmapResource.obtain(transformed, bitmapPool)
    }
    return result!!
  }

  protected abstract fun transform(context: Context, pool: BitmapPool,
                                   toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap

  override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    messageDigest.update(key().toByteArray())
  }
}