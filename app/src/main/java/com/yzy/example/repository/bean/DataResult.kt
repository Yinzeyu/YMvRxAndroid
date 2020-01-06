package com.yzy.example.repository.bean

/**
 * Created by luyao
 * on 2019/10/12 11:08
 */
sealed class DataResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}