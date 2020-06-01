package com.yzy.example.repository.service

import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.bean.PagerResponse
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.bean.ClassifyBean
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
    suspend fun getAritrilList(
        @Path("page") page: Int
    ): BaseResponse<PagerResponse<MutableList<ArticleDataBean>>>

    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopAritrilList(): BaseResponse<MutableList<ArticleDataBean>>

    /**
     * 获取最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjecNewData(@Path("page") pageNo: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    suspend fun getPublicTitle(): BaseResponse<ArrayList<ClassifyBean>>

    /**
     * 获取公众号数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getPublicData(@Path("page") pageNo: Int, @Path("id") id: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>>

    /**
     * 项目分类标题
     */
    @GET("project/tree/json")
    suspend fun getProjecTitle(): BaseResponse<ArrayList<ClassifyBean>>
    /**
     * 根据分类id获取项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjecDataByType(@Path("page") pageNo: Int, @Query("cid") cid: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>>

}