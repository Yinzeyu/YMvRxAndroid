//package com.yzy.example.im
//
//import android.annotation.SuppressLint
//import android.util.Log
//import com.yzy.example.im.entity.IMActionEntity
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
///**
// *description: IM的Action分发工具类.
// *@date 2019/3/11 15:06.
// *@author: yzy.
// */
//internal object IMActionDispatcher {
//
//    private val actionCallbackMap =
//        mutableMapOf<String, MutableList<(entity: IMActionEntity) -> Unit>>()
//
//    /**
//     *分发action
//     */
//    @SuppressLint("CheckResult")
//    fun dispatcherAction(action: IMActionEntity) {
//        Log.i("imAction", "action:${action.action}")
//        //需要切换到主线程
//        Observable.just(action)
//            .subscribeOn(Schedulers.io())
//            .unsubscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { actionEntity ->
//                actionCallbackMap[actionEntity.action]?.forEach {
//                    it.invoke(action)
//                }
//            }
//    }
//
//    /**
//     * 添加事件监听的回调
//     */
//    fun addActionCallBack(action: String?, callback: ((entity: IMActionEntity) -> Unit)?) {
//        action?.let { action1 ->
//            callback?.let { callback1 ->
//                if (actionCallbackMap[action1] == null) {
//                    actionCallbackMap[action1] = mutableListOf()
//                }
//                actionCallbackMap[action1]?.add(callback1)
//            }
//        }
//    }
//
//    /**
//     * 移除事件监听回调
//     */
//    fun removeActionCallBack(action: String?, callback: ((entity: IMActionEntity) -> Unit)?) {
//        callback?.let {
//            actionCallbackMap[action]?.remove(it)
//        }
//    }
//}