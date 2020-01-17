package com.yzy.example.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.onEnd
import com.yzy.baselibrary.extention.onUpdate
import com.yzy.example.R
import java.lang.ref.WeakReference


class VideoControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var excicleMagnification = 0f
    private var innerCircleShrinks = 0f
    /**
     * 视频实际录制最大时间
     */
    private var mMaxTime = 0
    /**
     * 视频实际录制最小时间
     */
    private var mMinTime = 0
    /**
     * 外圆半径
     */
    private var mExCircleRadius = 0f
    private var mInitExCircleRadius = 0f
    /**
     * 内圆半径
     */
    private var mInnerCircleRadius = 0f
    private var mInitInnerRadius = 0f
    /**
     * 外圆颜色
     */
    private var mAnnulusColor = 0
    /**
     * 内圆颜色
     */
    private var mInnerCircleColor = 0
    /**
     * 进度条颜色
     */
    private var mProgressColor = 0
    /**
     * 外圆画笔
     */
    private var mExCirclePaint: Paint = Paint()
    /**
     * 内圆画笔
     */
    private var mInnerCirclePaint: Paint = Paint()
    /**
     * 进度条画笔
     */
    private var mProgressPaint: Paint = Paint()
    /**
     * 是否正在录制
     */
    private var isRecording = false
    /**
     * 进度条值动画
     */
    private var mProgressAni: ValueAnimator = ValueAnimator.ofFloat(0f, 360f)
    /**
     * 开始录制时间
     */
    private var mStartTime: Long = 0
    /**
     * 录制 结束时间
     */
    private var mEndTime: Long = 0
    private var mWidth =SizeUtils.dp2px(100f).toFloat()
    private var mHeight = SizeUtils.dp2px(100f).toFloat()
    private var mCurrentProgress = 0f
    private val handler = MHandler(this)

    init {
        val a: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.VideoControlView, defStyleAttr, 0)
        mMaxTime = a.getInt(R.styleable.VideoControlView_maxTime, 10)
        mMinTime = a.getInt(R.styleable.VideoControlView_minTime, 1)
        excicleMagnification = a.getFloat(R.styleable.VideoControlView_excicleMagnification, 1.25f)
        innerCircleShrinks = a.getFloat(R.styleable.VideoControlView_excicleMagnification, 0.75f)
//        if (excicleMagnification < 1) {
//            throw RuntimeException("外圆放大倍数必须大于1")
//        }
//        if (innerCircleShrinks > 1) {
//            throw RuntimeException("内圆缩小倍数必须小于1")
//        }
        mExCircleRadius = a.getDimension(R.styleable.VideoControlView_excircleRadius, 12f)
        mInitExCircleRadius = mExCircleRadius
        mInnerCircleRadius = a.getDimension(R.styleable.VideoControlView_innerCircleRadius, 5f)
        mInitInnerRadius = mInnerCircleRadius
        mAnnulusColor = a.getColor(R.styleable.VideoControlView_annulusColor, Color.parseColor("#FFFFFF"))
        mInnerCircleColor =
            a.getColor(R.styleable.VideoControlView_innerCircleColor, Color.parseColor("#F5F5F5"))
        mProgressColor =
            a.getColor(R.styleable.VideoControlView_progressColor, Color.parseColor("#00A653"))
        a.recycle()
        //初始化外圆画笔
        mExCirclePaint.color = mAnnulusColor
        //初始化内圆画笔
        mInnerCirclePaint.color = mInnerCircleColor
        //初始化进度条画笔
        mProgressPaint.apply {
            color = mProgressColor
            strokeWidth = mExCircleRadius - mInnerCircleRadius
            style = Paint.Style.STROKE
        }
        //进度条的属性动画
        mProgressAni.duration = mMaxTime * 1000.toLong()
    }
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        if (w > 0 && h > 0) {
//            mWidth = w.toFloat()
//            mHeight = h.toFloat()
//
//        }
//    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        mWidth = MeasureSpec.getSize(widthMeasureSpec)
//        mHeight = MeasureSpec.getSize(heightMeasureSpec)
//        if (mExCircleRadius * 2 * excicleMagnification > min(mWidth, mHeight)) {
//            throw RuntimeException("设置的半径的2 * " + excicleMagnification + "倍要小于宽和高中的最小值的")
//        }
//        if (mInnerCircleRadius > mExCircleRadius) {
//            throw RuntimeException("设置的内圆半径要小于外圆半径")
//        } else if (mInnerCircleRadius == mExCircleRadius) {
//            Log.e(javaClass.name, "mInnerCircleRadius == mExCircleRadius 你将看不到进度条")
//        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画外圆
//        canvas.drawCircle(mWidth, mHeight, mExCircleRadius, mExCirclePaint)
        //画内圆
        canvas.drawCircle(mWidth, mHeight, 0f, mInnerCirclePaint)
        if (isRecording) {
//            drawProgress(canvas)
        }
    }

    /**
     * 绘制圆形进度条
     * Draw a circular progress bar.
     *
     * @param canvas
     */
