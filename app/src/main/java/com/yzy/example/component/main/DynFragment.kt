package com.yzy.example.component.main

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentDynBinding

class DynFragment() : CommFragment<DynViewModel,FragmentDynBinding>() {
    companion object {
        fun newInstance(): DynFragment {
            return DynFragment()
        }
    }
    private val dynAdapter by lazy { DynAdapter() }
    override fun initContentView() {
       dropDownRefresh()
//        smDynRefresh.setEnableRefresh(false)
//        smDynRefresh.setOnRefreshListener {
//            dropDownRefresh()
//        }
//
//        with(dynEpoxyRecycler) {
//            layoutManager= LinearLayoutManager(context)
//            adapter=dynAdapter
//            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//            ContextCompat.getDrawable(context,R.drawable.custom_divider)?.let { dividerItemDecoration.setDrawable(it) }
//            addItemDecoration(dividerItemDecoration)
//        }
//        viewModel.uiState.observe(this, Observer {
//            smDynRefresh.setEnableRefresh(true)
//            if (smDynRefresh.isRefreshing) smDynRefresh.finishRefresh()
//            if (viewModel.page == 0) dynAdapter.setList(it)
//            else dynAdapter.addData(it)
//            if (it.size != 20)  dynAdapter.loadMoreModule.loadMoreEnd(true)
//            else dynAdapter.loadMoreModule.loadMoreComplete()
//        })
//        dynAdapter.apply {
//            loadMoreModule.setOnLoadMoreListener {
//                viewModel.loadRequest(dynAdapter.data.size <=0)
//            }
//
//            setOnItemClickListener { adapter, v, position ->
//                val bean: GankAndroidBean = adapter.data[position] as GankAndroidBean
////                Navigation.findNavController(v).navigate( MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(bean.url ?: ""))
//            }
//        }
    }
    private fun dropDownRefresh() {
        viewModel.refreshRequest(dynAdapter.data.size <=0)
    }


    override fun getLayoutId(): Int =R.layout.fragment_dyn


}