package com.yzy.example.widget.ninegridview

import android.content.Context
import android.widget.ImageView

/**
 * description: NineGridViewAdapter.
 *
 * @date 2018/10/22 11:06.
 * @author: yzy.
 */
abstract class NineGridViewAdapter<T> {
    abstract fun onDisplayImage(context: Context, imageView: ImageView, t: T)

    fun onItemImageClick(context: Context, imageView: ImageView, index: Int, list: List<T>) {}

    fun onItemImageLongClick(context: Context, imageView: ImageView, index: Int, list: List<T>): Boolean { return false
    }

    abstract   fun generateImageView(context: Context): ImageView
}
