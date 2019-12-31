package com.yzy.example.imModel.emoji

import android.view.View

interface IEmotionExtClickListener {

    /**
     * 添加表情的按键点击
     */
    fun onEmotionAddClick(view: View)

    /**
     * 设置表情按键点击
     */
    fun onEmotionSettingClick(view: View)
}