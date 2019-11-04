package com.yzy.commonlibrary.repository.service

import android.media.Image
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName
import com.yzy.commonlibrary.http.response.BaseResponse
import com.yzy.commonlibrary.repository.bean.*
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 *description: Gank网络请求的Service.
 *@date 2019/7/15
 *@author: yzy.
 */
interface GankService {

    /**
     * 根据日期获取福利的接口
     */

    @Headers("urlName:ganKUrl")
    @GET("api/data/Android/{pageSize}/{page}")
    fun getAndroid(@Path("pageSize") pageSize: Int, @Path("page") page: Int): Observable<GankBaseBean<GankAndroidBean>>


    @GET("banner/json")
    fun banner(
            @Query("page") page: String,
            @Query("size") size: String
    ): Observable<BaseResponse<MutableList<BannerBean>>>

    @GET("article/list/{page}/json")
    fun article(
            @Path("page") page: String
    ): Observable<BaseResponse<ArticleDataBean>>
}