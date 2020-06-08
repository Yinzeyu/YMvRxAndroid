package com.yzy.baselibrary.http.state

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.IBaseResponse
import com.yzy.baselibrary.http.ExceptionHandle
import com.yzy.baselibrary.http.ResponseThrowable

/**
 * 作者　: yzy
 * 时间　: 2020/5/26
 * 描述　: 自定义结果集封装类
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onSuccess(data: T?): ResultState<T> = Success(data)
        fun <T> onEmpty(): ResultState<T> = Empty()
        fun <T> onLoading(loadingMessage:String): ResultState<T> = Loading(loadingMessage)
        fun <T> onError(error: ResponseThrowable): ResultState<T> = Error(error)
    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Empty(var str:String?=null) : ResultState<Nothing>()
    data class Success<out T>(val data: T?) : ResultState<T>()
    data class Error(val error: ResponseThrowable) : ResultState<Nothing>()
}
/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onError(ExceptionHandle.handleException(e))
}

fun <T> MutableLiveData<ResultState<T>>.paresEmpty() {
    this.value = ResultState.onEmpty()
}