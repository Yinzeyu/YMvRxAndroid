package com.yzy.pj.im

import android.content.Context
import com.yzy.pj.im.entity.IMActionEntity
import com.yzy.pj.im.entity.IMMessageType
import com.yzy.pj.im.entity.SendMessageBean
import com.yzy.pj.im.exception.IMException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.MessageContent

/**
 *description: im对外暴露的类.
 *@date 2019/3/13 16:38.
 *@author: yzy.
 */
object IM {

    /**
     * 操作失败的异常
     */
    private var operateFailedException = IMException(IMException.ERROR_OPERATE_FAILED)

    /**
     * 初始化IM
     */
    fun init(config: IMInitConfig) {
        config.context?.let { context ->
            config.key?.let { key ->
                IMCore.initIM(context, key)
                IMCore.fileUploadProvider = config.fileUploadProvider
                IMCore.tokenProvider = config.tokenProvider
            }
        }
    }

    /**
     * 注册融云要接收的自定义消息的类型
     */
    fun registerMessageType(vararg clazz: Class<out MessageContent>) {
        IMCore.registerMessageType(clazz.toList())
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        return IMCore.isLogin()
    }

    /**
     * 登录
     */
    fun login() {
        IMCore.login()
    }

    /**
     * 退出登录
     */
    fun logout() {
        IMCore.logout()
    }

    /**
     * 添加IMAction的监听
     * @param action 要监听的action [IMConstant] 中定义的action
     * @param onAction 指定action触发的回调
     */
    fun addActionListener(action: String, onAction: (entity: IMActionEntity) -> Unit) {
        IMActionDispatcher.addActionCallBack(action, onAction)
    }

    /**
     * 移除IMAction的监听
     * @param action 要监听的action [IMConstant] 中定义的action
     * @param onAction 指定action触发的回调
     */
    fun removeActionListener(action: String, onAction: (entity: IMActionEntity) -> Unit) {
        IMActionDispatcher.removeActionCallBack(action, onAction)
    }

    /**
     * 添加消息接收的监听
     * @param objectName 要监听的消息类型，为null时表示监听所有类型的消息，默认为null
     * @param callback 指定类型消息接收到的回调
     */
    fun addMessageReceivedListener(objectName: String? = null, callback: (message: Message) -> Unit) {
        IMMessageDispatcher.addMessageReceiveCallBack(callback, objectName)
    }

    /**
     * 移除消息接收的监听
     * @param objectName 要监听的消息类型，为null时表示监听所有类型的消息，默认为null
     * @param callback 指定类型消息接收到的回调
     */
    fun removeMessageReceivedListener(
            objectName: String? = null,
            callback: (message: Message) -> Unit
    ) {
        IMMessageDispatcher.removeMessageReceiveCallBack(callback, objectName)
    }

    /**
     * 发送消息
     * @param conversationType 聊天类型，私聊群聊等
     * @param targetId 聊天的target 私聊为对方ID，群聊为群ID
     * @param content 消息内容
     * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
     *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
     *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，传 null 即可，默认已经指定。
     * @param pushData  push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过PushNotificationMessage.getPushData()方法获取。
     * @param messageType  消息类型
     * @param success 消息发送成功的回调
     * @param error 消息发送失败的回调
     * @param attached 消息插入本地数据库的回调
     * @param canceled 取消消息发送的回调
     * @param progress 消息的发送进度的回调
     */
    fun sendMessage(
            conversationType: Conversation.ConversationType,
            targetId: String,
            content: MessageContent,
            pushContent: String? = null,
            pushData: String? = null,
            messageType: IMMessageType? = IMMessageType.Other,
            success: ((message: Message?, type: IMMessageType) -> Unit)? = null,
            error: ((message: Message?, type: IMMessageType, errorCode: Int?) -> Unit)? = null,
            attached: ((message: Message?, type: IMMessageType) -> Unit)? = null,
            canceled: ((message: Message?, type: IMMessageType) -> Unit)? = null,
            progress: ((message: Message?, type: IMMessageType, progress: Int) -> Unit)? = null
    ) {
        IMCore.sendMessage(
                conversationType,
                targetId,
                content,
                pushContent,
                pushData,
                messageType,
                success,
                error,
                attached,
                canceled,
                progress
        )
    }

