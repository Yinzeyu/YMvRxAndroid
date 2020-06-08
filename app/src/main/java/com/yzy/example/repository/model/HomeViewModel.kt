package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.DataUiState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.bean.BannerBean


class HomeViewModel(var state:SavedStateHandle ) : BaseViewModel<GankRepository>() {
    private val key = "key"
    fun setValue(value:  MutableList<BannerBean>) = state.set(key, value)
    fun getValue(): MutableList<BannerBean>? = state.get(key)

    private var page = 0

    //首页文章列表数据
    var homeDataState: MutableLiveData<DataUiState<MutableList<ArticleDataBean>>> = MutableLiveData()
    //首页轮播图数据
    var bannerDataState: MutableLiveData<DataUiState<MutableList<BannerBean>>> = MutableLiveData()

    fun getBanner(isRefresh: Boolean) {
        if (isRefresh) {
            page = 0
        }
        request({ repository.getHomeData(page) },success =  {homeData->
            if (page == 0) {
                request({ repository.banner(page) }, success = { bannerList ->
                    bannerDataState.postValue(DataUiState(isSuccess = true,data = bannerList,isRefresh = isRefresh))
                    homeDataState.postValue(DataUiState(isSuccess = true,isEmpty =homeData.isEmpty(),isRefresh = isRefresh))
                },emptyView = {
                    bannerDataState.postValue(DataUiState(isSuccess = false,isEmpty = true))
                }, error={
                    homeDataState.postValue( DataUiState(isSuccess = false,errMessage = it.message?:""))
                },isShowDialog = false)
                page++
            } else {
                homeDataState.postValue( DataUiState(isSuccess = true,  data = homeData.datas, hasMore = homeData.hasMore()))
            }
        },emptyView = {
            homeDataState.postValue(DataUiState(isSuccess = false,isEmpty =true))
        },error =  {
            homeDataState.postValue(DataUiState(isSuccess = false,errMessage = it.message?:"",errCode = it.code))
        })
    }

    fun loadData() {
        page+=1
        getBanner(false)
    }
}

