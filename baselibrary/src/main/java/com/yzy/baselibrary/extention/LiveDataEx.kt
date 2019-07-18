package com.yzy.baselibrary.extention

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 *description: LiveData的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline f: (T?) -> Unit) {
  this.observe(owner, Observer<T> { t -> f(t) })
}

inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline f: (T) -> Unit) {
  this.observe(owner, Observer<T> { t -> t?.let(f) })
}
