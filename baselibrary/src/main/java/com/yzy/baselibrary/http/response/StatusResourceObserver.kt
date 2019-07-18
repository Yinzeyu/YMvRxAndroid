package com.yzy.baselibrary.http.response

import io.reactivex.observers.ResourceObserver

/**
 *description: 关心各种状态的请求观察者.
 *@date 2019/7/15
 *@author: yzy.
 */
class StatusResourceObserver<T>(private val liveData: StatusDataLiveData<T>) :
    ResourceObserver<T>() {

    override fun onStart() {
        super.onStart()
    }

    override fun onComplete() {
    }

    override fun onNext(t: T) {
        liveData.value = StatusData.Success(t)
    }

    override fun onError(e: Throwable) {
        liveData.value = StatusData.Failure(e)
    }
}