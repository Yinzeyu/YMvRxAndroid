package com.yzy.example.component.tree

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.tree.adapter.SystemAdapter
import com.yzy.example.databinding.FragmentSystemBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.extention.setNbOnItemClickListener
import com.yzy.example.repository.bean.SystemBean
import com.yzy.example.repository.model.SystemViewModel
import kotlinx.android.synthetic.main.fragment_system.*
import com.yzy.example.widget.recyclerview.SpaceItemDecoration
import kotlinx.android.synthetic.main.layout_comm_title.*

class SystemFragment  : CommFragment<SystemViewModel, FragmentSystemBinding>(){
    private val systemAdapter: SystemAdapter by lazy {
        SystemAdapter(
            arrayListOf()
        )
    }
    override fun initContentView() {
        commTitleText.text = "体系"
        commTitleBack.gone()
        //初始化recyclerView
        recyclerSystemView.init(LinearLayoutManager(context), systemAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(fabSystemView)
        }
        viewModel.getSystemData()
        //初始化 SwipeRefreshLayout
        swipeSystemRefresh.init {
            //触发刷新监听时请求数据
            viewModel.getSystemData()
        }
        viewModel.systemDataState.observe(viewLifecycleOwner, Observer {
            swipeSystemRefresh.isRefreshing = false
            if (it.isSuccess) {
                systemAdapter.setNewInstance(it.listData)
            } else {
//                loadsir.setErrorText(it.errMessage)
//                loadsir.showCallback(ErrorCallback::class.java)
            }
        })
        systemAdapter.run {
            setNbOnItemClickListener { _, view, position ->
                nav().navigate(MainFragmentDirections.actionMainFragmentToSystemArrFragment( systemAdapter.data[position]))
//                    ,
//                    Bundle().apply {
//                        putParcelable("data", systemAdapter.data[position])
//                    }
//                )
            }
            setChildClick { item: SystemBean, view, position ->
                nav().navigate(MainFragmentDirections.actionMainFragmentToSystemArrFragment(item,position))
//                    ,
//                    Bundle().apply {
//                        putParcelable("data", item)
//                        putInt("index", position)
//                    }
//                )
            }
        }
    }

    override fun getLayoutId(): Int  = R.layout.fragment_system
}