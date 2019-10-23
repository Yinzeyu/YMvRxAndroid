package com.yzy.baselibrary.base

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController

open class MvRxEpoxyController<T>(
    val buildModelsCallback: EpoxyController.(data: T) -> Unit = { }
) : AsyncEpoxyController() {
    //数据
    var data: T? = null
        set(value) {
            field = value
            if (value != null) requestModelBuild()
        }

    override fun buildModels() {
        data?.let { buildModelsCallback(it) }
    }
}