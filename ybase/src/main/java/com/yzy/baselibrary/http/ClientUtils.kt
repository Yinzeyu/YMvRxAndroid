package com.yzy.baselibrary.http

import android.content.Context
import com.blankj.utilcode.util.Utils
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.yzy.baselibrary.BuildConfig
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.http.ClientUtils.inItConfig
import com.yzy.baselibrary.http.ClientUtils.inItGsonBuilder
import com.yzy.baselibrary.http.cookie.PersistentCookieJar
import com.yzy.baselibrary.http.cookie.cache.SetCookieCache
import com.yzy.baselibrary.http.cookie.persistence.SharedPrefsCookiePersistor
import com.yzy.baselibrary.http.interceptor.logging.LoggingInterceptor
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


fun retrofitConfig(config: ClientUtils.InitRetrofitConfig.() -> Unit) {
    val configBean = ClientUtils.InitRetrofitConfig()
    configBean.apply(config)
    inItConfig(configBean)
    inItGsonBuilder()
}

object ClientUtils {
    lateinit var retrofit: Retrofit
    fun inItGsonBuilder() {
        GsonBuilder().serializeNulls()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Double::class.java, JsonSerializer<Double> { src, _, _ ->
                if (src != null && src.equals(src.toLong())) {
                    return@JsonSerializer JsonPrimitive(src.toLong())//解决科学计数法转换的问题
                }
                JsonPrimitive(src)
            })
    }

    fun inItConfig(config: InitRetrofitConfig) {
        config.context?.let {
            val initOkHttp = initOkHttp(config)
            retrofit = initRetrofit(config, initOkHttp)
        }
    }

    private fun initOkHttp(config: InitRetrofitConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(config.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(config.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(config.TIME_OUT, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
        config.interceptors.forEach {
            builder.addInterceptor(it)
        }
        builder.addNetworkInterceptor(
            LoggingInterceptor().apply {
                isDebug = BuildConfig.DEBUG
                level = HttpLoggingInterceptor.Level.BASIC
                type = Platform.INFO
                requestTag = "Request"
                requestTag = "Response"
            })
        //设置缓存配置 缓存最大10M
        builder.cache(Cache(File(BaseApplication.instance().cacheDir, "cxk_cache"), 10 * 1024 * 1024))
        //添加Cookies自动持久化
//        builder.cookieJar(cookieJar)
        //测试服忽略证书校验
        SSLManager.createSSLSocketFactory()?.let {
            builder.sslSocketFactory(it, SSLManager.TrustAllCerts())
            builder.hostnameVerifier(SSLManager.hostnameVerifier)
        }

        return builder.build()
    }

//    private val cookieJar: PersistentCookieJar by lazy {
//        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(Utils.getApp()))
//    }

    private fun initRetrofit(config: InitRetrofitConfig, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())//使用自定义的解析
            .build()
    }

    data class InitRetrofitConfig(
        var context: Context? = null,
        var baseUrl: String = "www.baidu.com",
        var TIME_OUT: Long = 30L,
        val interceptors: ArrayList<Interceptor> = ArrayList()
    )
}

//kotlin实现
class RetrofitAPi private constructor() {
    companion object {
        val instance: RetrofitAPi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitAPi() }
    }

    private val stringRetrofitMap: MutableMap<String, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T> getApi(retrofitClass: Class<T>): T {
        var result: Any?
        synchronized(stringRetrofitMap) {
            result = stringRetrofitMap[retrofitClass.name]
            if (result == null) {
                result = ClientUtils.retrofit.create(retrofitClass)
                stringRetrofitMap[retrofitClass.name] = result as Any
            }
        }
        return result!! as T
    }
}





