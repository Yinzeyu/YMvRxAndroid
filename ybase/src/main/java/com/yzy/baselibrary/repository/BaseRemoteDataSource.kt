package com.yzy.baselibrary.repository

import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.di.RetrofitAPi
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import retrofit2.Retrofit

/**
 *description: 远程数据源的基类.
 *@date 2019/7/15
 *@author: yzy.
 */
open class BaseRemoteDataSource : IRemoteDataSource