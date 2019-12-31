package com.yzy.example.imModel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.yzy.example.R

class ConversationExtPageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : LinearLayoutCompat(context, attributeSet, defAttrStyle), View.OnClickListener {

    private var listener: OnExtViewClickListener? = null
    private var pageIndex: Int = 0

    private val imageViewList = mutableListOf<ImageView>()
    private val textViewList = mutableListOf<TextView>()

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.imui_layout_conversation_ext, this, false)
        addView(view)
    }

    /**
     * 更新扩展菜单
     */
    fun updateExtViews(exts: List<ConversationExt>) {
        imageViewList.clear()
        textViewList.clear()
        for (index in exts.indices) {
            val iconImageView = findViewWithTag<ImageView>("icon_$index")
            iconImageView.setImageResource(exts[index].iconResId())
            iconImageView.setOnClickListener(this)
            val titleTextView = findViewWithTag<TextView>("title_$index")
            titleTextView.text = exts[index].title(context)
            imageViewList.add(iconImageView)
            textViewList.add(titleTextView)
        }
    }

    /**
     * 设置icon的样式
     */
    fun setImageViewStyle(style: ImageView.() -> Unit) {
        imageViewList.forEach(style)
    }

    /**
     * 设置文字的样式
     */
    fun setTextViewStyle(style: TextView.() -> Unit) {
        textViewList.forEach {
            it.apply(style)
        }
    }

    fun getPageIndex(): Int {
        return pageIndex
    }

    fun setPageIndex(pageIndex: Int) {
        this.pageIndex = pageIndex
    }

    fun setOnExtViewClickListener(listener: OnExtViewClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            if (view.tag is String) {
                val tag = v.getTag() as String
                val index = Integer.parseInt(tag.substring(tag.lastIndexOf("_") + 1))
                listener?.onClick(pageIndex * ConversationExtManager.EXTENSION_PER_PAGE + index)
            }
        }
    }


    interface OnExtViewClickListener {
        fun onClick(index: Int)
    }
}