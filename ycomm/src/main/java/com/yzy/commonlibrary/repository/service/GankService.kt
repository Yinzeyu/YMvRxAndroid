package com.yzy.commonlibrary.repository.service

import android.media.Image
import com.yzy.commonlibrary.repository.bean.GankBaseBean
import com.yzy.commonlibrary.repository.bean.FuliBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName
import com.yzy.commonlibrary.http.response.BaseResponse
import com.yzy.commonlibrary.repository.bean.ArticleDataBean
import com.yzy.commonlibrary.repository.bean.BannerBean
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
    @GET("data/福利/{month}/{day}")
    fun getFuli(@Path("month") month: Int, @Path("day") day: Int): Observable<GankBaseBean<FuliBean>>

    @GET("data/%E7%A6%8F%E5%88%A9/{count}/1")
    fun latest(@Path("count") count: Int): Observable<GankBaseBean<String>>

    @GET("get/{count}/since/{year}/{month}/{day}")
    fun since(
        @Path("count") count: Int,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ): Observable<GankBaseBean<String>>

    @GET("get/{count}/before/{year}/{month}/{day}")
    fun before(
        @Path("count") count: Int,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ): Observable<GankBaseBean<String>>


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