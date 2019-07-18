package com.yzy.baselibrary.app

import android.app.Application

/**
 *description: Application的生命周期的回调.
 *@date 2019/7/15
 *@author: yzy.
 */
interface AppLifecycle {

  fun onCreate(application: Application)

  fun onTerminate(application: Application?)

}