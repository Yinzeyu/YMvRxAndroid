package com.yzy.pj.ui.video

import android.util.Log
import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.blankj.utilcode.util.ToastUtils
import com.pili.pldroid.player.*
import com.pili.pldroid.player.widget.PLVideoView
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.commonlibrary.TimeUtils
import com.yzy.pj.R
import com.yzy.pj.widget.AimyPlayerSeekBar
import kotlinx.android.synthetic.main.pl_item.view.*

@EpoxyModelClass(layout = R.layout.pl_item)
abstract class PlPlayItem : BaseEpoxyModel<BaseEpoxyHolder>() {
    var itemViewInit: View? = null
    /** 开始播放的时间(为了统计播放时长)  */
    private var mStartTime: Long = 0L
    @EpoxyAttribute
    lateinit var path: String
    /**
     * 当前播放时间
     */
    private var mCurrentPlayTime: Int = 0

    override fun onBind(itemView: View) {
        itemViewInit = itemView
        itemView.item_video_video_view.setOnPreparedListener(mPLOnPreparedListener)
        itemView.item_video_video_view.setOnInfoListener(mPLOnInfoListener)
//        itemView.item_video_video_view.setOnErrorListener(mPLOnErrorListener)
        itemView.item_video_video_view.setOnCompletionListener(mPLOnCompletionListener)
//        itemView.item_video_video_view.setOnSeekCompleteListener(mPLOnSeekCompleteListener)
//        itemView.item_video_video_view.setOnVideoSizeChangedListener(mPLOnVideoSizeChangedListener)
//        itemView.item_video_video_view.setOnBufferingUpdateListener(mPLOnBufferingUpdateListener)
        itemView.item_video_video_view.displayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT
//        checkCacheConfig()
        val options = AVOptions()
        //设置拖动模式，1位精准模式，即会拖动到时间戳的那一秒；0为普通模式，会拖动到时间戳最近的关键帧。默认为0
        options.setInteger(AVOptions.KEY_SEEK_MODE, 1)
        itemView.item_video_video_view.setAVOptions(options)
        itemView.item_video_video_view.setVideoPath(path)
        itemView.apsb_play_item_play_audio.setOnSeekBarChangeListener(mOnSeekBarChangeListener)

    }
    /**
     * 进度条回调
     */
    /**
     * 因为频繁拖动会导致播放器出问题，所以先记录，放手再seek
     */
    private var mSeekTime: Long = 0
    private val mOnSeekBarChangeListener = object : AimyPlayerSeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(progress: Long) {
            //设置拖动的进度
            mSeekTime = progress

        }

        override fun onMovingProgress(progress: Long) {
            itemViewInit?.tv_play_item_play_audio_time_left?.text = TimeUtils.instance.getPlayTime(progress)
        }

        override fun onStartTrackingTouch() {
            itemViewInit?.item_video_video_view?.pause()
        }

