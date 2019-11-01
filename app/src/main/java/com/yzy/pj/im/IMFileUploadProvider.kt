package com.yzy.pj.im

/**
 *description: 消息文件上传(包含图片)的提供者.
 *@date 2019/3/12 10:24.
 *@author: yzy.
 */
interface IMFileUploadProvider {

    /**
     * 上传文件
     * @param filePath 文件地址
     * @param success 上传成功的回调
     * @param failed 上传失败的回调
     * @param progress 文件上传进度
     */
    fun uploadFile(
        filePath: String,
        success: (filePath: String, url: String) -> Unit,
        failed: (filePath: String?) -> Unit,
        progress: (filePath: String, progress: Int) -> Unit
    )

}