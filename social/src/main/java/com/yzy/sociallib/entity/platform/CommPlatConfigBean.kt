package com.yzy.sociallib.entity.platform

import com.yzy.sociallib.config.PlatformType

/**
 * description: 通用平台的信息配置
 *@date 2019/7/15
 *@author: yzy.
 */
data class CommPlatConfigBean(
    override val name: PlatformType, // 平台类型
    override var appkey:String?         // 应用id
): PlatformConfig