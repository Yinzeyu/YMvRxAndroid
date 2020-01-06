package com.yzy.example.widget

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.example.R
import com.yzy.example.utils.MediaHelper
import com.yzy.example.utils.PermissionUtils
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * description : 消息模块录音控件,如果需要上下移动,设置paddingTop即可
 *
 * @author : case
 * @date : 2018/11/1 12:26
 */
class RecorderView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    //正常显示的中间录音按钮
    private var bitReNormal: Bitmap? = null
    //按下显示的中间录音按钮
    private var bitRePress: Bitmap? = null
    //绘制录音的画笔
    private var paintRe: Paint = Paint()
    //绘制竖线的画笔
    private var paintLine: Paint = Paint()
    //清除画布的画笔
    private var paintClear: Paint = Paint()
    //绘制文字的画笔
    private var paintText: Paint = Paint()
    // 绘制底部文字的画笔
    private var paintToastText: Paint = Paint()
    //竖线间的距离
    private var distanceLine = 0
    //竖线的宽度
    private var widthLine = 0
    //一边竖线的数量
    private var countLineOnSide = 8
    //竖线最大长度
    private var lenLineMin = 0
    //竖线最小长度
    private var lenLineMax = 0
    //默认竖线颜色
    private var colorLineNor = Color.parseColor("#A28DFF")
    //按下竖线颜色
    private var colorLinePres = Color.parseColor("#FF537D")
    private var bgColor = Color.parseColor("#f3f3f3")
    private var defaultTextColor = Color.parseColor("#a3a3a3")
    //是否处于按压状态
    private var isCanSpeak = false
    //是否变为取消
    private var isCancel = false
    //是否处于按压状态
    private var isTouch = false
    //默认执行
    private val linkList = LinkedList<Float>()
    //监听
    var listener: OnRecorderListener? = null
    //录音
    private var mediaRecorderHelper: MediaHelper? = null
    //是否在录制中
    private var isRecording = false
    //最大录制时长
    var maxDuration = 60 * 1000L
    //开始录制的时间
    private var startRecordTime = 0L
    //时间大小
    private var timeSize = 0
    //文字大小
    private var textSize = 0
    //文件间距
    private var distanceText = 0
    //文字和图片的偏移量
    private var distanceBitText = 0
    //1dp
    private var dp1 = 0
    //控件最小一半的高度值
    private var minHalfHeight = 0f
    //录音圆圈的半径
    private var circleRadius = 0
    //文字上边间距
    private var textPaddingTop = 0
