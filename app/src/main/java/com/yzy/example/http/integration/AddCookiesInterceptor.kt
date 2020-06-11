package com.yzy.example.http.integration

import com.yzy.example.utils.MMkvUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        MMkvUtils.instance.getCookie()?.let { preferences ->
            for (cookie in preferences) {
                builder.addHeader("Cookie", cookie)
            }

        }
        return chain.proceed(builder.build())
    }
}