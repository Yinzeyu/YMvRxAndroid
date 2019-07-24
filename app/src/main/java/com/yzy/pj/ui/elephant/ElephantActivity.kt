package com.yzy.pj.ui.elephant

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.yzy.baselibrary.base.activity.BaseMvRxEpoxyActivity
import com.yzy.baselibrary.base.simpleController
import com.yzy.baselibrary.http.response.StatusData
import com.yzy.commonlibrary.repository.model.GankViewModel
import com.yzy.pj.R
import com.yzy.pj.ui.atMeMessageItem
import com.yzy.pj.ui.loadMoreItem
import kotlinx.android.synthetic.main.activity_elephant.*
import kotlinx.android.synthetic.main.fragemnt_index.*

class ElephantActivity : BaseMvRxEpoxyActivity() {
    //加载显示loading
    private var needShowLoading = true
    private val gankViewModel: GankViewModel by lazy {
        GankViewModel()
    }

    override fun epoxyController(): AsyncEpoxyController =
        simpleController(gankViewModel) { state ->
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
                is StatusData.Success<*> -> {
                    dismissLoading()
                    commListSrl.finishRefresh()
                }

            }
        }

    override fun layoutResId(): Int = R.layout.activity_elephant

    override fun initView() {
        tElephantViewPager.adapter = epoxyController.adapter
        epoxyController.requestModelBuild()
        gankViewModel.loadData(10, 17)
    }

    override fun initDate() {
        subscribeVM(gankViewModel)
    }

}