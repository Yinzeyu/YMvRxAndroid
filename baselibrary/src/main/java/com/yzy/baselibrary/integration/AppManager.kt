package com.yzy.baselibrary.integration

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.yzy.baselibrary.bus.AppManagerEvent
import com.yzy.baselibrary.bus.RxBus
import io.reactivex.Flowable
import io.reactivex.subscribers.DefaultSubscriber
import java.util.*

/**
 *description: AppManager.
 *@date 2019/7/15
 *@author: yzy.
 */

class AppManager constructor(var application: Application?) {
    //管理所有activity
    var mActivityList: MutableList<Activity>?
    private var mAppManagerEventFlowable: Flowable<AppManagerEvent> = RxBus.getInstance()
        .toFlowableMainThreadBackpressure(AppManagerEvent::class.java)
    //当前在前台的activity
    private var mCurrentActivity: Activity? = null

    init {
        mActivityList = LinkedList()
        mAppManagerEventFlowable
            .subscribeWith(object : DefaultSubscriber<AppManagerEvent>() {
                override fun onNext(appManagerEvent: AppManagerEvent) {
                    when (appManagerEvent) {
                        is AppManagerEvent.StartActivity -> {
                            dispatchStart(appManagerEvent.page)
                        }
                        is AppManagerEvent.KillAllActivity -> {
                            killAll()
                        }
                        is AppManagerEvent.ExitApp -> {
                            appExit()
                        }
                    }
                }

                override fun onError(t: Throwable) {
                }

                override fun onComplete() {
                }
            })
    }


    /**
     * 打开activity
     */
    private fun dispatchStart(page: Any) {
        if (page is Intent) {
            startActivity(page)
        } else if (page is Class<*>) {
            startActivity(page)
        }
    }


    /**
     * 让在前台的activity,打开下一个activity
     */
    fun startActivity(intent: Intent) {
        if (getCurrentActivity() == null) {
            //如果没有前台的activity就使用new_task模式启动activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            application?.startActivity(intent)
            return
        }
        getCurrentActivity()!!.startActivity(intent)
    }

    /**
     * 让在前台的activity,打开下一个activity
     */
    fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(application, activityClass))
    }

    /**
     * 释放资源
     */
    fun release() {
        mActivityList?.clear()
        mActivityList = null
        mCurrentActivity = null
        application = null
    }

    /**
     * 获得当前在前台的activity
     */
    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    /**
     * 将在前台的activity保存
     */
    fun setCurrentActivity(currentActivity: Activity?) {
        this.mCurrentActivity = currentActivity
    }

    /**
     * 返回一个存储所有未销毁的activity的集合
     */
    fun getActivityList(): MutableList<Activity> {
        if (mActivityList == null) {
            mActivityList = LinkedList()
        }
        return mActivityList!!
    }

    /**
     * 添加Activity到集合
     */
    fun addActivity(activity: Activity) {
        if (mActivityList == null) {
            mActivityList = LinkedList()
        }
        synchronized(AppManager::class.java) {
            mActivityList?.let {
                if (!it.contains(activity)) {
                    it.add(activity)
                }
            }
        }
    }

    /**
     * 删除集合里的指定activity
     */
    fun removeActivity(activity: Activity) {
        mActivityList?.let {
            synchronized(AppManager::class.java) {
                if (it.contains(activity)) {
                    it.remove(activity)
                }
            }
        }
    }

    /**
     * 删除集合里的指定位置的activity
     */
    fun removeActivity(location: Int): Activity? {
        synchronized(AppManager::class.java) {
            mActivityList?.let {
                if (location > 0 && location < it.size) {
                    return it.removeAt(location)
                }
            }
        }
        return null
    }

    /**
     * 关闭指定activity
     */
    fun killActivity(activityClass: Class<*>) {
        mActivityList?.let {
            for (activity in it) {
                if (activity.javaClass == activityClass) {
                    activity.finish()
                }
            }
        }
    }


    /**
     * 指定的activity实例是否存活
     */
    fun activityInstanceIsLive(activity: Activity): Boolean {
        var isAlive = false
        mActivityList?.let {
            isAlive = it.contains(activity)
        }
        return isAlive
    }


    /**
     * 指定的activity class是否存活(一个activity可能有多个实例)
     */
    fun activityClassIsLive(activityClass: Class<*>): Boolean {
        var isAlive = false
        mActivityList?.let {
            for (activity in it) {
                if (activity.javaClass == activityClass) {
                    isAlive = true
                    break
                }
            }
        }
        return isAlive
    }


    /**
     * 关闭所有activity
     */
    fun killAll() {
        val iterator = getActivityList().iterator()
        while (iterator.hasNext()) {
            iterator.next().finish()
            iterator.remove()
        }
    }

    /**
     * 关闭除指定的activity以外所有activity
     */
    fun killAllWithout(activityClass: Class<*>) {
        val iterator = getActivityList().iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity.javaClass != activityClass) {
                activity.finish()
            }
        }
    }


    /**
     * 退出应用程序
     */
    fun appExit() {
        try {
            killAll()
            if (mActivityList != null) {
                mActivityList = null
            }
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}