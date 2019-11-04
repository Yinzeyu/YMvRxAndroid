package com.yzy.pj.im.entity

import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.MessageContent

/**
 *description: 消息发送DSL的Bean.
 *@date 2019/3/12 16:29.
 *@author: yzy.
 */
data class SendMessageConfig(
        var conversationType: Conversation.ConversationType? = null,
        var targetId: String? = null,
        var content: MessageContent? = null,
        var pushContent: String? = null,
        var pushData: String? = null,
        var messageType: IMMessageType = IMMessageType.Other,
        var success: ((message: Message?, type: IMMessageType) -> Unit)? = null,
        var error: ((message: Message?, type: IMMessageType, errorCode: Int?) -> Unit)? = null,
        var attached: ((message: Message?, type: IMMessageType) -> Unit)? = null,
        var canceled: ((message: Message?, type: IMMessageType) -> Unit)? = null,
        var progress: ((message: Message?, type: IMMessageType, progress: Int) -> Unit)? = null
)