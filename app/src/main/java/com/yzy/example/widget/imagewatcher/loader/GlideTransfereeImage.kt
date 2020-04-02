package com.yzy.example.widget.imagewatcher.loader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.yzy.example.widget.imagewatcher.loader.ImageLoader.SourceCallback

class GlideTransfereeImage private constructor(private val context: Context) :
    ImageLoader {
    override fun showImage(
        imageUrl: String,
        imageView: ImageView,
        placeholder: Drawable,
        sourceCallback: SourceCallback?) {
//        imageView.load(imageUrl, 0, success = {
//            if (sourceCallback != null) {
//                sourceCallback.onDelivered(1)
//                sourceCallback.onProgress(100)
//            }
//        }, failed = {
//            sourceCallback?.onDelivered(0)
//        })
    }
    companion object {
        fun with(context: Context): GlideTransfereeImage {
            return GlideTransfereeImage(context)
        }
    }
}