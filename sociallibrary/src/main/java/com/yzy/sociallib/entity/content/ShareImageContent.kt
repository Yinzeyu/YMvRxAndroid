package com.yzy.sociallib.entity.content

import android.graphics.Bitmap

/**
 * description: 图片分享实体类
 *@date 2019/7/15
 *@author: yzy.
 */
data class ShareImageContent(override var img: Bitmap?) : ShareContent()