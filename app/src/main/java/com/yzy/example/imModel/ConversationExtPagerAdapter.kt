package com.yzy.example.imModel

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class ConversationExtPagerAdapter(
    private val extList: List<ConversationExt>,
    private val listener: ConversationExtPageView.OnExtViewClickListener
) : PagerAdapter() {

    private val pagers = SparseArray<ConversationExtPageView>()

    private var imageStyle: (ImageView.() -> Unit)? = null
    private var textStyle: (TextView.() -> Unit)? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: ConversationExtPageView? = pagers.get(position)
        if (view == null) {
            view = ConversationExtPageView(container.context)
            view?.let {
                it.setPageIndex(position)
                it.setOnExtViewClickListener(listener)
                val startIndex = ConversationExtManager.EXTENSION_PER_PAGE * position
                val end =
                    if (startIndex + ConversationExtManager.EXTENSION_PER_PAGE > extList.size) extList.size else startIndex + ConversationExtManager.EXTENSION_PER_PAGE
                it.updateExtViews(extList.subList(startIndex, end))
                imageStyle?.let { style ->
                    it.setImageViewStyle(style)
                }
                textStyle?.let { style ->
                    it.setTextViewStyle(style)
                }
            }
            container.addView(view)
            pagers.put(position, view)
        }
        return view
    }


    /**
     * 设置icon的样式
     */
    fun setImageViewStyle(style: ImageView.() -> Unit) {
        this.imageStyle = style
    }

    /**
     * 设置文字的样式
     */
    fun setTextViewStyle(style: TextView.() -> Unit) {
        this.textStyle = style
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return (extList.size + ConversationExtManager.EXTENSION_PER_PAGE - 1) / ConversationExtManager.EXTENSION_PER_PAGE
    }
}