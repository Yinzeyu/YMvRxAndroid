//package com.yzy.example.widget
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.drawable.Drawable
//import android.util.AttributeSet
//import android.view.View
//import androidx.annotation.ColorInt
//import androidx.core.content.ContextCompat
//import com.blankj.utilcode.util.SizeUtils
//import com.yzy.example.R
//
//
///** @author Aidan Follestad (afollestad) */
//internal class ColorCircleView(
//    context: Context,
//    attrs: AttributeSet? = null
//) : View(context, attrs) {
//
//    private val strokePaint = Paint()
//    private val fillPaint = Paint()
//
//    private val borderWidth = SizeUtils.dp2px(1f)
//
//    private var transparentGrid: Drawable? = null
//
//    init {
//        setWillNotDraw(false)
//        strokePaint.style = Paint.Style.STROKE
//        strokePaint.isAntiAlias = true
//        strokePaint.color = Color.BLACK
//        strokePaint.strokeWidth = borderWidth.toFloat()
//        fillPaint.style = Paint.Style.FILL
//        fillPaint.isAntiAlias = true
//        fillPaint.color = Color.DKGRAY
//    }
//
//    @ColorInt
//    var color: Int = Color.BLACK
//        set(value) {
//            field = value
//            fillPaint.color = value
//            invalidate()
//        }
//    @ColorInt
//    var border: Int = Color.DKGRAY
//        set(value) {
//            field = value
//            strokePaint.color = value
//            invalidate()
//        }
//
//    override fun onMeasure(
//        widthMeasureSpec: Int,
//        heightMeasureSpec: Int
//    ) = super.onMeasure(widthMeasureSpec, widthMeasureSpec)
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        if (color == Color.TRANSPARENT) {
//            if (transparentGrid == null) {
//                transparentGrid = ContextCompat.getDrawable(context, R.drawable.transparentgrid)
//            }
//            transparentGrid?.setBounds(0, 0, measuredWidth, measuredHeight)
//            transparentGrid?.draw(canvas)
//        } else {
//            canvas.drawCircle(
//                measuredWidth / 2f,
//                measuredHeight / 2f,
//                (measuredWidth / 2f) - borderWidth,
//                fillPaint
//            )
//        }
//        canvas.drawCircle(
//            measuredWidth / 2f,
//            measuredHeight / 2f,
//            (measuredWidth / 2f) - borderWidth,
//            strokePaint
//        )
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        transparentGrid = null
//    }
//}
