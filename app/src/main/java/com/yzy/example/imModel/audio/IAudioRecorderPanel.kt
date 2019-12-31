package com.yzy.example.imModel.audio

import android.view.View
import android.widget.Button

interface IAudioRecorderPanel {

    /**
     * 设置录音的回调
     */
    fun setRecordListener(recordListener: OnRecordListener)

    /**
     * 将录音控件和按键绑定
     */
    fun attach(rootView: View, button: Button)


}

interface OnRecordListener {
    fun onRecordSuccess(audioFile: String, duration: Long)

    fun onRecordFail(e: Exception)

    fun onNoPermission()

    fun onRecordStateChanged(state: RecordState)
}

enum class RecordState {
    // 开始录音
    START,
    // 录音中
    RECORDING,
    // 用户准备取消
    TO_CANCEL,
    // 最长录音时间开到
    TO_TIMEOUT,
    // 录音结束
    STOP
}
