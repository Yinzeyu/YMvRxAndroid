package com.yzy.baselibrary.base.dialog

import android.os.Bundle
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.airbnb.mvrx.BaseMvRxViewModel


abstract class BaseMvRxEpoxyDialogFragment : CommonDialogFragment() {
    protected val epoxyController by lazy { epoxyController() }

    override fun invalidate() {
        epoxyController.requestModelBuild()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    abstract fun epoxyController(): MvRxEpoxyController
    protected fun subscribeVM(vararg viewModels: BaseMvRxViewModel<*>) {
        viewModels.forEach {
            it.subscribe(owner = this, subscriber = {
                postInvalidate()
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }

}
