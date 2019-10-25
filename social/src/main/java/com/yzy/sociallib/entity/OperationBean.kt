package com.yzy.sociallib.entity

import android.content.Context
import com.yzy.sociallib.callback.OperationCallback
import com.yzy.sociallib.config.OperationType
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.entity.content.OperationContent

/**
 * description: 第三方平台操作的实体
 *@date 2019/7/15
 *@author: yzy.
 */
data class OperationBean(
    var operationContext: Context,        // 操作上下文
    var operationPlat: PlatformType,      // 平台类型
    var operationType: OperationType,     // 操作类型
    var operationCallback: OperationCallback,   // 回调
    var operationContent: OperationContent? = null  // 平台内容
)