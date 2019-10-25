package com.yzy.sociallib.entity.content

/**
 * description: 文字分享
 *@date 2019/7/15
 *@author: yzy.
 */
data class ShareTextContent(
    override var description: String? = null,   //描述
    override var url: String? = null,   // 连接
    var atUser: String? = null
) : ShareContent()