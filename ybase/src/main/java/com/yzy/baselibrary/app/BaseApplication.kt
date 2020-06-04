package com.yzy.baselibrary.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.yzy.baselibrary.BuildConfig
import com.yzy.baselibrary.utils.autosize.AutoSizeConfig

abstract class BaseApplication : Application(), ViewModelStoreOwner {
    var launcherTime = 0L
    private lateinit var mAppViewModelStore: ViewModelStore
    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        Utils.init(this)
        this.baseInitCreate()
        if (ProcessUtils.isMainProcess()) {
            initInMainProcess()
        }
    }

    //主进程中的初始化
    @SuppressLint("CheckResult")
    private fun initInMainProcess() {
        LogUtils.getConfig()
            .setLogSwitch(BuildConfig.DEBUG)//log开关
            .setGlobalTag("BaseLib")
            .stackDeep = 3//log栈
        //主线程中的初始化(必要的放在这,不然APP打开会比较慢)
        initInMainThread()
        //字体sp不跟随系统大小变化
        val instance = AutoSizeConfig.getInstance()
        instance.isUseDeviceSize = false
        instance.isExcludeFontScale = true
        instance .init(this)
    }

    //主线程中的初始化(只在主进程中调用)
    abstract fun initInMainThread()

    abstract fun baseInitCreate()

    companion object {
        fun instance(): BaseApplication {
            return Utils.getApp() as BaseApplication
        }
    }
    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        launcherTime = System.currentTimeMillis()
    }

}
