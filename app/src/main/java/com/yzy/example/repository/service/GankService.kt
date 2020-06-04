package com.yzy.example.repository.service

import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.bean.*
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

    /**
     * 获取当前账户的个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getIntegral(): BaseResponse<IntegralBean>
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username: String, @Field("password") pwd: String): BaseResponse<UserInfo>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(@Field("username") username: String, @Field("password") pwd: String, @Field("repassword") rpwd: String): BaseResponse<Any>

    /**
     * 广场列表数据
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>>

    /**
     * 每日一问列表数据
     */
    @GET("wenda/list/{page}/json")
    suspend fun getAskData(@Path("page") page: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>>


    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getSystemData(): BaseResponse<ArrayList<SystemBean>>

    /**
     * 知识体系下的文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getSystemChildData(@Path("page") pageNo: Int, @Query("cid") cid: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>>


    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationData(): BaseResponse<ArrayList<NavigationBean>>

}