package com.yzy.baselibrary.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.mmkv.MMKV
import com.yzy.baselibrary.BuildConfig
import com.yzy.baselibrary.di.ClientModule
import com.yzy.baselibrary.di.GlobeConfigModule
import com.yzy.baselibrary.di.imageLoaderModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

open class BaseApplication : Application(), KodeinAware {
    var launcherTime = 0L
    final override val kodein: Kodein by Kodein.lazy {
        bind<Context>() with singleton { this@BaseApplication }
        import(androidCoreModule(this@BaseApplication))
        import(androidXModule(this@BaseApplication))
        import(imageLoaderModule)
        import(ClientModule.clientModule)
        initKodein(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        this.baseInitCreate()
        //MMKV初始化
        MMKV.initialize(this)
        if (isMainProcess()) {
            initThirdPart()
            initLiveBus()
            this.initInMainProcess()
        }
    }


    /**
     * 初始化第三方的一些东西
     */
    private fun initThirdPart() {
        //Utils
        Utils.init(this)
        LogUtils.getConfig()
            .setLogSwitch(BuildConfig.DEBUG)//log开关
            .setGlobalTag("zhixiang").stackDeep = 3//log栈
    }

    //初始化 liveDataBus
    private fun initLiveBus() {
        LiveEventBus
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false);
    }

    protected open fun initKodein(builder: Kodein.MainBuilder) {

    }

    /**
     * 在主进程中进行操作
     */
    protected open fun initInMainProcess() {

    }

    protected open fun baseInitCreate() {

    }

    companion object {
        lateinit var INSTANCE: BaseApplication
    }

    /**
     * 是否主进程
     */
    private fun isMainProcess(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (am.runningAppProcesses == null) {
            return false
        }
        val processInfo = am.runningAppProcesses
        val mainProcessName = packageName
        val myPid = Process.myPid()
        for (info in processInfo) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        launcherTime = System.currentTimeMillis()
    }

}
