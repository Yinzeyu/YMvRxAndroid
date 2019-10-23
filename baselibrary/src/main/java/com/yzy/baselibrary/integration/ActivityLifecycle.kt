package com.yzy.baselibrary.integration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yzy.baselibrary.base.BaseActivity

/**
 *description: ActivityLifecycleCallbacks实现.
 *@date 2019/7/15
 *@author: yzy.
 */
class ActivityLifecycle constructor(private val appManager: AppManager) :
    Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity?) {
        if (appManager.getCurrentActivity() == activity) {
            //FIXME 打开第三方页面时AppModule出现null无法强转为FragmentActivity的情况，所以暂时注释掉 by case 2018年10月25日11:36:22
//      appManager.setCurrentActivity(null)
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        activity?.let {
            //FIXME 打开QQ登录授权页非FragmentActivity会导致强转失败,所以当前页面只设置BaseActivity
            if (it is BaseActivity) appManager.setCurrentActivity(it)
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        activity?.let {
            AppBackgroundManager.onActivityStarted(activityName = it.localClassName)
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        activity?.let {
            appManager.removeActivity(it)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        activity?.let {
            AppBackgroundManager.onActivityStopped()
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        activity?.let {
            appManager.addActivity(it)
        }
    }
}