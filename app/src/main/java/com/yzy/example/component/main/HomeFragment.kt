package com.yzy.example.component.main

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.comm.item.dividerItem
import com.yzy.example.component.comm.item.loadMoreItem
import com.yzy.example.component.main.item.bannerItem
import com.yzy.example.component.main.item.wanArticleItem
import com.yzy.example.component.web.WebActivity
import com.yzy.example.repository.HomeArticleModelFactory
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.model.NewGankViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : CommFragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override val contentLayout: Int = R.layout.fragment_home
    private val mViewModel: NewGankViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            HomeArticleModelFactory()
        ).get(NewGankViewModel::class.java)
    }
    override fun initView(root: View?) {
        homeEpoxyRecycler.setController(epoxyController)
        EpoxyVisibilityTracker().attach(homeEpoxyRecycler)

        smRefresh.setOnRefreshListener {
            mViewModel.getBanner(true)
        }
        mViewModel.getBanner(true)
    }

    override fun initData() {
        mViewModel.run {
            uiState.observe(this@HomeFragment, Observer {
                if (it?.loading != false) {
                    showLoadingView()
                }
                it?.showSuccess?.let { list ->
                    dismissLoadingView()
                    smRefresh.finishRefresh()
                    epoxyController.data = list
                }
                it?.showError?.let { message ->
                    smRefresh.finishRefresh()
                    dismissLoadingView()
                }
            })
        }
    }

    private val epoxyController = MvRxEpoxyController<BannerAndArticleBean> { state ->
        //有数据
        if (!state.bannerBean.isNullOrEmpty() || !state.articleBean.isNullOrEmpty()) {
            //处理Banner'
            if (!state.bannerBean.isNullOrEmpty()) {
                bannerItem {
                    id("home_banner_${state.bannerBean.hashCode() + state.bannerBean.size}")
                    dataList(state.bannerBean)
                }
            }
            //处理文章列表
            if (!state.articleBean.isNullOrEmpty()) {
                state.articleBean.forEachIndexed { _, articleBean ->
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
//                    fail(state.request is Fail)
                    onLoadMore {
                        mViewModel.getBanner()
                    }
                }
            }
        } else {
            smRefresh.setEnableRefresh(false)
            //无数据
//            when {
//                state.request is Success -> errorEmptyItem {
//                    id("home_suc_no_data")
//                    imageResource(R.drawable.svg_no_data)
//                    tipsText(mContext.getString(R.string.no_data))
//                }
//                //无网络或者请求失败
//                state.request is Fail -> errorEmptyItem {
//                    id("home_fail_no_data")
//                    if (NetworkUtils.isConnected()) {
//                        imageResource(R.drawable.svg_fail)
//                        tipsText(mContext.getString(R.string.net_fail_retry))
//                    } else {
//                        imageResource(R.drawable.svg_no_network)
//                        tipsText(mContext.getString(R.string.net_error_retry))
//                    }
//                    onRetryClick {
//                        //                        homeViewModel.refreshData()
//                    }
//                }
//                else -> LogUtils.i("初始化无数据空白")
//            }
        }
    }
}