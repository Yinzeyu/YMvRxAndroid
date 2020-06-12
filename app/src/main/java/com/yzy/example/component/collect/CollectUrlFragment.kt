package com.yzy.example.component.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.example.R
import com.yzy.example.component.collect.adapter.CollectUrlAdapter
import com.yzy.example.databinding.FragmentCollectUrlBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.extention.setNbOnItemClickListener
import com.yzy.example.repository.bean.CollectUrlBean
import com.yzy.example.repository.model.CollectUrlViewModel
import com.yzy.example.widget.CollectView
import com.yzy.example.widget.recyclerview.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_collect_url.*

/**
 * 描述　: 收藏的网址集合Fragment
 */
class CollectUrlFragment : BaseFragment<CollectUrlViewModel, FragmentCollectUrlBinding>() {
    override fun getLayoutId(): Int  = R.layout.fragment_collect_url
    //界面状态管理者
//    private lateinit var loadsir: LoadService<Any>

    private val articleAdapter: CollectUrlAdapter by lazy { CollectUrlAdapter(arrayListOf()) }

    override fun initView(savedSate: Bundle?)  {
        viewModel.getCollectUrlData()
        //状态页配置
//        loadsir = LoadServiceInit(swipeRefresh) {
//            //点击重试时触发的操作
//            loadsir.showCallback(LoadingCallback::class.java)
//            mViewModel.getCollectUrlData()
//        }
        //初始化recyclerView
        rvCollectUrlRecycler.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            //初始化FloatingActionButton
            it.initFloatBtn(fabCollectUrlView)
        }
        smCollectUrlRefresh.setOnRefreshListener {
            viewModel.getCollectUrlData()
        }
        articleAdapter.run {
            setOnCollectViewClickListener(object :
                CollectUrlAdapter.OnCollectViewClickListener {
                override fun onClick(item: CollectUrlBean, v: CollectView, position: Int) {
//                    if (v.isChecked) {
//                        mViewModel.uncollectUrl(item.id)
//                    } else {
//                        mViewModel.collectUrl(item.name, item.link)
//                    }
                }
            })
            setNbOnItemClickListener { _, view, position ->
//               nav().navigate(R.id.action_collectFragment_to_webFragment, Bundle().apply {
//                        putParcelable("collectUrl", articleAdapter.data[position])
//                    })
            }
        }
        viewModel.urlDataState.observe(viewLifecycleOwner, Observer {
            smCollectUrlRefresh.finishRefresh()
//            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
            if (it.isSuccess) {
                //成功
                when {
                    //第一页并没有数据 显示空布局界面
                    it.isEmpty -> {
//                        loadsir.showCallback(EmptyCallback::class.java)
                    }
                    else -> {
//                        loadsir.showSuccess()
                        articleAdapter.setNewInstance(it.listData)
                    }
                }
            } else {
                //失败
//                loadsir.setErrorText(it.errMessage)
//                loadsir.showCallback(ErrorCallback::class.java)
            }
        })
    }

//    override fun lazyLoadData() {
//        mViewModel.getCollectUrlData()
//    }

//    override fun createObserver() {
//
//        mViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
//            if (it.isSuccess) {
//                for (index in articleAdapter.data.indices) {
//                    if (articleAdapter.data[index].id == it.id) {
//                        articleAdapter.remove(index)
//                        if (articleAdapter.data.size == 0) {
//                            loadsir.showCallback(EmptyCallback::class.java)
//                        }
//                        return@Observer
//                    }
//                }
//            } else {
//                showMessage(it.errorMsg)
//                for (index in articleAdapter.data.indices) {
//                    if (articleAdapter.data[index].id == it.id) {
//                        articleAdapter.notifyItemChanged(index)
//                        break
//                    }
//                }
//            }
//        })
//        eventViewModel.run {
//            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则 需要删除他 否则则请求最新收藏数据
//            collect.observe(viewLifecycleOwner, Observer {
//                for (index in articleAdapter.data.indices) {
//                    if (articleAdapter.data[index].id == it.id) {
//                        articleAdapter.data.removeAt(index)
//                        articleAdapter.notifyItemChanged(index)
//                        if (articleAdapter.data.size == 0) {
//                            loadsir.showCallback(EmptyCallback::class.java)
//                        }
//                        return@Observer
//                    }
//                }
//                mViewModel.getCollectUrlData()
//            })
//        }
//    }

}