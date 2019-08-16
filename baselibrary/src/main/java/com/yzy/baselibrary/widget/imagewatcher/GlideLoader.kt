package com.yzy.baselibrary.widget.imagewatcher

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import java.io.File

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/8 10:54
 */
internal class GlideLoader : ImageWatcher.Loader {

    @SuppressLint("DefaultLocale")
    override fun load(context: Context, uri: Uri, lc: ImageWatcher.LoadCallback?) {
        val url = uri.toString()
        if (TextUtils.isEmpty(url) || !url.toLowerCase().startsWith("http")) {
            val imgFile = File(url)
            if (lc != null && imgFile.exists()) {
                lc.onResourceReady(imgFile)
            }
            return
        }
        Glide.with(context)
            .asFile()
            .load(url)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(
                    resource: File,
                    transition: Transition<in File>?
                ) {
                    lc?.onResourceReady(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    lc?.onResourceReady(null)
                }
            })
    }
}