//    private var permissionDisposable: Disposable? = null

    init {
        bitReNormal = BitmapFactory.decodeResource(resources, R.drawable.ic_record)
        bitRePress = BitmapFactory.decodeResource(resources, R.drawable.ic_record_red)
        widthLine = dp2px(4f)
        paintLine.strokeWidth = widthLine.toFloat()
        paintLine.style = Paint.Style.FILL
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.isAntiAlias = true
        paintText.isAntiAlias = true
        paintToastText.isAntiAlias = true
        paintClear.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        distanceLine = dp2px(5f)
        distanceText = dp2px(4f)
        distanceBitText = dp2px(9f)
        circleRadius = dp2px(45f)
        dp1 = dp2px(1f)
        lenLineMin = (dp2px(4f) - widthLine).coerceAtLeast(1)
        lenLineMax = (dp2px(37f) - widthLine).coerceAtLeast(lenLineMin)
        timeSize = dp2px(14f)
        textSize = dp2px(12f)
        textPaddingTop = dp2px(20f)
        for (i in 0 until countLineOnSide) {
            linkList.add(0f)
        }
        initRecorderBuilder()
    }


    private fun initRecorderBuilder() {
        mediaRecorderHelper = MediaHelper(myOnRecorderListener)
        mediaRecorderHelper!!.setOnMaxAmplitudeListener {
            linkList.pop()
            linkList.add(Math.min(1f, Math.max(0f, (it.toFloat() - 45)) / 50f))
            if (System.currentTimeMillis() - startRecordTime >= maxDuration) {
                stopRecording()
            }
            invalidate()
        }
    }

    private var myOnRecorderListener = MediaHelper.OnRecorderListener { _, duration, _ ->
        linkList.pop()
        linkList.add(1f.coerceAtMost(0f.coerceAtLeast((duration.toFloat() - 45)) / 50f))
        if (System.currentTimeMillis() - startRecordTime >= maxDuration) {
            stopRecording()
        }
        invalidate()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) {
                if (!checkAudioPermission()) {
                    return true
                }
                isTouch = true
                isCancel = false
                //圆心坐标
                val centerX = width / 2f
                val centerY = minHalfHeight
                //点击位置x坐标与圆心的x坐标的距离
                val distanceX: Float = abs(centerX - event.x)
                //点击位置y坐标与圆心的y坐标的距离
                val distanceY: Float = abs(centerY - event.y)
                //点击位置与圆心的直线距离
                val distanceZ: Float =
                        sqrt(distanceX.toDouble().pow(2.0) + distanceY.toDouble().pow(2.0)).toFloat()
                //点中中间的圆才算开始录音
                isCanSpeak = distanceZ <= circleRadius
                if (isCanSpeak) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    startRecording()
                }
                invalidate()
            } else if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_MOVE) {
                isCancel = (event.y < minHalfHeight - bitReNormal!!.height / 2f) && isRecording
            } else if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP
                    || event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_CANCEL
            ) {
                if (isCancel && listener != null && isRecording) {
                    listener?.cancelRecorder()
                }
                isTouch = false
                isCanSpeak = false
                stopRecording()
                parent.requestDisallowInterceptTouchEvent(false)
                invalidate()
            }
        }
        return true
    }

    /**
     * 检测有没有录音权限
     */
    private fun checkAudioPermission(): Boolean {
        var audioPermissions = false
//    if (RTVoiceManager.isInRoom()) {
//      //在聊天房间中不用判断权限
//      context.toast("无法同时连麦与录音哦")
//      audioPermissions = false
//      return audioPermissions
//    }
        if (!PermissionUtils.hasRecordPermission()) {
//            permissionDisposable = RxPermissions(context as BaseActivity)
//                    .request(Manifest.permission.RECORD_AUDIO)
//                    .subscribe {
//                        if (it) {
//                            //这个返回true不准
//                            audioPermissions = if (PermissionUtils.hasRecordPermission()) {
//                                true
//                            } else {
//                                LogUtils.e("showAudioRecordPermissionDialog")
////                PermissionUtils.showAudioRecordPermissionDialog(context as ChatActivity)
//                                false
//                            }
//                        }
//                    }
        } else {
            audioPermissions = true
        }
        return audioPermissions
    }

    //开始录制
    private fun startRecording() {
        if (isRecording) return
        isRecording = true

        mediaRecorderHelper?.startRecorder()
        startRecordTime = System.currentTimeMillis()
        listener?.startRecorder()
    }

    //停止录制
    private fun stopRecording() {
        if (isRecording) {
            mediaRecorderHelper?.stopRecorder()
            isRecording = false
            if (listener != null && !isCancel && mediaRecorderHelper != null) {
                listener?.completeRecorder(
                        Uri.fromFile(mediaRecorderHelper?.voiceFile), mediaRecorderHelper?.duration ?: 0
                )
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            if (minHalfHeight == 0f) {
                minHalfHeight =
                        paddingTop + bitReNormal!!.height / 2f + distanceBitText + distanceText + textSize + timeSize
            }
            //清除画布
            canvas.drawPaint(paintClear)
            canvas.drawColor(bgColor)
            paintToastText.color = defaultTextColor
            drawBitmap(canvas, if (isTouch && isCancel) bitRePress!! else bitReNormal!!)
            if (isRecording && isTouch && isCanSpeak) {
                paintLine.color = if (isTouch && isCancel) colorLinePres else colorLineNor
                paintText.color = if (isTouch && isCancel) colorLinePres else colorLineNor
                drawText(canvas)
                drawLeftLine(canvas)
                drawRightLine(canvas)
            }
            drawToastText(canvas)
        }
    }

    //绘制文字
    private fun drawText(canvas: Canvas) {
        paintText.textSize = textSize.toFloat()
        val textStr = if (isCancel) "松手取消" else "上滑取消"
        val lenText = paintText.measureText(textStr)
        val textY = minHalfHeight - bitReNormal!!.height / 2f - textSize + distanceBitText
        //绘制文字
        canvas.drawText(textStr, width / 2f - lenText / 2f, textY, paintText)

        val time = maxDuration.coerceAtMost(System.currentTimeMillis() - startRecordTime)
        val timeStr = getPlayTimeMinAndSecond(time)
        paintText.textSize = timeSize.toFloat()
        val lenTime = paintText.measureText(timeStr)
        //绘制时间
        canvas.drawText(
                timeStr,
                width / 2f - lenTime / 2f - dp1,
                textY - distanceText - timeSize,
                paintText
        )
    }


    private fun drawToastText(canvas: Canvas) {
        paintToastText.textSize = textSize.toFloat()
        val textStr = if (!isRecording) "按住说话" else ""
        val lenText = paintToastText.measureText(textStr)
        val textY = minHalfHeight + bitReNormal!!.height / 2f + textPaddingTop
        //绘制文字
        canvas.drawText(textStr, width / 2f - lenText / 2f, textY, paintToastText)


    }

    //绘制图片
    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(
                bitmap,
                (width - bitmap.width) / 2f,
                minHalfHeight - bitmap.height / 2f,
                paintRe
        )
    }

    //绘制左边竖线
    private fun drawLeftLine(canvas: Canvas) {
        val posX = (width - bitReNormal!!.width) / 2f - widthLine / 2f
        val posY = minHalfHeight
        val len = (lenLineMax - lenLineMin) / 2f
        for (i in 0 until countLineOnSide) {
            val startX = posX - i * (widthLine + distanceLine)
            val startY = posY - lenLineMin / 2f - len * linkList[countLineOnSide - i - 1] / 2f
            val endY = posY + lenLineMin / 2f + len * linkList[countLineOnSide - i - 1] / 2f
            canvas.drawLine(startX, startY, startX, endY, paintLine)
        }
    }

    //绘制右边竖线
    private fun drawRightLine(canvas: Canvas) {
        val posX = (width + bitReNormal!!.width) / 2f + widthLine / 2f
        val posY = minHalfHeight
        val len = (lenLineMax - lenLineMin) / 2f
        for (i in 0 until countLineOnSide) {
            val startX = posX + i * (widthLine + distanceLine)
            val startY = posY - lenLineMin / 2f - len * linkList[countLineOnSide - i - 1] / 2f
            val endY = posY + lenLineMin / 2f + len * linkList[countLineOnSide - i - 1] / 2f
            canvas.drawLine(startX, startY, startX, endY, paintLine)
        }
    }

    //转换工具
    private fun dp2px(dp: Float): Int {
        val fontScale = context.resources.displayMetrics.density
        return (dp * fontScale + 0.5f).toInt()
    }

    /**时间 MM:SS*/
    private fun getPlayTimeMinAndSecond(time: Long): String {
        val minutes = time / 1000 / 60
        val seconds = time / 1000 % 60
        return (if (minutes < 10) "0$minutes" else minutes.toString()) + ":" +
                if (seconds < 10) "0$seconds" else seconds.toString()
    }

    interface OnRecorderListener {
        fun completeRecorder(path: Uri, duration: Int)
        fun cancelRecorder()
        fun startRecorder()
    }
}