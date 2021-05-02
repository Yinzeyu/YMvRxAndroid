package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean


class HomeViewModel(var state:SavedStateHandle) : BaseViewModel<GankRepository>() {
    private val key = "key"
    private val homeListKey = "homeListKey"
//    fun setValue(value:  ListDataUiState<BannerBean>) = state.set(key, value)
//    private fun getValue():MutableLiveData<ListDataUiState<BannerBean>>? = state.getLiveData(key)
//    fun setHomeListValue(value: ListDataUiState<ArticleDataBean>) = state.set(homeListKey, value)
//    private fun getHomeListValue(): MutableLiveData<ListDataUiState<ArticleDataBean>>? = state.getLiveData(homeListKey)
    fun clear(){
//        state.remove<ListDataUiState<ArticleDataBean>>(key)
//        state.remove<ListDataUiState<ArticleDataBean>>(homeListKey)
    }
    private var page = 0

    //首页列表数据
//    var homeDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()
//    //首页轮播图数据
//    var bannerDataState: MutableLiveData<ListDataUiState<BannerBean>> = MutableLiveData()

    fun loadLocal(isRefresh: Boolean){
//        if (getHomeListValue() != null && getValue() != null && getHomeListValue()?.value != null && getValue()?.value != null){
//            page = 0
//            homeDataState.postValue(getHomeListValue()?.value)
//            bannerDataState.postValue(getValue()?.value)
//            clear()
//            return
//        }
        getBanner(isRefresh)
    }
    fun getBanner(isRefresh: Boolean) {
        if (isRefresh) {
            page = 0
        }
        request({ repository.getHomeData(page) },success =  {homeData->
            if (page == 0) {
                request({ repository.banner(page) }, success = { bannerList ->
//                    bannerDataState.postValue(ListDataUiState(
//                        isSuccess = true,
//                        listData = bannerList,
//                        isRefresh = isRefresh))
//                    homeDataState.postValue(ListDataUiState(
//                        isSuccess = true,
//                        isEmpty =homeData.isEmpty(),
//                        isRefresh = isRefresh,
//                        listData = homeData.datas))
                },emptyView = {
//                    bannerDataState.postValue(ListDataUiState(isSuccess = false, isEmpty = true))
                }, error={
//                    homeDataState.postValue( ListDataUiState(
//                        isSuccess = false,
//                        errMessage = it.message?:""))
                },isShowDialog = false)
                page++
            } else {
//                homeDataState.postValue( ListDataUiState(isSuccess = true,  listData = homeData.datas, hasMore = homeData.hasMore()))
            }
        },emptyView = {
//            homeDataState.postValue(ListDataUiState(isSuccess = false, isEmpty =true))
        },error =  {
//            homeDataState.postValue(ListDataUiState(
//                isSuccess = false,
//                errMessage = it.message?:"",
//                errCode = it.code))
        })
    }

    fun loadData() {
        page+=1
        getBanner(false)
    }
}

