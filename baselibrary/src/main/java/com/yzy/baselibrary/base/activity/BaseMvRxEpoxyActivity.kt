package com.yzy.baselibrary.base.activity

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.mvrx.BaseMvRxViewModel

/**
 *description: MvRx和Epoxy都使用的Activity的基类.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseMvRxEpoxyActivity : BaseMvActivity() {
    protected val epoxyController by lazy { epoxyController() }
    override fun onStart() {
        super.onStart()
        postInvalidate()
    }

    protected fun subscribeVM(vararg viewModels: BaseMvRxViewModel<*>) {
        viewModels.forEach {
            it.subscribe(owner = this, subscriber = {
                postInvalidate()
            })
        }
    }

    override fun invalidate() {
        epoxyController.requestModelBuild()
    }

    abstract fun epoxyController(): AsyncEpoxyController
}