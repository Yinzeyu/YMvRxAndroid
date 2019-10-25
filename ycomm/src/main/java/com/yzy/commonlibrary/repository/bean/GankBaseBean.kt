package com.yzy.commonlibrary.repository.bean

/**
 *description: Gank接口返回的基类.
 *@date 2018/10/17 14:42.
 *@author: YangYang.
 */

data class GankBaseBean<out T>(
    val error: Boolean,
    val results: List<T>
)