    /**
     * 根据SendMessageBean发送消息
     */
    fun sendMessage(bean: SendMessageBean) {
        IMCore.sendMessage(bean)
    }

    /**
     * 插入发出去的消息
     * @param conversationType 聊天类型，私聊群聊等
     * @param targetId 聊天的target 私聊为对方ID，群聊为群ID
     * @param content 消息内容
     * @param sentStatus 插入的发出的消息状态，默认是已读
     * @param success 消息插入成功的回调
     * @param error 消息插入失败的回调
     */
    fun insertSendMessage(
            conversationType: Conversation.ConversationType,
            targetId: String,
            content: MessageContent,
            sentStatus: Message.SentStatus? = Message.SentStatus.READ,
            success: ((message: Message?) -> Unit)? = null,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.insertSendMessage(conversationType, targetId, content, sentStatus, success, error)
    }

    /**
     * 插入发出去的消息
     * @param conversationType 聊天类型，私聊群聊等
     * @param targetId 聊天的target 私聊为对方ID，群聊为群ID
     * @param content 消息内容
     * @param sentStatus 插入的发出的消息状态，默认是已读
     */
    fun insertSendMessage(
            conversationType: Conversation.ConversationType,
            targetId: String,
            content: MessageContent,
            sentStatus: Message.SentStatus? = Message.SentStatus.READ
    ): Observable<Message> {
        return Observable.create<Message> { emitter ->
            IMCore.insertSendMessage(conversationType, targetId, content, sentStatus, success = {
                if (it != null) {
                    emitter.onNext(it)
                } else {
                    emitter.onError(operateFailedException)
                }
                emitter.onComplete()
            }, error = {
                emitter.onError(createIMException(it))
                emitter.onComplete()
            })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 插入一条接收到的消息
     * @param conversationType 聊天类型，私聊群聊等
     * @param targetId 聊天的target 私聊为对方ID，群聊为群ID
     * @param senderUserId 消息发出的用户ID
     * @param sentTime 消息发出的事件
     * @param content 消息内容
     * @param receivedStatus 插入的接收到的消息状态，默认是已读
     * @param success 消息插入成功的回调
     * @param error 消息插入失败的回调
     */
    fun insertReceiveMessage(
            conversationType: Conversation.ConversationType,
            targetId: String,
            senderUserId: String,
            sentTime: Long,
            content: MessageContent,
            receivedStatus: Message.ReceivedStatus? = Message.ReceivedStatus(1),
            success: ((message: Message?) -> Unit)? = null,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.insertReceiveMessage(
                conversationType,
                targetId,
                senderUserId,
                sentTime,
                content,
                receivedStatus,
                success,
                error
        )
    }

    /**
     * 插入一条接收到的消息
     * @param conversationType 聊天类型，私聊群聊等
     * @param targetId 聊天的target 私聊为对方ID，群聊为群ID
     * @param senderUserId 消息发出的用户ID
     * @param sentTime 消息发出的事件
     * @param content 消息内容
     * @param receivedStatus 插入的接收到的消息状态，默认是已读
     */
    fun insertReceiveMessage(
            conversationType: Conversation.ConversationType,
            targetId: String,
            senderUserId: String,
            sentTime: Long,
            content: MessageContent,
            receivedStatus: Message.ReceivedStatus? = Message.ReceivedStatus(1)
    ): Observable<Message> {
        return Observable.create<Message> { emitter ->
            IMCore.insertReceiveMessage(
                    conversationType,
                    targetId,
                    senderUserId,
                    sentTime,
                    content,
                    receivedStatus,
                    success = {
                        if (it != null) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 删除指定消息
     */
    fun deleteMessages(
            vararg message: Message,
            success: ((isSuccess: Boolean) -> Unit)? = null,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.deleteMessages(message.toList(), success, error)
    }

    /**
     * 删除指定消息
     */
    fun deleteMessages(vararg message: Message): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.deleteMessages(
                    message.toList(),
                    success = {
                        if (it) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 删除指定ID的消息
     */
    fun deleteMessagesByIds(
            vararg id: Int,
            success: ((isSuccess: Boolean) -> Unit)? = null,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.deleteMessagesByIds(id.toList(), success, error)
    }

    /**
     * 删除指定ID的消息
     */
    fun deleteMessagesByIds(vararg id: Int): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.deleteMessagesByIds(
                    id.toList(),
                    success = {
                        if (it) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取所有会话的未读消息总数量
     */
    fun getTotalUnreadCount(
            success: (count: Int) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getTotalUnreadCount(success, error)
    }

    /**
     * 获取所有会话的未读消息总数量
     */
    fun getTotalUnreadCount(): Observable<Int> {
        return Observable.create<Int> { emitter ->
            IMCore.getTotalUnreadCount(
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取指定会话类型的未读消息总数量
     */
    fun getUnreadCount(
            vararg conversationTypes: Conversation.ConversationType,
            success: (count: Int) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getUnreadCount(conversationTypes.toList().toTypedArray(), success, error)
    }

    /**
     * 获取指定会话类型的未读消息总数量
     */
    fun getUnreadCount(vararg conversationTypes: Conversation.ConversationType): Observable<Int> {
        return Observable.create<Int> { emitter ->
            IMCore.getUnreadCount(
                    conversationTypes.toList().toTypedArray(),
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取指定会话的未读消息数量
     */
    fun getConversationUnreadCount(
            conversationType: Conversation.ConversationType,
            targetId: String,
            success: (count: Int) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getConversationUnreadCount(conversationType, targetId, success, error)
    }

    /**
     * 获取指定会话的未读消息数量
     */
    fun getConversationUnreadCount(
            conversationType: Conversation.ConversationType,
            targetId: String
    ): Observable<Int> {
        return Observable.create<Int> { emitter ->
            IMCore.getConversationUnreadCount(
                    conversationType,
                    targetId,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取本地会话的列表
     */
    fun getConversationList(
            success: (conversationList: List<Conversation>) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getConversationList(success, error)
    }

    /**
     * 获取本地会话的列表
     */
    fun getConversationList(): Observable<List<Conversation>> {
        return Observable.create<List<Conversation>> { emitter ->
            IMCore.getConversationList(
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取指定的本地会话
     */
    fun getConversation(
            conversationType: Conversation.ConversationType,
            targetId: String,
            success: (conversation: Conversation?) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getConversation(conversationType, targetId, success, error)
    }

    /**
     * 获取指定的本地会话
     */
    fun getConversation(
            conversationType: Conversation.ConversationType,
            targetId: String
    ): Observable<Conversation> {
        return Observable.create<Conversation> { emitter ->
            IMCore.getConversation(conversationType, targetId,
                    success = { conversation ->
                        conversation?.let {
                            emitter.onNext(it)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 移除指定会话
     * @param conversation 会话
     * @param isClearMessage 是否同时清除消息
     * @param success 结果的回调
     * @param error 异常回调
     */
    fun removeConversation(
            conversation: Conversation,
            isClearMessage: Boolean = false,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.removeConversation(conversation, isClearMessage, success, error)
    }

    /**
     * 移除指定会话
     * @param conversation 会话
     * @param isClearMessage 是否同时清除消息
     */
    fun removeConversation(
            conversation: Conversation,
            isClearMessage: Boolean = false
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.removeConversation(
                    conversation,
                    isClearMessage,
                    success = {
                        if (it) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 移除指定会话
     * @param conversationType
     * @param targetId
     * @param isClearMessage 是否同时清除消息
     * @param success 结果的回调
     * @param error 异常回调
     */
    fun removeConversation(
            conversationType: Conversation.ConversationType,
            targetId: String,
            isClearMessage: Boolean = false,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.removeConversation(conversationType, targetId, isClearMessage, success, error)
    }

    /**
     * 移除指定会话
     * @param conversationType
     * @param targetId
     * @param isClearMessage 是否同时清除消息
     */
    fun removeConversation(
            conversationType: Conversation.ConversationType,
            targetId: String,
            isClearMessage: Boolean = false
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.removeConversation(
                    conversationType,
                    targetId,
                    isClearMessage,
                    success = {
                        if (it) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 清除指定会话的未读消息的状态
     */
    fun clearMessagesUnreadStatus(
            conversation: Conversation,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.clearMessagesUnreadStatus(conversation, success, error)
    }

    /**
     * 清除指定会话的未读消息的状态
     */
    fun clearMessagesUnreadStatus(
            conversation: Conversation
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.clearMessagesUnreadStatus(
                    conversation,
                    success = {
                        if (it) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 清除指定会话的未读消息的状态
     */
    fun clearMessagesUnreadStatus(
            conversationType: Conversation.ConversationType,
            targetId: String,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.clearMessagesUnreadStatus(conversationType, targetId, success, error)
    }

    /**
     * 清除指定会话的未读消息的状态
     */
    fun clearMessagesUnreadStatus(
            conversationType: Conversation.ConversationType,
            targetId: String
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.clearMessagesUnreadStatus(
                    conversationType,
                    targetId,
                    success = {
                        if (it) {
                            emitter.onNext(it)
                        } else {
                            emitter.onError(operateFailedException)
                        }
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取指定会话的历史消息记录
     * @param conversation
     * @param oldestMessageId   最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为:-1。默认-1
     * @param count  要获取的消息数量。默认20
     * @param success
     * @param error
     */
    fun getHistoryMessages(
            conversation: Conversation,
            oldestMessageId: Int = -1,
            count: Int = 20,
            success: (messageList: List<Message>) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getHistoryMessages(conversation, oldestMessageId, count, success, error)
    }

    /**
     * 获取指定会话的历史消息记录
     * @param conversation
     * @param oldestMessageId   最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为:-1。默认-1
     * @param count  要获取的消息数量。默认20
     */
    fun getHistoryMessages(
            conversation: Conversation,
            oldestMessageId: Int = -1,
            count: Int = 20
    ): Observable<List<Message>> {
        return Observable.create<List<Message>> { emitter ->
            IMCore.getHistoryMessages(
                    conversation,
                    oldestMessageId,
                    count,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取指定会话的历史消息记录
     * @param conversationType
     * @param targetId
     * @param oldestMessageId   最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为:-1。默认-1
     * @param count  要获取的消息数量。默认20
     * @param success
     * @param error
     */
    fun getHistoryMessages(
            conversationType: Conversation.ConversationType,
            targetId: String,
            oldestMessageId: Int = -1,
            count: Int = 20,
            success: (messageList: List<Message>) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.getHistoryMessages(conversationType, targetId, oldestMessageId, count, success, error)
    }


    /**
     * 获取指定会话的历史消息记录
     * @param conversationType
     * @param targetId
     * @param oldestMessageId   最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为:-1。默认-1
     * @param count  要获取的消息数量。默认20
     */
    fun getHistoryMessages(
            conversationType: Conversation.ConversationType,
            targetId: String,
            oldestMessageId: Int = -1,
            count: Int = 20
    ): Observable<List<Message>> {
        return Observable.create<List<Message>> { emitter ->
            IMCore.getHistoryMessages(
                    conversationType,
                    targetId,
                    oldestMessageId,
                    count,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 根据关键字搜索指定会话的消息
     * @param conversation
     * @param keyword  要搜索的消息的关键字
     * @param count  要获取的消息数量, 传0时会返回所有搜索到的消息, 非0时,逐页返回。默认0
     * @param beginTime  查询记录的起始时间, 传0时从最新消息开始搜索。
     * @param success
     * @param error
     */
    fun searchMessages(
            conversation: Conversation,
            keyword: String,
            count: Int = 0,
            beginTime: Long = 0,
            success: (messageList: List<Message>) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.searchMessages(conversation, keyword, count, beginTime, success, error)
    }

    /**
     * 根据关键字搜索指定会话的消息
     * @param conversation
     * @param keyword  要搜索的消息的关键字
     * @param count  要获取的消息数量, 传0时会返回所有搜索到的消息, 非0时,逐页返回。默认0
     * @param beginTime  查询记录的起始时间, 传0时从最新消息开始搜索。
     */
    fun searchMessages(
            conversation: Conversation,
            keyword: String,
            count: Int = 0,
            beginTime: Long = 0
    ): Observable<List<Message>> {
        return Observable.create<List<Message>> { emitter ->
            IMCore.searchMessages(
                    conversation,
                    keyword,
                    count,
                    beginTime,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 根据关键字搜索指定会话的消息
     * @param conversationType
     * @param targetId
     * @param keyword  要搜索的消息的关键字
     * @param count  要获取的消息数量, 传0时会返回所有搜索到的消息, 非0时,逐页返回。默认0
     * @param beginTime  查询记录的起始时间, 传0时从最新消息开始搜索。
     * @param success
     * @param error
     */
    fun searchMessages(
            conversationType: Conversation.ConversationType,
            targetId: String,
            keyword: String,
            count: Int = 0,
            beginTime: Long = 0,
            success: (messageList: List<Message>) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        IMCore.searchMessages(conversationType, targetId, keyword, count, beginTime, success, error)
    }

    /**
     * 根据关键字搜索指定会话的消息
     * @param conversationType
     * @param targetId
     * @param keyword  要搜索的消息的关键字
     * @param count  要获取的消息数量, 传0时会返回所有搜索到的消息, 非0时,逐页返回。默认0
     * @param beginTime  查询记录的起始时间, 传0时从最新消息开始搜索。
     */
    fun searchMessages(
            conversationType: Conversation.ConversationType,
            targetId: String,
            keyword: String,
            count: Int = 0,
            beginTime: Long = 0
    ): Observable<List<Message>> {
        return Observable.create<List<Message>> { emitter ->
            IMCore.searchMessages(
                    conversationType,
                    targetId,
                    keyword,
                    count,
                    beginTime,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取会话的通知的状态
     */
    fun getConversationNotificationStatus(
            conversationType: Conversation.ConversationType,
            targetId: String
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.getConversationNotificationStatus(
                    conversationType,
                    targetId,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 设置会话的通知状态
     */
    fun setConversationNotificationStatus(
            conversationType: Conversation.ConversationType,
            targetId: String,
            isNotify: Boolean
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.setConversationNotificationStatus(
                    conversationType,
                    targetId,
                    isNotify,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 设置消息的接收状态
     */
    fun setMessageReceivedStatus(
            messageId: Int,
            receivedStatus: Message.ReceivedStatus
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.setMessageReceivedStatus(
                    messageId,
                    receivedStatus,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    //更新接收到的消息为已读
    fun updateReceivedMessageRead(messageId: Int): Observable<Boolean> {
        return setMessageReceivedStatus(messageId, Message.ReceivedStatus(1))
    }

    /**
     * 设置消息的扩展信息
     */
    fun setMessageExtra(
            messageId: Int,
            value: String
    ): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            IMCore.setMessageExtra(
                    messageId,
                    value,
                    success = {
                        emitter.onNext(it)
                        emitter.onComplete()
                    },
                    error = {
                        emitter.onError(createIMException(it))
                        emitter.onComplete()
                    })
        }.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 根据ErrorCode创建一个异常
     */
    private fun createIMException(errorCode: Int?): IMException {
        return IMException(errorCode ?: IMException.ERROR_UNKNOWN)
    }

}

/**
 * IM初始化的配置Bean
 */
data class IMInitConfig(
        var context: Context? = null,
        var key: String? = null,
        var fileUploadProvider: IMFileUploadProvider? = null,
        var tokenProvider: IMTokenProvider? = null
)