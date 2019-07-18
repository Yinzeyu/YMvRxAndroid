package com.yzy.sociallib.entity.content

/**
 * description: 支付宝支付信息实体， 从服务器获取的orderInfo
 *@date 2019/7/15
 *@author: yzy.
 */
data class AliPayContent(var orderInfo:String): PayContent()