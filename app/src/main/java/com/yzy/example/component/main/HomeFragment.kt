package com.yzy.example.component.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.extention.inflate
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentHomeBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.extention.load
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.model.NewGankViewModel
import com.yzy.example.widget.CycleViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.item_banner.view.*
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration

class HomeFragment : CommFragment<NewGankViewModel,FragmentHomeBinding>() {
    private val mAdapter by lazy { HomeListAdapter(mutableListOf(),true) }
    private lateinit var banner: CycleViewPager

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    override fun initContentView() {
        //初始化 toolbar
        toolbar.run {
            init("首页")
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
//                        nav().navigate(R.id.action_mainfragment_to_searchFragment)
                    }
                }
                true
            }
        }
        smRefresh.setEnableRefresh(false)
        smRefresh.setOnRefreshListener {
            viewModel.getBanner(true)
        }
        viewModel.getBanner(true)
        with(rvHomeRecycler) {
            adapter = mAdapter
            val bannerView = context.inflate(R.layout.item_banner)
            banner = bannerView.itemBanner
            banner.mViewPager2?.setPageTransformer(CompositePageTransformer())
            mAdapter.addHeaderView(bannerView)
            addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            initFloatBtn(floatbtn)
        }

        mAdapter.apply {
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadData()
            }
            setOnItemClickListener { adapter, v, position ->
                val bean: ArticleDataBean = adapter.data[position] as ArticleDataBean
                Navigation.findNavController(v).navigate(
                    MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(
                        bean.link ?: ""
                    )
                )
            }
        }

        viewModel.run {
            //监听首页文章列表请求的数据变化
            homeDataState.observe(viewLifecycleOwner, Observer {
                smRefresh.finishRefresh()
//                recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
                if (it.isSuccess) {
                    //成功
                    when {
                        //第一页并没有数据 显示空布局界面
                        it.isFirstEmpty -> {
                        }
                        //是第一页
                        it.isRefresh -> {
                            mAdapter.setNewInstance(it.listData)
                            loadMore(it.isEmpty)
                        }
                        //不是第一页
                        else -> {
                            mAdapter.addData(it.listData?: mutableListOf())
                            loadMore(it.isEmpty)
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
            //监听轮播图请求的数据变化
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                val bannerBean = resultState.bannerBean?: mutableListOf()
                if (bannerBean.isNotEmpty()) {
                    banner.listSize = bannerBean.size
                    val bannerAdapter = ViewPagerAdapter(bannerBean)
                    banner.setAdapter(bannerAdapter)
                    banner.setAutoTurning(3000L)
                }
                smRefresh.setEnableRefresh(true)
                val articleBean = resultState.articleBean
                articleBean?.let {
                    mAdapter.setList( it.data)
                    loadMore(it.isEmpty)
                }
            })
        }
    }


    private fun loadMore(size: Boolean) {
        if (size) mAdapter.loadMoreModule.loadMoreEnd(true)
        else mAdapter.loadMoreModule.loadMoreComplete()
    }

    private class ViewPagerAdapter(var list: MutableList<BannerBean>) :
        RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

        internal inner class ViewPagerViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var mContainer: ImageView = itemView.findViewById(R.id.itemBannerIV)

        }

        override fun getItemCount(): Int {
            return if (list.size <= 1) 1 else Integer.MAX_VALUE
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
            return ViewPagerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_banner_child, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
            holder.mContainer.tag = list[position % list.size]
            holder.mContainer.load(list[position % list.size].imagePath)

        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home


}
