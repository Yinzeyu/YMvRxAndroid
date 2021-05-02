package com.yzy.example.component.tree

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.tree.adapter.NavigationAdapter
import com.yzy.example.databinding.FragmentNavigationBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.model.NavigationViewModel
import com.yzy.example.widget.recyclerview.SpaceItemDecoration

class NavigationFragment : CommFragment<NavigationViewModel, FragmentNavigationBinding>() {
    private val navigationAdapter: NavigationAdapter by lazy { NavigationAdapter(arrayListOf()) }
    override fun initContentView() {
//        commTitleText.text = "导航"
//        commTitleBack.gone()
//        //初始化recyclerView
//        recyclerNavigationView.init(LinearLayoutManager(context), navigationAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
//            it.initFloatBtn(fabNavigationView)
//        }
//        viewModel.getNavigationData()
//        //初始化 SwipeRefreshLayout
//        swipeNavigationRefresh.init {
//            //触发刷新监听时请求数据
//            viewModel.getNavigationData()
//        }
//        viewModel.navigationDataState.observe(viewLifecycleOwner, Observer {
//            swipeNavigationRefresh.isRefreshing = false
//            if (it.isSuccess) {
//                navigationAdapter.setNewInstance(it.listData)
//            } else {
////                loadsir.setErrorText(it.errMessage)
////                loadsir.showCallback(ErrorCallback::class.java)
//            }
//        })
//        navigationAdapter.setNavigationClickInterFace(object :
//            NavigationAdapter.NavigationClickInterFace {
//            override fun onNavigationClickListener(item: ArticleDataBean, view: View) {
//
//                nav().navigate( MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(ariticleData = item))
////                nav().navigate(R.id.action_mainfragment_to_webFragment,
////                    Bundle().apply {
////                        putParcelable("ariticleData", item)
////                    }
////                )
//            }
//        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_navigation
}