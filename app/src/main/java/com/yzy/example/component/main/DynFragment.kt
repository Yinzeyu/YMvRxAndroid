package com.yzy.example.component.main

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.repository.bean.ArticleBean
import com.yzy.example.repository.bean.GankAndroidBean
import kotlinx.android.synthetic.main.fragment_dyn.*

class DynFragment() : CommFragment<DynViewModel>() {
    companion object {
        fun newInstance(): DynFragment {
            return DynFragment()
        }
    }
    private val dynAdapter by lazy { DynAdapter() }

    override fun fillStatus(): Boolean = true
    override val contentLayout: Int =R.layout.fragment_dyn
    private var page: Int = 0
    override fun initContentView() {
       dropDownRefresh()
        smDynRefresh.setOnRefreshListener {
            dropDownRefresh()
        }

        with(dynEpoxyRecycler) {
            layoutManager= LinearLayoutManager(mContext)
            adapter=dynAdapter
            val dividerItemDecoration = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(mContext,R.drawable.custom_divider)?.let { dividerItemDecoration.setDrawable(it) }
            addItemDecoration(dividerItemDecoration)
        }
        viewModel.uiState.observe(this, Observer {
            if (smDynRefresh.isRefreshing) smDynRefresh.finishRefresh()
            if (page == 1) dynAdapter.setList(it)
            else dynAdapter.addData(it)
            if (it.size != 20)  dynAdapter.loadMoreModule.loadMoreEnd()
            else dynAdapter.loadMoreModule.loadMoreComplete()
            page++
        })
    }
    private fun dropDownRefresh() {
        page = 0
        smDynRefresh.setEnableRefresh(true)
        viewModel.getAndroidSuspend(page,dynAdapter.data.size <=0)
    }

    override fun initData() {
        dynAdapter.apply {
            loadMoreModule.setOnLoadMoreListener {
                viewModel.getAndroidSuspend(page + 1,dynAdapter.data.size <=0)
            }

            setOnItemClickListener { adapter, v, position ->
                val bean: GankAndroidBean = adapter.data[position] as GankAndroidBean
                Navigation.findNavController(v).navigate( MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(bean.url ?: ""))
            }
        }
    }
}