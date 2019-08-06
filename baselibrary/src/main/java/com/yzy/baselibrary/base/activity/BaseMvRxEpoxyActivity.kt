package com.yzy.baselibrary.base.activity

import android.os.Bundle
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxView
import java.util.*

/**
 *description: MvRx和Epoxy都使用的Activity的基类.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseMvRxEpoxyActivity : BaseMvActivity(), MvRxView {
    protected val epoxyController by lazy { epoxyController() }
    final override val mvrxViewId: String by lazy { mvrxPersistedViewId }

    private lateinit var mvrxPersistedViewId: String

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

    override fun initMvrxPersistedViewId(savedInstanceState: Bundle?) {
        mvrxPersistedViewId = savedInstanceState?.getString(PERSISTED_VIEW_ID_KEY) ?: this::class.java.simpleName + "_" + UUID.randomUUID().toString()
        super.initMvrxPersistedViewId(savedInstanceState)
    }

    override fun initSaveInstanceState(outState: Bundle) {
        super.initSaveInstanceState(outState)
        outState.putString(PERSISTED_VIEW_ID_KEY, mvrxViewId)
    }

    abstract fun epoxyController(): AsyncEpoxyController
}
private const val PERSISTED_VIEW_ID_KEY = "mvrx:persisted_view_id"