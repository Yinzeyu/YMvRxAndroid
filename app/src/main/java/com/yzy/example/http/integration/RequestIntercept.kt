package com.yzy.example.http.integration

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *description: 描述.
 *@date 2019/7/15
 *@author: yzy.
 */
class RequestIntercept : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        var newBaseUrl: HttpUrl? = null
        val dynamicBaseUrl = HeaderManger.getInstance().getDynamicBaseUrl()
        val headerValues = request.headers("urlName")
        if ((null != dynamicBaseUrl && "" != dynamicBaseUrl) || headerValues.isNotEmpty()) {
            if (headerValues.isNotEmpty()) {
                //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                builder.removeHeader("urlName")
                //匹配获得新的BaseUrl
                val headerValue = headerValues[0]
                val headerUrlValue = "Domain"
                if (headerValue.contains(headerUrlValue)) {
                    val split = headerValue.substring(headerUrlValue.length)
                    if (split.isNotEmpty()) {
                        newBaseUrl = split.toHttpUrlOrNull()
                    }
                }
            } else {
                newBaseUrl = dynamicBaseUrl?.toHttpUrlOrNull()
                HeaderManger.getInstance().setDynamicBaseUrl(null)
            }
            newBaseUrl?.let {
                val newFullUrl = request.url.newBuilder()
                    .scheme(it.scheme)//更换网络协议
                    .host(it.host)//更换主机名
                    .port(it.port)//更换端口
                    .build()
                Log.e("Url", "intercept: $newFullUrl")
                //获取处理后的新newRequest
                val newRequest: Request = builder.url(newFullUrl).build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(builder.build())

    }

}