package com.yzy.example.repository.bean

import com.yzy.baselibrary.base.IBaseResponse1

/**
 *description: Gank接口返回的基类.
 *@date 2018/10/17 14:42.
 *@author: YangYang.
 */

data class GankBaseBean< T>(val error: Boolean, val results: T): IBaseResponse1<T> {

    override fun data() = results

    override fun isSuccess() =  results == null

}