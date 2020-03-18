package com.yzy.baselibrary.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.yzy.baselibrary.BuildConfig
import me.jessyan.autosize.AutoSizeConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.logging.Level

abstract class BaseApplication : Application() {
    var launcherTime = 0L

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(org.koin.core.logger.Level.DEBUG)
            androidContext(this@BaseApplication)
            androidFileProperties()
//            modules(appModule)
        }
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
        AutoSizeConfig.getInstance().isExcludeFontScale = true
    }

    //主线程中的初始化(只在主进程中调用)
    abstract fun initInMainThread()

    abstract fun baseInitCreate()

    companion object {
        fun getApp(): BaseApplication {
            return Utils.getApp() as BaseApplication
        }

        fun instance(): BaseApplication {
            return Utils.getApp() as BaseApplication
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        launcherTime = System.currentTimeMillis()
    }

}
