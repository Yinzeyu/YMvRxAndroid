package com.yzy.example.widget.imagewatcher.loader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.widget.imagewatcher.loader.ImageLoader.SourceCallback
import com.yzy.example.widget.imagewatcher.loader.ImageLoader.ThumbnailCallback

class GlideTransfereeImage private constructor(private val context: Context) :
    ImageLoader {
    override fun showImage(
        imageUrl: String,
        imageView: ImageView,
        placeholder: Drawable,
        sourceCallback: SourceCallback?) {
        imageView.load(imageUrl, 0, success = {
            if (sourceCallback != null) {
                sourceCallback.onDelivered(1)
                sourceCallback.onProgress(100)
            }
        }, failed = {
            sourceCallback?.onDelivered(0)
        })
    }

    override fun loadImageAsync(
        imageUrl: String,
        imageView: ImageView,
        callback: ThumbnailCallback
    ) {
        imageView.load(imageUrl, 0, success = {
        }, failed = {
            callback.onFinish(imageView.resources.getDrawable(R.drawable.ic_bg))
        })

    }

    companion object {
        fun with(context: Context): GlideTransfereeImage {
            return GlideTransfereeImage(context)
        }
    }
}