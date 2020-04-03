package com.yzy.example.repository

import com.yzy.baselibrary.http.RetrofitAPi
import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.bean.*
import com.yzy.example.repository.service.GankService

class GankRepository : BaseRepository() {
    private object SingletonHolder {
        val holder = GankRepository()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private val service: GankService = RetrofitAPi.instance.getApi(GankService::class.java)


    suspend fun getAndroidSuspend(
        pageSize: Int,
        page: Int
    ): GankBaseBean<MutableList<GankAndroidBean>> {
        return service.getAndroidSuspend(pageSize, page)
    }

    suspend fun banner(page: Int): BaseResponse<MutableList<BannerBean>> {
        return service.banner(page.toString(), "20")
    }

    suspend fun article(page: Int): BaseResponse<ArticleDataBean> {
        return service.article(page.toString())
    }
}
