package com.yzy.baselibrary.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *description: 全局网络请求的拦截.
 *@date 2019/7/15
 *@author: yzy.
 */
interface GlobeHttpHandler {

    companion object {
        val EMPTY: GlobeHttpHandler = object : GlobeHttpHandler {
            override fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response {
                return response
            }

            override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
                return request
            }
        }
    }

    fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response

    fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request

}