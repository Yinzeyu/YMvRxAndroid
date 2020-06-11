package com.yzy.example.component.tree.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yzy.example.R
import com.yzy.example.extention.setNbOnItemClickListener
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.SystemBean


class SystemAdapter(data: ArrayList<SystemBean>) : BaseQuickAdapter<SystemBean, BaseViewHolder>(R.layout.item_system, data), LoadMoreModule {

    private var method: (data: SystemBean, view: View, position: Int) -> Unit =
        { _: SystemBean, _: View, _: Int -> }

//    init {
//        setAdapterAnimion(SettingUtil.getListMode())
//    }

    override fun convert(holder: BaseViewHolder, item: SystemBean) {
        holder.setText(R.id.item_system_title, item.name.toHtml())
        holder.getView<RecyclerView>(R.id.item_system_rv).run {
            val foxayoutManager: FlexboxLayoutManager by lazy {
                FlexboxLayoutManager(context).apply {
                    //方向 主轴为水平方向，起点在左端
                    flexDirection = FlexDirection.ROW
                    //左对齐
                    justifyContent = JustifyContent.FLEX_START
                }
            }
            layoutManager = foxayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(200)
            isNestedScrollingEnabled = false
            adapter = SystemChildAdapter(
                item.children
            )
                .apply {
                setNbOnItemClickListener { _, view, position ->
                    method(item, view, position)
                }
            }

        }
    }

    interface SystemClickInterFace {
        fun onSystemClickListener(item: SystemBean, position: Int, view: View)
    }

    fun setChildClick(method: (data: SystemBean, view: View, position: Int) -> Unit) {
        this.method = method
    }
}