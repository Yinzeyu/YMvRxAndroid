package com.yzy.pj.ui.video

import android.content.Context
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ktx.immersionBar
import com.yzy.baselibrary.extention.startActivity
import com.yzy.commonlibrary.comm.CommActivity
import com.yzy.pj.R
import kotlinx.android.synthetic.main.activity_video_pldroid.*

class PLDroidActivity : CommActivity() {
    companion object {
        fun starPLDroidActivity(context: Context) {
            context.startActivity<PLDroidActivity>()
        }
    }

    override fun initStatus() {
        immersionBar { statusBarDarkFont(false) }
    }

    /**
     * 滑动切换的position
     */
    private var changePosition = -1
    private var currentPosition = -1
    var plAdapter: PlAdapter? = null
    override fun layoutResId(): Int = R.layout.activity_video_pldroid
    private var isFirst = true
    override fun initView() {
        plAdapter = PlAdapter()
        plListView.adapter = plAdapter
        plListView.orientation = ViewPager2.ORIENTATION_VERTICAL
        plListView.getChildAt(0).overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        val list = mutableListOf<String>()
        list.add("http://jzvd.nathen.cn/c494b340ff704015bb6682ffde3cd302/64929c369124497593205a4190d7d128-5287d2089db37e62345123a1be272f8b.mp4")
        plAdapter?.addList(list)
        plListView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changePosition = position
                if (isFirst) {
                    //后台播放按钮或者通知栏进入第一次`
                } else {
                    //if (MediaPlayHelper.isPlaying()) {
//                    stopPlayAudio()
                    //}
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == 1) {
                    // state == 1 滑动开始

                }
//                if ((changePosition != currentPosition || MediaPlayHelper.isStop()) && state == 0) {
//                    //state == 0 滑动停止,滑动停止后在加载详情数据，放在onPageSelected中会卡顿
//                    showItem(changePosition, true)
//                }
            }
        })
    }

    override fun initData() {
    }
//    private fun initVideoPlayer(url: String) {
//        val options = AVOptions()
//        // 快开模式，启用后会加快该播放器实例再次打开相同协议的视频流的速度
//        options.setInteger(AVOptions.KEY_FAST_OPEN, 1)
//        // 打开重试次数，设置后若打开流地址失败，则会进行重试
//        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5)
//        // 打开视频时单次 http 请求的超时时间，一次打开过程最多尝试五次
//        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000)
////        options.setInteger(AVOptions.KEY_LOG_LEVEL, 5)
//        // 请在开始播放之前配置
//        plAdVideo.setAVOptions(options)
//        plAdVideo.setVideoPath(url)
//        plAdVideo.isLooping = true
//        plAdVideo.setBufferingIndicator(adLoadingView)
//        plAdVideo.setVolume(0f, 0f)
//        coverView.loadInside(url)
//        plAdVideo.setCoverView(coverView)
//        plAdVideo.requestFocus()
//        if (type != 1) {
//            plAdVideo.start()
//        } else {
//            plAdVideo?.pause()
//        }
//        if (audioClosed) {
//            plAdVideo?.setVolume(0f, 0f)
//        }
//    }

}