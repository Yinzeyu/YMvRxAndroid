package com.yzy.example.imModel

import com.yzy.example.im.IMFileUploadProvider

/**
 *description: IM的文件上传具体实现类.
 *@date 2019/3/28 11:41.
 *@author: YangYang.
 */
class IMFileUploadProviderImpl : IMFileUploadProvider {
    override fun uploadFile(
            filePath: String,
            success: (filePath: String, url: String) -> Unit,
            failed: (filePath: String?) -> Unit,
            progress: (filePath: String, progress: Int) -> Unit) {
//    var isNeedCompression = true
//    val type = if (filePath.toLowerCase().endsWith(".mp4")) {
//      isNeedCompression = false
//      ResourceRepository.BucketType.Media.value
//    } else {
//      ResourceRepository.BucketType.Image.value
//    }
//    val params = mapOf("bucketType" to type)
//    val listener = object : IBaseUpLoadListener {
//      override fun onUpLoadSuccess(filePath: String?, url: String?, fileSize: Long) {
//        filePath?.let { path ->
//          url?.let { result ->
//            success.invoke(path, result)
//          }
//        }
//      }
//
//      override fun onUpLoadFailed(filePath: String?, throwable: Throwable?) {
//        failed.invoke(filePath)
//      }
//
//      override fun onProgress(filePath: String?, percent: Int) {
//        filePath?.let { path ->
//          progress.invoke(path, percent)
//        }
//      }
//    }
//    if (type == ResourceRepository.BucketType.Media.value) {
//      //视频要先压缩再上传
//      AMVideoCompress.compress(filePath,
//        AppFileDirManager.getImVideoDir(App.INSTANCE),
//        success = { result ->
//          AimyUpload.getInstance()
//            .uploadFile(
//              OauthRepository.getUserId(),
//              result,
//              params,
//              listener,
//              isNeedCompression,
//              true
//            )
//        },
//        failed = {
//          failed.invoke(filePath)
//        })
//    } else {
//      //图片直接上传
//      AimyUpload.getInstance()
//        .uploadFile(
//          OauthRepository.getUserId(),
//          filePath,
//          params,
//          listener,
//          isNeedCompression,
//          true
//        )
//    }
    }
}