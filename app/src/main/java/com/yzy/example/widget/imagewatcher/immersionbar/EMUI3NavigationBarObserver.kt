package com.yzy.example.widget.imagewatcher.immersionbar

import android.app.Application
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.yzy.baselibrary.app.BaseApplication
import java.util.*

/**
 * 华为Emui3状态栏监听器
 *
 * @author geyifeng
 * @date 2019/4/10 6:02 PM
 */
internal class EMUI3NavigationBarObserver private constructor() :
    ContentObserver(Handler(Looper.getMainLooper())) {
    private var mCallbacks: ArrayList<ImmersionCallback>? = null
    private var mApplication: Application = BaseApplication.getApp()
    private var mIsRegister = false
    fun register() {
        if (mApplication.contentResolver != null && !mIsRegister) {
            val uri =
                Settings.System.getUriFor(Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW)
            if (uri != null) {
                mApplication.contentResolver.registerContentObserver(uri, true, this)
                mIsRegister = true
            }
        }
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        mCallbacks?.let {
            if ( mApplication.contentResolver != null && it.isNotEmpty()) {
                val show = Settings.System.getInt(
                    mApplication.contentResolver,
                    Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW,
                    0
                )
                    for (callback in it) {
                        callback.onNavigationBarChange(show != 1)
                    }
            }

        }

    }

    fun addOnNavigationBarListener(callback: ImmersionCallback?) {
        if (callback == null) {
            return
        }
        if (mCallbacks == null) {
            mCallbacks = ArrayList()
        }
        mCallbacks?.let {
            if (!it.contains(callback)) {
                it.add(callback)
            }
        }

    }

    fun removeOnNavigationBarListener(callback: ImmersionCallback?) {
        if (callback == null || mCallbacks == null) {
            return
        }
        mCallbacks?.remove(callback)
    }

    private object NavigationBarObserverInstance {
        val INSTANCE = EMUI3NavigationBarObserver()
    }

    companion object {
        val instance: EMUI3NavigationBarObserver = NavigationBarObserverInstance.INSTANCE
    }
}