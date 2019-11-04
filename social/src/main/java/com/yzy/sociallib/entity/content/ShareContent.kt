package com.yzy.sociallib.entity.content

import android.graphics.Bitmap

/**
 * description: 分享的基类
 *@date 2019/7/15
 *@author: yzy.
 */
open class ShareContent : OperationContent {
    open var img: Bitmap? = null   // 图片或者缩略图
    open var url: String? = null   // 网页的地址
    open var description: String? = null  // 描述
}