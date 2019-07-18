package com.yzy.commonlibrary.integration

import android.text.TextUtils
import com.yzy.baselibrary.http.GlobeHttpHandler
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * description :
 *@date 2019/7/15
 *@author: yzy.
 */
internal class HeaderHttpHandler : GlobeHttpHandler {
    override fun onHttpResultResponse(
        httpResult: String,
        chain: Interceptor.Chain,
        response: Response
    ): Response {
        return response
    }

    //不需要添加token的接口
    private var listNoAddToken = mutableListOf<String>()

    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
        val builder = request.newBuilder()
        val staticHeaders = HeaderManger.getInstance().getStaticHeaders()
        val dynamicHeaders = HeaderManger.getInstance().getDynamicHeaders()
        for ((key, value) in staticHeaders) {
            builder.addHeader(key, value)
        }
        val url = request.url().toString()
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
        return builder.build()
    }

}