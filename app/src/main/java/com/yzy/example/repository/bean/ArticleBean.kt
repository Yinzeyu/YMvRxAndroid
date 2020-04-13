package com.yzy.example.repository.bean

import com.blankj.utilcode.util.TimeUtils


data class ArticleDataBean(
    var curPage: Int = 0,
    var offset: Int = 0,// 0,
    var over: Boolean = false,//false,
    var pageCount: Int = 0,//362,
    var size: Int = 20,//20,
    var total: Int = 0,//7240
    val datas: MutableList<ArticleBean>? = null
) {
    fun hasMore(): Boolean {
        return curPage == pageCount
    }
}
data class ArticleBean(
    var shareUser: String? = null,
    var apkLink: String? = null,
    var author: String? = null,
    var chapterId: Int = 0,
    var chapterName: String? = null,
    var isCollect: Boolean = false,
    var courseId: Int = 0,
    var desc: String? = null,
    var envelopePic: String? = null,
    var id: Int = 0,
    var originId: Int = -1,    // 收藏文章列表里面的原始文章id
    var link: String? = null,
    var niceDate: String? = null,
    var origin: String? = null,
    var projectLink: String? = null,
    var publishTime: Long = 0,
    var title: String? = null,
    var visible: Int = 0,
    var zan: Int = 0,
    var isFresh: Boolean = false,
    var isShowImage: Boolean = true,
    // 分类name
    var navigationName: String? = null
){
    var showAuthor: String? = null
        get() {
            if (field == null) {
                field = when {
                    author.isNullOrBlank() -> shareUser?.trim() ?: ""
                    shareUser.isNullOrBlank() -> author?.trim() ?: ""
                    else -> "未知"
            }
            }
            return field
        }
    var showTime: String? = null
        get() {
            if (field.isNullOrBlank() && publishTime > 0) {
                field = TimeUtils.millis2String(publishTime)
            }
            return field
        }
    var showInfo: String? = null
        get() {
            if (field == null) {
                field = when {
                    title.isNullOrBlank() -> desc?.trim() ?: ""
                    desc.isNullOrBlank() -> title?.trim() ?: ""
                    else -> String.format("%s\n%s", title?.trim() ?: "", desc?.trim() ?: "")
                }
            }
            return field
        }
}