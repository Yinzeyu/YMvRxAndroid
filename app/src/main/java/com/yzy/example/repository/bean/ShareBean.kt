package com.yzy.example.repository.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ShareBean(
    var coinInfo: IntegralBean,
    var shareArticles: PagerResponse<MutableList<ArticleDataBean>>
) : Parcelable