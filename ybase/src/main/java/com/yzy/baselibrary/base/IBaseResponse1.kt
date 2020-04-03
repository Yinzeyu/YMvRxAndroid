package com.yzy.baselibrary.base

/**
 *   @auther : Aleyn
 *   time   : 2020/01/13
 */
interface IBaseResponse1<T> {
    fun data(): T
    fun isSuccess(): Boolean
}