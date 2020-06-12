package com.yzy.example.repository.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class ColorBean<T>(
    var bean: T ,
    var itemBeanType: Int = 0
) : MultiItemEntity {
    override val itemType: Int = itemBeanType
    companion object {
        const val COLOR_BACK = 1
        const val COLOR_LIST = 2
    }
}
