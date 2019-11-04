package com.yzy.sociallib.entity.content

import android.graphics.Bitmap

/**
 * description: 文字图片分享
 *@date 2019/7/15
 *@author: yzy.
 */
data class ShareTextImageContent(
        override var url: String? = null,       //网页url
        override var description: String? = null,    //描述
        override var img: Bitmap? = null,    //缩略图
        var atUser: String? = null,
        var title: String? = null         //标题
) : ShareContent()