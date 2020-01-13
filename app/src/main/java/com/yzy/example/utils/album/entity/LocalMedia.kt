package com.yzy.example.utils.album.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.yzy.example.utils.album.MediaType
import kotlinx.android.parcel.Parcelize


/**
 * description : 多媒体文件信息
 *
 * @author : yzy
 * @date : 2019/4/16 10:11
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class LocalMedia(
    //图片地址
    var path: String? = "",
    //筛选导入类型:图片、音频、视频、所有
    var mimeType: Int = MediaType.IMAGE.ordinal,
    //获取到的文件类型:如"image/jpeg"、"image/png"、"video/mp4"等
    var pictureType: String? = "image/jpeg",
    //视频时长(毫秒)
    var duration: Long = 0L,
    //图片或者视频的宽度
    var width: Int = 0,
    //图片或者视频的高度
    var height: Int = 0,

    //在列表中展示的位置
    var position: Int = 0,
    var num: Int = 0,
    var isChecked: Boolean = false,
    var isCut: Boolean = false,
    var compressed: Boolean = false,
    var cutPath: String? = "",
    var compressPath: String? = "",
    var isVideo: Boolean = false,
    var isLongPic: Boolean = false,
    var isWidthPic: Boolean = false,
    var durationTime: String? = ""
) : Parcelable