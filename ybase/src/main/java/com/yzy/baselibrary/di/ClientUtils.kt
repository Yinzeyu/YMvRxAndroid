package com.yzy.baselibrary.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.yzy.baselibrary.di.ClientUtils.inItConfig
import com.yzy.baselibrary.di.ClientUtils.inItGsonBuilder
import com.yzy.baselibrary.http.ssl.SSLManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.concurrent.TimeUnit


fun RetrofitConfig(config: ClientUtils.InitRetrofitConfig.() -> Unit) {
    val configBean = ClientUtils.InitRetrofitConfig()
    configBean.apply(config)
    inItConfig(configBean)
    inItGsonBuilder()
}

//kotlin实现
class RetrofitAPi private constructor() {
    companion object {
        val instance: RetrofitAPi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitAPi() }
    }
    private val stringRetrofitMap: MutableMap<String, Any> = mutableMapOf()
    lateinit var retrofit: Retrofit
    @Suppress("UNCHECKED_CAST")
    fun <T> getApi(retrofitClass: Class<T>): T {
        var result: Any?
        synchronized(stringRetrofitMap) {
            result = stringRetrofitMap[retrofitClass.name]
            if (result == null) {
                result = retrofit.create(retrofitClass)
                stringRetrofitMap[retrofitClass.name] = result as Any
            }
        }
        return result!! as T
    }
}

object ClientUtils {
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
            val initRetrofit = initRetrofit(config, initOkHttp)
            RetrofitAPi.instance.retrofit = initRetrofit
        }
    }

    private fun initOkHttp(config: InitRetrofitConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(config.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(config.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(config.TIME_OUT, TimeUnit.SECONDS)
        config.interceptors.forEach {
            builder.addInterceptor(it)
        }
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })//log
        //测试服忽略证书校验
        SSLManager.createSSLSocketFactory()?.let {
            builder.sslSocketFactory(it, SSLManager.TrustAllCerts())
            builder.hostnameVerifier(SSLManager.hostnameVerifier)
        }
        return builder.build()
    }

    private fun initRetrofit(config: InitRetrofitConfig, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用rxjava
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



