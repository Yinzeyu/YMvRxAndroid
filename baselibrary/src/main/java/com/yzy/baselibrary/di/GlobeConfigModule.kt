package com.yzy.baselibrary.di

import android.os.Environment
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.http.GlobeHttpHandler
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.io.File
import java.util.*

/**
 *description: GlobeConfigModule.
 *@date 2019/7/15
 *@author: yzy.
 */
const val KODEIN_MODULE_GLOBECINFIG_TAG = "globeConfigModule"

const val KODEIN_TAG_FILE_CACHEDIR = "cacheFileDir"

class GlobeConfigModule private constructor(builder: Builder) {
    private lateinit var mApiUrl: HttpUrl
    private lateinit var mHandler: GlobeHttpHandler
    private lateinit var mInterceptors: List<Interceptor>
    private lateinit var mCacheFile: File
    val globeConfigModule = Kodein.Module(KODEIN_MODULE_GLOBECINFIG_TAG) {

        bind<HttpUrl>() with singleton {
            mApiUrl
        }

        bind<List<Interceptor>>() with singleton { mInterceptors }

        bind<GlobeHttpHandler>() with singleton { mHandler }

        bind<File>(KODEIN_TAG_FILE_CACHEDIR) with singleton {
            mCacheFile
        }
    }

    init {
        this.mApiUrl = builder.baseUrl ?: throw IllegalArgumentException("baseUrl can not be empty")
        this.mHandler = builder.handler ?: GlobeHttpHandler.EMPTY
        this.mInterceptors = builder.interceptors
        this.mCacheFile = builder.cacheFile
            ?: if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                var file: File? = BaseApplication.INSTANCE.externalCacheDir//获取系统管理的sd卡缓存文件
                if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                    val cacheFilePath =
                        Environment.getDataDirectory().path + BaseApplication.INSTANCE.packageName
                    file = File(cacheFilePath)
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                }
                file
            } else {
                BaseApplication.INSTANCE.cacheDir
            }
    }

    class Builder {

        var baseUrl: HttpUrl? = null
        var handler: GlobeHttpHandler? = null
        val interceptors = ArrayList<Interceptor>()
        var cacheFile: File? = null

        fun baseUrl(baseUrl: String): Builder {
            if (baseUrl.isBlank()) {
                throw IllegalArgumentException("baseUrl can not be empty")
            }
            this.baseUrl = baseUrl.toHttpUrlOrNull()
            return this
        }

        fun globeHttpHandler(handler: GlobeHttpHandler): Builder {//用来处理http响应结果
            this.handler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {//动态添加任意个interceptor
            this.interceptors.add(interceptor)
            return this
        }


        fun cacheFile(cacheFile: File): Builder {
            this.cacheFile = cacheFile
            return this
        }

        fun build(): GlobeConfigModule {
            return GlobeConfigModule(this)
        }


    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

}