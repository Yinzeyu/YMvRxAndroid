//package com.yzy.example.imageloader
//
//import android.os.Handler
//import android.os.Looper
//import okhttp3.MediaType
//import okhttp3.ResponseBody
//import okio.*
//
//import java.io.IOException
//
///**
// * description: Glide加载图片的进度回调.
// *
// * @date 2019/5/25 11:47.
// * @author: YangYang.
// */
//class ProgressResponseBody internal constructor(
//    private val url: String,
//    private val internalProgressListener: InternalProgressListener?,
//    private val responseBody: ResponseBody
//) : ResponseBody() {
//    private var bufferedSource: BufferedSource? = null
//
//    override fun contentType(): MediaType? {
//        return responseBody.contentType()
//    }
//
//    override fun contentLength(): Long {
//        return responseBody.contentLength()
//    }
//
//    override fun source(): BufferedSource {
//        if (bufferedSource == null) {
//            bufferedSource = source(responseBody.source()).buffer()
//        }
//        return bufferedSource!!
//    }
//
//    private fun source(source: Source): Source {
//        return object : ForwardingSource(source) {
//            var totalBytesRead: Long = 0
//            var lastTotalBytesRead: Long = 0
//            @Throws(IOException::class)
//            override fun read(sink: Buffer, byteCount: Long): Long {
//                val bytesRead = super.read(sink, byteCount)
//                totalBytesRead += if (bytesRead == -1L) 0 else bytesRead
//                if (lastTotalBytesRead != totalBytesRead) {
//                    lastTotalBytesRead = totalBytesRead
//                    mainThreadHandler.post {
//                        internalProgressListener?.onProgress(
//                                url,
//                                totalBytesRead,
//                                contentLength()
//                        )
//                    }
//                }
//                return bytesRead
//            }
//        }
//    }
//
//    internal interface InternalProgressListener {
//        fun onProgress(url: String, bytesRead: Long, totalBytes: Long)
//    }
//
//    companion object {
//        private val mainThreadHandler = Handler(Looper.getMainLooper())
//    }
//}
