package com.yzy.sociallib.callback

import com.yzy.sociallib.config.PlatformType


/**
 * description: 授权的callback
 *@date 2019/7/15
 *@author: yzy.
 */
data class AuthCallback(
    var onSuccess:((type: PlatformType, data:Map<String, String?>?) -> Unit)? = null,
    override var onErrors: ((type: PlatformType, errorCode: Int, errorMsg:String?) -> Unit)? = null,
    var onCancel:((type: PlatformType) -> Unit)? = null
): OperationCallback