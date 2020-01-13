package com.yzy.example.utils.album

/**
 * description :默认配置只加载非gif图片
 *
 * @author : yzy
 * @date : 2019/4/16 14:27
 */
class LoadConfig private constructor() {
    //全部展示的文件夹名称
    internal var allName = "全部"
    //加载gif
    internal var loadGif = false
    //加载大图
    internal var loadBigImg = true
    //加载音频/视频的最大时长(毫秒)
    internal var durationMax = 30 * 1000L
    //加载音频/视频的最短时长(毫秒)
    internal var durationMin = 5 * 1000L
    //资源类型
    internal var resourceType = MediaType.IMAGE
    //是否把视频单独放在第二个文件夹
    internal var videoInSecondDir = false
    //全部里面是否需要放视频
    internal var videoInFirstDir = true
    //第二个文件夹路径，当videoInSecondDir=true生效
    internal var secondDirName = "视频"

    private object SingletonHolder {
        val holder = LoadConfig()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    //重置数据
    fun reset(): LoadConfig {
        this.allName = "全部"
        this.secondDirName = "视频"
        this.resourceType = MediaType.IMAGE
        this.loadGif = false
        this.loadBigImg = true
        this.durationMax = 30 * 1000L
        this.durationMin = 5 * 1000L
        return this
    }

    fun buildVideoInSecondDir(allIn: Boolean): LoadConfig {
        this.videoInSecondDir = allIn
        return this
    }

    fun buildVideoInFirstDir(allIn: Boolean): LoadConfig {
        this.videoInFirstDir = allIn
        return this
    }

    fun buildAllName(name: String): LoadConfig {
        this.allName = name
        return this
    }

    fun buildSecondName(name: String): LoadConfig {
        this.secondDirName = name
        return this
    }

    fun buildType(type: MediaType): LoadConfig {
        this.resourceType = type
        return this
    }

    fun buildGif(load: Boolean): LoadConfig {
        this.loadGif = load
        return this
    }

    fun buildBigImg(load: Boolean): LoadConfig {
        this.loadBigImg = load
        if (this.resourceType != MediaType.ALL) {
            this.resourceType != MediaType.IMAGE
        }
        return this
    }

    fun buildDuration(
        min: Long,
        max: Long
    ): LoadConfig {
        if (min in 1 until max) {
            this.durationMin = min
            this.durationMax = max
            if (this.resourceType != MediaType.ALL) {
                this.resourceType = MediaType.VIDEO
            }
        }
        return this
    }
}