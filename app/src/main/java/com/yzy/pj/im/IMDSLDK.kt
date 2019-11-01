package com.yzy.pj.im

import com.yzy.pj.im.entity.SendMessageBean
import com.yzy.pj.im.entity.SendMessageConfig

/**
 *description: IM相关的DSL.
 *@date 2019/3/12 11:09.
 *@author: yzy.
 */

/**
 * IM初始化的DSL
 */
fun IMInit(config: IMInitConfig.() -> Unit) {
    val configBean = IMInitConfig()
    configBean.apply(config)
    IM.init(configBean)
}

/**
 * 发送im消息的DSL
 */
fun IMSend(config: SendMessageConfig.() -> Unit) {
    val configBean = SendMessageConfig()
    configBean.apply(config)
    val bean = SendMessageBean(
        configBean.conversationType,
        configBean.targetId,
        configBean.content,
        configBean.pushContent,
        configBean.pushData,
        configBean.messageType,
        configBean.success,
        configBean.error,
        configBean.attached,
        configBean.canceled,
        configBean.progress
    )
    IM.sendMessage(bean)
}