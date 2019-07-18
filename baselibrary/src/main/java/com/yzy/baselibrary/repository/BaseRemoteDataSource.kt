package com.yzy.baselibrary.repository

import com.yzy.baselibrary.integration.IRepositoryManager
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
  protected var kodein: Kodein = BaseApplication.INSTANCE.kodein

  private val repositoryManager: IRepositoryManager by kodein.instance()

  protected val retrofit: Retrofit by kodein.instance()

  fun <T> retrofitService(service: Class<T>): T = repositoryManager.obtainRetrofitService(service)
}