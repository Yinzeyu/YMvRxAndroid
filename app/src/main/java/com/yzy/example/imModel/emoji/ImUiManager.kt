package com.yzy.example.imModel.emoji

import android.content.Context

object ImUiManager {
    /**
     * 默认的消息显示时间提示的间隔时间：5分钟
     */
    private const val DEFAULT_MESSAGE_TIMETIPSSHOWINTERVAL = 5 * 60 * 1000L

    private var imageLoader: IImageLoader? = null
    private var messageTimeTipsShowInterval: Long = DEFAULT_MESSAGE_TIMETIPSSHOWINTERVAL

    fun init(
        context: Context,
        imageLoader: IImageLoader,
        stickerPath: String = ""
    ) {
        this.imageLoader = imageLoader
        EmotionManager.getInstance().init(context, stickerPath)
    }

    fun getImageLoader(): IImageLoader? {
        return imageLoader
    }

    fun getMessageTimeTipsShowInterval(): Long {
        return messageTimeTipsShowInterval
    }


    class ImUiConfig {
        var context: Context? = null
        /**
         * 图片加载
         */
        var imageLoader: IImageLoader? = null
        /**
         * 自定义表情的路径
         */
        var stickerPath: String = ""
        /**
         * 显示消息时间提示的间隔时间，默认5分钟
         */
        var messageTimeTipsShowInterval: Long = DEFAULT_MESSAGE_TIMETIPSSHOWINTERVAL
    }
}