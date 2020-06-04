package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.DataUiState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerAndArticleBean


class TreeViewModel : BaseViewModel<GankRepository>() {
    //广场数据
    var plazaDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()
    private var pageNo = 0

    /**
     * 获取广场数据
     */
    fun getPlazaData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ repository.getPlazaData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it?.isEmpty()?:false,
                    hasMore = it?.hasMore()?:false,
                    isFirstEmpty = isRefresh && it?.isEmpty()?:false,
                    listData = it?.datas
                )
            plazaDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.message?:"",
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleDataBean>()
                )
            plazaDataState.postValue(listDataUiState)
        })
    }

    fun  loadData(){
        pageNo++
        getPlazaData(false)
    }
}

