package com.yzy.baselibrary.integration

/**
 *description: RepositoryManager接口.
 *@date 2019/7/15
 *@author: yzy.
 */
interface IRepositoryManager {

  //注入RetrofitService
  fun injectRetrofitService(vararg services: Class<*>)

  //注入CacheService
  fun injectCacheService(vararg services: Class<*>)

  /**
   * 根据传入的Class获取对应的Retrofit service
   */
  fun <T> obtainRetrofitService(service: Class<T>): T

  /**
   * 根据传入的Class获取对应的RxCache service
   */
  fun <T> obtainCacheService(cache: Class<T>): T
}