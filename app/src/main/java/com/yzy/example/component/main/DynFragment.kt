package com.yzy.example.component.main

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.comm.item.dividerItem
import com.yzy.example.component.comm.item.errorEmptyItem
import com.yzy.example.component.comm.item.loadMoreItem
import com.yzy.example.component.main.item.gankAndroidItem
import com.yzy.example.component.main.model.DynViewModel
import com.yzy.example.component.web.WebsiteDetailFragment
import com.yzy.example.http.response.ApiException
import com.yzy.example.http.response.EmptyException
import com.yzy.example.repository.ViewModelFactory
import com.yzy.example.repository.bean.BaseDataBean
import com.yzy.example.repository.bean.GankAndroidBean
import kotlinx.android.synthetic.main.fragment_dyn.*

class DynFragment : CommFragment() {

    companion object {
        fun newInstance(): DynFragment {
            return DynFragment()
        }
    }

    override fun fillStatus(): Boolean = false

    private val mViewModel: DynViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(DynViewModel::class.java)
    }
    override val contentLayout: Int = R.layout.fragment_dyn


    override fun initView(root: View?) {
        dynEpoxyRecycler.setController(epoxyController)
        //把加载更多全部显示出来开始回调加载更多
        EpoxyVisibilityTracker().attach(dynEpoxyRecycler)
        mViewModel.getAndroidSuspend(true)
        smDynRefresh.setOnRefreshListener {
            mViewModel.getAndroidSuspend(true)
        }

    }

    override fun initData() {
        mViewModel.run {
            uiState.observe(this@DynFragment, Observer {
                if (it?.loading != false) {
                    showLoadingView()
                }
                it?.showSuccess?.let { list ->
                    dismissLoadingView()
                    smDynRefresh.finishRefresh()
                    epoxyController.data = list

                }
            })
        }
    }

    private val epoxyController =
        MvRxEpoxyController<BaseDataBean<MutableList<GankAndroidBean>>> { state ->
            //有数据
            if (!state.bean.isNullOrEmpty()) {
                state.bean.forEachIndexed { _, bean ->
                    //数据
                    gankAndroidItem {
                        id("dyn_${bean._id}")
                        dataBean(bean)
                        onItemClick { data ->
                            WebsiteDetailFragment.viewDetail(
                                mNavController,
                                R.id.action_mainFragment_to_websiteDetailFragment,
                                data.url ?: ""
                            )
                        }
                    }
                    //分割线
                    dividerItem {
                        id("dyn_line_${bean._id}")
                    }
                }
                //有数据支持下拉刷新
                smDynRefresh.setEnableRefresh(false)
                //根据返回信息判断是否可以加载更多
                if (state.hasMore) {
                    loadMoreItem {
                        id("dyn_line_more")
                        fail(false)
                        onLoadMore {
                            mViewModel.getAndroidSuspend(false)
                        }
                    }
                }
            } else {
                //无数据
                when (state.exception) {
                    is EmptyException -> errorEmptyItem {
                        id("home_suc_no_data")
                        imageResource(R.drawable.svg_no_data)
                        tipsText(mContext.getString(R.string.no_data))
                    }
                    //无网络或者请求失败
                    is ApiException -> errorEmptyItem {
                        id("home_fail_no_data")
                        if (NetworkUtils.isConnected()) {
                            imageResource(R.drawable.svg_fail)
                            tipsText(mContext.getString(R.string.net_fail_retry))
                        } else {
                            imageResource(R.drawable.svg_no_network)
                            tipsText(mContext.getString(R.string.net_error_retry))
                        }
                        onRetryClick {
                            mViewModel.getAndroidSuspend(true)
                        }
                    }
                    else -> LogUtils.i("初始化无数据空白")
                }
            }
        }
}