package com.yzy.baselibrary.imageloader.glide

import android.text.TextUtils
import com.yzy.baselibrary.http.ssl.SSLManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.*
import java.util.concurrent.TimeUnit
import java.io.IOException


/**
 * description: Glide配置的OkHttpClick.
 *@date 2019/7/15
 *@author: yzy.
 */
object GlideHttpClientManager {
    private val listenersMap =
        Collections.synchronizedMap(HashMap<String, OnImageProgressListener>())
    private val LISTENER = object : ProgressResponseBody.InternalProgressListener {
        override fun onProgress(url: String, bytesRead: Long, totalBytes: Long) {
            val onProgressListener = getProgressListener(url)
            if (onProgressListener != null) {
                val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
                val isComplete = percentage >= 100
                onProgressListener.onProgress(url, isComplete, percentage, bytesRead, totalBytes)
                if (isComplete) {
                    removeListener(url)
                }
            }
        }
    }
    val okHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.addNetworkInterceptor(NetworkInterceptor())
            SSLManager.createSSLSocketFactory()?.let {
                builder.sslSocketFactory(
                    it,
                    SSLManager.TrustAllCerts()
                )
            }
            builder.hostnameVerifier(SSLManager.hostnameVerifier)
            builder.connectTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            return builder.build()
        }

    fun addListener(url: String, listener: OnImageProgressListener?) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap[url] = listener
            listener.onProgress(url, false, 1, 0, 0)
        }
    }

    fun removeListener(url: String) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap.remove(url)
        }
    }

    fun getProgressListener(url: String): OnImageProgressListener? {
        return if (TextUtils.isEmpty(url) || listenersMap.isEmpty()) {
            null
        } else listenersMap[url]
    }

    class NetworkInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            response.newBuilder()
                .body(
                    ProgressResponseBody(
                        request.url.toString(),
                        LISTENER,
                        response.body!!
                    )
                )
                .build()
            return response
        }
    }
}