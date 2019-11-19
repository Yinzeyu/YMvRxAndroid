package com.yzy.example.comm

import com.tencent.mmkv.MMKV
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.repository.ILocalDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

/**
 *description: 本地缓存的数据源基类.
 *@date 2019/7/15
 *@author: yzy.
 */
open class BaseLocalDataSource :
    ILocalDataSource {

    protected var kodein: Kodein = BaseApplication.getApp().kodein
    //键值对缓存
    protected val mmkv: MMKV by kodein.instance()
}