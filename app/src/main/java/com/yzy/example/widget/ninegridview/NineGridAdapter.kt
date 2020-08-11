package com.yzy.example.widget.ninegridview

import android.content.Context
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils
import com.yzy.example.R
import com.yzy.example.extention.loadUrl
import com.yzy.example.repository.bean.PicBean

/**
 *description: 九宫格布局的Adapter.
 *@date 2018/10/24 14:20.
 *@author: yzy.
 */
class NineGridAdapter(private val imageSize: Int = SizeUtils.dp2px(100f)) :
    NineGridViewAdapter<PicBean>() {
    override fun generateImageView(context: Context): ImageView {
        return ImageView(context)
    }

    override fun onDisplayImage(context: Context, imageView: ImageView, t: PicBean) {
        t.let {bean->
            context.let { c ->
                imageView.loadUrl(bean.url, R.drawable.svg_placeholder_fail)
            }
        }
    }
}