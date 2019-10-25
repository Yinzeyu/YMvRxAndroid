package com.yzy.baselibrary.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import com.yzy.baselibrary.R

/**
 *description: 圆角的ImageView.
 * @date 2019/7/26 11:54.
 * @author: yzy.
 */
open class RoundImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : AppCompatImageView(context, attributeSet, defAttrStyle) {

    enum class ShapeType {
        Circle,
        Round
    }

    //defAttr var
    var mShapeType: ShapeType = ShapeType.Circle
        set(value) {
            field = value
            invalidate()
        }
    var mBorderWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mBorderColor: Int = Color.parseColor("#FFFFFF")
        set(value) {
            field = value
            invalidate()
        }
    var mRadius: Float = 0f
        set(value) {
            field = value
            refresh8Radius()
            invalidate()
        }
    var mLeftTopRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mLeftTopRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mRightTopRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mRightTopRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mLeftBottomRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mLeftBottomRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mRightBottomRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mRightBottomRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var mShowBorder: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    //drawTools var
    private lateinit var mShapePath: Path
    private lateinit var mBorderPath: Path
    private lateinit var mBitmapPaint: Paint
    private lateinit var mBorderPaint: Paint
    private lateinit var mMatrix: Matrix

    //temp var
    private var mWidth: Int = 0//View的宽度
    private var mHeight: Int = 0//View的高度

    init {
        initAttrs(context, attributeSet, defAttrStyle)//获取自定义属性的值
        initDrawTools()//初始化绘制工具
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet?, defAttrStyle: Int) {
        val array =
            context.obtainStyledAttributes(
                attributeSet,
                R.styleable.RoundImageView,
                defAttrStyle,
                0
            )
        (0..array.indexCount)
            .asSequence()
            .map { array.getIndex(it) }
            .forEach {
                when (it) {
                    R.styleable.RoundImageView_shape_type ->
                        mShapeType = when {
                            array.getInt(it, 0) == 0 -> ShapeType.Circle
                            array.getInt(it, 0) == 1 -> ShapeType.Round
                            else -> ShapeType.Circle
                        }
                    R.styleable.RoundImageView_border_width ->
                        mBorderWidth = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_border_color ->
                        mBorderColor = array.getColor(it, Color.parseColor("#FFFFFF"))
                    R.styleable.RoundImageView_radius ->
                        mRadius = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_left_top_radiusX ->
                        mLeftTopRadiusX = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_left_top_radiusY ->
                        mLeftTopRadiusY = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_left_bottom_radiusX ->
                        mLeftBottomRadiusX = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_left_bottom_radiusY ->
                        mLeftBottomRadiusY = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_right_bottom_radiusX ->
                        mRightBottomRadiusX = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_right_bottom_radiusY ->
                        mRightBottomRadiusY = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_right_top_radiusX ->
                        mRightTopRadiusX = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.RoundImageView_right_top_radiusY ->
                        mRightTopRadiusY = array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                }
            }
        refresh8Radius()
        mShowBorder = mBorderWidth != 0F
        array.recycle()
    }

    //刷新8个弧度值
    private fun refresh8Radius() {
        if (mRadius != 0F) {
            if (mLeftTopRadiusX == 0F) {
                mLeftTopRadiusX = mRadius
            }
            if (mLeftTopRadiusY == 0F) {
                mLeftTopRadiusY = mRadius
            }
            if (mLeftBottomRadiusX == 0F) {
                mLeftBottomRadiusX = mRadius
            }
            if (mLeftBottomRadiusY == 0F) {
                mLeftBottomRadiusY = mRadius
            }
            if (mRightBottomRadiusX == 0F) {
                mRightBottomRadiusX = mRadius
            }
            if (mRightBottomRadiusY == 0F) {
                mRightBottomRadiusY = mRadius
            }
            if (mRightTopRadiusX == 0F) {
                mRightTopRadiusX = mRadius
            }
            if (mRightTopRadiusY == 0F) {
                mRightTopRadiusY = mRadius
            }
        }
    }

