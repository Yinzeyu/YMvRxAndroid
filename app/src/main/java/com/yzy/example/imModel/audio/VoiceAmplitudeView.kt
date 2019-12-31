package com.yzy.example.imModel.audio

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class VoiceAmplitudeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : View(context, attributeSet, defAttrStyle) {

    private val paint: Paint = Paint()
    private var roundRect: RectF? = null
    private val pathRoundRect = Path()
    private val pathProgressRect = Path()

    private var progress: Int = 60

    init {
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (width != 0) {
            val round = width / 2.0f
            if (roundRect == null) {
                roundRect = RectF(0.0f, 0.0f, width.toFloat(), height.toFloat())
                pathRoundRect.addRoundRect(roundRect, round, round, Path.Direction.CW)
            }
            pathProgressRect.reset()
            val top = height.toFloat() * (1.0f - progress / 100f)
            pathProgressRect.addRect(0.0f, top, width.toFloat(), height.toFloat(), Path.Direction.CW)
            pathProgressRect.op(pathRoundRect, Path.Op.INTERSECT)
            canvas?.drawPath(pathProgressRect, paint)
        }
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }
}