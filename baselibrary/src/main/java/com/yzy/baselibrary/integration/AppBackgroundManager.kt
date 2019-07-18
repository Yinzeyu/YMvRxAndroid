package com.yzy.baselibrary.integration

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 *description: App前后台切换的管理类.
 *@date 2019/7/15
 *@author: yzy.
 */
object AppBackgroundManager {

  /** 一些状态基用于判断用户是否在前台  */
  private var isAppForeground = false

  private var mActivityStated: Int = 0

  private val STATE_OPEN = 0

  private val STATE_RESUMED = 1

  private val STATE_STOPPED = 2

  private var mListener: IAppStateChangeListener? = null

  private val mLastResume = AtomicBoolean(false)//上一次是否调用resume方法

  private val mMultiStart = AtomicInteger(0)

  private var mLastStartActivityName: String? = null//上一次触发resume的页面

  /**
   * 在Application的onActivityStarted中调用
   */
  fun onActivityStarted(activityName: String) {
    //如果跟上一次是同一个activity，则不认为是多次resume
    val isTheSame = activityName == mLastStartActivityName
    if (!isTheSame && mLastResume.get()) {
      mMultiStart.incrementAndGet()
    }
    mLastStartActivityName = activityName
    mLastResume.set(true)
    if (!isAppForeground) {//如果是切换进前台
      mActivityStated = STATE_OPEN//第一次打开状态
      onAppForegroundStateChange(true)
    } else {
      mActivityStated = STATE_RESUMED
    }
    isAppForeground = true
  }

  /**
   * 在Application的onActivityStopped中调用
   */
  fun onActivityStopped() {//连续两次stop会触发进入后台，如果是程序本身快速关闭两个页面导致的连续stop，需要过滤掉
    if (mMultiStart.get() > 1) {//上一次是stop，且上一次之前有连续多次不同activity的resume
      mMultiStart.decrementAndGet()
      return
    }
    mLastResume.set(false)
    if (mActivityStated == STATE_RESUMED) { //可以理解为最新的Activity在应用内
      mActivityStated = STATE_STOPPED
      return
    }
    if (isAppForeground) {
      mMultiStart.set(0)
      isAppForeground = false
      onAppForegroundStateChange(false)
    }
  }

  fun isAppOnForeground(): Boolean {
    return isAppForeground
  }


  //App前后台切换
  private fun onAppForegroundStateChange(isAppForeground: Boolean) {
    if (mListener == null) {
      return
    }
    mListener!!.onAppStateChanged(isAppForeground)
  }

  fun setAppStateListener(listener: IAppStateChangeListener) {
    mListener = listener
  }

  interface IAppStateChangeListener {

    fun onAppStateChanged(isAppForceground: Boolean)
  }

}