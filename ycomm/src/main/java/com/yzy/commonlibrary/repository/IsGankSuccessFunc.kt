package com.yzy.commonlibrary.repository

import com.yzy.commonlibrary.http.response.ApiException
import com.yzy.commonlibrary.repository.bean.GankBaseBean
import io.reactivex.functions.Function

/**
 *description: 服务器返回的接口是否成功.
 *@date 2019/7/15
 *@author: yzy.
 */
class IsGankSuccessFunc<T> : Function<GankBaseBean<T>, List<T>> {
  @Throws(Exception::class)
  override fun apply(bean: GankBaseBean<T>): List<T> {
    return if (bean.error) {
      throw ApiException(400, "接口请求异常")
    } else {
      bean.results
    }
  }
}