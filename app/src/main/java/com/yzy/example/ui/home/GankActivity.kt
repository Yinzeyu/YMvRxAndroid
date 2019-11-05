package com.yzy.example.ui.home

import android.content.Context
import android.util.Log
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.extention.mContext
import com.yzy.baselibrary.extention.startActivity
import com.yzy.example.R
import com.yzy.example.ui.comm.CommTitleActivity
import com.yzy.example.ui.item.dividerItem
import com.yzy.example.ui.item.errorEmptyItem
import com.yzy.example.ui.item.gankAndroidItem
import com.yzy.example.ui.item.loadMoreItem
import com.yzy.example.ui.web.WebActivity
import kotlinx.android.synthetic.main.fragment_dyn.*

class GankActivity : CommTitleActivity() {
    companion object {
        fun starGankActivity(context: Context) {
            context.startActivity<GankActivity>()
        }
    }

    private var isNeed = true

    override fun layoutResContentId(): Int = R.layout.fragment_dyn
    //数据层
    private val viewModel: DynViewModel by lazy {
        DynViewModel()
    }

    override fun initContentView() {
        dynEpoxyRecycler.setController(epoxyController)
        //把加载更多全部显示出来开始回调加载更多
        EpoxyVisibilityTracker().attach(dynEpoxyRecycler)
        smDynRefresh.setEnableLoadMore(false)
    }

    override fun initData() {
        //请求状态和结果监听
        viewModel.subscribe { state ->
            if (state.request is Loading) {//请求开始
                //如果没有显示下拉刷新则显示loading
                if (state.androidList.isNullOrEmpty() && isNeed) {
                    //显示loading
                    showLoadingView()
                    isNeed = false
                    //为了防止loading结束后还存在失败的view所以需刷新一下
                    if (state.androidList.isNullOrEmpty()) epoxyController.data = state
                }
            } else if (state.request.complete) {//请求结束
                smDynRefresh.finishRefresh(state.request is Success)
                smDynRefresh.finishLoadMore(state.request is Success)
                dismissLoadingView()
                epoxyController.data = state
                if (state.request is Fail) {//请求失败
                    Log.e("CASE", "失败原因:${(state.request as Fail<Any>).error.message ?: ""}")
                }
            }
        }
        //请求数据
        viewModel.refreshData()
    }

    //epoxy
    private val epoxyController = MvRxEpoxyController<DynState> { state ->
        //有数据
        if (!state.androidList.isNullOrEmpty()) {
            state.androidList.forEachIndexed { _, bean ->
                //数据
                gankAndroidItem {
                    id("dyn_${bean._id}")
                    dataBean(bean)
                    onItemClick { data -> WebActivity.startActivity(mContext, data.url ?: "") }
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
                    fail(state.request is Fail)
                    onLoadMore {
                        viewModel.loadMoreData()
                    }
                }
            }
        } else {
            //没有数据，不能下拉刷新也不能加载更多
            smDynRefresh.finishRefresh(state.request is Success)
            smDynRefresh.finishLoadMore(state.request is Success)
            //无数据
            when (state.request) {
                is Success -> errorEmptyItem {
                    id("dyn_suc_no_data")
                    imageResource(R.drawable.svg_no_data)
                    tipsText(mContext.getString(R.string.no_data))
                }
                //无网络或者请求失败
                is Fail -> errorEmptyItem {
                    id("dyn_fail_no_data")
                    if (NetworkUtils.isConnected()) {
                        imageResource(R.drawable.svg_fail)
                        tipsText(mContext.getString(R.string.net_fail_retry))
                    } else {
                        imageResource(R.drawable.svg_no_network)
                        tipsText(mContext.getString(R.string.net_error_retry))
                    }
                    onRetryClick { viewModel.refreshData() }
                }
                else -> LogUtils.i("初始化无数据空白")
            }
        }
    }
}