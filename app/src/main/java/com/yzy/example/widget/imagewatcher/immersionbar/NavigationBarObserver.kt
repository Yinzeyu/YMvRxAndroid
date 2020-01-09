package com.yzy.example.widget.imagewatcher.immersionbar

import android.app.Application
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import java.util.*

/**
 * 导航栏显示隐藏处理，目前只支持emui和miui带有导航栏的手机
 *
 * @author geyifeng
 * @date 2019/4/10 6:02 PM
 */
internal class NavigationBarObserver private constructor() :
    ContentObserver(Handler(Looper.getMainLooper())) {
    private var mListeners: ArrayList<OnNavigationBarListener>? = null
    private var mApplication: Application? = null
    private var mIsRegister = false
    fun register(application: Application?) {
        mApplication = application
        if ( mApplication != null && mApplication!!.contentResolver != null && !mIsRegister
        ) {
            var uri: Uri? = null
            if (OSUtils.isMIUI) {
                uri =
                    Settings.Global.getUriFor(Constants.IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW)
            } else if (OSUtils.isEMUI) {
                uri =
                    if (OSUtils.isEMUI3_x) {
                        Settings.System.getUriFor(Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW)
                    } else {
                        Settings.Global.getUriFor(Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW)
                    }
            }
            if (uri != null) {
                mApplication!!.contentResolver.registerContentObserver(uri, true, this)
                mIsRegister = true
            }
        }
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        if (  mApplication != null && mApplication!!.contentResolver != null && mListeners != null && !mListeners!!.isEmpty()
        ) {
            var show = 0
            if (OSUtils.isMIUI) {
                show = Settings.Global.getInt(
                    mApplication!!.contentResolver,
                    Constants.IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW,
                    0
                )
            } else if (OSUtils.isEMUI) {
                show =
                    if (OSUtils.isEMUI3_x ) {
                        Settings.System.getInt(
                            mApplication!!.contentResolver,
                            Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW,
                            0
                        )
                    } else {
                        Settings.Global.getInt(
                            mApplication!!.contentResolver,
                            Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW,
                            0
                        )
                    }
            }
            for (onNavigationBarListener in mListeners!!) {
                onNavigationBarListener.onNavigationBarChange(show != 1)
            }
        }
    }

    fun addOnNavigationBarListener(listener: OnNavigationBarListener?) {
        if (listener == null) {
            return
        }
        if (mListeners == null) {
            mListeners = ArrayList()
        }
        if (!mListeners!!.contains(listener)) {
            mListeners!!.add(listener)
        }
    }

    fun removeOnNavigationBarListener(listener: OnNavigationBarListener?) {
        if (listener == null || mListeners == null) {
            return
        }
        mListeners!!.remove(listener)
    }

    private object NavigationBarObserverInstance {
        val INSTANCE = NavigationBarObserver()
    }

    companion object {
        val instance: NavigationBarObserver=NavigationBarObserverInstance.INSTANCE
    }
}