package com.yzy.example.repository

import com.yzy.baselibrary.base.BaseRepository
import com.yzy.baselibrary.http.ResponseThrowable
import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.bean.*
import com.yzy.example.repository.service.GankService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GankRepository : BaseRepository() {
//    private object SingletonHolder {
//        val holder = GankRepository()
//    }
//
//    companion object {
//        val instance = SingletonHolder.holder
//    }
    private val service: GankService =mAPi.getApi(GankService::class.java)

//    suspend fun getAndroidSuspend(
//        pageSize: Int,
//        page: Int
//    ): GankBaseBean<MutableList<GankAndroidBean>> {
//        return service.getAndroidSuspend(pageSize, page)
//    }

    suspend fun banner(page: Int): BaseResponse<MutableList<BannerBean>> {
        return service.banner(page.toString(), "20")
    }

    /**
     * 获取项目标题数据
     */
    suspend fun getProjecTitle(): BaseResponse<ArrayList<ClassifyBean>> {
        return service.getProjecTitle()
    }

    /**
     * 获取项目标题数据
     */
    suspend fun getProjectData(
        pageNo: Int,
        cid: Int = 0,
        isNew: Boolean = false
    ): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>> {
        return if (isNew) {
            service.getProjecNewData(pageNo)
        } else {
          service.getProjecDataByType(pageNo, cid)
        }
    }


    /**
     * 获取首页文章数据
     */
    suspend fun getHomeData(pageNo: Int): BaseResponse<PagerResponse<MutableList<ArticleDataBean>>> {
        //同时异步请求2个接口，请求完成后合并数据
        return withContext(Dispatchers.IO) {
            val data = async { service.getAritrilList(pageNo) }
            //如果App配置打开了首页请求置顶文章，且是第一页
            if (pageNo == 0) {
                val topData = async { getTopData() }
                data.await().data.datas.addAll(0, topData.await().data)
                data.await()
            } else {
                data.await()
            }
        }
    }

    /**
     * 获取置顶文章数据
     */
    private suspend fun getTopData(): BaseResponse<MutableList<ArticleDataBean>> {
        return service.getTopAritrilList()
    }
    /**
     * 获取个人信息积分
     */
    suspend fun getIntegral(): BaseResponse<IntegralBean> {
        return service.getIntegral()
    }

    /**
     * 登录
     */
    suspend fun login(username: String, password: String): BaseResponse<UserInfo> {
        return service.login(username, password)
    }

    /**
     * 注册并登陆
     */
    suspend fun register(username: String, password: String): BaseResponse<UserInfo> {
        val registerData = service.register(username, password, password)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.isSuccess()) {
            return login(username, password)
        } else {
            //抛出错误异常
            throw ResponseThrowable(registerData.code, registerData.message)
        }
    }
    /**
     * 获取广场数据
     */
    suspend fun getPlazaData(pageNo: Int): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>> {
        return service.getSquareData(pageNo)
    }

    /**
     * 获取每日一问数据
     */
    suspend fun getAskData(pageNo: Int):  BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>> {
        return service.getAskData(pageNo)
    }
    /**
     * 获取体系数据
     */
    suspend fun getSystemData(): BaseResponse<ArrayList<SystemBean>> {
        return service.getSystemData()
    }

    /**
     * 获取导航数据
     */
    suspend fun getNavigationData(): BaseResponse<ArrayList<NavigationBean>> {
        return service.getNavigationData()
    }

    /**
     * 获取体系子数据
     */
    suspend fun getSystemChildData(
        pageNo: Int,
        cid: Int
    ): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>> {
        return service.getSystemChildData(pageNo, cid)
    }
    /**
     * 获取公众号标题数据
     */
    suspend fun getTitleData(): BaseResponse<ArrayList<ClassifyBean>> {
        return service.getPublicTitle()
    }

    /**
     * 根据公众号标题获取数据
     */
    suspend fun getPublicData(
        pageNo: Int,
        cid: Int = 0
    ): BaseResponse<PagerResponse<ArrayList<ArticleDataBean>>> {
        return service.getPublicData(pageNo, cid)
    }
}
