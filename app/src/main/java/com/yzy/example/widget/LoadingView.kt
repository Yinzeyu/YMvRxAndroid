package com.yzy.example.widget

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.ValueAnimator.ofFloat
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange
import kotlin.math.abs

/**
 * description: 全屏loading动画 执行完一遍动画2秒。截止2018年3月3日 15:23:13用到的地方有:耗时的弹窗LoadingDialog、发现(旧版本的乐圈)、Activity基类和Fragment基类
 *
 * @author : yzy
 * @date : 2018/3/3  10:22 创建
 */
class LoadingView(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {
    //宽度
    private var width: Float = 0f
    //高度
    private var height: Float = 0f
    //绘制圆形的
    private var paint: Paint? = null
    //颜色变化
    private val argbEvaluator = ArgbEvaluator()
    //最大圆形半径23dp
    private var maxRadius = 0f
    //最小圆形半径4dp
    private var minRadius = 0f
    //圆形1的半径
    private var radiusCircle1 = 0f
    //圆形2的半径
    private var radiusCircle2 = 0f
    //正在执行的动画
    private var valueAnimator: ValueAnimator? = null
    //是否在执行动画
    private var isAnimating = false


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            width = w.toFloat()
            height = h.toFloat()
            if (width < maxRadius) {
                maxRadius = width
                minRadius = maxRadius * 4f / 23
                radiusCircle1 = maxRadius
                radiusCircle2 = minRadius
            }
        }
    }

    init {
        paint = Paint()
        paint?.isAntiAlias = true
        paint?.style = Paint.Style.FILL
        context?.let {
            maxRadius = dp2px(it, 23 / 2f)
            minRadius = dp2px(it, 4 / 2f)
        }
        radiusCircle1 = maxRadius
        radiusCircle2 = minRadius
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint?.let {//绘制圆形1
            it.color = colorCircle1
            canvas.drawCircle(width / 2f, height / 2f, radiusCircle1, it)
            it.color = getCircle2Color(radiusCircle2)
            canvas.drawCircle(width / 2f, height / 2f, radiusCircle2, it)
        }
    }

    //根据进度获取半径(整个动画过程从0到1)
    private fun refreshRadius(
        @FloatRange(
            from = 0.toDouble(),
            to = 1.toDouble()
        ) progress: Float
    ) { //经过两点计算直线斜率:y=kx+b
        radiusCircle1 =
            minRadius + abs((maxRadius - minRadius) * (1 - 2f * progress))
        radiusCircle2 =
            maxRadius - abs((maxRadius - minRadius) * (2f * progress - 1))
    }

    //根据圆形的大小获取当前需要显示的颜色值
    private fun getCircle2Color(radius: Float): Int {
        return argbEvaluator.evaluate(
            (radius - minRadius) * 1f / (maxRadius - minRadius),
            colorCircle21, colorCircle22
        ) as Int
    }

    //dp转px
    private fun dp2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale
    }

    private fun startAnimation() {
        if (isAnimating) {
            return
        }
        isAnimating = true
        valueAnimator?.apply {
            ofFloat(0f, 0.5f, 1f)
            duration = 2000
            interpolator = LinearInterpolator()
            addUpdateListener { animation: ValueAnimator ->
                refreshRadius(animation.animatedValue as Float)
                postInvalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }

    private fun stopAnimation() {
        if (valueAnimator != null && isAnimating) {
            valueAnimator?.apply {
                removeAllUpdateListeners()
                cancel()
            }
            postInvalidate()
        }
        isAnimating = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    companion object {
        //第一个圆(初始化大的那个)的颜色
        private val colorCircle1 = Color.parseColor("#3356c66b") //颜色：#56c66b,不透明度：20%
        //第二个圆(初始化小的那个)最小时的颜色
        private val colorCircle21 = Color.parseColor("#B356c66b") //颜色：#56c66b,不透明度：70%）
        //第二个圆(初始化小的那个)最大时的颜色
        private val colorCircle22 = Color.parseColor("#6B56c66b") //颜色：#56c66b,不透明度42%
    }
}