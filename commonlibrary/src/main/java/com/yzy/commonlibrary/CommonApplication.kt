package com.yzy.commonlibrary

import com.yzy.baselibrary.app.BaseApplication
import com.yzy.commonlibrary.db.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

open class CommonApplication : BaseApplication() {
    //数据库
    private val boxStore: BoxStore by kodein.instance()

    override fun initKodein(builder: Kodein.MainBuilder) {
        super.initKodein(builder)
        builder.import(databaseModule)
    }

    override fun initInMainProcess() {
        super.initInMainProcess()
        initObjectDebug()
    }

    /**
     * 数据库调试
     */
    private fun initObjectDebug() {
        if (BuildConfig.DEBUG) {
            AndroidObjectBrowser(boxStore).start(this)
        }
    }

    private val databaseModule = Kodein.Module(KODEIN_MODULE_DATABASE_TAG) {
        bind<BoxStore>() with singleton {
            MyObjectBox.builder().androidContext(INSTANCE).build();
        }
    }
}

const val KODEIN_MODULE_DATABASE_TAG = "databaseModule"