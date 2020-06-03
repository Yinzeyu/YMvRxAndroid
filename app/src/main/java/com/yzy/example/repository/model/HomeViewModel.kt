package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.DataUiState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerAndArticleBean


class HomeViewModel : BaseViewModel<GankRepository>() {
    private var page = 0

    //首页文章列表数据
    var homeDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()

    //首页轮播图数据
    var bannerData: MutableLiveData<BannerAndArticleBean> = MutableLiveData()

    fun getBanner(isRefresh: Boolean) {
        if (isRefresh) {
            page = 0
        }
        request({ repository.getHomeData(page) }, {homeData->
            if (page == 0) {
                request({ repository.banner(page) }, { bannerList ->
                    val bannerAndArticleBean = BannerAndArticleBean(
                        bannerBean = bannerList, articleBean = DataUiState(
                            isSuccess = true,
                            isRefresh = isRefresh,
                            isEmpty = homeData?.isEmpty() ?: true,
                            hasMore = homeData?.hasMore() ?: false,
                            isFirstEmpty = isRefresh && homeData?.isEmpty() ?: true,
                            data = homeData?.datas
                        )
                    )
                    bannerData.postValue(bannerAndArticleBean)
                },{
                    val listDataUiState = ListDataUiState(
                            isSuccess = true,
                            isRefresh = isRefresh,
                            isEmpty = homeData?.isEmpty() ?: true,
                            hasMore = homeData?.hasMore() ?: false,
                            isFirstEmpty = isRefresh && homeData?.isEmpty() ?: true,
                            listData = homeData?.datas
                        )
                    homeDataState.postValue(listDataUiState)
                },false)
                page++
            } else {
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = true,
                        isRefresh = isRefresh,
                        isEmpty = homeData?.isEmpty() ?: true,
                        hasMore = homeData?.hasMore() ?: false,
                        isFirstEmpty = isRefresh && homeData?.isEmpty() ?: true,
                        listData = homeData?.datas
                    )
                homeDataState.postValue(listDataUiState)
            }
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.message?:"",
                    isRefresh = isRefresh,
                    listData = mutableListOf<ArticleDataBean>()
                )
            homeDataState.postValue(listDataUiState)
        })
    }

    fun loadData() {
        page+=1
        getBanner(false)
    }
}

