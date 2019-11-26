package com.yzy.example.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import com.yzy.example.R
import kotlin.math.min

class QMUIRoundButtonDrawable : GradientDrawable() {
    /**
     * 圆角大小是否自适应为 View 的高度的一般
     */
    private var mRadiusAdjustBounds = true
    private var mFillColors: ColorStateList? = null
    private var mStrokeWidth = 0
    private var mStrokeColors: ColorStateList? = null
    /**
     * 设置按钮的背景色(只支持纯色,不支持 Bitmap 或 Drawable)
     */
    fun setBgData(colors: ColorStateList?) {
        if (hasNativeStateListAPI()) {
            super.setColor(colors)
        } else {
            mFillColors = colors
            setColor(colors?.getColorForState(state, 0) ?: Color.TRANSPARENT)
        }
    }

    /**
     * 设置按钮的描边粗细和颜色
     */
    fun setStrokeData(width: Int, colors: ColorStateList?) {
        if (hasNativeStateListAPI()) {
            super.setStroke(width, colors)
        } else {
            mStrokeWidth = width
            mStrokeColors = colors
            setStroke(width, colors?.getColorForState(state, 0) ?: Color.TRANSPARENT)
        }
    }

    private fun hasNativeStateListAPI(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 设置圆角大小是否自动适应为 View 的高度的一半
     */
    fun setIsRadiusAdjustBounds(isRadiusAdjustBounds: Boolean) {
        mRadiusAdjustBounds = isRadiusAdjustBounds
    }

    override fun onStateChange(stateSet: IntArray): Boolean {
        var superRet = super.onStateChange(stateSet)
        mFillColors?.let {
            setColor(it.getColorForState(stateSet, 0))
            superRet = true
        }
        mStrokeColors?.let {
            setStroke(mStrokeWidth, it.getColorForState(stateSet, 0))
            superRet = true
        }
        return superRet
    }

    override fun isStateful(): Boolean {
        return (mFillColors != null && mFillColors?.isStateful ?: false
                || mStrokeColors != null && mStrokeColors?.isStateful ?: false
                || super.isStateful())
    }

    override fun onBoundsChange(r: Rect) {
        super.onBoundsChange(r)
        if (mRadiusAdjustBounds) { // 修改圆角为短边的一半
            cornerRadius = min(r.width(), r.height()) / 2.toFloat()
        }
    }

    companion object {
        fun fromAttributeSet(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
        ): QMUIRoundButtonDrawable {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.QMUIRoundButton, defStyleAttr, 0)
            val colorBg =
                typedArray.getColorStateList(R.styleable.QMUIRoundButton_qmui_backgroundColor)
            val colorBorder =
                typedArray.getColorStateList(R.styleable.QMUIRoundButton_qmui_borderColor)
            val borderWidth =
                typedArray.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_borderWidth, 0)
            var isRadiusAdjustBounds =
                typedArray.getBoolean(R.styleable.QMUIRoundButton_qmui_isRadiusAdjustBounds, false)
            val mRadius =
                typedArray.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radius, 0)
            val mRadiusTopLeft =
                typedArray.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radiusTopLeft, 0)
            val mRadiusTopRight =
                typedArray.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radiusTopRight, 0)
            val mRadiusBottomLeft = typedArray.getDimensionPixelSize(
                R.styleable.QMUIRoundButton_qmui_radiusBottomLeft,
                0
            )
            val mRadiusBottomRight = typedArray.getDimensionPixelSize(
                R.styleable.QMUIRoundButton_qmui_radiusBottomRight,
                0
            )
            typedArray.recycle()
            val bg = QMUIRoundButtonDrawable()
            bg.setBgData(colorBg)
            bg.setStrokeData(borderWidth, colorBorder)
            if (mRadiusTopLeft > 0 || mRadiusTopRight > 0 || mRadiusBottomLeft > 0 || mRadiusBottomRight > 0) {
                val radii = floatArrayOf(
                    mRadiusTopLeft.toFloat(), mRadiusTopLeft.toFloat(),
                    mRadiusTopRight.toFloat(), mRadiusTopRight.toFloat(),
                    mRadiusBottomRight.toFloat(), mRadiusBottomRight.toFloat(),
                    mRadiusBottomLeft.toFloat(), mRadiusBottomLeft
                        .toFloat()
                )
                bg.cornerRadii = radii
                isRadiusAdjustBounds = false
            } else {
                bg.cornerRadius = mRadius.toFloat()
                if (mRadius > 0) {
                    isRadiusAdjustBounds = false
                }
            }
            bg.setIsRadiusAdjustBounds(isRadiusAdjustBounds)
            return bg
        }
    }
}