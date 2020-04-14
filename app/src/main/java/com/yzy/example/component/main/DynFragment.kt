package com.yzy.example.component.main

import android.view.View
import androidx.lifecycle.Observer
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import kotlinx.android.synthetic.main.fragment_dyn.*

class DynFragment(override val contentLayout: Int=R.layout.fragment_dyn) : CommFragment<DynViewModel>() {

    companion object {
        fun newInstance(): DynFragment {
            return DynFragment()
        }
    }

    override fun fillStatus(): Boolean = false


    override fun initView(root: View?) {
        viewModel.getAndroidSuspend(true)
        smDynRefresh.setOnRefreshListener {
            viewModel.getAndroidSuspend(true)
        }

    }

    override fun initData() {
        viewModel.run {
            uiState.observe(this@DynFragment, Observer {
                val showLoading = it?.showLoading ?: false
                if (showLoading) {
                    showLoadingView()
                } else {
                    dismissLoadingView()
                }

                it?.success?.let { list ->
                    dismissLoadingView()
                    smDynRefresh.finishRefresh()
//                    epoxyController.data = list
                }

            })
        }
    }


//    private val epoxyController =
//        MvRxEpoxyController<BaseDataBean<MutableList<GankAndroidBean>>> { state ->
//            //有数据
//            if (!state.bean.isNullOrEmpty()) {
//                state.bean.forEachIndexed { _, bean ->
//                    //数据
////                    gankAndroidItem {
////                        id("dyn_${bean._id}")
////                        dataBean(bean)
////                        onItemClick { data ->
////                            startNavigate(view, MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(data.url ?: ""))
////                        }
////                    }
//                    //分割线
////                    dividerItem {
////                        id("dyn_line_${bean._id}")
////                    }
//                }
//                //有数据支持下拉刷新
//                smDynRefresh.setEnableRefresh(false)
//                //根据返回信息判断是否可以加载更多
//                if (state.hasMore) {
////                    loadMoreItem {
////                        id("dyn_line_more")
////                        fail(false)
////                        onLoadMore {
////                            viewModel.getAndroidSuspend(false)
////                        }
////                    }
//                }
//            } else {
//                //无数据
////                when (state.exception) {
////                    is EmptyException -> errorEmptyItem {
////                        id("home_suc_no_data")
////                        imageResource(R.drawable.svg_no_data)
////                        tipsText(mContext.getString(R.string.no_data))
////                    }
////                    //无网络或者请求失败
////                    is ApiException -> errorEmptyItem {
////                        id("home_fail_no_data")
////                        if (NetworkUtils.isConnected()) {
////                            imageResource(R.drawable.svg_fail)
////                            tipsText(mContext.getString(R.string.net_fail_retry))
////                        } else {
////                            imageResource(R.drawable.svg_no_network)
////                            tipsText(mContext.getString(R.string.net_error_retry))
////                        }
////                        onRetryClick {
////                            viewModel.getAndroidSuspend(true)
////                        }
////                    }
////                    else -> LogUtils.i("初始化无数据空白")
////                }
//            }
//        }
}