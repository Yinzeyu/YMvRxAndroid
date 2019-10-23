package com.yzy.pj.ui

import android.view.View
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.base.fragment.BaseMvRxEpoxyFragment
import com.yzy.baselibrary.base.simpleController
import com.yzy.commonlibrary.repository.model.GankViewModel
import com.yzy.pj.R
import kotlinx.android.synthetic.main.fragemnt_index.*

class IndexFragment : BaseMvRxEpoxyFragment() {
    lateinit var refreshLayout: SmartRefreshLayout
    override val contentLayout: Int = R.layout.fragemnt_index
    //加载显示loading
    private var needShowLoading = true
    private val gankViewModel: GankViewModel by lazy {
        GankViewModel()
    }

    override fun epoxyController(): MvRxEpoxyController = simpleController(gankViewModel) { state ->
        if (state.fuliBean.isNotEmpty()) {
            state.fuliBean.forEach {
                atMeMessageItem {
                    id(it.url)
                    messageBean(it)
                }
            }
            if (state.hasMore) {
                //有更多数据
                loadMoreItem {
                    id("IndexLoadMore")
                    tipsText("数据加载中…")//自定义提示文字
                    onLoadMore {
                        gankViewModel.loadMoreData(10, 27)
                    }
                }
            }
        }
        //加载失败
        when (state.request) {
            is Loading -> {
                if (state.fuliBean.isEmpty() && needShowLoading) {
                    //没有数据默认为第一次加载
                    showLoading()
                    needShowLoading = false
                }
            }
            is Fail -> {
                dismissLoading()
                commListSrl.finishRefresh()
                //数据加载失败
            }
            is Success -> {
                dismissLoading()
                commListSrl.finishRefresh()
            }

        }
    }


    override fun initView(root: View?) {
        commListErv.setController(epoxyController)
        EpoxyVisibilityTracker().attach(commListErv)
        refreshLayout = commListSrl
        gankViewModel.loadData(10, 17)
        refreshLayout.setOnRefreshListener {
            gankViewModel.loadData(10, 17)
        }
    }


    override fun initData() {
        subscribeVM(gankViewModel)
    }

    companion object {
        fun newInstance(): IndexFragment {
            return IndexFragment()
        }
    }

}