//package com.yzy.example.imageloader.transformations
//
//import android.content.Context
//import android.graphics.*
//import com.yzy.example.imageloader.CornerType
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
//
///**
// *description: 描述.
// *@date 2019/7/15
// *@author: yzy.
// */
//class RoundedCornersTransformation @JvmOverloads constructor(private val radius: Int, private val margin: Int, private val cornerType: CornerType = CornerType.ALL) : BitmapTransformation() {
//    private val diameter: Int = this.radius * 2
//
//    override fun transform(context: Context, pool: BitmapPool,
//                           toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
//        val width = toTransform.width
//        val height = toTransform.height
//
//        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
//        bitmap.setHasAlpha(true)
//
//        val canvas = Canvas(bitmap)
//        val paint = Paint()
//        paint.isAntiAlias = true
//        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
//        return bitmap
//    }
//
//    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
//        val right = width - margin
//        val bottom = height - margin
//
//        when (cornerType) {
//            CornerType.ALL -> canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), right, bottom), radius.toFloat(), radius.toFloat(), paint)
//            CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, right, bottom)
//            CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, right, bottom)
//            CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, right, bottom)
//            CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, right, bottom)
//            CornerType.TOP -> drawTopRoundRect(canvas, paint, right, bottom)
//            CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, right, bottom)
//            CornerType.LEFT -> drawLeftRoundRect(canvas, paint, right, bottom)
//            CornerType.RIGHT -> drawRightRoundRect(canvas, paint, right, bottom)
//            CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(canvas, paint, right, bottom)
//            CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(canvas, paint, right, bottom)
//            CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(canvas, paint, right, bottom)
//            CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(canvas, paint, right, bottom)
//            CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom)
//            CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom)
//        }
//    }
//
//    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), (margin + diameter).toFloat()),
//                radius.toFloat(), radius.toFloat(), paint)
//        canvas.drawRect(RectF(margin.toFloat(), (margin + radius).toFloat(), (margin + radius).toFloat(), bottom), paint)
//        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
//    }
//
//    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(right - diameter, margin.toFloat(), right, (margin + diameter).toFloat()), radius.toFloat(),
//                radius.toFloat(), paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint)
//        canvas.drawRect(RectF(right - radius, (margin + radius).toFloat(), right, bottom), paint)
//    }
//
//    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), bottom - diameter, (margin + diameter).toFloat(), bottom),
//                radius.toFloat(), radius.toFloat(), paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom - radius), paint)
//        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
//    }
//
//    private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(right - diameter, bottom - diameter, right, bottom), radius.toFloat(),
//                radius.toFloat(), paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint)
//        canvas.drawRect(RectF(right - radius, margin.toFloat(), right, bottom - radius), paint)
//    }
//
//    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), right, (margin + diameter).toFloat()), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF(margin.toFloat(), (margin + radius).toFloat(), right, bottom), paint)
//    }
//
//    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), bottom - diameter, right, bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right, bottom - radius), paint)
//    }
//
//    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
//    }
//
//    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(right - diameter, margin.toFloat(), right, bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint)
//    }
//
//    private fun drawOtherTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), bottom - diameter, right, bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRoundRect(RectF(right - diameter, margin.toFloat(), right, bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom - radius), paint)
//    }
//
//    private fun drawOtherTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRoundRect(RectF(margin.toFloat(), bottom - diameter, right, bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom - radius), paint)
//    }
//
//    private fun drawOtherBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), right, (margin + diameter).toFloat()), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRoundRect(RectF(right - diameter, margin.toFloat(), right, bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF(margin.toFloat(), (margin + radius).toFloat(), right - radius, bottom), paint)
//    }
//
//    private fun drawOtherBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float,
//                                              bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), right, (margin + diameter).toFloat()), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom), radius.toFloat(), radius.toFloat(),
//                paint)
//        canvas.drawRect(RectF((margin + radius).toFloat(), (margin + radius).toFloat(), right, bottom), paint)
//    }
//
//    private fun drawDiagonalFromTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float,
//                                                 bottom: Float) {
//        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), (margin + diameter).toFloat()),
//                radius.toFloat(), radius.toFloat(), paint)
//        canvas.drawRoundRect(RectF(right - diameter, bottom - diameter, right, bottom), radius.toFloat(),
//                radius.toFloat(), paint)
//        canvas.drawRect(RectF(margin.toFloat(), (margin + radius).toFloat(), right - diameter, bottom), paint)
//        canvas.drawRect(RectF((margin + diameter).toFloat(), margin.toFloat(), right, bottom - radius), paint)
//    }
//
//    private fun drawDiagonalFromTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float,
//                                                  bottom: Float) {
//        canvas.drawRoundRect(RectF(right - diameter, margin.toFloat(), right, (margin + diameter).toFloat()), radius.toFloat(),
//                radius.toFloat(), paint)
//        canvas.drawRoundRect(RectF(margin.toFloat(), bottom - diameter, (margin + diameter).toFloat(), bottom),
//                radius.toFloat(), radius.toFloat(), paint)
//        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom - radius), paint)
//        canvas.drawRect(RectF((margin + radius).toFloat(), (margin + radius).toFloat(), right, bottom), paint)
//    }
//
//    override fun key(): String {
//        return ("RoundedTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
//                + diameter + ", cornerType=" + cornerType.name + ")")
//    }
//}