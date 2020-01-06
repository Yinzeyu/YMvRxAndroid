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

abstract class BaseApplication : Application() {
    //子进程中的初始化是否完成,有的必须要子进程中的初始化完成后才能调用
    var initFinishInChildThread = false
    var launcherTime = 0L

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        this.baseInitCreate()
        if (ProcessUtils.isMainProcess()) {
            initInMainProcess()
//            MvRxMocks.install(this)
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
        //子线程中的初始化(为了防止APP打开太慢,将不必要的放在子线程中初始化)
//        Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
//            initInChildThread()
//            emitter.onNext(true)
//            emitter.onComplete()
//        })
//            .compose(applySchedulers())
//            .subscribe({
//                initFinishInChildThread = true
//            }, { LogUtils.e(it) }, {})
        //字体sp不跟随系统大小变化
        AutoSizeConfig.getInstance()
            .isExcludeFontScale = true
    }

    //主线程中的初始化(只在主进程中调用)
    abstract fun initInMainThread()

    //子线程中的初始化(只在主进程中调用)
    abstract fun initInChildThread()



    protected open fun baseInitCreate() {

    }

    companion object {
        fun getApp(): BaseApplication {
            return Utils.getApp() as BaseApplication
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        launcherTime = System.currentTimeMillis()
    }

}
