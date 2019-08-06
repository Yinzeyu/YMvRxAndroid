package com.yzy.baselibrary.base.dialog

import android.os.Bundle
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewModelStore
import java.util.*

abstract class CommonDialogFragment : BaseFragmentDialog(), MvRxView {


    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }
    final override val mvrxViewId: String by lazy { mvrxPersistedViewId }

    private lateinit var mvrxPersistedViewId: String

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }

    override fun onStart() {
        super.onStart()
        postInvalidate()
    }

    override fun initBeforeCreateView(savedInstanceState: Bundle?) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        mvrxPersistedViewId =
            savedInstanceState?.getString(PERSISTED_VIEW_ID_KEY) ?: this::class.java.simpleName+ "_" + UUID.randomUUID().toString()
    }
}

private const val PERSISTED_VIEW_ID_KEY = "mvrx:persisted_view_id"