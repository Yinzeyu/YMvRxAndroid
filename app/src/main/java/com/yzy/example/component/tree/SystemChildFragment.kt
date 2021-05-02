package com.yzy.example.component.tree

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.project.AriticleAdapter
import com.yzy.example.databinding.FragmentSystemChildBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.extention.setNbOnItemChildClickListener
import com.yzy.example.extention.setNbOnItemClickListener
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.model.SystemArrViewModel


class SystemChildFragment : BaseFragment<SystemArrViewModel, FragmentSystemChildBinding>() {
    override fun getLayoutId(): Int  =R.layout.fragment_system_child
    //界面状态管理者
//    private lateinit var loadsir: LoadService<Any>

    private var cid = -1

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
//    private lateinit var footView: DefineLoadMoreView

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

//    //收藏viewmodel 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的
//    private val requestCollectViewModel: RequestCollectViewModel by lazy { getViewModel<RequestCollectViewModel>() }
//
//    /** 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的 */
//    private val requestTreeViewModel: RequestTreeViewModel by lazy { getViewModel<RequestTreeViewModel>() }


    override fun initView(savedSate: Bundle?) {
//        arguments?.let {
//            cid = it.getInt("cid")
//        }
////        requestTreeViewModel.getSystemChildData(true, cid)
////        //状态页配置
////        loadsir = LoadServiceInit(swipeRefresh) {
////            //点击重试时触发的操作
////            loadsir.showCallback(LoadingCallback::class.java)
////            requestTreeViewModel.getSystemChildData(true, cid)
////        }
//        viewModel.getSystemChildData(true, cid)
//        //初始化recyclerView
//        recyclerSystemChildView.init(LinearLayoutManager(context), articleAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
//            //初始化FloatingActionButton
//            it.initFloatBtn(fabSystemChildView)
//        }
//        //初始化 SwipeRefreshLayout
//        swipeSystemChildRefresh.init {
//            //触发刷新监听时请求数据
//            viewModel.getSystemChildData(true, cid)
//        }
//        articleAdapter.run {
//            setOnCollectViewClickListener(object :
//                AriticleAdapter.OnCollectViewClickListener {
//                override fun onClick(item: ArticleDataBean, v: CollectView, position: Int) {
////                    if (shareViewModel.isLogin.value) {
////                        if (v.isChecked) {
////                            requestCollectViewModel.uncollect(item.id)
////                        } else {
////                            requestCollectViewModel.collect(item.id)
////                        }
////                    } else {
////                        v.isChecked = true
////                        nav().navigate(R.id.action_systemArrFragment_to_loginFragment)
////                    }
//                }
//            })
//            setNbOnItemClickListener { adapter, view, position ->
//                nav().navigate( MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(ariticleData =  articleAdapter.data[position]))
////
////                    , Bundle().apply {
////                    putParcelable("ariticleData",)
////                })
//            }
//            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
//            setNbOnItemChildClickListener { adapter, view, position ->
//                when (view.id) {
//                    R.id.item_home_author, R.id.item_project_author -> {
//                        nav().navigate(SystemArrFragmentDirections.actionSystemArrFragmentToLookInfoFragment(articleAdapter.data[position].userId))
//                    }
////                            R.id.action_systemArrFragment_to_lookInfoFragment,
////                            Bundle().apply {
////                                putInt("id", articleAdapter.data[position].userId)
////                            })
////                    }
//                }
//            }
//        }
//        viewModel.systemChildDataState.observe(viewLifecycleOwner, Observer {
////            swipeRefresh.isRefreshing = false
////            recyclerSystemChildView.loadMoreFinish(it.isEmpty, it.hasMore)
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
//                } else {
//                    //失败
////                if (it.isRefresh) {
////                    //如果是第一页，则显示错误界面，并提示错误信息
////                    loadsir.setErrorText(it.errMessage)
////                    loadsir.showCallback(ErrorCallback::class.java)
////                } else {
////                    recyclerView.loadMoreError(0, it.errMessage)
////                }
//                }
//            })
    }

//    override fun lazyLoadData() {
//
//    }

//    override fun createObserver() {
//
//        }

//        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
//            if (it.isSuccess) {
//                //收藏或取消收藏操作成功，发送全局收藏消息
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
//        }
//        //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
//        eventViewModel.collect.observe(viewLifecycleOwner, Observer {
//            for (index in articleAdapter.data.indices) {
//                if (articleAdapter.data[index].id == it.id) {
//                    articleAdapter.data[index].collect = it.collect
//                    articleAdapter.notifyItemChanged(index)
//                    break
//                }
//            }
//        })
//    }

    companion object {
        fun newInstance(cid: Int): SystemChildFragment {
            return SystemChildFragment().apply {
                arguments = Bundle().apply {
                    putInt("cid", cid)
                }
            }
        }
    }

}