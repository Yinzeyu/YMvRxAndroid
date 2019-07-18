package com.yzy.sociallib.entity.platform

import com.yzy.sociallib.config.PlatformType

/**
 * description:
 *@date 2019/7/15
 *@author: yzy.
 */
interface PlatformConfig {
    val name: PlatformType     // 平台类型
    var appkey:String?          // 应用id
}