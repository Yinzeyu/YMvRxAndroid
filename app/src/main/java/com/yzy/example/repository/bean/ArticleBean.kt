package com.yzy.example.repository.bean

import com.blankj.utilcode.util.TimeUtils

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/13 18:10
 */
data class ArticleBean(
//        var apkLink: String? = null,//"",
//        var audit: Int = 1,//1,
//        var author: String? = null,//"faith-hb",
//        var chapterId: Int = 0,//358,
//        var chapterName: String? = null,//"项目基础功能",
//        var collect: Boolean = false,//false,
//        var courseId: Long = 0,//13,
//        var desc: String? = null,//"自定义控件通用库：拿来就用，API文档详细，持续集成，长期维护",
//        var envelopePic: String? = null,//"https://wanandroid.com/blogimgs/96d176ea-31c6-4e3e-b333-0ccfea95133b.png",
//        var fresh: Boolean = false,//false,
//        var id: Long = 0,//9612,
//        var link: String? = null,//"https://www.wanandroid.com/blog/show/2683",
//        var niceDate: String? = null,//"2天前",
//        var niceShareDate: String? = null,//"2天前",
//        var origin: String? = null,//"",
//        var prefix: String? = null,//"",
//        var projectLink: String? = null,//"https://github.com/faith-hb/WidgetCase",
//        var publishTime: Long = 0,//1570784709000,
//        var selfVisible: Int = 0,//0,
//        var shareDate: Long = 0,//1570784709000,
//
//        var superChapterId: Long = 0,//294,
//        var superChapterName: String? = null,//"开源项目主Tab",
//        var tags: MutableList<TagBean>? = null,//[],
//        var title: String? = null,//"自定义控件通用库 WidgetCase",
//        var type: Int = 0,//0,
//        var userId: Long = 0,//-1,
//        var visible: Int = 0,//1,
//        var zan: Int = 0//0
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
}


//{
//
//    var showType: String? = null
//        get() {
//            if (field == null) {
//                field = if (!chapterName.isNullOrBlank() && !superChapterName.isNullOrBlank()) {
//                    String.format("%s  -  %s", superChapterName?.trim() ?: "", chapterName?.trim() ?: "")
//                } else if (chapterName.isNullOrBlank()) {
//                    superChapterName?.trim() ?: ""
//                } else if (superChapterName.isNullOrBlank()) {
//                    chapterName?.trim() ?: ""
//                } else {
//                    "未知"
//                }
//            }
//            return field
//        }
//

//
//    var showInfo: String? = null
//        get() {
//            if (field == null) {
//                field = when {
//                    title.isNullOrBlank() -> desc?.trim() ?: ""
//                    desc.isNullOrBlank() -> title?.trim() ?: ""
//                    else -> String.format("%s\n%s", title?.trim() ?: "", desc?.trim() ?: "")
//                }
//            }
//            return field
//        }
//
//    var showTime: String? = null
//        get() {
//            if (field.isNullOrBlank() && publishTime > 0) {
//                field = TimeUtils.millis2String(publishTime)
//            }
//            return field
//        }
//}