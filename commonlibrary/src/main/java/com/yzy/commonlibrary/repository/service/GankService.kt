package com.yzy.commonlibrary.repository.service

import com.yzy.commonlibrary.repository.bean.GankBaseBean
import com.yzy.commonlibrary.repository.bean.FuliBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *description: Gank网络请求的Service.
 *@date 2019/7/15
 *@author: yzy.
 */
interface GankService {

  /**
   * 根据日期获取福利的接口
   */
  @GET("data/福利/{month}/{day}")
  fun getFuli(@Path("month") month: Int, @Path("day") day: Int): Observable<GankBaseBean<FuliBean>>

}