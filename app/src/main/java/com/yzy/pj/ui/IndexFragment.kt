package com.yzy.pj.ui

import android.util.Log
import android.view.View
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.commonlibrary.comm.CommFragment
import com.yzy.commonlibrary.repository.model.ConversationDetailState
import com.yzy.commonlibrary.repository.model.GankViewModel
import com.yzy.pj.R
import kotlinx.android.synthetic.main.fragemnt_index.*

class IndexFragment : CommFragment() {
    override val contentLayout: Int = R.layout.fragemnt_index
    //加载显示loading
    private val gankViewModel: GankViewModel by lazy {
        GankViewModel()
    }

    private val epoxyController = MvRxEpoxyController<ConversationDetailState> { state ->
        if (!state.banners.isNullOrEmpty()) {
            state.banners.forEach {
                atMeMessageItem {
                    id("home_banner_${it.hashCode() + it.id}")
                    messageBean(it)
                }
            }
        }
    }


    override fun initView(root: View?) {
        commListErv.setController(epoxyController)
        EpoxyVisibilityTracker().attach(commListErv)
        gankViewModel.loadData()
        //请求状态和结果监听
        gankViewModel.subscribe { state ->
            if (state.request is Loading) {//请求开始
                //如果没有显示下拉刷新则显示loading
                if (state.banners.isNullOrEmpty()) {
                    //显示loading
                    showLoadingView()
                    //为了防止loading结束后还存在失败的view所以需刷新一下
                    epoxyController.data = state//透明背景的loading需要这样设置
                    //epoxyController.requestModelBuild()非透明背景的loading还可以这样设置
                }
            } else if (state.request.complete) {//请求结束
                dismissLoadingView()
                epoxyController.data = state

                if (state.request is Fail) {//请求失败
                    Log.e("CASE", "失败原因:${(state.request as Fail<Any>).error.message ?: ""}")
                }
            }
        }
    }


    override fun initData() {
    }

    companion object {
        fun newInstance(): IndexFragment {
            return IndexFragment()
        }
    }

}