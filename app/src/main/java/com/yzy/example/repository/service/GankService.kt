package com.yzy.example.repository.service

import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import retrofit2.http.*


/**
 *description: Gank网络请求的Service.
 *@date 2019/7/15
 *@author: yzy.
 */
interface GankService {
    //多api  和 from 提交 或者json 提交可以随意切换
    //默认from 提交
//    @Headers("urlName:requestJson&&Domain${ApiConstants.Address.GANK_URL}")
//    @Headers("urlName:Domain${ApiConstants.Address.GANK_URL}")
//    @GET("api/data/Android/{pageSize}/{page}")
//    suspend fun getAndroidSuspend(
//        @Path("pageSize") pageSize: Int,
//        @Path("page") page: Int
//    ): GankBaseBean<MutableList<GankAndroidBean>>

    @GET("banner/json")
    suspend fun banner(
        @Query("page") page: String,
        @Query("size") size: String
    ): BaseResponse<MutableList<BannerBean>>

    @GET("article/list/{page}/json")
    suspend fun article(
        @Path("page") page: String
    ): BaseResponse<ArticleDataBean>
}