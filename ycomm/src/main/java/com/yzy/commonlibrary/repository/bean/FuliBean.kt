package com.yzy.commonlibrary.repository.bean

/**
 *description: 福利接口返回的Bean.
 *@date 2019/7/15
 *@author: yzy.
 */
data class FuliBean(
        val _id: String?,
        val createdAt: String?,
        val desc: String?,
        val publishedAt: String?,
        val source: String?,
        val type: String?,
        val url: String?,
        val used: Boolean?,
        val who: String?,
        val images: String?
)