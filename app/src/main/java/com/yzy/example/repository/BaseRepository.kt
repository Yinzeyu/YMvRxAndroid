package com.yzy.example.repository

import com.yzy.example.http.response.ApiException
import com.yzy.example.http.response.BaseResponse
import com.yzy.example.http.response.EmptyException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import com.yzy.example.repository.bean.DataResult
import com.yzy.example.repository.bean.GankBaseBean

/**
 * Created by luyao
 * on 2019/4/10 9:41
 */
open class BaseRepository {
    suspend fun <T : Any> executeResponse(
        response: BaseResponse<T>
    ): DataResult<T> {
        return try {
            return coroutineScope {
                if (response.code != 0) {
                    DataResult.Error(ApiException(response.code,response.message))
                } else {
                    if (response.data == null )
                        DataResult.Error(EmptyException(response.code,response.message))
                    else
                        DataResult.Success(response.data)
                }
            }
        } catch (e: Exception) {
            DataResult.Error(ApiException(response.code,response.message))
        }
    }

    suspend fun <T : Any> executeResponse1(
        response: GankBaseBean<T>
    ): DataResult<T> {
        return try {
            return coroutineScope {
                if (response.error) {
                    DataResult.Error(ApiException(0,response.errorMsg))
                } else {
                    if (response.results == null )
                        DataResult.Error(EmptyException(0,response.errorMsg))
                    else
                        DataResult.Success(response.results)
                }
            }
        } catch (e: Exception) {
            DataResult.Error(ApiException(0,response.errorMsg))
        }
    }
}