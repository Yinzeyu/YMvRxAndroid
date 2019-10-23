package com.yzy.baselibrary.repository

import com.tencent.mmkv.MMKV
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.integration.IRepositoryManager
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

/**
 *description: 本地缓存的数据源基类.
 *@date 2019/7/15
 *@author: yzy.
 */
open class BaseLocalDataSource : ILocalDataSource {

    protected var kodein: Kodein = BaseApplication.INSTANCE.kodein

    private val repositoryManager: IRepositoryManager by kodein.instance()

    //键值对缓存
    protected val mmkv: MMKV by kodein.instance()


    /**
     * 获取RxCache的Service
     */
    fun  cacheService( service: Class<*>) = repositoryManager.obtainCacheService(service)


}