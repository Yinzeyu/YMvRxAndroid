package com.yzy.baselibrary.base.fragment

import android.os.Bundle
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.airbnb.mvrx.BaseMvRxViewModel
/**
 *description: 同时使用Epoxy和MvRx的Fragment基类.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseMvRxEpoxyFragment : BaseMvFragment() {
    protected val epoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun invalidate() {
        epoxyController.requestModelBuild()
    }

    protected fun subscribeVM(vararg viewModels: BaseMvRxViewModel<*>) {
        viewModels.forEach {
            it.subscribe(owner = this, subscriber = {
                postInvalidate()
            })
        }
    }

    abstract fun epoxyController(): MvRxEpoxyController

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }
}