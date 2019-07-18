package com.yzy.baselibrary.integration

import io.rx_cache2.internal.RxCache
import retrofit2.Retrofit
import java.util.*

/**
 *description: 用来管理网络请求层,以及数据缓存层,以后可以添加数据库请求层,需要在ConfigModule的实现类中先inject需要的服务
 *@date 2019/7/15
 *@author: yzy.
 */
class RepositoryManager constructor(private val retrofit: Retrofit, private val rxCache: RxCache) :
    IRepositoryManager {

  private val mRetrofitServiceCache = LinkedHashMap<String, Any>()
  private val mCacheServiceCache = LinkedHashMap<String, Any>()

  /**
   * 注入RetrofitService
   */
  override fun injectRetrofitService(vararg services: Class<*>) {
    for (service in services) {
      if (mRetrofitServiceCache.containsKey(service.name)) {
        continue
      }
      mRetrofitServiceCache[service.name] = retrofit.create(service)
    }
  }

  /**
   * 注入CacheService
   */
  override fun injectCacheService(vararg services: Class<*>) {
    for (service in services) {
      if (mCacheServiceCache.containsKey(service.name)) {
        continue
      }
      mCacheServiceCache[service.name] = rxCache.using(service)
    }
  }

  /**
   * 根据传入的Class获取对应的Retrofit service
   */
  @Suppress("UNCHECKED_CAST")
  override fun <T> obtainRetrofitService(service: Class<T>): T {
    return mRetrofitServiceCache[service.name] as T
  }

  /**
   * 根据传入的Class获取对应的RxCache service
   */
  @Suppress("UNCHECKED_CAST")
  override fun <T> obtainCacheService(cache: Class<T>): T {
    return mCacheServiceCache[cache.name] as T
  }
}