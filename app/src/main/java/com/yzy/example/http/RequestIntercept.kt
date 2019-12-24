package com.yzy.example.http

import android.text.TextUtils
import android.util.Log
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.integration.HeaderManger
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 *description: 描述.
 *@date 2019/7/15
 *@author: yzy.
 */
class RequestIntercept constructor(
    var listNoAddToken: MutableList<String>
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        val staticHeaders = HeaderManger.getInstance().getStaticHeaders()
        val dynamicHeaders = HeaderManger.getInstance().getDynamicHeaders()
        for ((key, value) in staticHeaders) {
            builder.addHeader(key, value)
        }
        val url = request.url.toString()
        var noAddToken = false
        for (i in 0 until listNoAddToken.size) {
            if (url.contains(listNoAddToken[i])) {
                noAddToken = true
                break
            }
        }
        for ((key, value) in dynamicHeaders) {
            if (noAddToken && TextUtils.equals("Authorization", key)) {
            } else {
                builder.addHeader(key, value)
            }
        }

        val headerValues = request.headers("urlName");
        if (headerValues.isNotEmpty()) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader("urlName")
            //匹配获得新的BaseUrl
            val headerValue = headerValues[0]
            var newBaseUrl: HttpUrl? = null
            if ("ganKUrl" == headerValue) {
                newBaseUrl = ApiConstants.Address.GANK_URL.toHttpUrlOrNull()
            }

            //重建新的HttpUrl，修改需要修改的url部分
            val newFullUrl = request.url
                .newBuilder()
                .scheme("https")//更换网络协议
                .host(newBaseUrl?.host ?: request.url.host)//更换主机名
                .port(newBaseUrl?.port ?: request.url.port)//更换端口
                .build()
            //重建这个request，通过builder.url(newFullUrl).build()；
            // 然后返回一个response至此结束修改
            Log.e("Url", "intercept: $newFullUrl");
            request = builder.url(newFullUrl).build()
        }
        request.body?.writeTo( Buffer())
        return chain.proceed(request)

    }

}
