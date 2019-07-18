package com.yzy.sociallib.entity.platform

import com.yzy.sociallib.config.PlatformType

/**
 * description:
 *@date 2019/7/15
 *@author: yzy.
 */
class SinaPlatConfigBean (
    override val name: PlatformType, // 平台类型
    override var appkey:String?,          // 应用id
    var redirectUrl: String,   // 微博回调url
    var scope: String   // 微博域
): PlatformConfig