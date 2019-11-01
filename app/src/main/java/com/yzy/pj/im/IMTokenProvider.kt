package com.yzy.pj.im

/**
 *description: IM token的提供者.
 *@date 2019/3/12 10:57.
 *@author: yzy.
 */
interface IMTokenProvider {

    /**
     * 上传文件
     * @param success  token获取成功
     */
    fun getToken(success: (token: String) -> Unit)

}