//    private fun drawProgress(canvas: Canvas) {
//        val rectF = RectF(
//            mWidth / 2 - (mInnerCircleRadius + (mExCircleRadius - mInnerCircleRadius) / 2),
//            mHeight / 2 - (mInnerCircleRadius + (mExCircleRadius - mInnerCircleRadius) / 2),
//            mWidth / 2 + (mInnerCircleRadius + (mExCircleRadius - mInnerCircleRadius) / 2),
//            mHeight / 2 + (mInnerCircleRadius + (mExCircleRadius - mInnerCircleRadius) / 2)
//        )
//        canvas.drawArc(rectF, -90f, mCurrentProgress, false, mProgressPaint)
//    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isRecording = true
                mStartTime = System.currentTimeMillis()
                handler.sendEmptyMessageDelayed(
                    MSG_START_LONG_RECORD,
                    800
                )
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isRecording = false
                mEndTime = System.currentTimeMillis()
                if (mEndTime - mStartTime < 800) { //Long press the action time too short.
                    if (handler.hasMessages(MSG_START_LONG_RECORD)) {
                        handler.removeMessages(MSG_START_LONG_RECORD)
                    }
                    onRecordListener?.onShortClick()
                } else {
                    if (mProgressAni.currentPlayTime / 1000 < mMinTime) { //The recording time is less than the minimum recording time.
                        onRecordListener?.OnFinish(0)
                    } else { //The end of the normal
                        onRecordListener?.OnFinish(1)
                    }
                }
                mExCircleRadius = mInitExCircleRadius
                mInnerCircleRadius = mInitInnerRadius
                mProgressAni.cancel()
                startAnimation(
                    mInitExCircleRadius * excicleMagnification,
                    mInitExCircleRadius,
                    mInitInnerRadius * innerCircleShrinks,
                    mInitInnerRadius
                )
            }
            MotionEvent.ACTION_MOVE -> {
            }
        }
        return true
    }

    /**
     * 设置外圆 内圆缩放动画
     *
     * @param bigStart
     * @param bigEnd
     * @param smallStart
     * @param smallEnd
     */
    private fun startAnimation(
        bigStart: Float,
        bigEnd: Float,
        smallStart: Float,
        smallEnd: Float
    ) {
        val bigObjAni = ValueAnimator.ofFloat(bigStart, bigEnd)
        bigObjAni?.apply {
            duration = 150
            onUpdate { v ->
                mExCircleRadius = v as Float
                invalidate()
            }
        }
        val smallObjAni = ValueAnimator.ofFloat(smallStart, smallEnd)
        smallObjAni?.apply {
            duration = 150
            onUpdate { v ->
                mInnerCircleRadius = v as Float
                invalidate()
            }
            onEnd {
                if (isRecording) {
                    startAniProgress()
                }
            }
        }
        bigObjAni.start()
        smallObjAni.start()

    }

    /**
     * 开始圆形进度值动画
     */
    private fun startAniProgress() {
        mProgressAni.apply {
            start()
            onUpdate {v->
                mCurrentProgress = v as Float
                invalidate()
            }
            onEnd {
                isRecording = false
                mCurrentProgress = 0f
                invalidate()
            }
        }
    }

    /**
     * 设置颜色外圆颜色
     *
     * @param mAnnulusColor
     */
    fun setAnnulusColor(mAnnulusColor: Int) {
        this.mAnnulusColor = mAnnulusColor
        mExCirclePaint.color = mAnnulusColor
    }

    /**
     * 设置进度圆环颜色
     *
     * @param mProgressColor
     */
    fun setProgressColor(mProgressColor: Int) {
        this.mProgressColor = mProgressColor
        mProgressPaint.color = mProgressColor
    }

    /**
     * 设置内圆颜色
     *
     * @param mInnerCircleColor
     */
    fun setInnerCircleColor(mInnerCircleColor: Int) {
        this.mInnerCircleColor = mInnerCircleColor
        mInnerCirclePaint.color = mInnerCircleColor
    }

    private var onRecordListener: OnRecordListener? = null
    fun setOnRecordListener(onRecordListener: OnRecordListener?) {
        this.onRecordListener = onRecordListener
    }

    abstract class OnRecordListener {
        /**
         * 点击拍照
         */
        abstract fun onShortClick()

        /**
         * 开始录制
         */
        abstract fun OnRecordStartClick()

        /**
         * 录制结束
         *
         * @param resultCode 0 录制时间太短 1 正常结束
         */
        abstract fun OnFinish(resultCode: Int)
    }

    internal class MHandler(controlView: VideoControlView?) : Handler() {
        private var weakReference: WeakReference<VideoControlView>? = null
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            weakReference?.let {
                it.get()?.let { videoControlView ->
                    when (msg.what) {
                        MSG_START_LONG_RECORD -> {
                            videoControlView.onRecordListener?.OnRecordStartClick()
                            //内外圆动画，内圆缩小，外圆放大
                            videoControlView.startAnimation(
                                videoControlView.mExCircleRadius,
                                videoControlView.mExCircleRadius * videoControlView.excicleMagnification,
                                videoControlView.mInnerCircleRadius,
                                videoControlView.mInnerCircleRadius * videoControlView.excicleMagnification
                            )
                        }
                    }
                }
            }

        }

        init {
            weakReference = WeakReference<VideoControlView>(controlView)
        }
    }

    companion object {
        private const val MSG_START_LONG_RECORD = 0x1
    }

}
