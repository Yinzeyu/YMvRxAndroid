package com.yzy.example.component.tree

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.extention.gone
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.tree.adapter.NavigationAdapter
import com.yzy.example.databinding.FragmentNavigationBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.repository.model.NavigationViewModel
import kotlinx.android.synthetic.main.fragment_navigation.*
import com.yzy.example.widget.recyclerview.SpaceItemDecoration
import kotlinx.android.synthetic.main.layout_comm_title.*

class NavigationFragment : CommFragment<NavigationViewModel, FragmentNavigationBinding>() {
    private val navigationAdapter: NavigationAdapter by lazy { NavigationAdapter(arrayListOf()) }
    override fun initContentView() {
        commTitleText.text = "导航"
        commTitleBack.gone()
        //初始化recyclerView
        recyclerNavigationView.init(LinearLayoutManager(context), navigationAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(fabNavigationView)
        }
        viewModel.getNavigationData()
        //初始化 SwipeRefreshLayout
        swipeNavigationRefresh.init {
            //触发刷新监听时请求数据
            viewModel.getNavigationData()
        }
        viewModel.navigationDataState.observe(viewLifecycleOwner, Observer {
            swipeNavigationRefresh.isRefreshing = false
            if (it.isSuccess) {
                navigationAdapter.setNewInstance(it.listData)
            } else {
//                loadsir.setErrorText(it.errMessage)
//                loadsir.showCallback(ErrorCallback::class.java)
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_navigation
}