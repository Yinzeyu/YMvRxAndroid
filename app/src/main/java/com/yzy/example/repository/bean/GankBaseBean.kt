package com.yzy.example.repository.bean

/**
 *description: Gank接口返回的基类.
 *@date 2018/10/17 14:42.
 *@author: YangYang.
 */

data class GankBaseBean<T>(
        val error: Boolean,
        val results: MutableList<T>
)
