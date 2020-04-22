package com.yzy.example.http

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.integration.HeaderManger
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 *description: 描述.
 *@date 2019/7/15
 *@author: yzy.
 */

//class RequestIntercept : Interceptor {
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//        val builder = request.newBuilder()
//        var newBaseUrl: HttpUrl? = null
//        val staticHeaders = HeaderManger.getInstance().getStaticHeaders()
//        val dynamicBaseUrl = HeaderManger.getInstance().getDynamicBaseUrl()
//        for ((key, value) in staticHeaders) {
//            builder.addHeader(key, value)
//        }
//        val headerValues = request.headers("urlName")
//        if ((null != dynamicBaseUrl && "" != dynamicBaseUrl) || headerValues.isNotEmpty()) {
//            var requestForm:String?=null
//            var dynamicUrl:String?=null
//            val contains = headerValues[0].contains("&&")
//            if (contains){
//                val split = headerValues[0].split("&&")
//                split.forEach {
//                    if (it == "requestJson"){
//                        requestForm = it
//                    }else if (it.contains("Domain")){
//                        dynamicUrl=it
//                    }
//                }
//            }else{
//                val isDomain = headerValues[0].contains("Domain")
//                val isRequestJson = headerValues[0].contains("requestJson")
//                when {
//                    isDomain -> {
//                        dynamicUrl=headerValues[0]
//                    }
//                    isRequestJson -> {
//                        requestForm=headerValues[0]
//                    }
//                    else -> {
//                        requestForm=null
//                        dynamicUrl =null
//                    }
//                }
//            }
//            builder.removeHeader("urlName")
//            if (dynamicUrl != null || HeaderManger.getInstance().getDynamicBaseUrl() != null) {
//                if (dynamicUrl != null) {
//                    //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
//                    dynamicUrl?.let {
//                        //匹配获得新的BaseUrl
//                        val headerUrlValue = "Domain"
//                        if (it.contains(headerUrlValue)) {
//                            val split = it.substring(headerUrlValue.length)
//                            if (split.isNotEmpty()) {
//                                newBaseUrl = split.toHttpUrlOrNull()
//                            }
//                        }
//                    }
//                } else if (HeaderManger.getInstance().getDynamicBaseUrl() != null) {
//                    newBaseUrl = dynamicBaseUrl?.toHttpUrlOrNull()
//                    HeaderManger.getInstance().setDynamicBaseUrl(null)
//                }
//            }
//            val https = request.url.isHttps
//            //重建新的HttpUrl，修改需要修改的url部分
//            val newFullUrl = request.url
//                .newBuilder()
//                .scheme(if (https)"https" else "http")//更换网络协议
//                .host(newBaseUrl?.host ?: request.url.host)//更换主机名
//                .port(newBaseUrl?.port ?: request.url.port)//更换端口
//                .build()
//            Log.e("Url", "intercept: $newFullUrl")
//            if (requestForm != null){
//                builder.removeHeader("Content-Type")
//                builder.addHeader("Content-Type", "application/json;charset=utf-8")
//                val map: HashMap<String, String> = HashMap()
//                if (request.body is FormBody) {
//                    val size = (request.body as FormBody).size
//                    for (index   in 0 until size) {
//                        val value = (request.body as FormBody).value(index)
//                        val name = (request.body as FormBody).name(index)
//                        map[name] = value
//                    }
//                    val strEntity: String = Gson().toJson(map)
//                    val body =strEntity.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
//                    val build = builder.url(newFullUrl)
//                    build.method(request.method,body)
//                    request =  build.build()
//                } else {
//                    request = builder.url(newFullUrl).build()
//                }
//            }else{
//                request = builder.url(newFullUrl).build()
//            }
//        }
//        return chain.proceed(request)
//    }
//}


//
//class RequestIntercept : Interceptor {
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//        val builder = request.newBuilder()
//        var newBaseUrl: HttpUrl? = null
//        val staticHeaders = HeaderManger.getInstance().getStaticHeaders()
//        val dynamicBaseUrl = HeaderManger.getInstance().getDynamicBaseUrl()
//        for ((key, value) in staticHeaders) {
//            builder.addHeader(key, value)
//        }
//        val headerValues = request.headers("urlName")
//        if ((null !=dynamicBaseUrl  && "" != dynamicBaseUrl)|| headerValues.isNotEmpty()) {
//            if (headerValues.isNotEmpty()) {
//                //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
//                builder.removeHeader("urlName")
//                //匹配获得新的BaseUrl
//                val headerValue = headerValues[0]
//                val headerUrlValue = "Domain"
//                if (headerValue.contains(headerUrlValue)) {
//                    val split = headerValue.substring(headerUrlValue.length)
//                    if (split.isNotEmpty()){
//                        newBaseUrl = split.toHttpUrlOrNull()
//                    }
//                }
//            } else {
//                newBaseUrl = dynamicBaseUrl?.toHttpUrlOrNull()
//                HeaderManger.getInstance().setDynamicBaseUrl(null)
//            }
//            //重建新的HttpUrl，修改需要修改的url部分
//            val newFullUrl = request.url
//                .newBuilder()
//                .scheme("https")//更换网络协议
//                .host(newBaseUrl?.host ?: request.url.host)//更换主机名
//                .port(newBaseUrl?.port ?: request.url.port)//更换端口
//                .build()
//            Log.e("Url", "intercept: $newFullUrl")
//            request = builder.url(newFullUrl).build()
//        }
////        request.body?.writeTo(Buffer())
//        return chain.proceed(request)
//
//    }
//
//}

class RequestIntercept : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        var newBaseUrl: HttpUrl? = null
        val staticHeaders = HeaderManger.getInstance().getStaticHeaders()
        val dynamicHeaders = HeaderManger.getInstance().getDynamicHeaders()
        val dynamicBaseUrl = HeaderManger.getInstance().getDynamicBaseUrl()
        for ((key, value) in staticHeaders) {
            builder.addHeader(key, value)
        }

        for ((key, value) in dynamicHeaders) {
            builder.addHeader(key, value)
        }

        val headerValues = request.headers("urlName")
        if ((null !=dynamicBaseUrl  && "" != dynamicBaseUrl)|| headerValues.isNotEmpty()) {
            if (headerValues.isNotEmpty()) {
                //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                builder.removeHeader("urlName")
                //匹配获得新的BaseUrl
                val headerValue = headerValues[0]
                val headerUrlValue = "Domain"
                if (headerValue.contains(headerUrlValue)) {
                    val split = headerValue.substring(headerUrlValue.length)
                    if (split.isNotEmpty()){
                        newBaseUrl = split.toHttpUrlOrNull()
                    }
                }
            } else {
                newBaseUrl = dynamicBaseUrl?.toHttpUrlOrNull()
                HeaderManger.getInstance().setDynamicBaseUrl(null)
            }
            //重建新的HttpUrl，修改需要修改的url部分
            val newFullUrl = request.url
                .newBuilder()
                .scheme("https")//更换网络协议
                .host(newBaseUrl?.host ?: request.url.host)//更换主机名
                .port(newBaseUrl?.port ?: request.url.port)//更换端口
                .build()
            Log.e("Url", "intercept: $newFullUrl")
            request = builder.url(newFullUrl).build()
        }
//        request.body?.writeTo(Buffer())
        return chain.proceed(request)

    }

}
