package com.yzy.example.component.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.yzy.baselibrary.extention.inflate
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.main.item.HomeListAdapter
import com.yzy.example.extention.load
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.model.NewGankViewModel
import com.yzy.example.widget.CycleViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_banner.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class HomeFragment : CommFragment<NewGankViewModel,ViewDataBinding>() {
    private val mAdapter by lazy { HomeListAdapter() }
    private lateinit var banner: CycleViewPager
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun fillStatus(): Boolean = false

    override val contentLayout: Int = R.layout.fragment_home
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun initView(root: View?) {
        smRefresh.setOnRefreshListener {
            viewModel.getBanner(true)
        }
        viewModel.getBanner(true)
    }

    override fun initData() {
        with(rv_home) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            //banner

           val  bannerView = mContext.inflate(R.layout.item_banner)
           banner= bannerView.itemBanner
           banner.mViewPager2?.setPageTransformer(CompositePageTransformer())
           mAdapter.addHeaderView(bannerView)
        }

        mAdapter.apply {
            val addLoadMoreModule = addLoadMoreModule(this)
            addLoadMoreModule.setOnLoadMoreListener {

            }
            setOnItemClickListener { adapter, _, position ->
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            val bannerBean = it.bannerBean
                    if (bannerBean.isNotEmpty()){
                        banner.listSize = bannerBean.size
                        val bannerAdapter = ViewPagerAdapter(bannerBean)
                        banner.setAdapter(bannerAdapter)
                        banner.setAutoTurning(3000L)
                    }
                    mAdapter.setList(it.articleBean)
        })

        viewModel.defUI.showDialog.observe(viewLifecycleOwner, Observer {
            showLoadingView()
        })

        viewModel.defUI.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoadingView()
        })
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
}
