package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.livedata.IntLiveData
import com.yzy.baselibrary.http.livedata.StringLiveData
import com.yzy.baselibrary.http.state.ResultState
import com.yzy.example.http.DataUiState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.bean.IntegralBean


class MeViewModel : BaseViewModel<GankRepository>() {
    var meData = MutableLiveData<ResultState<IntegralBean>>()
    var name = StringLiveData("请先登录~")

    var integral = IntLiveData(0)

    var info = StringLiveData("id：--　排名：-")
    fun getIntegral() {
        request({ repository.getIntegral() }, meData)
    }
}

