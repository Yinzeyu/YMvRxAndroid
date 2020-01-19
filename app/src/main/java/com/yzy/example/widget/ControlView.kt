package com.yzy.example.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.extention.onEnd
import com.yzy.baselibrary.extention.onStart
import com.yzy.baselibrary.extention.onUpdate


class ControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mStartTime: Long = 0
    private val mLongClickTime: Long = 500 //长按最短时间(毫秒)，
    private val mMaxTime = 6000 //录制最大时间s
    private val mMinTime = 3000 //录制最短时间
    private var mHeight = 0 //当前View的高
    private var mWidth = 0//当前View的宽
    private var mInitBitRadius = 0f
    private var mInitSmallRadius = 0f
    private var mBigRadius = 0f
    private var mSmallRadius = 0f
    private var strokeWidth = 10f
    private val whatLongClick = 1
    private var mEndTime: Long = 0
    /**
     * 外圆画笔
     */
    private var mExCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 内圆画笔
     */
    private var mSmallCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 内圆画笔
     */
    private var mProgressCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    //清除画布的画笔
    private var paintClear: Paint = Paint()
    private var isRecording = false//录制状态
    private var isMaxTime = false //达到最大录制时间

    private var mCurrentProgress = 0f //当前进度
    private val mProgressAni: ValueAnimator =  ValueAnimator.ofFloat(0f, 360f) //圆弧进度变化

    init {
        mExCirclePaint.style = Paint.Style.FILL
        mExCirclePaint.color = Color.parseColor("#FFFFFF")

        mSmallCirclePaint.style = Paint.Style.FILL
        mSmallCirclePaint.color = Color.RED
        paintClear.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)


        mProgressCirclePaint.style = Paint.Style.STROKE
        mProgressCirclePaint.color = Color.parseColor("#6ABF66")
        mProgressCirclePaint.strokeWidth = strokeWidth
        mProgressAni.duration = (mMaxTime).toLong()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        val radius = mWidth / 1.8f * 0.75f
        mInitBitRadius = radius
        mBigRadius = radius
        val fl = mBigRadius * 0.9f
        mInitSmallRadius = fl
        mSmallRadius = fl
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //清除画布
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mBigRadius, mExCirclePaint)
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mSmallRadius, mSmallCirclePaint)
        //录制的过程中绘制进度条
        if (isRecording) {
            drawProgress(canvas)
        }
    }

    /**
     * 绘制圆形进度
     * @param canvas
     */
    private fun drawProgress(canvas: Canvas) {
        //用于定义的圆弧的形状和大小的界限
        val oval = RectF(
            mWidth / 2 - (mBigRadius - strokeWidth / 2),
            mHeight / 2 - (mBigRadius - strokeWidth / 2),
            mWidth / 2 + (mBigRadius - strokeWidth / 2),
            mHeight / 2 + (mBigRadius - strokeWidth / 2)
        )
        //根据进度画圆弧
        canvas.drawArc(oval, -90f, mCurrentProgress, false, mProgressCirclePaint)
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                whatLongClick -> {
                    //长按事件触发
                        onRecordClickListener?.onRecordStart()
                    //内外圆动画，内圆缩小，外圆放大
                    startAnimation(mBigRadius, mBigRadius * 1.2f, mSmallRadius, mSmallRadius * 0.3f)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                mStartTime = System.currentTimeMillis()
                val mMessage: Message = Message.obtain()
                mMessage.what = whatLongClick
                mHandler.sendMessageDelayed(mMessage, mLongClickTime)
            }
            MotionEvent.ACTION_UP -> {
                isPressed = false
                isRecording = false
                mEndTime = System.currentTimeMillis()
                if (mEndTime - mStartTime < mLongClickTime) {
                    mHandler.removeMessages(whatLongClick)
                    onRecordClickListener?.onClick()
                } else {
                    startAndEnd()
                }
            }
        }
        return true
    }

    private  fun startAndEnd(){
        //手指离开时动画复原
        startAnimation(mBigRadius, mInitBitRadius, mSmallRadius, mInitSmallRadius)
        if (mProgressAni.currentPlayTime / 1000 < mMinTime && !isMaxTime) {
            onRecordClickListener?.onNoMinRecord(mMinTime)
            mProgressAni.cancel()
        } else {
            //录制完成
            if (!isMaxTime) {
                onRecordClickListener?.onRecordFinished()
            }
        }
    }

    private fun startAnimation(
        bigStart: Float,
        bigEnd: Float,
        smallStart: Float,
        smallEnd: Float
    ) {
        val bigObjAni = ValueAnimator.ofFloat(bigStart, bigEnd)
        bigObjAni?.apply {
            duration = 150
            onUpdate {
                mBigRadius = it as Float
                invalidate()
            }
        }
        val smallObjAni = ValueAnimator.ofFloat(smallStart, smallEnd)
        smallObjAni?.apply {
            duration = 150
            onUpdate {
                mSmallRadius = it as Float
                invalidate()
            }
            onStart {
                isRecording = false
            }
            onEnd {
                //开始绘制圆形进度
                if (isPressed) {
                    isRecording = true
                    isMaxTime = false
                    startProgressAnimation()
                }
            }
        }
        bigObjAni.start()
        smallObjAni.start()
    }

    /**
     * 圆形进度变化动画
     */
    private fun startProgressAnimation() {
        mProgressAni.apply {
            onUpdate {
                mCurrentProgress = it as Float
                LogUtils.e("mCurrentProgress$mCurrentProgress")
                invalidate()
            }
            onEnd {
                if (isPressed) {
                    isPressed = false
                    isMaxTime = true
                    onRecordClickListener?.onRecordFinished()
                    startAnimation(mBigRadius, mInitBitRadius, mSmallRadius, mInitSmallRadius)
                    //影藏进度进度条
                    mCurrentProgress = 0f
                    invalidate()
                }
            }
            start()
        }
    }

    /**
     * 长按监听器
     */
    interface OnRecordClickListener {
        //未达到最小录制时间
        fun onNoMinRecord(currentTime: Int)

        //录制完成
        fun onRecordFinished()

        //录制完成
        fun onRecordStart()

        //拍照
        fun onClick()
    }

    var onRecordClickListener: OnRecordClickListener? = null


}