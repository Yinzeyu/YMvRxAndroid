package com.yzy.example.utils.album

import com.yzy.example.utils.album.entity.LocalMediaFolder

/**
 * description : ☆☆☆需要注意的是页面onResume的时候会再次回调loadComplete需要自己处理一下☆☆☆
 *
 * @author : yzy
 * @date : 2019/4/16 15:52
 */
interface LocalMediaLoadListener {
    //读取失败返回Null
    fun loadComplete(folders: List<LocalMediaFolder>?)
}