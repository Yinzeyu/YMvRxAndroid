package com.yzy.example.app

import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory
import com.dueeeke.videoplayer.player.VideoView
import com.dueeeke.videoplayer.player.VideoViewConfig
import com.dueeeke.videoplayer.player.VideoViewManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.http.RetrofitConfig
import com.yzy.example.R
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.RequestIntercept
import com.yzy.example.widget.RefreshHeader
import com.yzy.example.widget.file.AppFileDirManager
import io.microshow.rxffmpeg.RxFFmpegInvoke


class App : BaseApplication() {
    override fun initInMainThread() {
        initLiveBus()
        RetrofitConfig {
            context = this@App
            baseUrl = ApiConstants.Address.BASE_URL
            interceptors.add(
                RequestIntercept(mutableListOf())
            )
        }
        //初始化存储文件的目录
        AppFileDirManager.initAppFile(this@App)
        //Sketch配置视频封面加载
//        val configuration: Configuration = Sketch.with(this).configuration
//        configuration.uriModelManager.add(
//            VideoThumbnailUriModel()
//        )
        //RxFFmpeg
        RxFFmpegInvoke.getInstance()
            .setDebug(true)
        //视频播放全局配置
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                //使用ExoPlayer解码
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT)
                .build()
        )
    }

    override fun baseInitCreate() {
    }


    private fun initLiveBus() {
        LiveEventBus
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)
    }

    init {
        //设置全局的Header构建器
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            RefreshHeader(context)
        }
    }
}

