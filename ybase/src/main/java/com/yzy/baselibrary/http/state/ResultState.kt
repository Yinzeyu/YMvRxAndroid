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
        fun <T> onAppSuccess(data: T?): ResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage:String): ResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: ResponseThrowable): ResultState<T> = Error(error)
    }

    data class Loading(val loadingMessage:String) : ResultState<Nothing>()
    data class Success<out T>(val data: T?) : ResultState<T>()
    data class Error(val error: ResponseThrowable) : ResultState<Nothing>()
}


/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: IBaseResponse<T>) {
    value = if (result.isSuccess()) ResultState.onAppSuccess(result.data()) else
        ResultState.onAppError(ResponseThrowable(result.code(), result.msg()))
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onAppSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onAppError(ExceptionHandle.handleException(e))
}

