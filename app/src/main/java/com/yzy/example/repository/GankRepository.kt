package com.yzy.example.repository

import com.yzy.baselibrary.extention.applySchedulers
import com.yzy.baselibrary.http.RetrofitAPi
import com.yzy.example.http.RxGlobalHandleUtil
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.bean.GankAndroidBean
import com.yzy.example.repository.service.GankService
import io.reactivex.Observable

class GankRepository  {
    private object SingletonHolder {
        val holder = GankRepository()
    }
    companion object {
        val instance = SingletonHolder.holder
    }

    private val service: GankService = RetrofitAPi.instance.getApi(GankService::class.java)
    /**
     * 获取福利
     */
    fun androidList(pageSize: Int, page: Int): Observable<MutableList<GankAndroidBean>> {
        return service.getAndroid(pageSize, page).compose(applySchedulers()).map(IsGankSuccessFunc())
    }

//
//    fun androidList(pageSize: Int, page: Int): Observable<MutableList<GankAndroidBean>> {
//        return  service.getAndroidSuspend(pageSize, page).a
//    }


    fun banner(): Observable<MutableList<BannerBean>> {
        return service.banner(1.toString(), 20.toString()).compose(RxGlobalHandleUtil.globalHandle())
    }

    fun article(page: Int): Observable<ArticleDataBean> {
        return service.article(page.toString()).compose(RxGlobalHandleUtil.globalHandle())
    }
}


//const val TAG_KODEIN_MODULE_REPOSITORY_BLACK = "blackRepositoryModel"
//val blackRepositoryModel = Kodein.Module(TAG_KODEIN_MODULE_REPOSITORY_BLACK) {
//    bind<GankRepository>() with singleton {
//        GankRepository()
//    }
//}