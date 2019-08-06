package com.yzy.baselibrary.base.activity

import android.os.Bundle
import com.airbnb.mvrx.MvRxViewModelStore
import com.airbnb.mvrx.MvRxViewModelStoreOwner

abstract class BaseMvActivity : BaseActivity(), MvRxViewModelStoreOwner {
    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }

    override fun initBeforeCreateView(savedInstanceState: Bundle?) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        initMvrxPersistedViewId(savedInstanceState)
    }

    protected open fun initMvrxPersistedViewId(savedInstanceState: Bundle?) {
    }
    protected open fun initSaveInstanceState(outState: Bundle) {
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
        initSaveInstanceState(outState)
    }
}