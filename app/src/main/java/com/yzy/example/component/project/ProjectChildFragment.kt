package com.yzy.example.component.project

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.example.R
import com.yzy.example.databinding.FragmentProjectChildBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.model.ProjectChildViewModel
import com.yzy.example.widget.CollectView
import kotlinx.android.synthetic.main.fragment_project_child.*
import com.yzy.example.widget.recyclerview.SpaceItemDecoration

/**
 * 作者　: yzy
 * 时间　: 2020/2/28
 * 描述　:
 */
class ProjectChildFragment : BaseFragment<ProjectChildViewModel, FragmentProjectChildBinding>() {

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    //界面状态管理者
//    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
//    private lateinit var footView: DefineLoadMoreView

    //是否是最新项目
    private var isNew = false

    //改项目对应的id
    private var cid = 0

    //收藏viewmodel 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的
//    private val requestCollectViewModel: RequestCollectViewModel by lazy { getViewModel<RequestCollectViewModel>() }
//
//    //请求的ViewModel 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的
//    private val requestProjectViewModel: RequestProjectViewModel by lazy { getViewModel<RequestProjectViewModel>() }

//    override fun layoutId() = R.layout.include_list

    override fun initView(savedSate: Bundle?) {
        arguments?.let {
            isNew = it.getBoolean("isNew")
            cid = it.getInt("cid")
        }
        articleAdapter.run {
            setOnCollectViewClickListener(object :
                AriticleAdapter.OnCollectViewClickListener {
                override fun onClick(item: ArticleDataBean, v: CollectView, position: Int) {
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
                }
            })
            //初始化recyclerView
            recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
                it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))

//                footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
//                    //触发加载更多时请求数据
//                    requestProjectViewModel.getProjectData(false, cid, isNew)
//                })
                //初始化FloatingActionButton
                it.initFloatBtn(floatbtn)
            }
            viewModel.loadLocal(true, cid, isNew)
            viewModel.projectDataState.observe(viewLifecycleOwner, Observer {
                swipeRefresh.isRefreshing = false
//                recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
                if (it.isSuccess) {
                    //成功
                    when {
                        //第一页并没有数据 显示空布局界面
                        it.isFirstEmpty -> {
//                            loadsir.showCallback(EmptyCallback::class.java)
                        }
                        //是第一页
                        it.isRefresh -> {
//                            loadsir.showSuccess()
                            articleAdapter.setNewInstance(it.listData)
                        }
                        //不是第一页
                        else -> {
//                            loadsir.showSuccess()
                            articleAdapter.addData(it.listData?: mutableListOf())
                        }
                    }
                } else {
                    //失败
                    if (it.isRefresh) {
                        //如果是第一页，则显示错误界面，并提示错误信息
//                        loadsir.setErrorText(it.errMessage)
//                        loadsir.showCallback(ErrorCallback::class.java)
                    } else {
//                        recyclerView.loadMoreError(0, it.errMessage)
                    }
                }
            })
    }

//    override fun lazyLoadData() {
//        //状态页配置
//        loadsir = LoadServiceInit(swipeRefresh) {
//            //点击重试时触发的操作
//            loadsir.showCallback(LoadingCallback::class.java)
//            requestProjectViewModel.getProjectData(true, cid, isNew)
//        }
//
//        //初始化 SwipeRefreshLayout
//        swipeRefresh.init {
//            //触发刷新监听时请求数据
//            requestProjectViewModel.getProjectData(true, cid, isNew)
//        }
//
//            setNbOnItemClickListener { adapter, view, position ->
//                nav().navigate(R.id.action_mainfragment_to_webFragment, Bundle().apply {
//                    putParcelable("ariticleData", articleAdapter.data[position])
//                })
//            }
//            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
//            setNbOnItemChildClickListener { adapter, view, position ->
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

//    override fun createObserver() {

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
    }

    companion object {
        fun newInstance(cid: Int, isNew: Boolean): ProjectChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            args.putBoolean("isNew", isNew)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        viewModel.projectDataState.value?.let { viewModel.setValue( "projectChild$cid",it) }
        super.onDestroyView()
    }

    override fun getLayoutId(): Int =R.layout.fragment_project_child

}