package com.yzy.example.repository

import com.yzy.example.http.response.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import com.yzy.example.repository.bean.DataResult

/**
 * Created by luyao
 * on 2019/4/10 9:41
 */
open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return call.invoke()
    }

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> DataResult<T>,
        errorMessage: String
    ): DataResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            DataResult.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <T : Any> executeResponse(
        response: BaseResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): DataResult<T> {
        return coroutineScope {
            if (response.code == -1) {
                errorBlock?.let { it() }
                DataResult.Error(IOException(response.message))
            } else {
                successBlock?.let { it() }
                DataResult.Success(response.data)
            }
        }
    }


}