package com.yzy.example.component.collect

import android.os.Bundle
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.example.R
import com.yzy.example.component.collect.adapter.CollectAdapter
import com.yzy.example.databinding.FragmentCollectAriticleBinding
import com.yzy.example.repository.model.CollectViewModel

/**
 * 描述　: 收藏的文章集合Fragment
 */
class CollectAriticleFragment : BaseFragment<CollectViewModel, FragmentCollectAriticleBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_collect_ariticle
    //界面状态管理者
//    private lateinit var loadsir: LoadService<Any>

    private val articleAdapter: CollectAdapter by lazy { CollectAdapter(arrayListOf()) }

//    override fun layoutId() = R.layout.include_list

    override fun initView(savedSate: Bundle?) {
        //状态页配置
//        loadsir = LoadServiceInit(swipeRefresh) {
//            //点击重试时触发的操作
//            loadsir.showCallback(LoadingCallback::class.java)
//            mViewModel.getCollectAriticleData(true)
//        }
        viewModel.getCollectAriticleData(true)
        //初始化recyclerView
//        rvCollectAriticleRecycler.init(LinearLayoutManager(context), articleAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
////            it.initFooter(SwipeRecyclerView.LoadMoreListener {
////                mViewModel.getCollectAriticleData(false)
////            })
//            //初始化FloatingActionButton
//            it.initFloatBtn(fabCollectAriticleView)
//        }
//        smCollectAriticleRefresh.setOnRefreshListener {
//            viewModel.getCollectAriticleData(true)
//        }
        //初始化 SwipeRefreshLayout
//        swipeRefresh.init {
//            //触发刷新监听时请求数据
//            mViewModel.getCollectAriticleData(true)
//        }
//        articleAdapter.run {
//            setOnCollectViewClickListener(object :
//                CollectAdapter.OnCollectViewClickListener {
//                override fun onClick(item: ArticleDataBean, v: CollectView, position: Int) {
////                    if (v.isChecked) {
////                        mViewModel.uncollect(item.originId)
////                    } else {
////                        mViewModel.collect(item.originId)
////                    }
//                }
//            })
//            setNbOnItemClickListener { _, view, position ->
////                nav().navigate(R.id.action_collectFragment_to_webFragment, Bundle().apply {
////                    putParcelable("collect", articleAdapter.data[position])
////                })
//            }
//        }
//        viewModel.ariticleDataState.observe(viewLifecycleOwner, Observer {
//            smCollectAriticleRefresh.finishRefresh()
////            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
//            if (it.isSuccess) {
//                //成功
//                when {
//                    //第一页并没有数据 显示空布局界面
//                    it.isFirstEmpty -> {
////                        loadsir.showCallback(EmptyCallback::class.java)
//                    }
//                    //是第一页
//                    it.isRefresh -> {
////                        loadsir.showSuccess()
//                        articleAdapter.setNewInstance(it.listData)
//                    }
//                    //不是第一页
//                    else -> {
////                        loadsir.showSuccess()
//                        articleAdapter.addData(it.listData?: mutableListOf())
//                    }
//                }
//            } else {
//                //失败
////                if (it.isRefresh) {
////                    //如果是第一页，则显示错误界面，并提示错误信息
////                    loadsir.setErrorText(it.errMessage)
////                    loadsir.showCallback(ErrorCallback::class.java)
////                } else {
////                    recyclerView.loadMoreError(0, it.errMessage)
////                }
//            }
//        })
    }

//    override fun lazyLoadData() {
//
//    }
//
//    override fun createObserver() {
//        mViewModel.ariticleDataState.observe(viewLifecycleOwner, Observer {
//            swipeRefresh.isRefreshing = false
//            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
//            if (it.isSuccess) {
//                //成功
//                when {
//                    //第一页并没有数据 显示空布局界面
//                    it.isFirstEmpty -> {
//                        loadsir.showCallback(EmptyCallback::class.java)
//                    }
//                    //是第一页
//                    it.isRefresh -> {
//                        loadsir.showSuccess()
//                        articleAdapter.setNewInstance(it.listData)
//                    }
//                    //不是第一页
//                    else -> {
//                        loadsir.showSuccess()
//                        articleAdapter.addData(it.listData)
//                    }
//                }
//            } else {
//                //失败
//                if (it.isRefresh) {
//                    //如果是第一页，则显示错误界面，并提示错误信息
//                    loadsir.setErrorText(it.errMessage)
//                    loadsir.showCallback(ErrorCallback::class.java)
//                } else {
//                    recyclerView.loadMoreError(0, it.errMessage)
//                }
//            }
//        })
//        mViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
//            if (it.isSuccess) {
//                //收藏或取消收藏操作成功，发送全局收藏消息
//                eventViewModel.collect.postValue(CollectBus(it.id, it.collect))
//            } else {
//                showMessage(it.errorMsg)
//                for (index in articleAdapter.data.indices) {
//                    if (articleAdapter.data[index].originId == it.id) {
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
//                    if (articleAdapter.data[index].originId == it.id) {
//                        articleAdapter.remove(index)
//                        if (articleAdapter.data.size == 0) {
//                            loadsir.showCallback(EmptyCallback::class.java)
//                        }
//                        return@Observer
//                    }
//                }
//                mViewModel.getCollectAriticleData(true)
//            })
//        }
//    }

}