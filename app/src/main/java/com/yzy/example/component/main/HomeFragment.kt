package com.yzy.example.component.main

import android.util.Log
import android.view.View
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.comm.item.dividerItem
import com.yzy.example.component.comm.item.errorEmptyItem
import com.yzy.example.component.comm.item.loadMoreItem
import com.yzy.example.component.main.item.bannerItem
import com.yzy.example.component.main.item.wanArticleItem
import com.yzy.example.component.main.model.HomeState
import com.yzy.example.component.main.model.HomeViewModel
import com.yzy.example.component.web.WebActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: CommFragment(){

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
    private val homeViewModel: HomeViewModel by lazy {
        HomeViewModel()
    }
    override val contentLayout: Int = R.layout.fragment_home

    override fun initView(root: View?) {
        homeEpoxyRecycler.setController(epoxyController)
        EpoxyVisibilityTracker().attach(homeEpoxyRecycler)

        smRefresh.setOnRefreshListener {
            homeViewModel.refreshData()
        }
        homeViewModel.refreshData()
    }

    override fun initData() {
        //请求状态和结果监听
        homeViewModel.subscribe { state ->
            if (state.request is Loading) {//请求开始
                //如果没有显示下拉刷新则显示loading
                if (state.banners.isNullOrEmpty() && state.articles.isNullOrEmpty()
                ) {
                    //显示loading
                    showLoadingView()
                    //为了防止loading结束后还存在失败的view所以需刷新一下
                    epoxyController.data = state//透明背景的loading需要这样设置
                }
            } else if (state.request.complete) {//请求结束
                smRefresh.finishRefresh(state.request is Success)
                smRefresh.finishLoadMore(state.request is Success)
                dismissLoadingView()
                epoxyController.data = state
                if (state.request is Fail) {//请求失败
                    Log.e("yzy", "失败原因:${state.request.error.message ?: ""}")
                }
            }
        }
    }
    private val epoxyController = MvRxEpoxyController<HomeState> { state ->
        //有数据
        if (!state.banners.isNullOrEmpty() || !state.articles.isNullOrEmpty()) {
            //处理Banner'
            if (!state.banners.isNullOrEmpty()) {
                bannerItem {
                    id("home_banner_${state.banners.hashCode() + state.banners.size}")
                    dataList(state.banners)
                }
            }
            //处理文章列表
            if (!state.articles.isNullOrEmpty()) {
                state.articles.forEachIndexed { _, articleBean ->
                    //文章
                    wanArticleItem {
                        id(articleBean.id)
                        dataBean(articleBean)
                        onItemClick {
                            it?.link?.let { url -> WebActivity.startActivity(mContext, url) }
                        }
                    }
                    //分割线
                    dividerItem {
                        id("home_line_banner")
                    }
                }
            }
            //有数据支持下拉刷新
            smRefresh.setEnableRefresh(true)
            //根据返回信息判断是否可以加载更多
            if (state.hasMore) {
                loadMoreItem {
                    id("home_line_more")
                    fail(state.request is Fail)
                    onLoadMore {
                        homeViewModel.loadMoreData()
                    }
                }
            }
        } else {
            smRefresh.setEnableRefresh(false)
            //无数据
            when {
                state.request is Success -> errorEmptyItem {
                    id("home_suc_no_data")
                    imageResource(R.drawable.svg_no_data)
                    tipsText(mContext.getString(R.string.no_data))
                }
                //无网络或者请求失败
                state.request is Fail -> errorEmptyItem {
                    id("home_fail_no_data")
                    if (NetworkUtils.isConnected()) {
                        imageResource(R.drawable.svg_fail)
                        tipsText(mContext.getString(R.string.net_fail_retry))
                    } else {
                        imageResource(R.drawable.svg_no_network)
                        tipsText(mContext.getString(R.string.net_error_retry))
                    }
                    onRetryClick { homeViewModel.refreshData() }
                }
                else -> LogUtils.i("初始化无数据空白")
            }
        }
    }
}