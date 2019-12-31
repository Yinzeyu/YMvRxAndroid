package com.yzy.example.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.blankj.utilcode.util.LogUtils

/**
 *description: 录音权限检测的工具类.
 *@date 2019/7/15 15:13.
 *@author: YangYang.
 */
class AudioPermissionHelper {

  companion object {
    /**
     * 是否有录音权限
     */
    fun hasRecordPermission(): Boolean {
      val minBufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_STEREO,
        AudioFormat.ENCODING_PCM_16BIT
      )
      var audioRecord: AudioRecord? = AudioRecord(
        MediaRecorder.AudioSource.MIC, 44100,
        AudioFormat.CHANNEL_IN_STEREO,
        AudioFormat.ENCODING_PCM_16BIT, minBufferSize
      )
      try {
        // 开始录音
        audioRecord?.startRecording()
      } catch (e: Exception) {
        //可能情况一
        audioRecord?.release()
        LogUtils.e("MMAudioRecorderPanel hasRecordPermission", e)
      }

      // 检测是否在录音中,6.0以下会返回此状态
      if (audioRecord?.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
        //可能情况二
        try {
          audioRecord?.stop()
          audioRecord?.release()
          audioRecord = null
        } catch (e: Exception) {
          LogUtils.e("MMAudioRecorderPanel hasRecordPermission", e)
        }
        return false
      }
      val bufferSizeInBytes = 1024
      val audioData = ByteArray(bufferSizeInBytes)
      var readSize = 0
      // 正在录音
      readSize = audioRecord?.read(audioData, 0, bufferSizeInBytes)
      if (readSize == AudioRecord.ERROR_INVALID_OPERATION || readSize <= 0) {
        return false
      }
      try {
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
      } catch (e: Exception) {
        LogUtils.e("MMAudioRecorderPanel hasRecordPermission", e)
      }
      return true
    }
  }
}