package com.yzy.commonlibrary.http

import com.yzy.baselibrary.extention.applyFollowableSchedulers
import com.yzy.baselibrary.extention.applyMaybeSchedulers
import com.yzy.baselibrary.extention.applySchedulers
import com.yzy.baselibrary.extention.applySingleSchedulers
import com.yzy.baselibrary.http.retry.FlowableRetryDelay
import com.yzy.baselibrary.http.retry.ObservableRetryDelay
import com.yzy.baselibrary.http.retry.RetryConfig
import com.yzy.commonlibrary.http.response.BaseResponse
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *description: 网络请求的全局拦截及异常处理.
 *@date 2019/7/15
 *@author: yzy.
 */
class GlobalHandleTransformer<T> constructor(
    private val globalOnNextInterceptor: (BaseResponse<T>) -> Observable<T> = { Observable.just(it.data) },
    private val globalOnErrorResume: (Throwable) -> Observable<T> = { Observable.error(it) },
    private val retryConfigProvider: (Throwable) -> RetryConfig = { RetryConfig() },
    private val upStreamSchedulerProvider: () -> Scheduler = { AndroidSchedulers.mainThread() },
    private val downStreamSchedulerProvider: () -> Scheduler = { AndroidSchedulers.mainThread() }
) : ObservableTransformer<BaseResponse<T>, T>,
    FlowableTransformer<BaseResponse<T>, T>,
    SingleTransformer<BaseResponse<T>, T>,
    MaybeTransformer<BaseResponse<T>, T> {

    override fun apply(upstream: Observable<BaseResponse<T>>): Observable<T> =
        upstream
            .flatMap {
                globalOnNextInterceptor(it)
            }
            .onErrorResumeNext { throwable: Throwable ->
                globalOnErrorResume(throwable)
            }
            .observeOn(upStreamSchedulerProvider())
            .retryWhen(ObservableRetryDelay(retryConfigProvider))
            .observeOn(downStreamSchedulerProvider())
            .compose(applySchedulers())

    override fun apply(upstream: Flowable<BaseResponse<T>>): Flowable<T> =
        upstream
            .flatMap {
                globalOnNextInterceptor(it)
                    .toFlowable(BackpressureStrategy.BUFFER)
            }
            .onErrorResumeNext { throwable: Throwable ->
                globalOnErrorResume(throwable)
                    .toFlowable(BackpressureStrategy.BUFFER)
            }
            .observeOn(upStreamSchedulerProvider())
            .retryWhen(FlowableRetryDelay(retryConfigProvider))
            .observeOn(downStreamSchedulerProvider())
            .compose(applyFollowableSchedulers())

    override fun apply(upstream: Maybe<BaseResponse<T>>): Maybe<T> =
        upstream
            .flatMap {
                globalOnNextInterceptor(it)
                    .firstElement()
            }
            .onErrorResumeNext { throwable: Throwable ->
                globalOnErrorResume(throwable)
                    .firstElement()
            }
            .observeOn(upStreamSchedulerProvider())
            .retryWhen(FlowableRetryDelay(retryConfigProvider))
            .observeOn(downStreamSchedulerProvider())
            .compose(applyMaybeSchedulers())

    override fun apply(upstream: Single<BaseResponse<T>>): Single<T> =
        upstream
            .flatMap {
                globalOnNextInterceptor(it)
                    .firstOrError()
            }
            .onErrorResumeNext { throwable ->
                globalOnErrorResume(throwable)
                    .firstOrError()
            }
            .observeOn(upStreamSchedulerProvider())
            .retryWhen(FlowableRetryDelay(retryConfigProvider))
            .observeOn(downStreamSchedulerProvider())
            .compose(applySingleSchedulers())
}