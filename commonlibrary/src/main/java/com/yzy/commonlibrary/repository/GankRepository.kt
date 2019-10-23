package com.yzy.commonlibrary.repository

import com.yzy.baselibrary.http.GlobeHttpHandler
import com.yzy.baselibrary.repository.BaseRemoteDataSource
import com.yzy.commonlibrary.repository.bean.FuliBean
import com.yzy.baselibrary.utils.SchedulersUtil
import com.yzy.commonlibrary.http.RxGlobalHandleUtil
import com.yzy.commonlibrary.repository.bean.ArticleDataBean
import com.yzy.commonlibrary.repository.bean.BannerBean
import com.yzy.commonlibrary.repository.service.GankService
import io.reactivex.Observable
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class GankRepository : BaseRemoteDataSource() {
    private val service: GankService = getApi(GankService::class.java)
    /**
     * 获取福利
     */
    fun getSysMsgList(month: Int, day: Int): Observable<List<FuliBean>> {
        return service.getFuli(month, day).compose(SchedulersUtil.applySchedulers()).map(IsGankSuccessFunc())
    }


    fun banner(): Observable<MutableList<BannerBean>> {
        return service.banner(1.toString(), 20.toString()).compose(RxGlobalHandleUtil.globalHandle())
    }

    fun article(page:Int): Observable<ArticleDataBean> {
        return service.article(page.toString()).compose(RxGlobalHandleUtil.globalHandle())
    }
}

const val TAG_KODEIN_MODULE_REPOSITORY_BLACK = "blackRepositoryModel"
val blackRepositoryModel = Kodein.Module(TAG_KODEIN_MODULE_REPOSITORY_BLACK) {
    bind<GankRepository>() with singleton {
        GankRepository()
    }
}