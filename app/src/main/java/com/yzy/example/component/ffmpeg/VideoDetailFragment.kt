package com.yzy.example.component.ffmpeg

import android.graphics.Color
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.navArgs
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.dueeeke.videocontroller.component.CompleteView
import com.dueeeke.videocontroller.component.ErrorView
import com.dueeeke.videocontroller.component.PrepareView
import com.dueeeke.videocontroller.component.TitleView
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.StatusBarHelper
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.utils.MediaUtils
import com.yzy.example.utils.VideoUtils
import com.yzy.example.widget.video.controller.StandardVideoController
import com.yzy.example.widget.video.controller.VodControlView
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.dkplayer_layout_prepare_view.view.thumb
import java.io.File

class VideoDetailFragment : BaseFragment<NoViewModel, ViewDataBinding>() {
    val args: VideoDetailFragmentArgs by navArgs()

    //控制器
    private var controller: StandardVideoController? = null

    //准备播放页
    private var prepareView: PrepareView? = null

    //每次设置资源后的第一次播放
    private var isFirstPlay = true
    override fun isBack(): Boolean {
        return false
    }
    override fun fillStatus() = true
    override fun statusColor(): Int {
        return Color.parseColor("#000000")
    }

    override val contentLayout: Int = R.layout.activity_video_detail


    override fun initView(root: View?) {
        videoDetailBack.pressEffectAlpha()
        videoDetailBack.click { onBackPressed() }
        //控制器
        controller = StandardVideoController(mContext)
        prepareView = PrepareView(mContext)
        controller?.let {
            it.addControlComponent(prepareView)//播放前预览封面
            it.addControlComponent(CompleteView(mContext)) //自动完成播放界面
            it.addControlComponent(ErrorView(mContext)) //错误界面
            val titleView = TitleView(mContext) //标题栏
            it.addControlComponent(titleView)
            val vodControlView = VodControlView(mContext) //点播控制条
            //是否显示底部进度条,默认显示
            vodControlView.showBottomProgress(true)
            vodControlView.setVerticalFullListener(object : VodControlView.VerticalFullListener {
                override fun isVerticalVideo(): Boolean {
                    val videoSize = videoDetailVideoView.videoSize
                    return if (videoSize != null) {
                        videoSize[0] < videoSize[1]//纵向视频
                    } else {
                        false
                    }
                }

                override fun isStopOutFull(): Boolean {
                    return false
                }
            })
            it.addControlComponent(vodControlView)
        }
        controller?.setEnableOrientation(true)
        videoDetailVideoView.setVideoSizeChangeListener { videoWidth, videoHeight ->
            //全屏时跟随屏幕旋转
            controller?.setEnableOrientation(videoWidth > videoHeight)
        }
//    controller?.isNeedNoFullShowBack = true
        //设置控制器
        videoDetailVideoView.setVideoController(controller)
        videoDetailVideoView.setLooping(false)
        //内部处理生命周期
        videoDetailVideoView.setLifecycleOwner(this)
    }

    override fun initData() {
        val url: String =
            if (args.videoUrl == "value") "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4" else args.videoUrl
        videoDetailVideoView.setUrl(url)
        controller?.thumb?.click {
            when {
                isFirstPlay -> {
                    videoDetailVideoView.start()
                    isFirstPlay = false
                }
                videoDetailVideoView.isPlaying -> {
                    videoDetailVideoView.pause()
                }
                else -> {
                    videoDetailVideoView.resume()
                }
            }
        }
        if (url.startsWith("http", true)) {
            VideoUtils.instance.getNetVideoFistFrame(url) { bit ->
                if (bit != null) {
                    controller?.thumb?.setImageBitmap(bit)
                } else {
                    controller?.thumb?.setImageResource(R.drawable.svg_placeholder_fail)
                }
            }
        } else if (File(url).exists()) {
            if (MediaUtils.instance.isVideoFile(url)) {
                VideoUtils.instance.getFirstFrame(File(url)) { suc, info ->
                    if (suc) controller?.thumb?.load(File(info).path)
                    else LogUtils.e("CASE:视频文件封面获取失败:$url")
                }
            } else LogUtils.e("CASE:非视频文件:$url")
        } else LogUtils.e("CASE:未知视频播放源:$url")
    }

    override fun onBackPressed() {
        if (videoDetailVideoView?.onBackPressed() == false) super.onBackPressed()
    }

    override fun onDestroyView() {
        StatusBarHelper.setStatusBarLightMode(mContext)
        super.onDestroyView()
    }
}