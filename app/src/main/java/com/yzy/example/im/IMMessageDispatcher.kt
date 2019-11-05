package com.yzy.example.im

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.rong.imlib.model.Message

/**
 *description: IM接收到的Message分发工具类.
 *@date 2019/3/12 11:46.
 *@author: yzy.
 */
internal object IMMessageDispatcher {

    /**
     * 根据objectName分类的消息接收回调
     */
    private val messageReceiveCallbackMap =
            mutableMapOf<String, MutableList<(message: Message) -> Unit>>()

    /**
     * 所有消息的回调
     */
    private val allMessageReceiveCallbackList = mutableListOf<(message: Message) -> Unit>()

    /**
     *分发消息
     */
    fun dispatcherMessage(message: Message) {
        //需要切换到主线程
        val disposable = Observable.just(message)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { message1 ->
                    allMessageReceiveCallbackList.forEach {
                        it.invoke(message1)
                    }
                    messageReceiveCallbackMap[message1.objectName]?.forEach {
                        it.invoke(message1)
                    }
                }
    }

    /**
     * 添加消息接收的回调
     * @param callback 回调
     * @param objectName 消息的objectName,为空的话表示所有类型的消息都监听
     */
    fun addMessageReceiveCallBack(
            callback: (message: Message) -> Unit,
            objectName: String? = null
    ) {
        if (objectName.isNullOrBlank()) {
            allMessageReceiveCallbackList.add(callback)
        } else {
            objectName?.let {
                if (messageReceiveCallbackMap[it] == null) {
                    messageReceiveCallbackMap[it] = mutableListOf()
                }
                messageReceiveCallbackMap[it]?.add(callback)
            }
        }
    }

    /**
     * 移除消息接收的回调
     * @param callback 回调
     * @param objectName 消息的objectName,为空的话表示所有类型的消息都监听
     */
    fun removeMessageReceiveCallBack(
            callback: (message: Message) -> Unit,
            objectName: String? = null
    ) {
        if (objectName.isNullOrBlank()) {
            allMessageReceiveCallbackList.remove(callback)
        } else {
            objectName?.let {
                messageReceiveCallbackMap[it]?.remove(callback)
            }
        }
    }
}