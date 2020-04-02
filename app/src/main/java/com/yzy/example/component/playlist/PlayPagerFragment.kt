package com.yzy.example.component.playlist

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import cc.abase.demo.component.playlist.view.PagerController
import com.dueeeke.videoplayer.player.VideoView
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.baselibrary.extention.removeParent
import com.yzy.example.R
import com.yzy.example.component.playlist.adapter.PlayPagerAdapter
import com.yzy.example.component.playlist.viewmoel.PlayPagerViewModel
import com.yzy.example.repository.ViewModelFactory
import com.yzy.example.repository.bean.VideoBean
import com.yzy.example.widget.video.controller.VodControlView
import com.yzy.example.widget.video.view.ExoVideoView
import kotlinx.android.synthetic.main.activity_play_pager.*

class PlayPagerFragment : BaseFragment() {


    //当前播放位置
    private var mCurPos = 0

    //适配器
    private var mPlayPagerAdapter: PlayPagerAdapter? = null

    //不显示移动网络播放
    private var mController: PagerController? = null

    //播放控件
    private var mVideoView: ExoVideoView? = null

    //数据源
    private var mVideoList: MutableList<VideoBean> = mutableListOf()

    //是否可以加载更多
    var hasMore: Boolean = true

    //数据层
    private val mViewModel: PlayPagerViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(PlayPagerViewModel::class.java)
    }

    override fun fillStatus() = false

    override val contentLayout: Int = R.layout.activity_play_pager

    override fun initView(root: View?) {

        //返回按钮
        playPagerBack.pressEffectAlpha()
        playPagerBack.click { onBackPressed() }
        //播放控件
        mVideoView = ExoVideoView(mContext)
        mVideoView?.setLooping(true)
        mVideoView?.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT)
        //控制器
        mController = PagerController(mContext)
        mController?.addControlComponent(VodControlView(mContext))
        mVideoView?.setVideoController(mController)
        //列表
        playPagerViewPager.offscreenPageLimit = 4
//        mSmartSwipeRefresh.setOnRefreshListener {
//            mViewModel.getVideoList(true)
//        }
//        mSmartSwipeRefresh.setOnLoadMoreListener {
//          mViewModel.getVideoList(false)
//        }

        //下拉刷新
//        mSmartSwipeRefresh = SmartSwipeRefresh.translateMode(playPagerViewPager, false)
//        mSmartSwipeRefresh.disableRefresh
//        mSmartSwipeRefresh.disableLoadMore()
//        mSmartSwipeRefresh.isNoMoreData = !hasMore
//        mSmartSwipeRefresh.dataLoader = object : SmartSwipeRefreshDataLoader {
//            override fun onLoadMore(ssr: SmartSwipeRefresh?) {
//                viewModel.loadMore()
//            }
//
//            override fun onRefresh(ssr: SmartSwipeRefresh?) {
//            }
//        }
    }


    override fun initData() {
        mViewModel.getVideoList(true)
        mViewModel.run {
            uiState.observe(this@PlayPagerFragment, Observer {
                it?.showSuccess?.let { list ->
                    mVideoList = list
                    if (mPlayPagerAdapter == null) {
                        initAdapter(list)
                    } else {
                        mPlayPagerAdapter?.setNewData(list)
                    }
                }
            })
        }
//        viewModel.subscribe(this) {
//            if (it.request.complete) {
//                dismissLoadingView()
//                mVideoList = it.videoList
//                hasMore = it.hasMore
//                mSmartSwipeRefresh?.finished(it.request is Success)
//                mSmartSwipeRefresh?.isNoMoreData = !hasMore
//                if (mPlayPagerAdapter == null) {
//                    initAdapter(it.videoList)
//                } else {
//                    mPlayPagerAdapter?.setNewData(it.videoList)
//                }
//            }
//        }
//        showLoadingView()
//        viewModel.loadData()
    }

    //初始化adapter
    private fun initAdapter(datas: MutableList<VideoBean>) {
        if (mPlayPagerAdapter == null && playPagerViewPager != null) {
            mPlayPagerAdapter = PlayPagerAdapter(datas)
            playPagerViewPager.adapter = mPlayPagerAdapter
            //随机从某一个开始播放
            val index = (Math.random() * datas.size).toInt()
            if (index != 0) {
                playPagerViewPager.currentItem = index
                //如果直接到最后一条需要显示没有更多
                if (index == mVideoList.size - 1) {
//                  mSmartSwipeRefresh.setEnableLoadMore(false)
//                    mSmartSwipeRefresh?.swipeConsumer?.enableBottom()
//                    mSmartSwipeRefresh?.isNoMoreData = true
                }
            }
            //第一次加载的时候设置currentItem会滚动刷新，所以播放需要延时
            playPagerViewPager.post {
                startPlay(index)
                playPagerViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        if (position == mCurPos) return
                        startPlay(position)
                        if (position == (mVideoList.size - 1)) {
//                          mSmartSwipeRefresh.setEnableLoadMore(false)
//                            mSmartSwipeRefresh?.swipeConsumer?.enableBottom()
//                            mSmartSwipeRefresh?.isNoMoreData = !hasMore
                        } else {
//                          mSmartSwipeRefresh.setEnableLoadMore(true)
//                            mSmartSwipeRefresh?.disableLoadMore()
                        }
                    }
                })
            }
        }
    }

    //开始播放
    private fun startPlay(position: Int) {
        //预加载更多
        if (position >= mVideoList.size - 5) mViewModel.getVideoList(false)
        //遍历加载信息和播放
        val count: Int = playPagerViewPager.childCount
        var findCount = 0//由于复用id是混乱的，所以需要保证3个都找到才跳出循环(为了节约性能)
        for (i in 0 until count) {
            val itemView: View = playPagerViewPager.getChildAt(i)
            val viewHolder: PlayPagerAdapter.PagerHolder = itemView.tag as PlayPagerAdapter.PagerHolder
            if (viewHolder.mPosition == position) {
                mVideoView?.release()
                mVideoView?.removeParent()
                val videoBean: VideoBean = mVideoList[viewHolder.mPosition]
                mVideoView?.setUrl(videoBean.url)
                mController?.addControlComponent(viewHolder.mPagerItemView, true)
                viewHolder.mPlayerContainer?.addView(mVideoView, 0)
                mVideoView?.start()
                mCurPos = position
                findCount++
            } else if (position > 0 && viewHolder.mPosition == position - 1) {//预加载上一个数据，否则滑动可能出现复用的数据
                mPlayPagerAdapter?.fillData(mVideoList[viewHolder.mPosition], viewHolder)
                findCount++
            } else if (position < mVideoList.size - 1 && viewHolder.mPosition == position + 1) {//预加载下一个数据，否则滑动可能出现复用的数据
                mPlayPagerAdapter?.fillData(mVideoList[viewHolder.mPosition], viewHolder)
                findCount++
            }
            if (findCount >= 3) break
        }
    }

    override fun onResume() {
        super.onResume()
        mVideoView?.resume()
    }

    override fun onPause() {
        super.onPause()
        mVideoView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView?.release()
    }

    override fun onBackPressed() {
        if (mVideoView == null || mVideoView?.onBackPressed() == false) {
            super.onBackPressed()
        }
    }
}