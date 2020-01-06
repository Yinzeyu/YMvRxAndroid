package com.yzy.baselibrary.base

import androidx.lifecycle.ViewModel


open class BaseViewModel : ViewModel() {
    open class BaseUiModel<T>(
        var loading: Boolean=false,
        var showError: String? = null,
        var showSuccess: T? = null,
        var showEnd: Boolean = false, // 加载更多
        var isRefresh: Boolean = false // 刷新
    )
}