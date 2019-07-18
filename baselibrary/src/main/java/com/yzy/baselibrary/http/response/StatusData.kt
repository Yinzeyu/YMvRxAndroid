package com.yzy.baselibrary.http.response

/**
 *description: 请求结果的data.
 *@date 2019/7/15
 *@author: yzy.
 */
sealed class StatusData<out T> {
  /**
   * 请求成功
   */
  data class Success<out T>(val data: T?) : StatusData<T>()

  /**
   * 请求失败，无论哪种失败都会返回
   */
  data class Failure<out T>(val throwable: Throwable) : StatusData<T>()
}