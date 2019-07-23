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
import com.yzy.baselibrary.di.appModule
import com.yzy.baselibrary.di.imageLoaderModule
import com.yzy.baselibrary.integration.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.ArrayList

open class BaseApplication : Application(), KodeinAware {
    private val mLifecycle = ArrayList<AppLifecycle>()
    private var configModules: List<ConfigModule>? = null

    private val appManager: AppManager by instance()

    private val repositoryManager: IRepositoryManager by instance()

    var launcherTime = 0L

    final override val kodein: Kodein by Kodein.lazy {
        bind<Context>() with singleton { this@BaseApplication }
        import(androidCoreModule(this@BaseApplication))
        import(androidXModule(this@BaseApplication))
        import(imageLoaderModule)
        configModules = getModuleConfig()
        configModules?.let {
            import(getGlobeConfigModule(it).globeConfigModule)
        }
        import(appModule)
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
            registerActivityLifecycleCallbacks(ActivityLifecycle(appManager))
            injectConfigModule(configModules)
            initThirdPart()
            initLiveBus()
            mLifecycle.forEach {
                it.onCreate(this@BaseApplication)
            }
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
        LiveEventBus.get()
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

    /**
     * 获取模块的配置
     */
    private fun getModuleConfig(): List<ConfigModule> = ManifestParser(this@BaseApplication).parse()

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     */
    private fun getGlobeConfigModule(modules: List<ConfigModule>): GlobeConfigModule {
        val builder = GlobeConfigModule
            .builder()
            .baseUrl("https://api.github.com")//为了防止用户没有通过GlobeConfigModule配置baseurl,而导致报错,所以提前配置个默认baseurl
        for (module in modules) {
            module.applyOptions(this, builder)
        }
        return builder.build()
    }

    private fun injectConfigModule(modules: List<ConfigModule>?) {
        modules?.forEach {
            it.injectAppLifecycle(this, mLifecycle)
            it.registerComponents(this@BaseApplication, repositoryManager)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        launcherTime = System.currentTimeMillis()
    }

    /**
     * 程序终止的时候执行
     */
    override fun onTerminate() {
        super.onTerminate()
        mLifecycle.forEach {
            it.onTerminate(this@BaseApplication)
        }
    }
}
