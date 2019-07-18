package com.yzy.sociallib.entity.content

import android.graphics.Bitmap

/**
 * description: 网页分享
 *@date 2019/7/15
 *@author: yzy.
 */
data class ShareWebContent(
    var webPageUrl: String? = null,   //待分享的网页url
    var title: String? = null,  //网页标题
    override var description: String? = null, //网页描述
    override var img: Bitmap? = null          //网页缩略图
) : ShareContent()