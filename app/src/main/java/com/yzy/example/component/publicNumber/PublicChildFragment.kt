package com.yzy.example.component.publicNumber

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.example.R
import com.yzy.example.component.project.AriticleAdapter
import com.yzy.example.databinding.FragmentProjectChildBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.repository.model.PublicChildViewModel
import kotlinx.android.synthetic.main.fragment_public_number_child.*
import com.yzy.example.widget.recyclerview.SpaceItemDecoration

class PublicChildFragment :
    BaseFragment<PublicChildViewModel, FragmentProjectChildBinding>() {

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    //该项目对应的id
    private var cid = 0

    override fun getLayoutId(): Int = R.layout.fragment_public_number_child

    override fun initView(savedSate: Bundle?) {
        arguments?.let {
            cid = it.getInt("cid")
        }
        viewModel.getPublicData(true, cid)
        //初始化recyclerView
        recyclerPublicChildView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))

            //初始化FloatingActionButton
            it.initFloatBtn(fabPublicChildView)
        }

        //初始化 SwipeRefreshLayout
        swipePublicChildRefresh.init {
            //触发刷新监听时请求数据
            viewModel.getPublicData(true, cid)
        }
        viewModel.publicDataState.observe(viewLifecycleOwner, Observer {
            swipePublicChildRefresh.isRefreshing = false
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
                        articleAdapter.addData(it.listData ?: mutableListOf())
                    }
                }
            } else {
                //失败
                if (it.isRefresh) {
                    //如果是第一页，则显示错误界面，并提示错误信息
                } else {
                }
            }
        })
        articleAdapter.apply {
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadData(cid)
            }
        }
    }

//    override fun lazyLoadData() {
//        //状态页配置
//        loadsir = LoadServiceInit(swipeRefresh) {
//            //点击重试时触发的操作
//            loadsir.showCallback(LoadingCallback::class.java)
//            requestPublicNumberViewModel.getPublicData(true, cid)
//        }
//        //初始化recyclerView
//        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
//            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
//                //触发加载更多时请求数据
//                requestPublicNumberViewModel.getPublicData(false, cid)
//            })
//            //初始化FloatingActionButton
//            it.initFloatBtn(floatbtn)
//        }
//
//        //初始化 SwipeRefreshLayout
//        swipeRefresh.init {
//            //触发刷新监听时请求数据
//            requestPublicNumberViewModel.getPublicData(true, cid)
//        }
//
//        articleAdapter.run {
//            setOnCollectViewClickListener(object :
//                AriticleAdapter.OnCollectViewClickListener {
//                override fun onClick(item: AriticleResponse, v: CollectView, position: Int) {
//                    if (shareViewModel.isLogin.value) {
//                        if (v.isChecked) {
//                            requestCollectViewModel.uncollect(item.id)
//                        } else {
//                            requestCollectViewModel.collect(item.id)
//                        }
//                    } else {
//                        v.isChecked = true
//                        nav().navigate(R.id.action_mainFragment_to_loginFragment)
//                    }
//                }
//            })
//            setNbOnItemClickListener { _, view, position ->
//                nav().navigate(R.id.action_mainfragment_to_webFragment, Bundle().apply {
//                    putParcelable("ariticleData", articleAdapter.data[position])
//                })
//            }
//            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
//            setNbOnItemChildClickListener { _, view, position ->
//                when (view.id) {
//                    R.id.item_home_author, R.id.item_project_author -> {
//                        nav().navigate(
//                            R.id.action_mainfragment_to_lookInfoFragment,
//                            Bundle().apply {
//                                putInt("id", articleAdapter.data[position].userId)
//                            })
//                    }
//                }
//            }
//        }
//
//    }
//
//    override fun createObserver() {
//
//        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
//            if (it.isSuccess) {
//                eventViewModel.collect.postValue(
//                    CollectBus(
//                        it.id,
//                        it.collect
//                    )
//                )
//            } else {
//                showMessage(it.errorMsg)
//                for (index in articleAdapter.data.indices) {
//                    if (articleAdapter.data[index].id == it.id) {
//                        articleAdapter.data[index].collect = it.collect
//                        articleAdapter.notifyItemChanged(index)
//                        break
//                    }
//                }
//            }
//        })
//        shareViewModel.run {
//            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
//            userinfo.observe(viewLifecycleOwner, Observer {
//                if (it != null) {
//                    it.collectIds.forEach { id ->
//                        for (item in articleAdapter.data) {
//                            if (id.toInt() == item.id) {
//                                item.collect = true
//                                break
//                            }
//                        }
//                    }
//                } else {
//                    for (item in articleAdapter.data) {
//                        item.collect = false
//                    }
//                }
//                articleAdapter.notifyDataSetChanged()
//            })
//            //监听全局的主题颜色改变
//            appColor.observe(viewLifecycleOwner, Observer {
//                setUiTheme(it, floatbtn, swipeRefresh, loadsir, footView)
//            })
//            //监听全局的列表动画改编
//            appAnimation.observe(viewLifecycleOwner, Observer {
//                articleAdapter.setAdapterAnimion(it)
//            })
//            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
//            eventViewModel.collect.observe(viewLifecycleOwner, Observer {
//                for (index in articleAdapter.data.indices) {
//                    if (articleAdapter.data[index].id == it.id) {
//                        articleAdapter.data[index].collect = it.collect
//                        articleAdapter.notifyItemChanged(index)
//                        break
//                    }
//                }
//            })
//        }
//    }

    companion object {
        fun newInstance(cid: Int): PublicChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            val fragment = PublicChildFragment()
            fragment.arguments = args
            return fragment
        }
    }
}