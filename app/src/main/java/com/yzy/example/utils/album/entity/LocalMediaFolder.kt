package com.yzy.example.utils.album.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

/**
* description :多媒体文件夹信息
*
* @author : yzy
* @date : 2019/4/16 10:50
*/
@SuppressLint("ParcelCreator")
@Parcelize
data class LocalMediaFolder(
    //文件夹名称
    var name: String? = "",
    //文件夹路径
    var path: String? = "",
    //第一张图片地址
    var firstImagePath: String? = "",
    //文件夹里面文件数量
    var imageNum: Int = 0,
    //文件夹子文件内容
    var images: MutableList<LocalMedia>? = ArrayList(),

    //文件夹里面选中的数量
    var checkedNum: Int = 0,
    //判断当前显示的内容是不是本文件夹的内容
    var isChecked: Boolean = false
) : Parcelable