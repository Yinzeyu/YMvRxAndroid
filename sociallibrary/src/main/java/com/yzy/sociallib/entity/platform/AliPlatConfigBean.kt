package com.yzy.sociallib.entity.platform

import com.yzy.sociallib.config.PlatformType

/**
 * description:支付宝的配置实体类
 *@date 2019/7/15
 *@author: yzy.
 */
data class AliPlatConfigBean (
    override val name: PlatformType, // 平台类型
    override var appkey:String?,          // 应用id
    var authToken: String   // 授权token
): PlatformConfig