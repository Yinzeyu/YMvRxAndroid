package com.yzy.baselibrary.bus

/**
 *description: app级别的事件.
 *@date 2019/7/15
 *@author: yzy.
 */
sealed class AppManagerEvent {

    //打开指定的activity可以是class也可以是Intent
    data class StartActivity(val page: Any) : AppManagerEvent()

    //关闭所有页面退出不会杀死进程
    object KillAllActivity : AppManagerEvent()

    //杀死进程退出
    object ExitApp : AppManagerEvent()

}