package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.livedata.BooleanLiveData
import com.yzy.baselibrary.http.livedata.IntLiveData
import com.yzy.baselibrary.http.livedata.StringLiveData
import com.yzy.baselibrary.http.state.ResultState
import com.yzy.example.http.DataUiState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.bean.IntegralBean
import com.yzy.example.repository.bean.UserInfo


class RegisterViewModel : BaseViewModel<GankRepository>() {
    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<UserInfo>>()

    //用户名
    var username = StringLiveData()

    //密码(登录注册界面)
    var password = StringLiveData()

    var password2 = StringLiveData()

    //是否显示明文密码（登录注册界面）
    var isShowPwd = BooleanLiveData()

    var isShowPwd2 = BooleanLiveData()

    fun loginReq(username: String, password: String) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        request(
            { repository.login(username, password) }//请求体
            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )
    }

    fun registerAndlogin(username: String, password: String) {
        request(
            { repository.register(username, password) }
            , loginResult,
            true,
            "正在注册中..."
        )
    }
}

