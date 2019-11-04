package com.yzy.pj.im

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.yzy.pj.im.entity.IMActionEntity
import com.yzy.pj.im.entity.IMMessageType
import com.yzy.pj.im.entity.SendMessageBean
import com.yzy.pj.im.message.VideoMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.MessageContent
import io.rong.message.ImageMessage
import io.rong.push.RongPushClient
import java.util.concurrent.TimeUnit

/**
 *description: 融云IM的功能核心类.
 *@date 2019/3/11 14:30.
 *@author: yzy.
 */
internal object IMCore {

    /**
     * 消息发送的间隔，毫秒
     */
    private const val MESSAGE_SEND_INTERVAL = 333L
    /**
     * 登录的最小间隔
     */
    private const val LOGIN_INTERVAL = 3000L
    /**
     * 文件上传的提供者
     */
    var fileUploadProvider: IMFileUploadProvider? = null
    /**
     * token获取的提供者
     */
    var tokenProvider: IMTokenProvider? = null
    /**
     * 上一次登录的时间
     */
    private var lastLoginTime: Long = 0L
    /**
     * 融云连接的回调
     */
    private val mConnectCallback = object : RongIMClient.ConnectCallback() {
        override fun onSuccess(s: String?) {
            IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.LoginAction.ACTION_IM_LOGIN_SUCCESS))
        }

        override fun onError(errorCode: RongIMClient.ErrorCode?) {
            Log.e("ConnectCallback", "登录失败 code = ${errorCode?.value},message = ${errorCode?.message}")
            IMActionDispatcher.dispatcherAction(
                    IMActionEntity(
                            IMConstant.LoginAction.ACTION_IM_LOGIN_ERROR,
                            Bundle().apply {
                                errorCode?.let {
                                    this.putInt(IMActionEntity.DATA_KEY_ERROR_CODE, it.value)
                                    this.putString(IMActionEntity.DATA_KEY_ERROR_MESSAGE, it.message)
                                }
                            })
            )
        }

        override fun onTokenIncorrect() {
            IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.LoginAction.ACTION_IM_LOGIN_TOKENINCORRECT))
        }
    }
    /**
     * 融云连接状态的监听
     */
    private val mConnectionStatusListener = RongIMClient.ConnectionStatusListener {
        Log.e("ConnectionStatus", "ConnectionStatus value : ${it.value} message : ${it.message}")
        it?.run {
            when (this) {
                RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE -> {
                    //网络不可用
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_NETWORK_UNAVAILABLE))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED -> {
                    //连接成功
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_CONNECTED))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING -> {
                    //连接中
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_CONNECTING))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED -> {
                    //连接断开
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_DISCONNECTED))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT -> {
                    //被挤下线
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_KICKED_OFFLINE_BY_OTHER_CLIENT))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.TOKEN_INCORRECT -> {
                    //token失效
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_TOKEN_INCORRECT))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.SERVER_INVALID -> {
                    //服务器无效
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_SERVER_INVALID))
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONN_USER_BLOCKED -> {
                    //用户被封禁
                    IMActionDispatcher.dispatcherAction(IMActionEntity(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_CONN_USER_BLOCKED))
                }
            }
        }
    }
    /**
     * 消息接收的回调
     */
    private val receiveMessageListener =
            RongIMClient.OnReceiveMessageListener { message, _ ->
                /**
                 * 接收到新消息的回调
                 * @param message 消息
                 * @param hasCount 剩余未拉取消息数目
                 */
                message?.let {
                    IMMessageDispatcher.dispatcherMessage(it)
                }
                false
            }
    private var sendMessageList: List<SendMessageBean> = emptyList()
    private var sendLoopDisposable: Disposable? = null
    /**
     * @param context
     * @param key 融云的Key
     */
    fun initIM(context: Context, key: String) {
        RongIMClient.init(context, key)
        RongPushClient.init(context, key)
        //监听连接状态
        RongIMClient.setConnectionStatusListener(mConnectionStatusListener)
        //消息接收的回调
        RongIMClient.setOnReceiveMessageListener(receiveMessageListener)
    }

    /**
     * 注册融云要接收的自定义消息的类型
     */
    fun registerMessageType(clazzList: List<Class<out MessageContent>>) {
        clazzList.forEach {
            RongIMClient.registerMessageType(it)
        }
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        return RongIMClient.getInstance().currentConnectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED
    }

    /**
     * 登录
     */
    fun login() {
        if (RongIMClient.getInstance().currentConnectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
            //连接中
            return
        }
        tokenProvider?.getToken { token ->
            if (token.isNotBlank()) {
                //登录
                RongIMClient.connect(token, mConnectCallback)
            }
        }
    }

    /**
     * 消息发送异常时检测是否需要重新登录
     */
    private fun checkReLogin(errorCode: RongIMClient.ErrorCode?) {
        if (System.currentTimeMillis() - lastLoginTime < LOGIN_INTERVAL) {
            return
        }
        lastLoginTime = System.currentTimeMillis()
        errorCode?.let {
            if (it == RongIMClient.ErrorCode.RC_NET_CHANNEL_INVALID) {
                //Socket does not exist
                //进行通信操作过程中，当前 Socket 失效。
                logout()
                login()
            }
        }
    }

    /**
     * 退出登录
     */
    fun logout() {
        RongIMClient.getInstance().disconnect()
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
        val bean = SendMessageBean(
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
        sendMessage(bean)
    }

    /**
     * 根据SendMessageBean发送消息
     */
    fun sendMessage(bean: SendMessageBean) {
        if (bean.conversationType != null &&
                bean.targetId?.isNotBlank() == true &&
                bean.content != null
        ) {
            sendMessageList = sendMessageList + bean
            loopSendMessage()
        }
    }

    //循环发送聊天消息
    @Synchronized
    private fun loopSendMessage() {
        Log.e("IMCore", "loopSendMessage")
        if (sendMessageList.isEmpty()) {
            Log.e("IMCore", "loopSendMessage sendMessageList.isEmpty()")
            //列表中已经没有需要发送的消息了
            sendLoopDisposable?.run {
                if (!isDisposed) {
                    dispose()
                }
            }
            return
        }
        if (sendLoopDisposable != null && sendLoopDisposable?.isDisposed != true) {
            //正在循环中
            Log.e("IMCore", "loopSendMessage 正在循环中")
            return
        }
        val bean = sendMessageList[0]
        sendMessageList = sendMessageList - bean
        when (bean.messageType) {
            IMMessageType.Image ->
                imSendImageMessage(bean)
            IMMessageType.Voice ->
                imSendVoiceMessage(bean)
            IMMessageType.Video ->
                imSendVideoMessage(bean)
            IMMessageType.Other ->
                imSendDefaultMessage(bean)
        }
        sendLoopDisposable = Observable.just(sendMessageList)
                .delay(MESSAGE_SEND_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sendLoopDisposable?.dispose()
                    loopSendMessage()
                }, {
                    Log.e("IMCore", "Send message loop error!")
                })
    }

    /**
     * 真正调用Im发送图片消息
     */
    private fun imSendImageMessage(bean: SendMessageBean) {
        val message = Message.obtain(bean.targetId, bean.conversationType, bean.content)
        RongIMClient.getInstance().sendImageMessage(
                message,
                bean.pushContent,
                bean.pushData,
                object : RongIMClient.SendImageMessageWithUploadListenerCallback() {
                    override fun onAttached(
                            message: Message,
                            uploadImageStatusListener: RongIMClient.UploadImageStatusListener
                    ) {
                        val messageContent = message.content
                        if (messageContent is ImageMessage) {
                            messageContent.localPath.path?.let { filePath ->
                                fileUploadProvider?.uploadFile(filePath,
                                        success = { _, url ->
                                            uploadImageStatusListener.success(Uri.parse(url))
                                        },
                                        failed = {
                                            uploadImageStatusListener.error()
                                        },
                                        progress = { _, progress ->
                                            uploadImageStatusListener.update(progress)
                                        })
                                bean.attached?.invoke(message, IMMessageType.Image)
                            }
                        }
                    }

                    override fun onSuccess(message: Message?) {
                        bean.success?.invoke(message, IMMessageType.Image)
                    }

                    override fun onProgress(message: Message?, progress: Int) {
                        bean.progress?.invoke(message, IMMessageType.Image, progress)
                    }

                    override fun onError(message: Message?, errorCode: RongIMClient.ErrorCode?) {
                        checkReLogin(errorCode)
                        bean.error?.invoke(message, IMMessageType.Image, errorCode?.value)
                    }
                }
        )
    }

    /**
     * 真正调用Im发送声音消息
     */
    private fun imSendVoiceMessage(bean: SendMessageBean) {
        val message = Message.obtain(bean.targetId, bean.conversationType, bean.content)
        RongIMClient.getInstance()
                .sendMessage(
                        message,
                        bean.pushContent,
                        bean.pushData,
                        object : IRongCallback.ISendMediaMessageCallback {
                            override fun onProgress(message: Message?, progress: Int) {
                                bean.progress?.invoke(message, IMMessageType.Voice, progress)
                            }

                            override fun onCanceled(message: Message?) {
                                bean.canceled?.invoke(message, IMMessageType.Voice)
                            }

                            override fun onAttached(message: Message?) {
                                bean.attached?.invoke(message, IMMessageType.Voice)
                            }

                            override fun onSuccess(message: Message?) {
                                bean.success?.invoke(message, IMMessageType.Voice)
                            }

                            override fun onError(message: Message?, errorCode: RongIMClient.ErrorCode?) {
                                checkReLogin(errorCode)
                                bean.error?.invoke(message, IMMessageType.Voice, errorCode?.value)
                            }
                        })
    }

    /**
     * 真正调用Im发送短视频消息
     */
    private fun imSendVideoMessage(bean: SendMessageBean) {
        val message = Message.obtain(bean.targetId, bean.conversationType, bean.content)
        RongIMClient.getInstance().sendMediaMessage(
                message,
                bean.pushContent,
                bean.pushData,
                object : IRongCallback.ISendMediaMessageCallbackWithUploader {
                    override fun onAttached(
                            message: Message?,
                            uploadMediaStatusListener: IRongCallback.MediaMessageUploader?
                    ) {
                        val messageContent = message?.content
                        if (messageContent is VideoMessage) {
                            val coverPath = messageContent.coverLocalUri?.path
                            coverPath?.let { coverLocal ->
                                fileUploadProvider?.uploadFile(coverLocal,
                                        success = { _, url ->
                                            messageContent.coverUrl = url
                                            val videoPath = messageContent.videoLocalUri?.path
                                            videoPath?.let { videoLocal ->
                                                fileUploadProvider?.uploadFile(videoLocal,
                                                        success = { _, url ->
                                                            messageContent.videoUrl = url
                                                            uploadMediaStatusListener?.success(Uri.parse(url))
                                                        },
                                                        failed = {
                                                            uploadMediaStatusListener?.error()
                                                        },
                                                        progress = { _, progress ->
                                                            //视屏的整体进度占90%
                                                            uploadMediaStatusListener?.update((10 + progress * 0.9f).toInt())
                                                        })
                                            }
                                        },
                                        failed = {
                                            uploadMediaStatusListener?.error()
                                        },
                                        progress = { _, progress ->
                                            //图片的整体进度占10%
                                            uploadMediaStatusListener?.update((progress * 0.1f).toInt())
                                        })
                                bean.attached?.invoke(message, IMMessageType.Video)
                            }
                        }
                    }

                    override fun onCanceled(message: Message?) {
                        bean.canceled?.invoke(message, IMMessageType.Video)
                    }

                    override fun onSuccess(message: Message?) {
                        bean.success?.invoke(message, IMMessageType.Video)
                    }

                    override fun onProgress(message: Message?, progress: Int) {
                        bean.progress?.invoke(message, IMMessageType.Video, progress)
                    }

                    override fun onError(message: Message?, errorCode: RongIMClient.ErrorCode?) {
                        checkReLogin(errorCode)
                        bean.error?.invoke(message, IMMessageType.Voice, errorCode?.value)
                    }
                }
        )
    }

    /**
     * 真正调用Im发送默认消息（包括文字，位置等）
     */
    private fun imSendDefaultMessage(bean: SendMessageBean) {
        val message = Message.obtain(bean.targetId, bean.conversationType, bean.content)
        RongIMClient.getInstance()
                .sendMessage(
                        message,
                        bean.pushContent,
                        bean.pushData,
                        object : IRongCallback.ISendMessageCallback {
                            override fun onAttached(message: Message?) {
                                bean.attached?.invoke(message, IMMessageType.Other)
                            }

                            override fun onSuccess(message: Message?) {
                                bean.success?.invoke(message, IMMessageType.Other)
                            }

                            override fun onError(message: Message?, errorCode: RongIMClient.ErrorCode?) {
                                checkReLogin(errorCode)
                                bean.error?.invoke(message, IMMessageType.Other, errorCode?.value)
                            }
                        })
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
        RongIMClient.getInstance().insertOutgoingMessage(conversationType, targetId,
                sentStatus, content,
                object : RongIMClient.ResultCallback<Message>() {
                    override fun onSuccess(message: Message?) {
                        success?.invoke(message)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
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
        RongIMClient.getInstance().insertIncomingMessage(conversationType, targetId, senderUserId,
                receivedStatus, content, sentTime,
                object : RongIMClient.ResultCallback<Message>() {
                    override fun onSuccess(message: Message?) {
                        success?.invoke(message)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }

    /**
     * 删除指定消息
     */
    fun deleteMessages(
            messageList: List<Message>,
            success: ((isSuccess: Boolean) -> Unit)? = null,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        val ids = mutableListOf<Int>()
        messageList.forEachIndexed { index, m ->
            ids[index] = m.messageId
        }
        deleteMessagesByIds(ids, success, error)
    }

    /**
     * 删除指定ID的消息
     */
    fun deleteMessagesByIds(
            messageIds: List<Int>,
            success: ((isSuccess: Boolean) -> Unit)? = null,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance()
                .deleteMessages(messageIds.toIntArray(), object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(isSuccess: Boolean) {
                        success?.invoke(isSuccess)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }

    //会话相关
    /**
     * 获取所有会话的未读消息总数量
     */
    fun getTotalUnreadCount(
            success: (count: Int) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance().getUnreadCount(object : RongIMClient.ResultCallback<Int>() {
            override fun onSuccess(count: Int) {
                success.invoke(count)
            }

            override fun onError(errorCode: RongIMClient.ErrorCode?) {
                error?.invoke(errorCode?.value)
            }
        })
    }

    /**
     * 获取指定会话类型的未读消息总数量
     */
    fun getUnreadCount(
            conversationTypes: Array<Conversation.ConversationType>,
            success: (count: Int) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance()
                .getUnreadCount(conversationTypes, object : RongIMClient.ResultCallback<Int>() {
                    override fun onSuccess(count: Int) {
                        success.invoke(count)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
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
        RongIMClient.getInstance().getUnreadCount(conversationType,
                targetId,
                object : RongIMClient.ResultCallback<Int>() {
                    override fun onSuccess(count: Int) {
                        success.invoke(count)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }

    /**
     * 获取本地会话的列表
     */
    fun getConversationList(
            success: (conversationList: List<Conversation>) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance().getConversationList(
                object : RongIMClient.ResultCallback<List<Conversation>>() {
                    override fun onSuccess(list: List<Conversation>?) {
                        success.invoke(list ?: emptyList())
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
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
        RongIMClient.getInstance().getConversation(conversationType, targetId,
                object : RongIMClient.ResultCallback<Conversation>() {
                    override fun onSuccess(conversation: Conversation?) {
                        success.invoke(conversation)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
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
        removeConversation(
                conversation.conversationType,
                conversation.targetId,
                isClearMessage,
                success,
                error
        )
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
        RongIMClient.getInstance().removeConversation(conversationType, targetId,
                object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(isSuccess: Boolean) {
                        if (isClearMessage) {
                            //同时清除消息记录
                            RongIMClient.getInstance().clearMessages(conversationType, targetId,
                                    object : RongIMClient.ResultCallback<Boolean>() {
                                        override fun onSuccess(isSuccess: Boolean) {
                                            success.invoke(isSuccess)
                                        }

                                        override fun onError(errorCode: RongIMClient.ErrorCode?) {
                                            error?.invoke(errorCode?.value)
                                        }
                                    })
                        } else {
                            success.invoke(isSuccess)
                        }
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }

    /**
     * 清除指定会话的未读消息的状态
     */
    fun clearMessagesUnreadStatus(
            conversation: Conversation,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        clearMessagesUnreadStatus(
                conversation.conversationType,
                conversation.targetId,
                success,
                error
        )
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
        RongIMClient.getInstance().clearMessagesUnreadStatus(conversationType, targetId,
                object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(isSuccess: Boolean) {
                        success.invoke(isSuccess)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
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
        getHistoryMessages(
                conversation.conversationType,
                conversation.targetId,
                oldestMessageId,
                count,
                success,
                error
        )
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
        RongIMClient.getInstance()
                .getHistoryMessages(conversationType, targetId, oldestMessageId, count,
                        object : RongIMClient.ResultCallback<List<Message>>() {
                            override fun onSuccess(messageList: List<Message>?) {
                                success.invoke(messageList ?: emptyList())
                            }

                            override fun onError(errorCode: RongIMClient.ErrorCode?) {
                                error?.invoke(errorCode?.value)
                            }
                        })
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
        searchMessages(
                conversation.conversationType,
                conversation.targetId,
                keyword,
                count,
                beginTime,
                success,
                error
        )
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
        RongIMClient.getInstance()
                .searchMessages(conversationType, targetId, keyword, count, beginTime,
                        object : RongIMClient.ResultCallback<List<Message>>() {
                            override fun onSuccess(messageList: List<Message>?) {
                                success.invoke(messageList ?: emptyList())
                            }

                            override fun onError(errorCode: RongIMClient.ErrorCode?) {
                                error?.invoke(errorCode?.value)
                            }
                        })
    }

    /**
     * 获取会话是否处于通知的状态
     */
    fun getConversationNotificationStatus(
            conversationType: Conversation.ConversationType,
            targetId: String,
            success: (isNotify: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance()
                .getConversationNotificationStatus(conversationType, targetId,
                        object : RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                            override fun onSuccess(state: Conversation.ConversationNotificationStatus?) {
                                if (state == Conversation.ConversationNotificationStatus.NOTIFY) {
                                    success.invoke(true)
                                } else {
                                    success.invoke(false)
                                }
                            }

                            override fun onError(errorCode: RongIMClient.ErrorCode?) {
                                error?.invoke(errorCode?.value)
                            }
                        })
    }

    /**
     * 设置会话是否通知
     */
    fun setConversationNotificationStatus(
            conversationType: Conversation.ConversationType,
            targetId: String,
            isNotify: Boolean,
            success: (isNotify: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance()
                .setConversationNotificationStatus(conversationType, targetId, if (isNotify) {
                    Conversation.ConversationNotificationStatus.NOTIFY
                } else {
                    Conversation.ConversationNotificationStatus.DO_NOT_DISTURB
                }, object : RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    override fun onSuccess(state: Conversation.ConversationNotificationStatus?) {
                        if (state == Conversation.ConversationNotificationStatus.NOTIFY) {
                            success.invoke(true)
                        } else {
                            success.invoke(false)
                        }
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }

    /**
     * 设置消息的接收状态
     */
    fun setMessageReceivedStatus(
            messageId: Int,
            receivedStatus: Message.ReceivedStatus,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance().setMessageReceivedStatus(messageId, receivedStatus,
                object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(isSuccess: Boolean) {
                        success.invoke(isSuccess)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }

    /**
     * 设置消息的扩展信息
     */
    fun setMessageExtra(
            messageId: Int,
            value: String,
            success: (isSuccess: Boolean) -> Unit,
            error: ((errorCode: Int?) -> Unit)? = null
    ) {
        RongIMClient.getInstance().setMessageExtra(messageId, value,
                object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(isSuccess: Boolean) {
                        success.invoke(isSuccess)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
                        error?.invoke(errorCode?.value)
                    }
                })
    }
}