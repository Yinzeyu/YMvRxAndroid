package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.state.ResultState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.ClassifyBean

class ProjectViewModel : BaseViewModel<GankRepository>() {
    //页码
    var pageNo = 1

    var titleData: MutableLiveData<ResultState<ArrayList<ClassifyBean>>> = MutableLiveData()

    var projectDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()


    fun getProjectTitleData() {
        request({ repository.getProjecTitle() }, titleData)
    }

    fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        if (isRefresh) {
            pageNo = if (isNew) 0 else 1
        }
        request({repository.getProjectData(pageNo, cid, isNew)},{
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
            projectDataState.postValue(listDataUiState)
        },{
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleDataBean>()
                )
            projectDataState.postValue(listDataUiState)
        })
    }
}