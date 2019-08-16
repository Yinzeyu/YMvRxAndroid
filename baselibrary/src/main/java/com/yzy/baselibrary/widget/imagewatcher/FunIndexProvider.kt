package com.yzy.baselibrary.widget.imagewatcher

import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

import androidx.core.content.ContextCompat

import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.R
import com.yzy.baselibrary.utils.StatusBarUtils

/**
 * description : 左上角有预览数字x/xx的样式
 *
 * @author :yzy
 * @date : 2018/12/10 16:33
 */
internal class FunIndexProvider : ImageWatcher.IndexProvider {
    private var tCurrentIdx: TextView? = null
    private var linearLayout: LinearLayout? = null

    override fun initialView(context: Context): View {
        linearLayout = LinearLayout(context)
        linearLayout?.let {
            it.setPadding(0, StatusBarUtils.getStatusBarHeight(context), 0, 0)
            it.setBackgroundColor(ContextCompat.getColor(context, R.color.black_40))
            tCurrentIdx = TextView(context)
            tCurrentIdx?.let { tv ->
                tv.includeFontPadding = false
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_fff))
                val lpCurrentIdx = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
                )
                lpCurrentIdx.gravity = Gravity.TOP or Gravity.CENTER_VERTICAL
                tv.layoutParams = lpCurrentIdx
                val displayMetrics = context.resources.displayMetrics
                val tCurrentIdxTransX =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        16f,
                        displayMetrics
                    ) + 0.5f
                tv.translationX = tCurrentIdxTransX
            }

            it.addView(tCurrentIdx)
            val lpLinearLayout = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(64f)
            )
            lpLinearLayout.gravity = Gravity.TOP
            it.layoutParams = lpLinearLayout
        }

        return linearLayout as LinearLayout
    }

    override fun onPageChanged(imageWatcher: ImageWatcher, position: Int, dataList: List<Uri>) {
        tCurrentIdx?.let {
            if (dataList.size > 1) {
                linearLayout?.visibility = View.VISIBLE
                it.visibility = View.VISIBLE
                val idxInfo = (position + 1).toString() + " / " + dataList.size
                it.text = idxInfo
            } else {
                linearLayout?.visibility = View.GONE
                it.visibility = View.GONE
            }
        }

    }
}
