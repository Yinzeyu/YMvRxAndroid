package com.yzy.example.widget.imagewatcher.listeners

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity

/**
 * author : maning
 * time   : 2018/04/10
 * desc   : 点击监听
 * version: 1.0
 */
interface OnClickListener {
    fun onClick(activity: FragmentActivity, view: ImageView, position: Int, url: String
    )
}