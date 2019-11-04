package com.yzy.sociallib.entity.content

import android.graphics.Bitmap

/**
 * description: 视频分享
 *@date 2019/7/15
 *@author: yzy.
 */
data class ShareVideoContent(
        override var img: Bitmap? = null,   //缩略图
        override var url: String? = null,        //网页
        override var description: String? = null,    //描述
        var videoUrl: String? = null,       //视频url
        var title: String? = null     //标题
) : ShareContent()