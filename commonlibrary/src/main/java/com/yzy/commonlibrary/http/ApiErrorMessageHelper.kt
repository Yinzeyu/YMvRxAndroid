import com.yzy.baselibrary.utils.SchedulersUtil
import io.reactivex.Observable

//package com.yzy.commonlibrary.http
//
//import com.yzy.baselibrary.utils.SchedulersUtil
//import io.reactivex.Observable
//
///**
// *description: 接口请求返回异常时的提示信息处理工具类.
// *@date 2019/7/15
// *@author: yzy.
// */
object ApiErrorMessageHelper {

  /**
   * 根据errorCode，显示相应的信息
   */
  fun showToastMessage(errorCode: Int, serviceMessage: String?) {
      serviceMessage?.let {
        Observable.just(it)
          .compose(SchedulersUtil.applySchedulers())
          .subscribe({

          }, {})
    }
  }
}