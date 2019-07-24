package com.yzy.baselibrary.extention

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jakewharton.rxbinding3.view.clicks
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindUntilEvent
import java.util.concurrent.TimeUnit

/**
 *description: View的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */

@SuppressLint("CheckResult")
inline fun View.click(crossinline function: () -> kotlin.Unit) {
    clicks()
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .subscribe {
            function()
        }
}

/**
 * 点击事件，默认销毁的时候
 */
@SuppressLint("CheckResult")
inline fun View.click(owner: LifecycleOwner, crossinline function: () -> kotlin.Unit) {
    clicks()
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
        .subscribe {
            function()
        }
}


/**
 * 点击事件
 */
@SuppressLint("CheckResult")
inline fun View.click(
    owner: LifecycleOwner, event: Lifecycle.Event,
    crossinline function: () -> kotlin.Unit
) {
    clicks()
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .bindUntilEvent(owner, event)
        .subscribe {
            function()
        }
}

/**
 * 点击事件
 */
@SuppressLint("CheckResult")
inline fun View.longClick(
    owner: LifecycleOwner, event: Lifecycle.Event,
    crossinline function: () -> kotlin.Unit
) {
    clicks()
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .bindUntilEvent(owner, event)
        .subscribe {
            function()
        }
}

// 黑暗 0.0F ~ 1.0F 透明
fun Context.setBackgroundAlpha(alpha: Float) {
    val act = this as? Activity ?: return
    val attributes = act.window.attributes
    attributes.alpha = alpha
    act.window.attributes = attributes
}

// 快速双击
private var lastClickTime: Long = 0
private const val SPACE_TIME = 500
fun isDoubleClick(): Boolean {
    val currentTime = System.currentTimeMillis()
    val isDoubleClick: Boolean // in range
    isDoubleClick = currentTime - lastClickTime <= SPACE_TIME
    if (!isDoubleClick) {
        lastClickTime = currentTime
    }
    return isDoubleClick
}


fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

