package com.yzy.example.component.playlist.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.dueeeke.videoplayer.controller.ControlWrapper
import com.dueeeke.videoplayer.controller.IControlComponent
import com.dueeeke.videoplayer.player.VideoView
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.toast
import com.yzy.baselibrary.extention.visible
import com.yzy.example.R
import kotlinx.android.synthetic.main.item_play_pager.view.*
import kotlin.math.abs

/**
 * Description:
 * @author: yzy
 * @date: 2019/12/13 12:07
 */
class PagerItemView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), IControlComponent {

  private var thumb: ImageView? = null
  private var mPlayBtn: ImageView? = null
  private var mLoadingView: View? = null

  private var mControlWrapper: ControlWrapper? = null
  private var mScaledTouchSlop = 0
  private var mStartX: Int = 0
  private var mStartY: Int = 0

  init {
    LayoutInflater.from(getContext()).inflate(R.layout.item_play_pager, this, true)
    thumb = itemPlayPagerThumb
    mPlayBtn = itemPlayPagerBtn
    mLoadingView = itemPlayPagerLoading
    this.click { if (mLoadingView?.visibility != View.VISIBLE) mControlWrapper?.togglePlay() }
    mScaledTouchSlop = ViewConfiguration.get(getContext())
        .scaledTouchSlop
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        mStartX = event.x.toInt()
        mStartY = event.y.toInt()
        return true
      }
      MotionEvent.ACTION_UP -> {
        val endX = event.x.toInt()
        val endY = event.y.toInt()
        if (abs(endX - mStartX) < mScaledTouchSlop && abs(endY - mStartY) < mScaledTouchSlop) {
          performClick()
        }
      }
    }
    return false
  }

  override fun onPlayStateChanged(playState: Int) {
    when (playState) {
      VideoView.STATE_IDLE -> thumb?.visible()
      VideoView.STATE_PLAYING -> {
        thumb?.gone()
        mPlayBtn?.gone()
        mLoadingView?.gone()
      }
      VideoView.STATE_PAUSED -> mPlayBtn?.visible()
      VideoView.STATE_BUFFERING -> if (mPlayBtn?.visibility != View.VISIBLE) mLoadingView?.visible()
      VideoView.STATE_BUFFERED -> mLoadingView?.gone()
      VideoView.STATE_PREPARED -> LogUtils.i("STATE_PREPARED " + hashCode())
      VideoView.STATE_ERROR -> context?.toast(StringUtils.getString(R.string.dkplayer_error_message))
    }
  }

  override fun attach(controlWrapper: ControlWrapper) {
    mControlWrapper = controlWrapper
  }

  override fun getView(): View {
    return this
  }

  override fun onPlayerStateChanged(playerState: Int) {
  }

  override fun setProgress(
    duration: Int,
    position: Int
  ) {
  }

  override fun onVisibilityChanged(
    isVisible: Boolean,
    anim: Animation?
  ) {
  }

  override fun onLockStateChanged(isLocked: Boolean) {
  }
}