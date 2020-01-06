package com.yzy.example.repository

import com.yzy.baselibrary.http.RetrofitAPi
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.bean.DataResult
import com.yzy.example.repository.service.GankService

class GankRepository : BaseRepository() {
    private object SingletonHolder {
        val holder = GankRepository()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private val service: GankService = RetrofitAPi.instance.getApi(GankService::class.java)

    private suspend fun requestSystemTypes(page: Int): DataResult<MutableList<BannerBean>> = executeResponse(service.banner(page.toString(),"20"))

    suspend fun banner(page: Int): DataResult<MutableList<BannerBean>> {
        return  safeApiCall(call = {   requestSystemTypes(page) }, errorMessage = "网络错误")
    }
    private suspend fun requestBlogArticle(page: Int): DataResult<ArticleDataBean> = executeResponse(service.article(page .toString()))

    suspend fun article(page: Int): DataResult<ArticleDataBean> {
        return  safeApiCall(call = {   requestBlogArticle(page) }, errorMessage = "网络错误")
    }
}
