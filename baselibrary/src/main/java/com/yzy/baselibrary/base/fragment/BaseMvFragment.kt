package com.yzy.baselibrary.base.fragment

import android.os.Bundle
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewModelStore
import java.util.*

/**
 *description: 使用MvRx的CommFragment..
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseMvFragment : BaseFragment(), MvRxView {
    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }
    final override val mvrxViewId: String by lazy { mvrxPersistedViewId }

    private lateinit var mvrxPersistedViewId: String

    override fun initBeforeCreateView(savedInstanceState: Bundle?) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        mvrxPersistedViewId = savedInstanceState?.getString(PERSISTED_VIEW_ID_KEY) ?: this::class.java.simpleName + "_" + UUID.randomUUID().toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }

    override fun onStart() {
        super.onStart()
        postInvalidate()
    }
}

private const val PERSISTED_VIEW_ID_KEY = "mvrx:persisted_view_id"