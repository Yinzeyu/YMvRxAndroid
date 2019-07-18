package com.yzy.pj

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.yzy.baselibrary.base.activity.BaseMvRxEpoxyActivity
import com.yzy.baselibrary.base.simpleController
import com.yzy.commonlibrary.repository.model.GankViewModel
import com.yzy.pj.ui.atMeMessageItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvRxEpoxyActivity() {
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
            }

        }

    override fun layoutResId(): Int = R.layout.activity_main;

    override fun initView() {
        commListErv.setController(epoxyController)
        EpoxyVisibilityTracker().attach(commListErv)
        gankViewModel.getFuli(10, 17)

    }

    override fun initDate() {
        subscribeVM(gankViewModel)
    }


}
