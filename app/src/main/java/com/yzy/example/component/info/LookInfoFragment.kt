package com.yzy.example.component.info

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.example.R
import com.yzy.example.component.project.AriticleAdapter
import com.yzy.example.databinding.FragmentLookinfoBinding
import com.yzy.example.extention.setNbOnItemClickListener
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.model.LookInfoViewModel
import com.yzy.example.widget.CollectView
import com.yzy.example.widget.recyclerview.SpaceItemDecoration

/**
 * 描述　: 查看他人信息
 */
class LookInfoFragment : BaseFragment<LookInfoViewModel, FragmentLookinfoBinding>() {
    override fun getLayoutId(): Int =R.layout.fragment_lookinfo
    //对方的Id
    private var shareId = 0

    //界面状态管理者
//    private lateinit var loadsir: LoadService<Any>

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

//    //收藏viewmodel 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的
//    private val requestCollectViewModel: RequestCollectViewModel by lazy { getViewModel<RequestCollectViewModel>() }
//
//    //专门负责请求数据的Viewmodel 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的
//    private val requestLookInfoViewModel: RequestLookInfoViewModel by lazy { getViewModel<RequestLookInfoViewModel>() }

    override fun initView(savedSate: Bundle?) {
        arguments?.let {
            shareId = it.getInt("id")
        }
        viewModel.getLookinfo(shareId, true)
        binding.vm = viewModel
//        "他的信息"
//        shareViewModel.appColor.value.let { share_layout.setBackgroundColor(it) }
//
//        toolbar.initClose() {
//            nav().navigateUp()
//        }
//        loadsir = LoadServiceInit(share_linear) {
//            loadsir.showCallback(LoadingCallback::class.java)
//            requestLookInfoViewModel.getLookinfo(shareId, true)
//        }
//        recyclerInfoView.init(LinearLayoutManager(context), articleAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
//
//            it.initFloatBtn(fabInfo)
//        }
//        swipeInfoRefresh.init {
//            viewModel.getLookinfo(shareId, true)
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
////                        nav().navigate(R.id.action_lookInfoFragment_to_loginFragment)
////                    }
//                }
//            })
//            setNbOnItemClickListener { adapter, view, position ->
////                nav().navigate(R.id.action_lookInfoFragment_to_webFragment, Bundle().apply {
////                    putParcelable(
////                        "ariticleData",
////                        articleAdapter.data[position - recyclerView.headerCount]
////                    )
////                })
//            }
//        }
//        viewModel.shareResponse.observe(viewLifecycleOwner, Observer {
//            viewModel.name.postValue(it.coinInfo.username)
//            viewModel.info.postValue("积分 : ${it.coinInfo.coinCount}　排名 : ${it.coinInfo.rank}")
//        })
//        viewModel.shareListDataUistate.observe(viewLifecycleOwner, Observer {
//            swipeInfoRefresh.isRefreshing = false
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
    }

//    override fun lazyLoadData() {
//
//    }
//
//    override fun createObserver() {
//
//    }
}