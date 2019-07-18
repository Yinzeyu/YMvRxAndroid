package com.yzy.baselibrary.http.response

import io.reactivex.observers.ResourceObserver

/**
 *description: 只关心成功失败的请求观察者.
 *@date 2019/7/15
 *@author: yzy.
 */
class SimpleResourceObsever<T>(private val success: ((T) -> kotlin.Unit)?,
                               private val failure: ((Throwable) -> kotlin.Unit)? = null) : ResourceObserver<T>() {
  override fun onComplete() {
  }

  override fun onNext(t: T) {
    success?.invoke(t)
  }

  override fun onError(e: Throwable) {
    failure?.invoke(e)
  }
}