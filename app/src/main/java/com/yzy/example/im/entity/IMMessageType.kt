package com.yzy.example.im.entity

/**
 *description: IM的消息类型.
 *@date 2019/3/11 19:10.
 *@author: yzy.
 */
enum class IMMessageType(val value: Int) {
    /**
     * 消息类型：图片
     */
    Image(1),
    /**
     * 消息类型：声音
     */
    Voice(2),
    /**
     * 消息类型：视频
     */
    Video(3),
    /**
     * 消息类型：其他，包含文字，位置等
     */
    Other(0)
}