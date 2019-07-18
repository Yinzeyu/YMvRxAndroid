package com.yzy.sociallib

import java.lang.ref.WeakReference

/**
 *description: 弱引用的封装.
 * @date 2019/7/15
 * @author: yzy.
 */
class WeakRef<T : Any> constructor(any: T) {
  private val weakRef = WeakReference(any)

  operator fun invoke(): T? {
    return weakRef.get()
  }
}

//任何类型的弱引用扩展
fun <T : Any> T.weakReference() = WeakRef(this)