package com.yzy.example.imModel


import com.yzy.example.im.IMTokenProvider

/**
 *description: IM获取登录的token的实现类.
 *@date 2019/3/28 11:42.
 *@author: YangYang.
 */
class IMTokenProviderImpl : IMTokenProvider {

    override fun getToken(success: (token: String) -> Unit) {
//    val oauthRepository: OauthRepository by App.INSTANCE.kodein.instance()
//    val disposable = oauthRepository.getRongCloudToken()
//      .subscribe({
//        success.invoke(it.token)
//      }, {
//        LogUtils.e("融云token获取失败", it)
//      })
    }
}