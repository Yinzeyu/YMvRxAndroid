package com.yzy.baselibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * description: 没有内边距的textView
 * @date 2019/7/26 11:54.
 * @author: yzy.
 */
class NoPaddingTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : AppCompatTextView(context, attributeSet, def) {

    private val textPaint = TextPaint()
    private val rect = Rect()
    private val lineContent = arrayListOf<String>()

    init {
        // 设置字体抗锯齿
        textPaint.isAntiAlias = true
        // 字体颜色
        textPaint.color = currentTextColor
        // 字体大小
        textPaint.textSize = textSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取文字的行高和宽度
        textPaint.getTextBounds(text.toString(), 0, text.toString().length, rect)
        getTextContents()
        val widthAndHeight = getViewWidthAndHeight(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthAndHeight.first, widthAndHeight.second)
    }

    override fun onDraw(canvas: Canvas) {
        lineContent.forEachIndexed { index, it ->
            val space = index * (lineSpacingExtra * lineSpacingMultiplier + rect.bottom - rect.top)
            canvas.drawText(it, -rect.left.toFloat(), (-rect.top) + space, textPaint)
        }
    }

    /**
     * 获取需要绘制的文字内容
     */
    private fun getTextContents() {
        lineContent.clear()
        for (i in 0 until layout.lineCount) {
            val start = layout.getLineStart(i)
            val end = layout.getLineEnd(i)
            lineContent.add(text.toString().substring(start, end))
        }
    }

    /**
     * 测量view，并返回最后真正需要的宽度和高度
     * @param widthSpec
     * @param heightSpec
     * @return
     */
    private fun getViewWidthAndHeight(widthSpec: Int, heightSpec: Int): Pair<Int, Int> {
        val widthMode = MeasureSpec.getMode(widthSpec)
        val heightMode = MeasureSpec.getMode(heightSpec)
        val width = MeasureSpec.getSize(widthSpec)
        val height = MeasureSpec.getSize(heightSpec)

        val realWidth = if (widthMode == MeasureSpec.EXACTLY) {
            width
        } else {
            rect.right - rect.left
        }
        val realHeight = if (heightMode == MeasureSpec.EXACTLY) {
            height
        } else {
            layout.lineCount * (rect.bottom - rect.top) +
                    ((layout.lineCount - 1) * lineSpacingExtra * lineSpacingMultiplier).toInt()
        }

        return Pair(realWidth, realHeight)
    }
}