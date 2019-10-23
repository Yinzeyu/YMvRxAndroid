package com.yzy.pj.ui.elephant

import android.content.Context
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.xiaomi.push.it
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.extention.startActivity
import com.yzy.commonlibrary.repository.model.ConversationDetailState
import com.yzy.commonlibrary.repository.model.GankViewModel
import com.yzy.pj.R
import kotlinx.android.synthetic.main.activity_elephant.*

class ViewPager2Activity : BaseActivity() {

    companion object {
        fun starElephantActivity(context: Context) {
            context.startActivity<ViewPager2Activity>()
        }
    }

    //加载显示loading
    private var needShowLoading = true
    private val gankViewModel: GankViewModel by lazy {
        GankViewModel()
    }
    private val epoxyController = MvRxEpoxyController<ConversationDetailState> { state ->
        if (state.fuliBean.isNotEmpty()) {
            state.fuliBean.forEach {
                elephantItem {
                    id(it.url)
                    messageBean(it)
                }
            }
        }
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
                //数据加载失败
            }
            is Success -> {
                dismissLoading()
            }

        }
    }


    override fun layoutResId(): Int = R.layout.activity_elephant

    override fun initView() {
        gankViewModel.loadData(10, 17)
        tElephantViewPager.adapter = epoxyController.adapter
        tElephantViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        tElephantViewPager.getChildAt(0).overScrollMode = ViewPager2.OVER_SCROLL_NEVER

    }

    override fun initDate() {
        subscribeVM(gankViewModel)
    }

}