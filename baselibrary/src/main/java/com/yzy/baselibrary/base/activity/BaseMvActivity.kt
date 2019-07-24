package com.yzy.baselibrary.base.activity

import android.os.Bundle
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewModelStore
import java.util.*

abstract class BaseMvActivity : BaseActivity(), MvRxView {
    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }
    private lateinit var mvrxPersistedViewId: String
    final override val mvrxViewId: String by lazy { mvrxPersistedViewId }
    override fun initBeforeCreateView(savedInstanceState: Bundle?) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        mvrxPersistedViewId =
            savedInstanceState?.getString(PERSISTED_VIEW_ID_KEY) ?: this::class.java.simpleName
                    + "_" + UUID.randomUUID().toString()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }
}

private const val PERSISTED_VIEW_ID_KEY = "mvrx:persisted_view_id"