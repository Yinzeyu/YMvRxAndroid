package com.yzy.baselibrary.di

import com.yzy.baselibrary.integration.IRepositoryManager
import com.yzy.baselibrary.integration.RepositoryManager
import com.google.gson.Gson
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.integration.AppManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 *description: AppModule.
 *@date 2019/7/15
 *@author: yzy.
 */
const val KODEIN_MODULE_APP_TAG = "appModule"
val appModule = Kodein.Module(KODEIN_MODULE_APP_TAG) {

    //AppManager
    bind<AppManager>() with singleton { AppManager(BaseApplication.INSTANCE) }

    //IRepositoryManager
    bind<IRepositoryManager>() with singleton { RepositoryManager(instance()) }

    //GSON
    bind<Gson>() with singleton { Gson() }
}
