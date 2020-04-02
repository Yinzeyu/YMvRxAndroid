package com.yzy.example.component.playlist

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yzy.example.component.playlist.viewmoel.PlayListViewModel
import com.blankj.utilcode.util.StringUtils
import com.dueeeke.videocontroller.component.*
import com.dueeeke.videoplayer.player.VideoView
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.extention.extKeepScreenOn
import com.yzy.baselibrary.extention.removeParent
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.comm.CommTitleFragment
import com.yzy.example.repository.ViewModelFactory
import com.yzy.example.repository.bean.VideoBean
import com.yzy.example.widget.video.controller.StandardVideoController
import com.yzy.example.widget.video.view.ExoVideoView
import kotlinx.android.synthetic.main.activity_play_list.*
import kotlinx.android.synthetic.main.item_list_video.view.itemVideoContainer
import kotlinx.android.synthetic.main.item_list_video.view.itemVideoPrepareView

class PlayListFragment : CommTitleFragment() {

  private var mVideoView: VideoView<*>? = null
  private var mController: StandardVideoController? = null
  private var mErrorView: ErrorView? = null
  private var mCompleteView: CompleteView? = null
  private var mTitleView: TitleView? = null
  //当前播放的位置
  private var mCurPos = -1
  //上次播放的位置，用于页面切回来之后恢复播放
  private var mLastPos: Int = mCurPos
  //数据
  private var mVideoList: MutableList<VideoBean> = mutableListOf()

  //数据层
  private val mViewModel: PlayListViewModel by lazy {
    ViewModelProvider(
      requireActivity(),
      ViewModelFactory()
    ).get(PlayListViewModel::class.java)
  }
  override fun layoutResContentId() = R.layout.activity_play_list


  override fun initContentView() {
    mContext.extKeepScreenOn()
    setTitleText(StringUtils.getString(R.string.title_play_list))
    //播放相关
    mVideoView = ExoVideoView(mContext)
    mVideoView?.setOnStateChangeListener(object : VideoView.SimpleOnStateChangeListener() {
      override fun onPlayStateChanged(playState: Int) {
        super.onPlayStateChanged(playState)
        //监听VideoViewManager释放，重置状态
        if (playState == VideoView.STATE_IDLE) {
          mVideoView?.removeParent()
          mLastPos = mCurPos
          mCurPos = -1
        }
      }
    })
    //播放控制器
    mController = StandardVideoController(mContext)
    mErrorView = ErrorView(mContext)
    mCompleteView = CompleteView(mContext)
    mTitleView = TitleView(mContext)
    mController?.addControlComponent(mErrorView)
    mController?.addControlComponent(mCompleteView)
    mController?.addControlComponent(mTitleView)
    mController?.addControlComponent(VodControlView(mContext))
    mController?.addControlComponent(GestureView(mContext))
    mController?.setEnableOrientation(true)
    mVideoView?.setVideoController(mController)
    //列表相关
    playListRecycler.setController(epoxyController)
    playListRecycler.addOnChildAttachStateChangeListener(object :
        RecyclerView.OnChildAttachStateChangeListener {
      override fun onChildViewDetachedFromWindow(view: View) {
        view.itemVideoContainer?.getChildAt(0)
            ?.let {
              if (it == mVideoView && mVideoView?.isFullScreen == false) {
                releaseVideoView()
              }
            }
      }

      override fun onChildViewAttachedToWindow(view: View) {
      }
    })
    mViewModel.getVideoList()
  }

  override fun initData() {

    mViewModel.run {
      uiState.observe(this@PlayListFragment, Observer {
        it?.showSuccess?.let { list ->
          dismissLoadingView()
          epoxyController.data = list

        }
      })
    }
  }

  //epoxy
  private val epoxyController = MvRxEpoxyController<MutableList<VideoBean>> { state ->
    //记录数据，方便点击的时候计算位置，因为没有添加分割线，所以不需要处理播放位置
    mVideoList = state
    //添加视频item
    state.forEachIndexed { index, videoBean ->
      videoListItem {
        id("play_list_${videoBean.id}")
        videoBean(videoBean)
        onItemClick { mContext.toast(videoBean.title?:"") }
        onContainerClick { startPlay(mVideoList.indexOf(it)) }
      }
    }
    //添加没有更多的item
//    if (!state.hasMore) {
//      loadMoreItem {
//        id("play_list_more")
//        fail(true)
//        tipsText(StringUtils.getString(R.string.no_more_data))
//      }
//    }
  }

  override fun onPause() {
    super.onPause()
    releaseVideoView()
  }

  override fun onResume() {
    super.onResume()
    if (mLastPos == -1) return
    //恢复上次播放的位置
    startPlay(mLastPos)
  }

  //开始播放
  private fun startPlay(position: Int) {
    if (mCurPos == position) return
    if (mCurPos != -1) releaseVideoView()
    val videoBean = mVideoList[position]
    mVideoView?.setUrl(videoBean.url)
    playListRecycler.layoutManager?.findViewByPosition(position)
        ?.let {
          //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true
          mController?.addControlComponent(it.itemVideoPrepareView, true)
          mVideoView?.removeParent()
          it.itemVideoContainer.addView(mVideoView, 0)
          mVideoView?.start()
          mCurPos = position
        }
  }

  //释放播放
  @SuppressLint("SourceLockedOrientationActivity")
  private fun releaseVideoView() {
    mVideoView?.let {
      it.release()
      if (it.isFullScreen) it.stopFullScreen()
      if (mContext.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        mContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
      }
      mCurPos = -1
    }
  }
}