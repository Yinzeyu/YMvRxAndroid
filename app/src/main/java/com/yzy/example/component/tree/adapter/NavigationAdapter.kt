package com.yzy.example.component.tree.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yzy.example.R
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.NavigationBean


class NavigationAdapter(data: ArrayList<NavigationBean>) :
    BaseQuickAdapter<NavigationBean, BaseViewHolder>(R.layout.item_system, data) {

    private lateinit var navigationClickInterFace: NavigationClickInterFace

//    init {
//        setAdapterAnimion(SettingUtil.getListMode())
//    }

    override fun convert(holder: BaseViewHolder, item: NavigationBean) {
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
            adapter = NavigationChildAdapter(item.articles).apply {
//                setNbOnItemClickListener { adapter, view, position ->
//                    navigationClickInterFace.onNavigationClickListener(
//                        item.articles[position], view
//                    )
//                }
            }
        }
    }

    interface NavigationClickInterFace {
        fun onNavigationClickListener(item: ArticleDataBean, view: View)
    }

    fun setNavigationClickInterFace(face: NavigationClickInterFace) {
        navigationClickInterFace = face
    }
}