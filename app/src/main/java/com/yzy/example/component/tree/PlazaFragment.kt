package com.yzy.example.component.tree

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.project.AriticleAdapter
import com.yzy.example.databinding.FragmentPlazaBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.repository.model.TreeViewModel
import kotlinx.android.synthetic.main.fragment_plaza.*
import com.yzy.example.widget.recyclerview.SpaceItemDecoration

class PlazaFragment  : CommFragment<TreeViewModel, FragmentPlazaBinding>(){
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(),showTag = true ) }
    override fun initContentView() {
        //初始化recyclerView
        recyclerPlazaView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            //初始化FloatingActionButton
            it.initFloatBtn(fabPlazaView)
        }
        viewModel.getPlazaData(true)
        //初始化 SwipeRefreshLayout
        swipePlazaRefresh.init {
            //触发刷新监听时请求数据
            viewModel.getPlazaData(true)
        }
        articleAdapter.apply {
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadData()
            }
        }
        viewModel.plazaDataState.observe(viewLifecycleOwner, Observer {
            swipePlazaRefresh.isRefreshing = false
            if (it.isSuccess) {
                //成功
                when {
                    //第一页并没有数据 显示空布局界面
                    it.isFirstEmpty -> {
                    }
                    //是第一页
                    it.isRefresh -> {
                        articleAdapter.setNewInstance(it.listData)
                    }
                    //不是第一页
                    else -> {
                        articleAdapter.addData(it.listData?: mutableListOf())
                    }
                }
            } else {
                //失败
                if (it.isRefresh) {
                    //如果是第一页，则显示错误界面，并提示错误信息
//                    loadsir.setErrorText(it.errMessage)
//                    loadsir.showCallback(ErrorCallback::class.java)
                } else {
//                    recyclerView.loadMoreError(0, it.errMessage)
                }
            }
        })
    }

    override fun getLayoutId(): Int  = R.layout.fragment_plaza
}