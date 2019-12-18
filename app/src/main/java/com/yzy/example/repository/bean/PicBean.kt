package com.yzy.example.repository.bean

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/4 13:44
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PicBean(
        var id: Long = 0L,
        var url: String? = null,
        var mBounds: Rect? = null, // 记录坐标
        var width: Int = 100,
        var height: Int = 100,
        var size: Float = 0f
) : Parcelable