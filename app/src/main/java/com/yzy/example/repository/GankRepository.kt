package com.yzy.example.repository

import com.yzy.baselibrary.http.RetrofitAPi
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


    suspend fun getAndroidSuspend(pageSize: Int, page: Int): DataResult<MutableList<GankAndroidBean>> {
        return executeResponse1(service.getAndroidSuspend(pageSize, page))
    }

    suspend fun banner(page: Int): DataResult<MutableList<BannerBean>> {
        return executeResponse(service.banner(page.toString(), "20"))
    }

    suspend fun article(page: Int): DataResult<ArticleDataBean> {
        return executeResponse(response = service.article(page.toString()))
    }
}
