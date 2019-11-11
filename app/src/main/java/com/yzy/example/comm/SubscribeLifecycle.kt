package com.yzy.example.comm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


/**
 *description: LifecycleOwner生命周期订阅的DSL.
 *@date 2019/7/15
 *@author: yzy.
 */
class SubscribeLifecycle {
    var lifecycleOwner: LifecycleOwner? = null
    var onCreate: (() -> Unit)? = null
    var onStart: (() -> Unit)? = null
    var onResume: (() -> Unit)? = null
    var onPause: (() -> Unit)? = null
    var onStop: (() -> Unit)? = null
    var onDestroy: (() -> Unit)? = null
}

fun subscribeLifecycle(lifecycleOwner: LifecycleOwner, init: SubscribeLifecycle.() -> Unit) {
    val subscribeLifecycle = SubscribeLifecycle()
    subscribeLifecycle.lifecycleOwner = lifecycleOwner
    subscribeLifecycle.init()
    observerLifecycle(subscribeLifecycle)
}

private fun observerLifecycle(subscribeLifecycle: SubscribeLifecycle) {
    subscribeLifecycle.lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            subscribeLifecycle.onCreate?.invoke()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            subscribeLifecycle.onStart?.invoke()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            subscribeLifecycle.onResume?.invoke()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            subscribeLifecycle.onPause?.invoke()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            subscribeLifecycle.onStop?.invoke()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            subscribeLifecycle.onDestroy?.invoke()
        }
    })
}