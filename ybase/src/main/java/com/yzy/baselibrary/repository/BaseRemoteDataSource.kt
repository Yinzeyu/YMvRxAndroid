package com.yzy.baselibrary.repository

import com.yzy.baselibrary.app.BaseApplication
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import retrofit2.Retrofit

/**
 *description: 远程数据源的基类.
 *@date 2019/7/15
 *@author: yzy.
 */
open class BaseRemoteDataSource : IRemoteDataSource {
    protected var kodein: Kodein = BaseApplication.getApp().kodein
    private val stringRetrofitMap: MutableMap<String, Any> = mutableMapOf()
    private val retrofit: Retrofit by kodein.instance()

    fun <T> getApi(retrofitClass: Class<T>): T {
        var result: Any? = null
        synchronized(stringRetrofitMap) {
            result = stringRetrofitMap[retrofitClass.name]
            if (result == null) {
                result = retrofit.create(retrofitClass)
                stringRetrofitMap[retrofitClass.name] = result as Any
            }
        }
        return result!! as T
    }

    /**
     * 清除缓存中的api的Service
     *
     * @param retrofitClass 接口对应的接口类
     */
    fun <T> clearApi(retrofitClass: Class<T>) {
        synchronized(stringRetrofitMap) {
            if (stringRetrofitMap.containsKey(retrofitClass.name)) {
                stringRetrofitMap.remove(retrofitClass.name)
            }
        }
    }

    /**
     * 清除所有缓存的api的Service
     */
    fun clearAllCache() {
        synchronized(stringRetrofitMap) {
            stringRetrofitMap.clear()
        }
    }
}