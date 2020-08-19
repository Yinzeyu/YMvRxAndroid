package com.yzy.baselibrary.extention

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.base.IBaseResponse
import com.yzy.baselibrary.http.ExceptionHandle
import com.yzy.baselibrary.http.ResponseThrowable
import com.yzy.baselibrary.http.state.ResultState
import com.yzy.baselibrary.http.state.paresEmpty
import com.yzy.baselibrary.http.state.paresException
import com.yzy.baselibrary.http.state.paresResult
import kotlinx.coroutines.*

/**
 * 作者　: yzy
 * 时间　: 2020/5/26
 * 描述　:BaseViewModel请求协程封装
 */

//
///**
// * net request 不校验请求结果数据是否是成功
// * @param block 请求体方法
// * @param resultState 请求回调的ResultState数据
// * @param isShowDialog 是否显示加载框
// * @param loadingMessage 加载框提示内容
// */
//fun <T> BaseViewModel<*>.requestNoCheck(
//    block: suspend () -> T,
//    resultState: MutableLiveData<ResultState<T>>,
//    isShowDialog: Boolean = false,
//    loadingMessage: String = "请求网络中..."
//) {
//    viewModelScope.launch {
//        runCatching {
//            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
//            withContext(Dispatchers.IO) { block() }
//        }.onSuccess {
//            resultState.paresResult(it)
//        }.onFailure {
//            resultState.paresException(it)
//        }
//    }
//}


///**
// *  不过滤请求结果
// * @param block 请求体 必须要用suspend关键字修饰
// * @param success 成功回调
// * @param error 失败回调 可不给
// * @param isShowDialog 是否显示加载框
// * @param loadingMessage 加载框提示内容
// */
//fun <T> BaseViewModel<*>.requestNoCheck(
//    block: suspend () -> T?,
//    success: (T?) -> Unit,
//    error: (ResponseThrowable) -> Unit = {},
//    isShowDialog: Boolean = false,
//    loadingMessage: String = "请求网络中..."
//) {
//    //如果需要弹窗 通知Activity/fragment弹窗
//    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
//    viewModelScope.launch {
//        runCatching {
//            //请求时调度在Io线程中
//            withContext(Dispatchers.IO) { block() }
//        }.onSuccess {
//            //网络请求成功 关闭弹窗
//            loadingChange.dismissDialog.call()
//            //成功回调
//            success(it)
//        }.onFailure {
//            //网络请求异常 关闭弹窗
//            loadingChange.dismissDialog.call()
//            //失败回调
//            error(ExceptionHandle.handleException(it))
//        }
//    }
//}
/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> parseState(
    resultState: ResultState<T>,
    onSuccess: (T?) -> Unit,
    onEmpty: (() -> Unit)? = null,
    onError: ((ResponseThrowable) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            onSuccess(resultState.data)
        }
        is ResultState.Empty -> {
            onEmpty?.run { this }
        }
        is ResultState.Error -> {
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法0
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel<*>.request(
    block: suspend () -> IBaseResponse<T>,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
) {
    if (isShowDialog) resultState.value = ResultState.onLoading(loadingMessage)
    viewModelScope.launch {
        handleException({
            withContext(Dispatchers.IO) { block() }
        }, { res ->
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.call()
            executeResponse(res, success = {
                resultState.paresResult(it)
            }, emptyView = {
                resultState.paresEmpty()
            }
            )
        }, {
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.call()
            if (it.code == -1001) {
                loadingChange.tokenError.postValue(1001)
            }
            resultState.paresException(it)
        })
    }
}

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */

//inline fun BaseViewModel<*>.doSuccessView(crossinline success: (T) -> Unit) =
//    addRequestResult(onSuccess = success)
//
//inline fun BaseViewModel<*>.doEmptyView(crossinline emptyView: () -> Unit) =
//    addRequestResult<T>(onEmpty = emptyView)
//
//inline fun BaseViewModel<*>.doErrorView(crossinline error: (ResponseThrowable) -> Unit) = addRequestResult<T>(
//    onError = error
//)
//
//interface RequestResult<T> {
//    fun onSuccess(any: T)
//    fun onError(error: ResponseThrowable)
//    fun onEmpty()
//}
//
//inline fun <T> addRequestResult(
//    crossinline onSuccess: ((T) -> Unit) = {},
//    crossinline onError: ((ResponseThrowable) -> Unit) = {},
//    crossinline onEmpty: (() -> Unit) = {}
//): RequestResult<T> {
//    return object : RequestResult<T> {
//        override fun onSuccess(any: T) {
//            onSuccess.invoke(any)
//        }
//
//        override fun onError(error: ResponseThrowable) {
//            onError.invoke(error)
//        }
//
//        override fun onEmpty() {
//            onEmpty.invoke()
//        }
//    }
//}
//
// fun <T> BaseViewModel<*>.requestResult(
//     block: suspend () -> IBaseResponse<T>,
//    isShowDialog: Boolean = false) {
//    var  addRequestResult = addRequestResult<T>
//}

fun <T> BaseViewModel<*>.request(
    block: suspend () -> IBaseResponse<T>,
    success: (T) -> Unit,
    error: (ResponseThrowable) -> Unit = {},
    isShowDialog: Boolean = false,
    emptyView: suspend CoroutineScope.() -> Unit = {},
    loadingMessage: String = "请求网络中..."
) {
    //如果需要弹窗 通知Activity/fragment弹窗
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    viewModelScope.launch {
        handleException({
            withContext(Dispatchers.IO) { block() }
        }, { res ->
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.call()
            executeResponse(res, success = {

                success.invoke(it)
            }, emptyView = emptyView)
        }, {
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.call()
            if (it.code == -1001) {
                loadingChange.tokenError.postValue(1001)
            }
            error(ExceptionHandle.handleException(it))
        })
    }
}

/**
 * 异常统一处理
 */
private suspend fun <T> handleException(
    block: suspend CoroutineScope.() -> IBaseResponse<T>,
    success: suspend CoroutineScope.(IBaseResponse<T>) -> Unit,
    error: suspend CoroutineScope.(ResponseThrowable) -> Unit
) {
    coroutineScope {
        try {
            success(block())
        } catch (e: Throwable) {
            error(ExceptionHandle.handleException(e))
        }
    }
}

/**
 * 请求结果过滤
 */
private suspend fun <T> executeResponse(
    response: IBaseResponse<T>,
    emptyView: suspend CoroutineScope.() -> Unit,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        if (response.isSuccess()) {
            val data = response.data()
            if (data == null) {
                emptyView()
            } else {
                success(data)
            }

        } else throw ResponseThrowable(response.code(), response.msg())
    }
}