        override fun onStopTrackingTouch() {
            itemViewInit?.item_video_video_view?.seekTo(mSeekTime)
            itemViewInit?.item_video_video_view?.start()
        }
    }
    /**
     * 播放器准备的监听
     */
    private val mPLOnPreparedListener = PLOnPreparedListener { preparedTime ->
        Log.e("CASE", "---------------------:mPLOnPreparedListener")

        //准备完成
        val duration = itemViewInit?.item_video_video_view?.duration ?: 0L
        itemViewInit?.let {
            it.tv_play_item_play_audio_time_left.text = TimeUtils.instance.getPlayTime(0)
            if (it.apsb_play_item_play_audio.max == 0L) {
                it.tv_play_item_play_audio_time_right.text =
                    TimeUtils.instance.getPlayTime(duration)
                it.apsb_play_item_play_audio.max = duration
            }
        }
        itemViewInit?.item_video_video_view?.start()

//        //准备完成可以拖动
//        mViewHolder.mAimySeekBar.setEnabled(true)
//        mViewHolder.mImageViewStartPause.setEnabled(true)
//        //如果准备好了，但是页面已经暂停了，则让页面重启启动的时候开始播放
//        if (mContext is PLDroidActivity && (mContext as PLDroidActivity).isOnPause()) {
//            (mContext as PLDroidActivity).setNeedResumePlay()
//        } else if (mContext is ListPlayActivity && (mContext as ListPlayActivity).isOnPause()) {
//            (mContext as ListPlayActivity).setNeedResumePlay()
//        } else {
//            setPlayStart()
//        }//开始播放
//        if (mOnPlayClick != null) {
//            mOnPlayClick.preparePlayTimes(mVideoBean)
//        }
    }
    /**
     * Seek完成
     */
    private val mPLOnSeekCompleteListener = PLOnSeekCompleteListener { this.setPlayStart() }
    /**
     * 视频宽高
     */
    private val mPLOnVideoSizeChangedListener = PLOnVideoSizeChangedListener { width, height -> }
    /**
     * 文件的缓冲进度(整个文件)
     */
    private val mPLOnBufferingUpdateListener = PLOnBufferingUpdateListener { percent -> }

    /**
     * 设置播放
     */
    fun setPlayStart() {
//        if (mViewHolder != null && mVideoBean != null && isPrepared) {
//            start()
//            mViewHolder.mImageViewStartPause.setImageResource(R.drawable.pause_video_white)
//            mViewHolder.mImageViewStartPause.setTag(null)
//            addAudioFocusChangeListener()
//        }
    }

    /**
     * 是否需要刷新画面或重置
     */
    private var needRefreshFrame: Boolean = false
    /**
     * 播放完成
     */
    private val mPLOnCompletionListener = PLOnCompletionListener {
        //2018年5月7日 14:27:17 需要播放完成自动重新播放，因此needRefreshFrame改为false,isAutoPlay改为true
        needRefreshFrame = false
        refreshPlayTime()
        //因为不能让图标变化，所以直接调用内部方法，不调用图标变化的方法
        start()
//        resetProgressZero(false)
    }

    /** 更新播放时间  */
    private fun refreshPlayTime() {
        //播放暂停,播放完成，播放出错，播放停止都需要更新
        if (System.currentTimeMillis() > mStartTime && mStartTime > 0) {
            Log.e("CASE", "增加播放时长:" + (System.currentTimeMillis() - mStartTime) + "ms")
//            mPlayTimeUpBean.setPlayTime(mPlayTimeUpBean.getPlayTime() + System.currentTimeMillis() - mStartTime)
            mStartTime = System.currentTimeMillis()
        }
    }

    /** 开始播放，不会设置其他图标的状态  */
    private fun start() {
        itemViewInit?.item_video_video_view?.start()
        mStartTime = System.currentTimeMillis()
    }

    /**
     * 播放信息
     */
    private val mPLOnInfoListener = PLOnInfoListener { what, extra ->
        when (what) {
            //第一帧视频已成功渲染
            PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START -> {
                itemViewInit?.item_video_video_first_frame?.alpha = 0f
            }
            //连接成功
            PLOnInfoListener.MEDIA_INFO_CONNECTED -> {
            }
            //开始缓冲
            PLOnInfoListener.MEDIA_INFO_BUFFERING_START -> {
//                mViewHolder.mVideoLoadingParent.setVisibility(View.VISIBLE)
//                canSeek = false
//                mViewHolder.mAimySeekBar.setEnabled(false)
//                mViewHolder.mImageViewStartPause.setEnabled(false)
                refreshPlayTime()
                mStartTime = 0
            }
            //停止缓冲
            PLOnInfoListener.MEDIA_INFO_BUFFERING_END -> {
//                mViewHolder.mVideoLoadingParent.setVisibility(View.GONE)
                mStartTime = System.currentTimeMillis()
//                canSeek = true
                //拖动太快有问题，所以延时500ms进行设置
//                postDelayed({
//                    if (canSeek && mViewHolder.mAimySeekBar != null) {
//                        mViewHolder.mAimySeekBar.setEnabled(true)
//                        mViewHolder.mImageViewStartPause.setEnabled(true)
//                    }
//                }, 500)
            }
            //获取到视频的播放角度
            PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED -> {
            }
            //第一帧音频已成功播放
            PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START -> {
            }
            //上一次 seekTo 操作尚未完成
            PLOnInfoListener.MEDIA_INFO_IS_SEEKING -> {
            }
            //音频的帧率统计结果
            PLOnInfoListener.MEDIA_INFO_AUDIO_FPS -> {
                //为了解决seek正在loading的时候点击了暂停，导致loading不停止的问题
//                if (mViewHolder != null
//                    && mVideoBean != null
//                    && !mViewHolder.mVideoView.isPlaying()
//                    && mViewHolder.mVideoLoadingParent.getVisibility() === View.VISIBLE
//                ) {
//                    mViewHolder.mVideoLoadingParent.setVisibility(View.GONE)
//                }
            }
            //音频播放时间
            PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING -> {
                mCurrentPlayTime = extra
                itemViewInit?.tv_play_item_play_audio_time_left?.text = TimeUtils.instance.getPlayTime(mCurrentPlayTime.toLong())
                itemViewInit?.apsb_play_item_play_audio?.setProgress(mCurrentPlayTime / 1000 * 1000)
//                if (mBitmapPause != null) {
//                    mViewHolder.mImageViewPauseFrame.setVisibility(View.INVISIBLE)
//                    mViewHolder.mImageViewPauseFrame.setImageBitmap(null)
//                    mBitmapPause = null
//                } else if (needRefreshFrame) {
//                    needRefreshFrame = false
//                    setPlayPause()
//                    return
//                }
//                //每秒更新播放进度
//                if (mCurrentPlayTime / 1000 != extra / 1000) {
//                    //防止播放按钮不对
//                    if (mViewHolder.mImageViewStartPause.getTag() != null && mViewHolder.mVideoView.isPlaying()) {
//                        mViewHolder.mImageViewStartPause.setImageResource(R.drawable.pause_video_white)
//                        mViewHolder.mImageViewStartPause.setTag(null)
//                    } else if (!mViewHolder.mVideoView.isPlaying()) {
//                        mViewHolder.mImageViewStartPause.setImageResource(R.drawable.play_video_white)
//                        mViewHolder.mImageViewStartPause.setTag("pause")
//                    }
//                    mCurrentPlayTime = extra
//                    mViewHolder.mTextViewProgress.setText(TimeUtils.getPlayTime(mCurrentPlayTime))
//                    //为了让进度每秒走动，所以以秒为单位.只有手指放开才进行设置
//                    if (mViewHolder.mAimySeekBar.isEnabled()) {
//                        mViewHolder.mAimySeekBar.setProgress(mCurrentPlayTime / 1000 * 1000)
//                    }
//                }
//                //开始播放隐藏loading
//                if (mViewHolder.mVideoLoadingParent.getVisibility() === View.VISIBLE) {
//                    mViewHolder.mVideoLoadingParent.setVisibility(View.GONE)
//                }
//                //结束缓冲统计
//                if (mVideoBean != null
//                    && !mVideoBean.isLocalVideo()
//                    && mPlayStatisticalBean.getLoadStartTime() > 0
//                    && mPlayStatisticalBean.getNetworkType() < 0
//                ) {
//                    mPlayStatisticalBean.setNetworkType(0)
//                    //防止万一遇到没有设置的情况
//                    if (mPlayStatisticalBean.getSetResourceTime() === 0) {
//                        mPlayStatisticalBean.setSetResourceTime(System.currentTimeMillis())
//                    }
//                    if (mPlayTimeUpBean.getStatisticalTime() === 0) {
//                        mPlayTimeUpBean.setStatisticalTime(System.currentTimeMillis())
//                    }
//                    val loadTime =
//                        System.currentTimeMillis() - mPlayStatisticalBean.getLoadStartTime()
//                    //load时间
//                    mPlayStatisticalBean.setLoadTime(
//                        String.format(Locale.getDefault(), "%.3f", loadTime / 1000f)
//                    )
//                    //load大小
//                    mPlayStatisticalBean.setCacheSize(
//                        (TrafficStatisticsUtil.getTotalRxBytes() - mPlayStatisticalBean.getCacheSize()) / 1024
//                    )
//                    //load速度
//                    mPlayStatisticalBean.setLoadSpeed(
//                        String.format(
//                            Locale.getDefault(), "%.3f",
//                            mPlayStatisticalBean.getCacheSize() * 1000f / loadTime
//                        )
//                    )
//                    //网络类型
//                    val networkType = NetworkUtils.getNetworkType()
//                    val networkTypeName: String
//                    if (networkType == NetworkUtils.NetworkType.NETWORK_2G) {
//                        networkTypeName = "2G"
//                        mPlayStatisticalBean.setNetworkType(2)
//                    } else if (networkType == NetworkUtils.NetworkType.NETWORK_3G) {
//                        networkTypeName = "3G"
//                        mPlayStatisticalBean.setNetworkType(3)
//                    } else if (networkType == NetworkUtils.NetworkType.NETWORK_4G) {
//                        networkTypeName = "4G"
//                        mPlayStatisticalBean.setNetworkType(4)
//                    } else if (networkType == NetworkUtils.NetworkType.NETWORK_WIFI) {
//                        networkTypeName = "WIFI"
//                        mPlayStatisticalBean.setNetworkType(4)
//                    } else {
//                        networkTypeName = "未知"
//                        mPlayStatisticalBean.setNetworkType(0)
//                    }
//                    //运营商类型
//                    var simOperatorByMnc = PhoneUtils.getSimOperatorByMnc()
//                    if (TextUtils.isEmpty(simOperatorByMnc)) {
//                        simOperatorByMnc = "未知"
//                        mPlayStatisticalBean.setNetworkSeller(0)
//                    } else if (simOperatorByMnc.contains("电信")) {
//                        mPlayStatisticalBean.setNetworkSeller(1)
//                    } else if (simOperatorByMnc.contains("移动")) {
//                        mPlayStatisticalBean.setNetworkSeller(2)
//                    } else if (simOperatorByMnc.contains("联通")) {
//                        mPlayStatisticalBean.setNetworkSeller(3)
//                    } else {
//                        simOperatorByMnc = "未知"
//                        mPlayStatisticalBean.setNetworkSeller(0)
//                    }
//                    //所在省份
//                    mPlayStatisticalBean.setProvince(SharedPreferencesUtils.getProvince())
//                    //所在城市
//                    mPlayStatisticalBean.setCity(SharedPreferencesUtils.getCity())
//                    //当前IP
//                    mPlayStatisticalBean.setIp(SharedPreferencesUtils.getClientIp())
//                    //播放地址
//                    mPlayStatisticalBean.setWorkUrl(mVideoBean.getVideoUrl())
//                    //进行上报
//                    val message = EventBusMessage()
//                    message.setT(mPlayStatisticalBean)
//                    EventBus.getDefault().post(message, EventConstants.UPLOAD_PLAY_STATISTICAL)
//                    Log.e(
//                        "CASE",
//                        "-----------------------------开始打印视频缓冲信息-----------------------------"
//                    )
//                    Log.e("CASE", "作品对应服务器是否有缓存:" + (mPlayStatisticalBean.getHasCache() === 1))
//                    Log.e("CASE", "作品缓冲总耗时:" + mPlayStatisticalBean.getLoadTime() + "秒")
//                    Log.e("CASE", "作品缓冲速度:" + mPlayStatisticalBean.getLoadSpeed() + "KB/S")
//                    Log.e("CASE", "作品缓冲总大小:" + mPlayStatisticalBean.getCacheSize() + "KB")
//                    Log.e("CASE", "作品是否是视频:" + (mPlayStatisticalBean.getWorkType() === 0))
//                    Log.e("CASE", "作品ID:" + mPlayStatisticalBean.getWorkId())
//                    if (!TextUtils.isEmpty(mPlayStatisticalBean.getAudioId()) && !TextUtils.equals(
//                            "0",
//                            mPlayStatisticalBean.getAudioId()
//                        )
//                    ) {
//                        Log.e("CASE", "音频ID:" + mPlayStatisticalBean.getAudioId())
//                    }
//                    Log.e("CASE", "作品缓冲网络类型:$networkTypeName")
//                    Log.e("CASE", "作品缓冲运营商:$simOperatorByMnc")
//                    Log.e("CASE", "作品缓冲时所在省份:" + mPlayStatisticalBean.getProvince())
//                    Log.e("CASE", "作品缓冲时所在城市:" + mPlayStatisticalBean.getCity())
//                    Log.e("CASE", "作品缓冲播放地址:" + mPlayStatisticalBean.getWorkUrl())
//                    Log.e(
//                        "CASE",
//                        "-----------------------------视频缓冲信息打印结束-----------------------------"
//                    )
//                }
            }
            else -> {
            }
        }
    }
    /**
     * 播放出错
     */
    private val mPLOnErrorListener = PLOnErrorListener { errorCode ->
        when (errorCode) {
            //未知错误
            PLOnErrorListener.MEDIA_ERROR_UNKNOWN -> {
            }
            //播放器打开失败
            PLOnErrorListener.ERROR_CODE_OPEN_FAILED -> {
            }
            //网络异常
            PLOnErrorListener.ERROR_CODE_IO_ERROR -> {
            }
            //拖动失败
            PLOnErrorListener.ERROR_CODE_SEEK_FAILED -> {
            }
            //AudioTrack 初始化失败，可能无法播放音频
            PLOnErrorListener.ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED -> {
            }
            else -> {
            }
        }
        refreshPlayTime()
//        uploadPlayStatistical()
//        resetProgressZero(true)
//        if (mViewHolder != null) {
//            mViewHolder.mVideoLoadingParent.setVisibility(View.GONE)
//        }
//        stopCountDown()
        ToastUtils.showShort("播放失败")
        false
    }

    /**
     * 检查缓存配置
     */
    private fun checkCacheConfig() {
        //播放参数配置
        val options = AVOptions()
        //设置拖动模式，1位精准模式，即会拖动到时间戳的那一秒；0为普通模式，会拖动到时间戳最近的关键帧。默认为0
        options.setInteger(AVOptions.KEY_SEEK_MODE, 1)
        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
//        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE)
        //因为没有后缀名或者不是以mp4结尾的文件播放缓存有问题，所以非mp4结尾文件不缓存
        //设置缓存地址
//                options.setString(
//                    AVOptions.KEY_CACHE_DIR,
//                    FilePathUtils.getAppPath() + FilePathConstants.VIDEO
//                )
        itemViewInit?.item_video_video_view?.setAVOptions(options)
    }
}

