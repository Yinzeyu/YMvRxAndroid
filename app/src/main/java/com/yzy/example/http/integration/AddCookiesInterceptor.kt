package com.yzy.example.http.integration

import android.util.Log
import com.yzy.example.utils.MMkvUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        MMkvUtils.instance.getCookie()?.let {preferences->
            for (cookie in preferences) {
                builder.addHeader("Cookie", cookie)
                Log.v("OkHttp", "Adding Header: $cookie") // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }

        }
        return chain.proceed(builder.build())
    }
}