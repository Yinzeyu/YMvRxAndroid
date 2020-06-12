package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.state.ResultState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.ClassifyBean
import com.yzy.example.repository.bean.NavigationBean


class PublicNumberViewModel : BaseViewModel<GankRepository>() {


    var titleData: MutableLiveData<ResultState<MutableList<ClassifyBean>>> = MutableLiveData()


    fun getPublicTitleData() {
        request({ repository.getTitleData() }, titleData)
    }

}

