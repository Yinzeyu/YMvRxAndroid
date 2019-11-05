package com.yzy.example.ui.video

import android.graphics.Color
import com.airbnb.epoxy.EpoxyAdapter

class PlAdapter : EpoxyAdapter() {

    public fun addList(str: MutableList<String>) {
        str.forEach {
            val plItem = PlPlayItem_().apply {
                id("PlAdapter$it")
                bgColor(Color.parseColor("#111111"))
                path(it)
            }
            addModel(plItem)
        }
    }
}