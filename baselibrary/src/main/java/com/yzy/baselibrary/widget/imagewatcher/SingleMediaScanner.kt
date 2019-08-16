package com.aimymusic.midamusic.widget.imagewatcher

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

/**
 *description: 媒体库刷新的类.
 *@date 2019/7/5 18:17.
 *@author: YangYang.
 */
class SingleMediaScanner(context: Context, val path: String) :
  MediaScannerConnection.MediaScannerConnectionClient {

  private val mediaScannerConnection: MediaScannerConnection = MediaScannerConnection(context, this)

  init {
    mediaScannerConnection.connect()
  }

  override fun onMediaScannerConnected() {
    mediaScannerConnection.scanFile(path, null)
  }

  override fun onScanCompleted(path: String?, uri: Uri?) {
    mediaScannerConnection.disconnect()
  }
}