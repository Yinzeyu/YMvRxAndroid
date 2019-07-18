package com.yzy.baselibrary.bus

import com.yzy.baselibrary.utils.SchedulersUtil
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 *description: RxBus.
 *@date 2019/7/15
 *@author: yzy.
 */
class RxBus private constructor() {

    private val bus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    fun post(o: Any) {
        bus.onNext(o)
    }

    fun <T> toFlowable(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType)
    }

    fun <T> toFlowableMainThread(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType).compose<T>(SchedulersUtil.applyFlowableSchedulers())
    }

    fun <T> toFlowableBackpressure(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType).onBackpressureBuffer()
    }

    fun <T> toFlowableMainThreadBackpressure(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType).onBackpressureBuffer()
            .compose<T>(SchedulersUtil.applyFlowableSchedulers())
    }

    fun <T> toFlowable(eventType: Class<T>, consumer: Consumer<T>): Disposable {
        return bus.ofType(eventType)
            .subscribe(consumer)
    }

    fun <T> toFlowableMainThread(eventType: Class<T>, consumer: Consumer<T>): Disposable {
        return bus.ofType(eventType)
            .compose<T>(SchedulersUtil.applyFlowableSchedulers())
            .subscribe(consumer)
    }

    fun <T> toFlowableBackpressure(eventType: Class<T>, consumer: Consumer<T>): Disposable {
        return bus.ofType(eventType)
            .onBackpressureBuffer()
            .subscribe(consumer)
    }

    fun <T> toFlowableMainThreadBackpressure(
        eventType: Class<T>,
        consumer: Consumer<T>
    ): Disposable {
        return bus.ofType(eventType)
            .onBackpressureBuffer()
            .compose<T>(SchedulersUtil.applyFlowableSchedulers())
            .subscribe(consumer)
    }

    private object Holder {
        val INSTANCE = RxBus()
    }
}