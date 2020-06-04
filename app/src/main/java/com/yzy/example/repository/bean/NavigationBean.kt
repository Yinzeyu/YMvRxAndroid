package com.yzy.example.repository.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class NavigationBean(
    var articles: ArrayList<ArticleDataBean>,
    var cid: Int,
    var name: String
) : Parcelable