    private fun initDrawTools() {
        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            //最终绘制图片的画笔,需要设置BitmapShader着色器，从而实现把图片绘制在不同形状图形上
            style = Paint.Style.FILL
        }
        mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            //绘制边框画笔
            style = Paint.Style.STROKE
            color = mBorderColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = mBorderWidth
        }
        mShapePath = Path()//描述形状轮廓的path路径
        mBorderPath = Path()//描述图片边框轮廓的path路径
        mMatrix = Matrix()//用于缩放图片的矩阵
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {//View的测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mShapeType == ShapeType.Circle) {
            mWidth = Math.min(measuredWidth, measuredHeight)
            mRadius = mWidth / 2.0f
            setMeasuredDimension(mWidth, mWidth)
        } else {
            mWidth = measuredWidth
            mHeight = measuredHeight
            setMeasuredDimension(mWidth, mHeight)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {//确定了最终View的尺寸
        super.onSizeChanged(w, h, oldw, oldh)
        mBorderPath.reset()
        mShapePath.reset()
        when (mShapeType) {
            ShapeType.Round -> {
                mWidth = w
                mHeight = h
                buildRoundPath()
            }
            ShapeType.Circle -> {
                buildCirclePath()
            }
        }
    }

    private fun buildCirclePath() {//构建圆形类型的Path路径
        if (!mShowBorder) {//绘制不带边框的圆形实际上只需要把一个圆形扔进path即可
            mShapePath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW)
        } else {//绘制带边框的圆形需要把内部圆形和外部圆形边框都要扔进path
            mShapePath.addCircle(mRadius, mRadius, mRadius - mBorderWidth, Path.Direction.CW)
            mBorderPath.addCircle(
                mRadius,
                mRadius,
                mRadius - mBorderWidth / 2.0f,
                Path.Direction.CW
            )
        }
    }

    private fun buildRoundPath() {//构建圆角类型的Path路径
        if (!mShowBorder) {//绘制不带边框的圆角实际上只需要把一个圆角矩形扔进path即可
            floatArrayOf(
                mLeftTopRadiusX, mLeftTopRadiusY,
                mRightTopRadiusX, mRightTopRadiusY,
                mRightBottomRadiusX, mRightBottomRadiusY,
                mLeftBottomRadiusX, mLeftBottomRadiusY
            ).run {
                mShapePath.addRoundRect(
                    RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat()),
                    this,
                    Path.Direction.CW
                )
            }

        } else {//绘制带边框的圆角实际上只需要把一个圆角矩形和一个圆角矩形的变量都扔进path即可
            floatArrayOf(
                mLeftTopRadiusX - mBorderWidth / 2.0f,
                mLeftTopRadiusY - mBorderWidth / 2.0f,
                mRightTopRadiusX - mBorderWidth / 2.0f,
                mRightTopRadiusY - mBorderWidth / 2.0f,
                mRightBottomRadiusX - mBorderWidth / 2.0f,
                mRightBottomRadiusY - mBorderWidth / 2.0f,
                mLeftBottomRadiusX - mBorderWidth / 2.0f,
                mLeftBottomRadiusY - mBorderWidth / 2.0f
            ).run {
                mBorderPath.addRoundRect(
                    RectF(
                        mBorderWidth / 2.0f,
                        mBorderWidth / 2.0f,
                        mWidth.toFloat() - mBorderWidth / 2.0f,
                        mHeight.toFloat() - mBorderWidth / 2.0f
                    ), this, Path.Direction.CW
                )
            }

            floatArrayOf(
                mLeftTopRadiusX - mBorderWidth, mLeftTopRadiusY - mBorderWidth,
                mRightTopRadiusX - mBorderWidth, mRightTopRadiusY - mBorderWidth,
                mRightBottomRadiusX - mBorderWidth, mRightBottomRadiusY - mBorderWidth,
                mLeftBottomRadiusX - mBorderWidth, mLeftBottomRadiusY - mBorderWidth
            ).run {
                mShapePath.addRoundRect(
                    RectF(
                        mBorderWidth,
                        mBorderWidth,
                        mWidth.toFloat() - mBorderWidth,
                        mHeight.toFloat() - mBorderWidth
                    ),
                    this, Path.Direction.CW
                )
            }

        }
    }

    override fun onDraw(canvas: Canvas?) {//由于经过以上根据不同逻辑构建了boderPath和shapePath,path中已经储存相应的形状，现在只需要把相应shapePath中形状用带BitmapShader画笔绘制出来,boderPath用普通画笔绘制出来即可
        drawable ?: return
        mBitmapPaint.shader = getBitmapShader()//获得相应的BitmapShader着色器对象
        when (mShapeType) {
            ShapeType.Circle -> {
                if (mShowBorder) {
                    canvas?.drawPath(mBorderPath, mBorderPaint)//绘制圆形图片边框path
                }
                canvas?.drawPath(mShapePath, mBitmapPaint)//绘制圆形图片形状path
            }
            ShapeType.Round -> {
                if (mShowBorder) {
                    canvas?.drawPath(mBorderPath, mBorderPaint)//绘制圆角图片边框path
                }
                canvas?.drawPath(mShapePath, mBitmapPaint)//绘制圆角图片形状path
            }
        }
    }

    private fun getBitmapShader(): BitmapShader {
        val bitmap = drawableToBitmap(drawable)
        return BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
            var scale = 1.0f
            if (mShapeType == ShapeType.Circle) {
                scale = (mWidth * 1.0F) / (Math.min(bitmap.width, bitmap.height) * 1.0F)
            } else if (mShapeType == ShapeType.Round) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                if (!(width == bitmap.width && width == bitmap.height)) {
                    scale = Math.max(width * 1.0f / bitmap.width, height * 1.0f / bitmap.height)
                }
            }
            // shader的变换矩阵，我们这里主要用于放大或者缩小
            mMatrix.setScale(scale, scale)
            setLocalMatrix(mMatrix)
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val drawableW = if (drawable.intrinsicWidth > 0) {
            drawable.intrinsicWidth
        } else {
            if (width > 0) {
                width
            } else {
                10
            }
        }
        val drawableH = if (drawable.intrinsicHeight > 0) {
            drawable.intrinsicHeight
        } else {
            if (height > 0) {
                height
            } else {
                10
            }
        }
        return Bitmap.createBitmap(drawableW, drawableH, Bitmap.Config.ARGB_8888).apply {
            drawable.setBounds(0, 0, drawableW, drawableH)
            drawable.draw(Canvas(this@apply))
        }
    }

    companion object {
        private const val STATE_INSTANCE = "state_instance"
        private const val STATE_INSTANCE_SHAPE_TYPE = "state_shape_type"
        private const val STATE_INSTANCE_BORDER_WIDTH = "state_border_width"
        private const val STATE_INSTANCE_BORDER_COLOR = "state_border_color"
        private const val STATE_INSTANCE_RADIUS_LEFT_TOP_X = "state_radius_left_top_x"
        private const val STATE_INSTANCE_RADIUS_LEFT_TOP_Y = "state_radius_left_top_y"
        private const val STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X = "state_radius_left_bottom_x"
        private const val STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y = "state_radius_left_bottom_y"
        private const val STATE_INSTANCE_RADIUS_RIGHT_TOP_X = "state_radius_right_top_x"
        private const val STATE_INSTANCE_RADIUS_RIGHT_TOP_Y = "state_radius_right_top_y"
        private const val STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X = "state_radius_right_bottom_x"
        private const val STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y = "state_radius_right_bottom_y"
        private const val STATE_INSTANCE_RADIUS = "state_radius"
        private const val STATE_INSTANCE_SHOW_BORDER = "state_radius_show_border"
    }

    //View State Save
    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable(STATE_INSTANCE, super.onSaveInstanceState())
        putInt(
            STATE_INSTANCE_SHAPE_TYPE, when (mShapeType) {
                ShapeType.Circle -> 0
                ShapeType.Round -> 1
            }
        )
        putFloat(STATE_INSTANCE_BORDER_WIDTH, mBorderWidth)
        putInt(STATE_INSTANCE_BORDER_COLOR, mBorderColor)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_X, mLeftTopRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_Y, mLeftTopRadiusY)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X, mLeftBottomRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y, mLeftBottomRadiusY)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_X, mRightTopRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_Y, mRightTopRadiusY)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X, mRightBottomRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y, mRightBottomRadiusY)
        putFloat(STATE_INSTANCE_RADIUS, mRadius)
        putBoolean(STATE_INSTANCE_SHOW_BORDER, mShowBorder)
    }

    //View State Restore
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }

        with(state) {
            super.onRestoreInstanceState(getParcelable(STATE_INSTANCE))
            mShapeType = when {
                getInt(STATE_INSTANCE_SHAPE_TYPE) == 0 -> ShapeType.Circle
                getInt(STATE_INSTANCE_SHAPE_TYPE) == 1 -> ShapeType.Round
                else -> ShapeType.Circle
            }
            mBorderWidth = getFloat(STATE_INSTANCE_BORDER_WIDTH)
            mBorderColor = getInt(STATE_INSTANCE_BORDER_COLOR)
            mLeftTopRadiusX = getFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_X)
            mLeftTopRadiusY = getFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_Y)
            mLeftBottomRadiusX = getFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X)
            mLeftBottomRadiusY = getFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y)
            mRightTopRadiusX = getFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_X)
            mRightTopRadiusY = getFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_Y)
            mRightBottomRadiusX = getFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X)
            mRightBottomRadiusY = getFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y)
            mRadius = getFloat(STATE_INSTANCE_RADIUS)
            mShowBorder = getBoolean(STATE_INSTANCE_SHOW_BORDER)
        }
    }

}