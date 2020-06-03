package com.yzy.example.http.integration

import com.blankj.utilcode.util.LogUtils
import com.yzy.example.utils.MMkvUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ReceivedCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        originalResponse.headers.toMultimap().filter { it.key.contains("cookie") } .forEach { map ->
            for (value in map.value) {
                if (value.contains("JSESSIONID", true)) {
                    LogUtils.e(value)
                    MMkvUtils.instance.setToken(value)
                }
            }
        }
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
           MMkvUtils.instance.setCookie(cookies)
        }
        return originalResponse
    }
}