package com.yzy.example.component.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.inflate
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentHomeBinding
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.extention.loadUrl
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.model.HomeViewModel
import com.yzy.example.widget.CycleViewPager

class HomeFragment : CommFragment<HomeViewModel, FragmentHomeBinding>() {
    private val mAdapter by lazy {
        HomeListAdapter(
            mutableListOf(),
            true
        )
    }
    private lateinit var banner: CycleViewPager

    override fun initContentView() {
//        binding.vm = viewModel
//        commTitleText.text = "首页"
//        commTitleBack.gone()
//        smRefresh.setEnableRefresh(false)
//        smRefresh.setOnRefreshListener {
//            viewModel.getBanner(true)
//        }
//        viewModel.loadLocal(true)
//        with(rvHomeRecycler) {
//            adapter = mAdapter
//            val bannerView = context.inflate(R.layout.item_banner)
//            banner = bannerView.itemBanner
//            banner.mViewPager2?.setPageTransformer(CompositePageTransformer())
//            mAdapter.removeAllHeaderView()
//            mAdapter.addHeaderView(bannerView)
//            addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
//            initFloatBtn(floatbtn)
//        }
//
//        mAdapter.apply {
//            loadMoreModule.setOnLoadMoreListener {
//                viewModel.loadData()
//            }
//            setOnItemClickListener { adapter, v, position ->
//                val bean: ArticleDataBean = adapter.data[position] as ArticleDataBean
//                Navigation.findNavController(v).navigate(
//                    MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(ariticleData = bean)
//                )
//            }
//        }
//
//        viewModel.run {
//            //监听首页文章列表请求的数据变化
//            homeDataState.observe(viewLifecycleOwner, Observer {
//                smRefresh.finishRefresh()
//                if (it.isSuccess) {
//                    //成功
//                    when {
//                        //是第一页
//                        it.isRefresh -> {
//                            mAdapter.setNewInstance(it.listData)
//                            loadMore(it.isEmpty)
//                        }
//                        //不是第一页
//                        else -> {
//                            mAdapter.addData(it.listData ?: mutableListOf())
//                            loadMore(it.isEmpty)
//                        }
//                    }
//                } else {
//                    loadMore(false)
//                }
//            })
//            //监听轮播图请求的数据变化
//            bannerDataState.observe(viewLifecycleOwner, Observer { resultState ->
//                val bannerBean = resultState.listData ?: mutableListOf()
//                if (bannerBean.isNotEmpty()) {
//                    banner.listSize = bannerBean.size
//                    val bannerAdapter =
//                        ViewPagerAdapter(
//                            bannerBean
//                        )
//                    banner.setAdapter(bannerAdapter)
//                    banner.setAutoTurning(3000L)
//                }
//            })
//        }
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
            holder.mContainer.loadUrl(list[position % list.size].imagePath)

        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        viewModel.bannerDataState.value?.let { viewModel.setValue(it) }
//        viewModel.homeDataState.value?.let { viewModel.setHomeListValue(it) }
        super.onViewStateRestored(savedInstanceState)
    }
}
