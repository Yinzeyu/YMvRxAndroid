package com.yzy.baselibrary.http.retry

import com.yzy.baselibrary.http.retry.RetryConfig
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

/**
 *description: Observable的接口请求重连.
 *@date 2019/7/15
 *@author: yzy.
 */
class ObservableRetryDelay(val retryConfigProvider: (Throwable) -> RetryConfig) : Function<Observable<Throwable>, ObservableSource<*>> {

    private var retryCount: Int = 0

    override fun apply(throwableObs: Observable<Throwable>): ObservableSource<*> {
        return throwableObs
                .flatMap { error ->
                    val (maxRetries, delay, retryCondition) = retryConfigProvider(error)

                    if (++retryCount <= maxRetries) {
                        retryCondition()
                                .flatMapObservable { retry ->
                                    if (retry)
                                        Observable.timer(delay.toLong(), TimeUnit.MILLISECONDS)
                                    else
                                        Observable.error<Any>(error)
                                }
                    } else Observable.error<Any>(error)
                }
    }
}