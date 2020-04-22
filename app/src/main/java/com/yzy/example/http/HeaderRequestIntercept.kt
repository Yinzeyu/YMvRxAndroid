package com.yzy.example.http

import com.yzy.example.http.integration.HeaderManger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *description: 描述.
 *@date 2019/7/15
 *@author: yzy.
 */
class HeaderRequestIntercept : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val staticHeaders = HeaderManger.getInstance().getStaticHeaders()
        val dynamicHeaders = HeaderManger.getInstance().getDynamicHeaders()
        for ((key, value) in staticHeaders) {
            builder.addHeader(key, value)
        }
        for ((key, value) in dynamicHeaders) {
            builder.addHeader(key, value)
        }
        return chain.proceed(builder.build())

    }

}
