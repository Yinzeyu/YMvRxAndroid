package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.livedata.StringLiveData
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.ShareBean

class LookInfoViewModel : BaseViewModel<GankRepository>() {
    var name: StringLiveData = StringLiveData("--")

    var imageUrl: StringLiveData =
        StringLiveData("https://upload.jianshu.io/users/upload_avatars/9305757/93322613-ff1a-445c-80f9-57f088f7c1b1.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/300/format/webp")

    var info: StringLiveData = StringLiveData()
    var pageNo = 1

//    var shareListDataUistate = MutableLiveData<ListDataUiState<ArticleDataBean>>()

    var shareResponse = MutableLiveData<ShareBean>()

    fun getLookinfo(id: Int, isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ repository.getLookinfoById(id, pageNo) }, {
            //请求成功
            pageNo++
            shareResponse.postValue(it)
//            val listDataUiState =
//                ListDataUiState(
//                    isSuccess = true,
//                    isRefresh = it.shareArticles.isRefresh(),
//                    isEmpty = it.shareArticles.isEmpty(),
//                    hasMore = it.shareArticles.hasMore(),
//                    isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
//                    listData = it.shareArticles.datas
//                )
//            shareListDataUistate.postValue(listDataUiState)
        }, {
            //请求失败
//            val listDataUiState =
//                ListDataUiState(
//                    isSuccess = false,
//                    errMessage = it.errMsg,
//                    isRefresh = isRefresh,
//                    listData = arrayListOf<ArticleDataBean>()
//                )
//            shareListDataUistate.postValue(listDataUiState)
        })
    }
}