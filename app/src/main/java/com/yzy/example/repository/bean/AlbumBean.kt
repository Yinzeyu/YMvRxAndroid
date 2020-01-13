package com.yzy.example.repository.bean

import com.yzy.example.utils.album.entity.LocalMediaFolder


/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/13 18:10
 */
data class AlbumBean(
    val data: MutableList<LocalMediaFolder> = mutableListOf(),
    val selectIndex: Int = 0,
    var scroll2Top: Boolean = false
    )
