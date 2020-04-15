package com.yzy.baselibrary.widget.refresh.listener;

import androidx.annotation.NonNull;

import com.yzy.baselibrary.widget.refresh.api.RefreshLayout;


/**
 * 刷新监听器
 * Created by scwang on 2017/5/26.
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
