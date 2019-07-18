package com.yzy.sociallib.entity.content

import android.content.Intent

/**
 * description: activity 回调操作的实体
 *@date 2019/7/15
 *@author: yzy.
 */
data class ActivityResultContent(
    var request: Int,
    var result: Int,
    var data: Intent?
) : OperationContent