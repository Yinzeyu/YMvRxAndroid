package com.yzy.example.component.comm.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

class ViewAdapter {
    @BindingAdapter("layoutManager")
    fun setLayoutManager(
        recyclerView: RecyclerView,
        layoutManagerFactory: LayoutManagers.LayoutManagerFactory
    ) {
        recyclerView.layoutManager = layoutManagerFactory.create(recyclerView)
    }
}