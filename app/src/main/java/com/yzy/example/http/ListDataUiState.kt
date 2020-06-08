package com.yzy.example.http

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 列表数据状态类
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class ListDataUiState<T : Parcelable>(
    //是否请求成功
    val isSuccess: Boolean,
    //错误消息 isSuccess为false才会有
    val errMessage: String = "",
    /**
     * 错误类型
     */
    val errCode: Int = 0,
    //是否为刷新
    val isRefresh: Boolean = false,
    //是否为空
    val isEmpty: Boolean = false,
    //是否还有更多
    val hasMore: Boolean = false,
    //是第一页且没有数据
    val isFirstEmpty:Boolean = false,
    //列表数据
    val listData: MutableList<T>? = mutableListOf()
    ) : Parcelable