package com.yzy.example.repository.local

import com.yzy.baselibrary.repository.BaseLocalDataSource
import io.objectbox.Box
import io.objectbox.BoxStore
import org.kodein.di.generic.instance

open class LocalDataSource : BaseLocalDataSource() {
    //数据库缓存
    private val boxStore: BoxStore by kodein.instance()

    /**
     * 获取ObjectBox操作的Box
     */
    fun <T> box(entity: Class<T>): Box<T> = boxStore.boxFor(entity)
}