package com.yzy.pj.im

import java.lang.ref.WeakReference

/**
 *description: 弱引用的封装.
 *@date 2019/3/12 15:32.
 *@author: yzy.
 */
class WeakRef<T : Any> constructor(any: T) {
    private val weakRef = WeakReference(any)

    operator fun invoke(): T? {
        return weakRef.get()
    }
}

//任何类型的弱引用扩展
fun <T : Any> T.weakReference() = WeakRef(this)