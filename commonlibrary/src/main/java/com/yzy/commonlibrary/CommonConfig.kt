package com.yzy.commonlibrary

import android.app.Application
import android.content.Context
import com.yzy.baselibrary.app.AppLifecycle
import com.yzy.baselibrary.di.GlobeConfigModule
import com.yzy.baselibrary.http.RequestIntercept
import com.yzy.baselibrary.integration.ConfigModule
import com.yzy.baselibrary.integration.IRepositoryManager
import com.yzy.commonlibrary.integration.HeaderHttpHandler
import com.yzy.commonlibrary.repository.service.GankService

/**
 * description :
 *
 *@date 2019/7/15
 *@author: yzy.
 */
class CommonConfig : ConfigModule {
    override fun applyOptions(context: Context, builder: GlobeConfigModule.Builder) {
        builder.baseUrl("https://gank.io/api/")//
        builder.addInterceptor(RequestIntercept(HeaderHttpHandler()))
    }

    override fun registerComponents(context: Context, repositoryManager: IRepositoryManager) {
        //config
        repositoryManager.injectRetrofitService(GankService::class.java)
    }

    override fun injectAppLifecycle(context: Context, lifecycles: ArrayList<AppLifecycle>) {
        val commAppLifecycle: AppLifecycle = object :
            AppLifecycle {
            override fun onCreate(application: Application) {
            }

            override fun onTerminate(application: Application?) {
            }
        }
        lifecycles.add(commAppLifecycle)
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: ArrayList<Application.ActivityLifecycleCallbacks>
    ) {
    }
}