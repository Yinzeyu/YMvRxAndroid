package com.yzy.baselibrary.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.yzy.baselibrary.BuildConfig
import com.yzy.baselibrary.di.ClientModule
import com.yzy.baselibrary.di.imageLoaderModule
import com.yzy.baselibrary.extention.applySchedulers
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import me.jessyan.autosize.AutoSizeConfig
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

abstract class BaseApplication : Application(), KodeinAware {
    //子进程中的初始化是否完成,有的必须要子进程中的初始化完成后才能调用
    var initFinishInChildThread = false
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
        //子线程中的初始化(为了防止APP打开太慢,将不必要的放在子线程中初始化)
        Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            initInChildThread()
            emitter.onNext(true)
            emitter.onComplete()
        })
            .compose(applySchedulers())
            .subscribe({
                initFinishInChildThread = true
            }, { LogUtils.e(it) }, {})
        //字体sp不跟随系统大小变化
        AutoSizeConfig.getInstance()
            .isExcludeFontScale = true
    }

    //主线程中的初始化(只在主进程中调用)
    abstract fun initInMainThread()

    //子线程中的初始化(只在主进程中调用)
    abstract fun initInChildThread()


    protected open fun initKodein(builder: Kodein.MainBuilder) {

    }


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
