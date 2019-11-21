package com.yzy.example.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * https://www.jianshu.com/p/a9d09cb7577f
 * description: 闪动的TextView,滑动解锁用.
 *
 * @date 2018/7/5 16:03.
 * @author: YangYang.
 */
class FlashingTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatTextView(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var gradient: LinearGradient? = null
    private var mMatrix: Matrix? = null
    //渐变的速度
    private var deltaX = 0
    private var flashColor = Color.WHITE

    init {
        val paint1 = Paint()
        paint1.color = Color.parseColor("#ff0099cc")
        paint1.style = Paint.Style.FILL

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mWidth == 0) {
            mWidth = measuredWidth
            val paint2: Paint = paint
            //颜色渐变器
            gradient = LinearGradient(
                0f,
                0f,
                mWidth.toFloat(),
                0f,
                intArrayOf(currentTextColor, flashColor, currentTextColor),
                floatArrayOf(0f, 0.5f, 1.0f),
                Shader.TileMode.CLAMP
            )
            paint2.shader = gradient
            mMatrix = Matrix()//用于缩放图片的矩阵
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (matrix != null) {
            deltaX += mWidth / 5
            if (deltaX > 2 * mWidth) {
                deltaX = -mWidth
            }
        }
        //关键代码通过矩阵的平移实现
        matrix?.setTranslate(deltaX.toFloat(), 0f)
        gradient?.setLocalMatrix(matrix)
        postInvalidateDelayed(120)
    }

    fun setFlashColor(flashColor: Int) {
        this.flashColor = flashColor
        postInvalidate()
    }
}