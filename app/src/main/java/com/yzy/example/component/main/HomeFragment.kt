package com.yzy.example.component.main

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.stx.xhb.androidx.XBanner
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.main.item.HomeListAdapter
import com.yzy.example.repository.model.NewGankViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class HomeFragment : CommFragment<NewGankViewModel,ViewDataBinding>() {
    private val mAdapter by lazy { HomeListAdapter() }
    private lateinit var banner: XBanner
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun fillStatus(): Boolean = false

    override val contentLayout: Int = R.layout.fragment_home
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun initView(root: View?) {
        smRefresh.setOnRefreshListener {
            viewModel.getBanner(true)
        }
        viewModel.getBanner(true)
    }

    override fun initData() {

        with(rv_home) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            //banner
            banner = XBanner(context)
            banner.minimumWidth = MATCH_PARENT
            banner.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, SizeUtils.dp2px(120f))
            banner.loadImage(GlideImageLoader())
        }

        mAdapter.apply {
            addHeaderView(banner)
//            setOnLoadMoreListener(this@HomeFragment::loadMore, rv_home)
            setOnItemClickListener { adapter, _, position ->
//                val item = adapter.data[position] as ArticlesBean
//                val intent = Intent(context, DetailActivity::class.java)
//                intent.putExtra("url", item.link)
//                startActivity(intent)
            }
        }
        viewModel.run {
            uiState.observe(this@HomeFragment, Observer{
                val showLoading = it?.showLoading ?: false
                if (showLoading) {
                    showLoadingView()
                } else {
                    dismissLoadingView()
                }

                it?.success?.let { list ->
                    dismissLoadingView()
                    smRefresh.finishRefresh()
                    banner.setBannerData(list.bannerBean)
                    mAdapter.setList(list.articleBean)

                }
            })
        }
    }
}