package com.yzy.example.repository

import com.yzy.example.http.response.ApiException
import com.yzy.example.repository.bean.GankBaseBean
import io.reactivex.functions.Function

/**
 *description: 服务器返回的接口是否成功.
 *@date 2019/7/15
 *@author: yzy.
 */
class IsGankSuccessFunc<T> : Function<GankBaseBean<T>, MutableList<T>> {
    @Throws(Exception::class)
    override fun apply(bean: GankBaseBean<T>): MutableList<T> {
        return if (bean.error) {
            throw ApiException(400, "接口请求异常")
        } else {
            bean.results
        }
    }
}